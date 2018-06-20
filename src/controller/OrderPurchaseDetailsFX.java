/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.CompanyAddress;
import model.Employee;
import model.OrderPurchase;
import model.PurchaseItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseDetailsFX implements Initializable {

    @FXML
    private HBox root_hbox;
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
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, Integer> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> product_column;
    @FXML
    private TableColumn<PurchaseItem, String> itemdesc_column;
    @FXML
    private TableColumn<PurchaseItem, Date> deliverydate_column;
    @FXML
    private TableColumn<PurchaseItem, Double> unitprice_column; 
    @FXML
    private TableColumn<PurchaseItem, String> import_column;
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
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
    
    private Stage add_stage;
    
    private static List<PurchaseItem> purchase_items = new ArrayList<PurchaseItem>();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        purchase_items.clear();
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        product_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductDAO().find(c.getValue().getProduct_id()).toString()));
        itemdesc_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        deliverydate_column.setCellValueFactory(new PropertyValueFactory<>("delivery_date"));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        import_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity() * c.getValue().getUnit_price()));
        
        if(OrderPurchaseFX.getOrder_purchase().getId() == null){
            newOrder();
            ivarate_field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                ivarate_field.setText(newValue.replaceAll("[^.\\d]", ""));
                try{
                    if(!ivarate_field.getText().isEmpty()){
                        Double.parseDouble(ivarate_field.getText());
                        if(Double.parseDouble(ivarate_field.getText()) > 999) ivarate_field.setText(oldValue);
                        ivarate_field.setStyle(null);
                        computeTotal();
                    }
                }catch(Exception e){
                    ivarate_field.setStyle("-fx-border-color: red ;");
                }
            });
            
            purchaseitem_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PurchaseItem> observable, PurchaseItem oldValue, PurchaseItem newValue) -> {
                delete_button.setDisable(purchaseitem_tableview.getSelectionModel().isEmpty());
            });
            
            supplier_combo.setOnAction((ActionEvent) -> {
                address_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(supplier_combo.getSelectionModel().getSelectedItem(), true)));
            });
            
            add_button.setOnAction((ActionEvent) -> {
                showAdd_stage();
                purchaseitem_tableview.setItems(FXCollections.observableArrayList(purchase_items));
                computeTotal();
            });
            
            delete_button.setOnAction((ActionEvent) -> {
                purchase_items.remove(purchaseitem_tableview.getSelectionModel().getSelectedItem());
                purchaseitem_tableview.setItems(FXCollections.observableArrayList(purchase_items));
                computeTotal();
            });
            
            save_button.setOnAction((ActionEvent) -> {
                if(!testFields()) return;
                msabase.getOrderPurchaseDAO().create(employee_combo.getSelectionModel().getSelectedItem(), supplier_combo.getSelectionModel().getSelectedItem()
                        , address_combo.getSelectionModel().getSelectedItem(), mapOrderPurchase(OrderPurchaseFX.getOrder_purchase()));
                for(PurchaseItem p : purchase_items){
                    msabase.getPurchaseItemDAO().create(OrderPurchaseFX.getOrder_purchase(), msabase.getProductDAO().find(p.getProduct_id()), p);
                }
            });
            
        }else{
            loadOrder(OrderPurchaseFX.getOrder_purchase());
        }
        
    }

    public OrderPurchase mapOrderPurchase(OrderPurchase order_purchase){
        order_purchase.setOrder_date(java.sql.Date.valueOf(orderdate_picker.getValue()));
        order_purchase.setExchange_rate(Double.parseDouble(exchangerate_field.getText()));
        order_purchase.setIva_rate(Double.parseDouble(ivarate_field.getText()));
        order_purchase.setDescription(description_area.getText());
        order_purchase.setActive(!active_check.isSelected());
        order_purchase.setSub_total(Double.parseDouble(subtotal_field.getText()));
        order_purchase.setIva(Double.parseDouble(iva_field.getText()));
        order_purchase.setTotal(Double.parseDouble(total_field.getText()));
        return order_purchase;
    }
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(orderdate_picker.getValue() == null){
            orderdate_picker.setStyle("-fx-border-color: red ;");
            b = false;
        }
        if(supplier_combo.getSelectionModel().isEmpty()){
            supplier_combo.setStyle("-fx-border-color: red ;");
            b = false;
        }
        if(address_combo.getSelectionModel().isEmpty()){
            address_combo.setStyle("-fx-border-color: red ;");
            b = false;
        }
        if(description_area.getText().replace(" ", "").equals("")){
            description_area.setStyle("-fx-border-color: red ;");
            b = false;
        }
        try{
            Double.parseDouble(exchangerate_field.getText());
        }catch(Exception e){
            exchangerate_field.setStyle("-fx-border-color: red ;");
            b = false;
        }        
        try{
            Double.parseDouble(ivarate_field.getText());
        }catch(Exception e){
            ivarate_field.setStyle("-fx-border-color: red ;");
            b = false;
        }
        if(purchase_items.isEmpty()){
            purchaseitem_tableview.setStyle("-fx-border-color: red ;");
            b = false;
        }
        return b;
    }
    public void clearStyle(){
        orderdate_picker.setStyle(null);
        supplier_combo.setStyle(null);
        address_combo.setStyle(null);
        description_area.setStyle(null);
        exchangerate_field.setStyle(null);
        ivarate_field.setStyle(null);
        purchaseitem_tableview.setStyle(null);
    }
    public void computeTotal(){
        if(ivarate_field.getText().isEmpty()){
            ivarate_field.setStyle("-fx-border-color: red ;");
            return;
        }
        double subtotal_count = 0;
        double iva_count = 0;
        double total_count = 0;
        
        for(PurchaseItem p : purchase_items){
            subtotal_count += (p.getQuantity()*p.getUnit_price());
        }
        iva_count = (Double.parseDouble(ivarate_field.getText()) * 0.01)*subtotal_count;
        total_count = subtotal_count+iva_count;
        
        subtotal_field.setText(""+subtotal_count);
        iva_field.setText(""+iva_count);
        total_field.setText(""+total_count);
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/PurchaseItemFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar Producto");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
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
        purchase_items = msabase.getPurchaseItemDAO().list(order_purchase);
        purchaseitem_tableview.setItems(FXCollections.observableArrayList(purchase_items));
    }
    
    public void disableFields(boolean value){
        supplier_combo.setDisable(value);
        address_combo.setDisable(value);
        orderdate_picker.setDisable(value);
        description_area.setDisable(value);
        active_check.setDisable(value);
        exchangerate_field.setDisable(value);
        ivarate_field.setDisable(value);
        add_button.setDisable(value);
        save_button.setDisable(value);
        
    }
    
    public static List<PurchaseItem> getPurchaseItems(){
        return purchase_items;
    }
}
