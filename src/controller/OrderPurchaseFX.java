/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.CompanyAddress;
import model.IncomingReport;
import model.OrderPurchase;
import model.PurchaseItem;
import msa_ms.MainApp;


/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseFX implements Initializable {
    @FXML
    private TableView<OrderPurchase> orderpurchase_tableview;
    @FXML
    private TableColumn<OrderPurchase, Integer> orderid_column;
    @FXML
    private TableColumn<OrderPurchase, Date> orderdate_column;
    @FXML
    private TableColumn<OrderPurchase, String> status_column;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private Button changestatus_button;
    @FXML
    private Button pdf_button;
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
    
    private static ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setOrderPurchaseTable();
        updateOrderPurchaseList(msabase);
        orderpurchase_tableview.setItems(orderpurchase_list);
        status_combo.disableProperty().bind(orderpurchase_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button.disableProperty().bind(orderpurchase_tableview.getSelectionModel().selectedItemProperty().isNull());
        changestatus_button.disableProperty().bind(status_combo.getSelectionModel().selectedItemProperty().isNull());
        
        
        setPurchaseItemTable();
        
        status_combo.setItems(FXCollections.observableArrayList(MainApp.orderpurchase_status));
        
        orderpurchase_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchase> observable, OrderPurchase oldValue, OrderPurchase newValue) -> {
            try{
            purchaseitem_tableview.setItems(FXCollections.observableArrayList(msabase.getPurchaseItemDAO().list(newValue)));
            company_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findCompany(newValue)));
            companyaddress_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findCompanyAddress(newValue)));
            company_combo.getSelectionModel().selectFirst();
            companyaddress_combo.getSelectionModel().selectFirst();
            exchangerate_field.setText(""+newValue.getExchange_rate());
            ivarate_field.setText(""+newValue.getIva_rate());
            calculateTotals();
            comments_area.setText(newValue.getComments());
            }catch(Exception e){
                clearFields();
            }
            status_combo.getSelectionModel().clearSelection();
        });
        
        changestatus_button.setOnAction((ActionEvent) -> {
            orderpurchase_tableview.getSelectionModel().getSelectedItem().setStatus(status_combo.getSelectionModel().getSelectedItem());
            msabase.getOrderPurchaseDAO().update(orderpurchase_tableview.getSelectionModel().getSelectedItem());
            updateOrderPurchaseList(msabase);
        });
    }
    
    public void clearFields(){
        purchaseitem_tableview.getItems().clear();
        company_combo.getItems().clear();
        companyaddress_combo.getItems().clear();
        exchangerate_field.setText("N/A");
        ivarate_field.setText("N/A");
        subtotal_field.setText("N/A");
        iva_field.setText("N/A");
        total_field.setText("N/A");
        comments_area.setText("N/A");
    }
    
    public void setOrderPurchaseTable(){
        orderid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setPurchaseItemTable(){
        productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue())).getId()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue())).getDescription()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getPurchaseItemDAO().findProductSupplier(c.getValue()).getQuantity()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue())).getUnit_measure()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_unit()+" USD"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        price_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_total()+" USD"));
    }
    
    public static void updateOrderPurchaseList(DAOFactory msabase){
        orderpurchase_list.setAll(msabase.getOrderPurchaseDAO().list());
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
    
}
