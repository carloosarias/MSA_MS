/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.OrderPurchaseIncomingItem;
import model.OrderPurchaseIncomingReport;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<OrderPurchaseIncomingReport> orderpurchaseincomingreport_tableview;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, Integer> orderpurchaseid_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> company_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> reportdate_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> employee_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingReport, String> comments_column;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<OrderPurchaseIncomingItem> orderpurchaseincomingitem_tableview;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> serialnumber_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> description_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, String> quantity_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, Integer> unitsordered_column;
    @FXML
    private TableColumn<OrderPurchaseIncomingItem, Integer> unitsarrived_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
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
        
        details_tab.disableProperty().bind(orderpurchaseincomingreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        orderpurchaseincomingreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchaseIncomingReport> observable, OrderPurchaseIncomingReport oldValue, OrderPurchaseIncomingReport newValue) -> {
            updateOrderPurchaseIncomingItemTable(newValue);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateOrderPurchaseIncomingReportTable();
        });
    }
    
    public void updateOrderPurchaseIncomingItemTable(OrderPurchaseIncomingReport report){
        try{
            orderpurchaseincomingitem_tableview.getItems().setAll(msabase.getOrderPurchaseIncomingItemDAO().list(report));
        }catch(Exception e){
            orderpurchaseincomingitem_tableview.getItems().clear();
        }
    }
    
    public void updateOrderPurchaseIncomingReportTable(){
        orderpurchaseincomingreport_tableview.getItems().setAll(msabase.getOrderPurchaseIncomingReportDAO().list());
    }
    
    public void setOrderPurchaseIncomingReportTable(){
        orderpurchaseid_column.setCellValueFactory(new PropertyValueFactory<>("orderpurchase_id"));
        company_column.setCellValueFactory(new PropertyValueFactory<>("orderpurchase_companyname"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_employeename"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void setOrderPurchaseIncomingItemTable(){
        serialnumber_column.setCellValueFactory(new PropertyValueFactory<>("productsupplier_serialnumber"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("product_description"));
        quantity_column .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProductsupplier_quantity()+" "+c.getValue().getProduct_unitmeasure()));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory<>("purchaseitem_unitsordered"));
        unitsarrived_column.setCellValueFactory(new PropertyValueFactory<>("units_arrived"));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
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
