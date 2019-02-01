/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.OrderPurchaseIncomingItem;
import model.OrderPurchaseIncomingReport;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<OrderPurchaseIncomingReport> orderpurchaseincomingreport_tableview;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, Integer> orderpurchaseincomingreportid_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, Integer> orderpurchaseid_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> company_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, Date> reportdate_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, Integer> employeeid_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> employeename_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> comments_column;
    
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
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
        
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setOrderPurchaseIncomingReportTable();
        setOrderPurchaseIncomingItemTable();
        updateOrderPurchaseIncomingReportTable();
        
        orderpurchaseincomingreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchaseIncomingReport> observable, OrderPurchaseIncomingReport oldValue, OrderPurchaseIncomingReport newValue) -> {
            try{
                updateOrderPurchaseIncomingItemTable(newValue);
            }catch(Exception e){
                orderpurchaseincomingitem_tableview.getItems().clear();
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateOrderPurchaseIncomingReportTable();
        });
    }
    
    public void updateOrderPurchaseIncomingItemTable(OrderPurchaseIncomingReport report){
        orderpurchaseincomingitem_tableview.getItems().setAll(msabase.getOrderPurchaseIncomingItemDAO().list(report));
    }
    
    public void updateOrderPurchaseIncomingReportTable(){
        orderpurchaseincomingreport_tableview.getItems().setAll(msabase.getOrderPurchaseIncomingReportDAO().list());
    }
    
    public void setOrderPurchaseIncomingReportTable(){
        orderpurchaseincomingreportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderpurchaseid_column.setCellValueFactory(new PropertyValueFactory<>("orderpurchase_id"));
        company_column.setCellValueFactory(new PropertyValueFactory<>("orderpurchase_comapanyname"));
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        employeeid_column.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        employeename_column.setCellValueFactory(new PropertyValueFactory<>("employee_employeename"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void setOrderPurchaseIncomingItemTable(){
    productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(msabase.getOrderPurchaseIncomingItemDAO().findPurchaseItem(c.getValue()))).getId()));
    description_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(msabase.getOrderPurchaseIncomingItemDAO().findPurchaseItem(c.getValue()))).getDescription()));
    quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getPurchaseItemDAO().findProductSupplier(msabase.getOrderPurchaseIncomingItemDAO().findPurchaseItem(c.getValue())).getQuantity()));
    unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getProductSupplierDAO().findProduct(msabase.getPurchaseItemDAO().findProductSupplier(msabase.getOrderPurchaseIncomingItemDAO().findPurchaseItem(c.getValue()))).getUnit_measure()));
    unitsordered_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getOrderPurchaseIncomingItemDAO().findPurchaseItem(c.getValue()).getUnits_ordered()));
    unitsarrived_column.setCellValueFactory(new PropertyValueFactory<>("units_arrived"));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateOrderPurchaseIncomingReportFX.fxml"));
            Scene scene = new Scene(root);

            add_stage.setTitle("Nuevo Reporte Reciba de Orden de Compra");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
