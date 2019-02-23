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
import model.OrderPurchaseIncomingReport;
import model.PurchaseItem;
import msa_ms.MainApp;
import static msa_ms.MainApp.setDatePicker;

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
    private TableColumn<OrderPurchaseIncomingItem, String> serialnumber_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> description_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, Double> quantity_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> unitmeasure_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, Integer> unitsordered_column;
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
        reportdate_picker.setValue(LocalDate.now());
        setDatePicker(reportdate_picker);
        comments_area.setText("N/A");
        employee_combo.setItems(FXCollections.observableArrayList(MainApp.current_employee));
        employee_combo.getSelectionModel().selectFirst();
        status_combo.setItems(FXCollections.observableArrayList(MainApp.orderpurchase_status));
        
        //Fill this list with Pending and Incomplete OrderPurchases
        orderpurchase_combo.getItems().setAll(msabase.getOrderPurchaseDAO().listOfStatus(MainApp.orderpurchase_status.get(0)));
        orderpurchase_combo.getItems().addAll(msabase.getOrderPurchaseDAO().listOfStatus(MainApp.orderpurchase_status.get(1)));
        
        status_combo.disableProperty().bind(orderpurchase_combo.getSelectionModel().selectedItemProperty().isNull());
        save_button.disableProperty().bind(status_combo.getSelectionModel().selectedItemProperty().isNull());
        orderpurchaseincomingitem_tableview.disableProperty().bind(orderpurchase_combo.getSelectionModel().selectedItemProperty().isNull());
        setOrderPurchaseIncomingItemTable();
        
        
        orderpurchase_combo.setOnAction((ActionEvent) -> {
            try{
                updateOrderPurchaseIncomingItemTable();
                status_combo.getSelectionModel().select(orderpurchase_combo.getSelectionModel().getSelectedItem().getStatus());
            }
            catch(Exception e){
                orderpurchaseincomingitem_tableview.getItems().clear();
            }
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveOrderPurchaseIncomingReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    
    public void saveOrderPurchaseIncomingReport(){
        OrderPurchaseIncomingReport report = new OrderPurchaseIncomingReport();
        report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        report.setComments(comments_area.getText());
        try{
            msabase.getOrderPurchaseIncomingReportDAO().create(orderpurchase_combo.getSelectionModel().getSelectedItem(), employee_combo.getSelectionModel().getSelectedItem(), report);
        }catch(DAOException e){
            System.out.println("Failed to generate OrderPurchaseIncomingReport; DB Entries were not saved\n\n\n"+e.getMessage());
            return;
        }
        System.out.println("Created report: "+report.getId());
        saveOrderPurchaseIncomingItems(report);
    }
    
    public void saveOrderPurchaseIncomingItems(OrderPurchaseIncomingReport report){
        try{
            for(OrderPurchaseIncomingItem item : orderpurchaseincomingitem_tableview.getItems()){
                msabase.getOrderPurchaseIncomingItemDAO().create(report, item.getTemp_purchaseitem(), item);
            }
        }catch(DAOException e){
            for(OrderPurchaseIncomingItem item : msabase.getOrderPurchaseIncomingItemDAO().list(report)){
                msabase.getOrderPurchaseIncomingItemDAO().delete(item);
            }
            msabase.getOrderPurchaseIncomingReportDAO().delete(report);
            System.out.println("Failed to generate OrderPurchaseIncomingItem; DB Entries were deleted\n\n\n"+e.getMessage());
            return;
        }
        OrderPurchase order_purchase = msabase.getOrderPurchaseIncomingReportDAO().findOrderPurchase(report);
        order_purchase.setStatus(status_combo.getSelectionModel().getSelectedItem());
        msabase.getOrderPurchaseDAO().update(order_purchase);
    }
    
    public void clearStyle(){
            reportdate_picker.setStyle(null);
            orderpurchase_combo.setStyle(null);
        }

        public boolean testFields(){
            boolean b = true;
            clearStyle();
        
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            reportdate_picker.setValue(LocalDate.now());
            b = false;
        }
        
        if(orderpurchase_combo.getSelectionModel().isEmpty()){
            orderpurchase_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setText("N/A");
        }
        
        return b;
    }
    
    public void setOrderPurchaseIncomingItemTable(){
        serialnumber_column.setCellValueFactory(new PropertyValueFactory<>("productsupplier_serialnumber"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("product_description"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("productsupplier_quantity"));
        unitmeasure_column.setCellValueFactory(new PropertyValueFactory<>("product_unitmeasure"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory<>("purchaseitem_unitsordered"));

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
            incoming_item.setProduct_description(item.getProduct_description());
            incoming_item.setProductsupplier_serialnumber(item.getProductsupplier_serialnumber());
            incoming_item.setProductsupplier_quantity(item.getProductsupplier_quantity());
            incoming_item.setProduct_unitmeasure(item.getProduct_unitmeasure());
            incoming_item.setPurchaseitem_unitsordered(item.getUnits_ordered());
            orderpurchaseincomingitem_list.add(incoming_item);
        }
        orderpurchaseincomingitem_tableview.setItems(orderpurchaseincomingitem_list);
    }
    
}
