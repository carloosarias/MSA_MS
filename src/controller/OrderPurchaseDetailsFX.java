/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Company;
import model.CompanyAddress;
import model.Employee;
import model.OrderPurchase;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseDetailsFX implements Initializable {

    @FXML
    private TextField id_field;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker orderdate_picker;
    @FXML
    private ComboBox<Company> supplier_combo;
    @FXML
    private ComboBox<CompanyAddress> address_combo;
    @FXML
    private TextArea description_area;
    @FXML
    private CheckBox active_check;
    @FXML
    private TextField exchangerate_field;
    @FXML
    private TextField ivarate_field;
    @FXML
    private Button additem_button;
    @FXML
    private Button deleteitem_button;
    @FXML
    private Button details_button;
    @FXML
    private TableView<?> purchaseitem_tableview;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> product_column;
    @FXML
    private TableColumn<?, ?> itemdesc_column;
    @FXML
    private TextField subtotal_field;
    @FXML
    private TextField iva_field;
    @FXML
    private TextField total_field;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList(
        msabase.getOrderPurchaseDAO().list()
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(OrderPurchaseFX.getOrder_purchase().getId() == null){
            newOrder();
            supplier_combo.setOnAction((ActionEvent) -> {
                address_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(supplier_combo.getSelectionModel().getSelectedItem(), true)));
            });
        }else{
            loadOrder(OrderPurchaseFX.getOrder_purchase());
        }
    }

    public void newOrder(){
        disableFields(false);
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        employee_combo.getSelectionModel().selectFirst();
        supplier_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listSupplier(true)));
        
    }
    
    public void loadOrder(OrderPurchase order_purchase){
        disableFields(true);
        id_field.setText(""+order_purchase.getId());
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findEmployee(order_purchase)));
        employee_combo.getSelectionModel().selectFirst();
        orderdate_picker.setValue(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(order_purchase.getOrder_date())));
        supplier_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findCompany(order_purchase)));
        supplier_combo.getSelectionModel().selectFirst();        
        address_combo.setItems(FXCollections.observableArrayList(msabase.getOrderPurchaseDAO().findAddress(order_purchase)));
        address_combo.getSelectionModel().selectFirst();
    }
    
    public void disableFields(boolean value){
        
    }
}
