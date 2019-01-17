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
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
    private TableColumn<PurchaseItem, String> productid_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
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
    private TextArea comments_area;
    @FXML
    private Button cancel_button;
    @FXML
    private Button save_button;

    public static Company company_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        reportdate_picker.setValue(LocalDate.now());
        
        company_combo.getItems().setAll(company_selection);
        company_combo.getSelectionModel().selectFirst();
        
        companyaddress_combo.getItems().setAll(msabase.getCompanyAddressDAO().listActive(company_selection, true));
        companyaddress_combo.getSelectionModel().selectFirst();
        companyaddress_combo.disableProperty().bind(companyaddress_combo.itemsProperty().isNull());
        
        setPurchaseItemTable();
        
        purchaseitem_tableview.setItems(filterCart_list(OrderPurchaseCartFX.cart_list, company_selection));
        
        calculateTotals();
        
        ivarate_field.setOnAction((ActionEvent) -> {
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
            OrderPurchaseCartFX.cart_list.removeAll(purchaseitem_tableview.getItems());
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
        }catch(DAOException e){
            System.out.println("Failed to generate order purchase; DB Entries were not saved");
            return;
        }
        savePurchaseItems(order_purchase);
    }
    
    public void savePurchaseItems(OrderPurchase order_purchase){
        try{
            for(PurchaseItem item : purchaseitem_tableview.getItems()){
                item.setPrice_updated(item.getPrice_timestamp());
                item.setDate_modified(order_purchase.getReport_date());
                item.setModified(false);
                msabase.getPurchaseItemDAO().create(order_purchase, item.getTemp_productsupplier(), item);
            }
        }catch(DAOException e){
            for(PurchaseItem item : msabase.getPurchaseItemDAO().list(order_purchase)){
                msabase.getPurchaseItemDAO().delete(item);
            }
            msabase.getOrderPurchaseDAO().delete(order_purchase);
            System.out.println("Failed to generate Purchase Items; DB Entries were deleted");
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
            ivarate_field.setText("0");
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
        
        subtotal_field.setText(""+subtotal);
    }
    public void calculateTotals(){
        setSubtotal();
        setIva();
        setTotal();
    }
    public void setIva(){
        try{
            Integer.parseInt(ivarate_field.getText());
        } catch(NumberFormatException e){
            ivarate_field.setText("0");
        }
        iva_field.setText(""+(Double.parseDouble(subtotal_field.getText())*(Double.parseDouble(ivarate_field.getText())/100)));
    }
    
    public void setTotal(){
        total_field.setText(""+(Double.parseDouble(subtotal_field.getText())+Double.parseDouble(iva_field.getText())));
    }
    
    public ObservableList<PurchaseItem> filterCart_list(ObservableList<PurchaseItem> cart_list, Company company_selection){
        ObservableList<PurchaseItem> filtered_list = FXCollections.observableArrayList(); 
        for(PurchaseItem item : cart_list){
            if(msabase.getProductSupplierDAO().findCompany(item.getTemp_productsupplier()).equals(company_selection)){
                filtered_list.add(item);
            }
        }
        return filtered_list;
    }
    
    
    public void setPurchaseItemTable(){
        productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(c.getValue().getTemp_productsupplier()).getId()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(c.getValue().getTemp_productsupplier()).getDescription()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_productsupplier().getQuantity()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(c.getValue().getTemp_productsupplier()).getUnit_measure()));
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
