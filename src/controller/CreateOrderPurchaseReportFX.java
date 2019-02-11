/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOException;
import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Company;
import model.CompanyAddress;
import model.OrderPurchase;
import model.PurchaseItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateOrderPurchaseReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private ComboBox<CompanyAddress> companyaddress_combo;
    @FXML
    private TextField exchangerate_field;
    @FXML
    private TextField ivarate_field;
    @FXML
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, String> serialnumber_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasureprice_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitprice_column;
    @FXML
    private TableColumn<PurchaseItem, Integer> unitsordered_column;
    @FXML
    private TableColumn<PurchaseItem, String> price_column;
    @FXML
    private TextField subtotal_field;
    @FXML
    private TextField iva_field;
    @FXML
    private TextField total_field;
    @FXML
    private TextField mxnsubtotal_field;
    @FXML
    private TextField mxniva_field;
    @FXML
    private TextField mxntotal_field;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button cancel_button;
    @FXML
    private Button save_button;

    public static Company company_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    DecimalFormat df = new DecimalFormat("#");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(4);
        
        reportdate_picker.setValue(LocalDate.now());
        
        company_combo.getItems().setAll(company_selection);
        company_combo.getSelectionModel().selectFirst();
        
        companyaddress_combo.getItems().setAll(msabase.getCompanyAddressDAO().listActive(company_selection, true));
        companyaddress_combo.getSelectionModel().selectFirst();
        companyaddress_combo.disableProperty().bind(companyaddress_combo.itemsProperty().isNull());
        
        setPurchaseItemTable();
        
        purchaseitem_tableview.setItems(filterCart_list(OrderPurchaseCartFX.cart_list, company_selection));
        
        calculateTotals();
        
        ivarate_field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            calculateTotals();
        });
        
        exchangerate_field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            calculateTotals();
        });
                
        cancel_button.setOnAction((ActionEvent) -> {
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveOrderPurchase();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void saveOrderPurchase(){
        OrderPurchase order_purchase = new OrderPurchase();
        order_purchase.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        order_purchase.setComments(comments_area.getText());
        order_purchase.setStatus(MainApp.orderpurchase_status.get(0));
        order_purchase.setExchange_rate(Double.parseDouble(exchangerate_field.getText()));
        order_purchase.setIva_rate(Double.parseDouble(ivarate_field.getText()));
        try{
            msabase.getOrderPurchaseDAO().create(company_selection, companyaddress_combo.getSelectionModel().getSelectedItem(), order_purchase);
            savePurchaseItems(order_purchase);
        }catch(DAOException e){
            System.out.println("Failed to generate order purchase; DB Entries were not saved"+e.getMessage());
            return;
        }
    }
    
    public void savePurchaseItems(OrderPurchase order_purchase){
        try{
            OrderPurchaseCartFX.cart_list.removeAll(purchaseitem_tableview.getItems());
            for(PurchaseItem item : purchaseitem_tableview.getItems()){
                msabase.getPurchaseItemDAO().create(order_purchase, item.getTemp_productsupplier(), item);
            }
        }catch(DAOException e){
            for(PurchaseItem item : msabase.getPurchaseItemDAO().list(order_purchase)){
                msabase.getPurchaseItemDAO().delete(item);
            }
            msabase.getOrderPurchaseDAO().delete(order_purchase);
            OrderPurchaseCartFX.cart_list.addAll(purchaseitem_tableview.getItems());
            System.out.println("Failed to generate Purchase Items; DB Entries were deleted"+e.getMessage());
        }
        
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        companyaddress_combo.setStyle(null);
        exchangerate_field.setStyle(null);
        ivarate_field.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            reportdate_picker.setValue(LocalDate.now());
            b = false;
        }
        
        if(companyaddress_combo.getSelectionModel().isEmpty()){
            companyaddress_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            Double.parseDouble(exchangerate_field.getText());
        }catch(Exception e){
            exchangerate_field.setStyle("-fx-background-color: lightpink;");
            exchangerate_field.setText("0.0");
            b = false;
        }
        
        try {
            Double.parseDouble(ivarate_field.getText());
        }catch(Exception e){
            ivarate_field.setStyle("-fx-background-color: lightpink;");
            ivarate_field.setText("0.0");
            b = false;
        }
        
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setText("N/A");
        }
        
        return b;
    }
    
    public void setSubtotal(){
        Double subtotal = 0.0;
        
        for(PurchaseItem item : purchaseitem_tableview.getItems()){
            subtotal += item.getPrice_total();
        }
        
        subtotal_field.setText(df.format(subtotal));
        mxnsubtotal_field.setText(df.format(subtotal*Double.parseDouble(exchangerate_field.getText())));
    }
    
    public void clearTotals(){
        subtotal_field.setText("0.0");
        mxnsubtotal_field.setText("0.0");
        iva_field.setText("0.0");
        mxniva_field.setText("0.0");
        mxntotal_field.setText("0.0");
        mxntotal_field.setText("0.0");
    }
    
    public void calculateTotals(){
        try{
            exchangerate_field.setStyle(null);
            Double.parseDouble(exchangerate_field.getText());
        }catch(Exception e){
            exchangerate_field.setStyle("-fx-background-color: lightpink;");
            clearTotals();
            return;
        }
        try{
            ivarate_field.setStyle(null);
            Double.parseDouble(ivarate_field.getText());
        }catch(Exception e){
            ivarate_field.setStyle("-fx-background-color: lightpink;");
            clearTotals();
            return;
        }
        setSubtotal();
        setIva();
        setTotal();
    }
    
    public void setIva(){
        iva_field.setText((df.format(Double.parseDouble(subtotal_field.getText())*(Double.parseDouble(ivarate_field.getText())/100))));
        mxniva_field.setText(df.format(Double.parseDouble(iva_field.getText())*Double.parseDouble(exchangerate_field.getText())));
    }
    
    public void setTotal(){
        total_field.setText(df.format((Double.parseDouble(subtotal_field.getText())+Double.parseDouble(iva_field.getText()))));
        mxntotal_field.setText(df.format((Double.parseDouble(total_field.getText())*Double.parseDouble(exchangerate_field.getText()))));
    }
    
    public ObservableList<PurchaseItem> filterCart_list(ObservableList<PurchaseItem> cart_list, Company company_selection){
        ObservableList<PurchaseItem> filtered_list = FXCollections.observableArrayList(); 
        for(PurchaseItem item : cart_list){
            if(item.getTemp_productsupplier().getCompany_id().equals(company_selection.getId())){
                filtered_list.add(item);
            }
        }
        return filtered_list;
    }
    
    
    public void setPurchaseItemTable(){
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("productsupplier_serialnumber"));
        description_column.setCellValueFactory(new PropertyValueFactory("product_description"));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getProductsupplier_quantity())));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct_unitmeasure()));
        unitmeasureprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getPrice_unit()/c.getValue().getProductsupplier_quantity()))+" USD"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_unit()+" USD"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        price_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_total()+" USD"));
        
        unitsordered_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        unitsordered_column.setOnEditCommit((TableColumn.CellEditEvent<PurchaseItem, Integer> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_ordered(t.getNewValue());
            if(t.getNewValue() < 1){
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_ordered(0);
            }
            purchaseitem_tableview.refresh();
            calculateTotals();
        });
    }
}
