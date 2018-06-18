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
import model.OrderPurchase;
        //WITH THIS CODE YOU CAN RETRIEVE THE VALUE OF ANOTHER OBJECT BY USING THE ID OF THIS OBJECT
        //description_column.setCellValueFactory(c-> new SimpleStringProperty(msabase.getCompanyDAO().find(c.getValue().getId()).getName()));
        //Useful to populate the PartNumber stuff
/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<OrderPurchase> orderpurchase_tableview;
    @FXML
    private TableColumn<OrderPurchase, Integer> id_column;
    @FXML
    private TableColumn<OrderPurchase, String> supplier_column;
    @FXML
    private TableColumn<OrderPurchase, String> description_column;
    @FXML
    private TableColumn<OrderPurchase, Date> orderdate_column;
    @FXML
    private TableColumn<OrderPurchase, String> employee_column;
    @FXML
    private Button add_button;
    @FXML
    private Button details_button;
    
    private Stage detailsStage;
    
    private static OrderPurchase order_purchase;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList(
        msabase.getOrderPurchaseDAO().list()
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        detailsStage = new Stage();
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        supplier_column.setCellValueFactory(c-> new SimpleStringProperty(msabase.getOrderPurchaseDAO().findCompany(c.getValue()).toString()));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        orderdate_column.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        employee_column.setCellValueFactory(c-> new SimpleStringProperty(msabase.getOrderPurchaseDAO().findEmployee(c.getValue()).toString()));
        details_button.setDisable(orderpurchase_tableview.getSelectionModel().isEmpty() || detailsStage.isShowing());
        updateOrderpurchase_tableview();
        
        orderpurchase_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchase> observable, OrderPurchase oldValue, OrderPurchase newValue) -> {
            details_button.setDisable(orderpurchase_tableview.getSelectionModel().isEmpty() || detailsStage.isShowing());
        });
        
        details_button.setOnAction((ActionEvent) -> {
            showDetails(orderpurchase_tableview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showDetails(new OrderPurchase());
        });
    }
    
    public void showDetails(OrderPurchase order_purchase){
        this.order_purchase = order_purchase;
        try {
            detailsStage = new Stage();
            detailsStage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseDetailsFX.fxml"));
            Scene scene = new Scene(root);
            
            detailsStage.setTitle("Detalles de Órden de Compra");
            detailsStage.setResizable(false);
            detailsStage.initStyle(StageStyle.UTILITY);
            detailsStage.setScene(scene);
            detailsStage.showAndWait();   
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void updateOrderpurchase_tableview(){
        orderpurchase_list.setAll(msabase.getOrderPurchaseDAO().list());
        orderpurchase_tableview.setItems(orderpurchase_list);
    }

    public static OrderPurchase getOrder_purchase() {
        return order_purchase;
    }
    
}
