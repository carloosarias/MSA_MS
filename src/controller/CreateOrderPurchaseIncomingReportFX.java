/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Employee;
import model.OrderPurchase;
import model.OrderPurchaseIncomingItem;
import model.PurchaseItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateOrderPurchaseIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<OrderPurchase> orderpurchase_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private TextArea comments_area;
    @FXML
    private TableView<OrderPurchaseIncomingItem> orderpurchaseincomingitem_tableview;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> productid_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> description_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> quantity_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> unitmeasure_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> unitsordered_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, Integer> unitsarrived_column;
    @FXML
    private Button cancel_button;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Fill this list with Pending and Incomplete OrderPurchases
        orderpurchase_combo.getItems().setAll(msabase.getOrderPurchaseDAO().listOfStatus(MainApp.orderpurchase_status.get(0)));
        orderpurchase_combo.getItems().addAll(msabase.getOrderPurchaseDAO().listOfStatus(MainApp.orderpurchase_status.get(1)));
        
        status_combo.disableProperty().bind(orderpurchase_combo.getSelectionModel().selectedItemProperty().isNull());
        save_button.disableProperty().bind(status_combo.getSelectionModel().selectedItemProperty().isNull());
        setOrderPurchaseIncomingItemTable();
        
        
        //On input text check for valid ID entry; if so then update OrderPurchaseIncomingItem Tableview
        orderpurchase_combo.setOnAction((ActionEvent) -> {
            try {
                orderpurchase_combo.getSelectionModel().select(msabase.getOrderPurchaseDAO().find(Integer.parseInt(orderpurchase_combo.getEditor().textProperty().getValue())));
                updateOrderPurchaseIncomingItemTable();
                
            }catch(Exception e){
                orderpurchase_combo.getEditor().selectAll();
                orderpurchaseincomingitem_tableview.getItems().clear();
            }
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
        
    }
    
    public void setOrderPurchaseIncomingItemTable(){
        productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue().getTemp_purchaseitem())).getId()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue().getTemp_purchaseitem())).getDescription()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getPurchaseItemDAO().findProductSupplier(c.getValue().getTemp_purchaseitem()).getQuantity()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(c.getValue().getTemp_purchaseitem())).getUnit_measure()));
        unitsordered_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_purchaseitem().getUnits_ordered()));

        unitsarrived_column.setCellValueFactory(new PropertyValueFactory<>("units_arrived"));
        unitsarrived_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        unitsarrived_column.setOnEditCommit((TableColumn.CellEditEvent<OrderPurchaseIncomingItem, Integer> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_arrived(t.getNewValue());
            if(t.getNewValue() < 1){
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_arrived(0);
            }
            orderpurchaseincomingitem_tableview.refresh();
        });
    }
    
    public void updateOrderPurchaseIncomingItemTable(){
        ObservableList<OrderPurchaseIncomingItem> orderpurchaseincomingitem_list = FXCollections.observableArrayList();
        for(PurchaseItem item: msabase.getPurchaseItemDAO().list(orderpurchase_combo.getSelectionModel().getSelectedItem())){
            OrderPurchaseIncomingItem incoming_item = new OrderPurchaseIncomingItem();
            incoming_item.setTemp_purchaseitem(item);
            incoming_item.setUnits_arrived(0);
            orderpurchaseincomingitem_list.add(incoming_item);
        }
        orderpurchaseincomingitem_tableview.setItems(orderpurchaseincomingitem_list);
    }
    
}
