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
        
        setPurchaseItemTable();
        orderpurchase_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchase> observable, OrderPurchase oldValue, OrderPurchase newValue) -> {
            purchaseitem_tableview.setItems(FXCollections.observableArrayList(msabase.getPurchaseItemDAO().list(newValue)));
            company_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findCompany(newValue)));
            companyaddress_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findCompanyAddress(newValue)));
            company_combo.getSelectionModel().selectFirst();
            companyaddress_combo.getSelectionModel().selectFirst();
        });
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
    
}
