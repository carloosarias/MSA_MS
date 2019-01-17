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
import model.OrderPurchase;


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
    private ComboBox<?> company_combo;
    @FXML
    private ComboBox<?> companyaddress_combo;
    @FXML
    private TextField exchangerate_field;
    @FXML
    private TableColumn<?, ?> productid_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> unitmeasure_column;
    @FXML
    private TableColumn<?, ?> unitprice_column;
    @FXML
    private TableColumn<?, ?> unitsordered_column;
    @FXML
    private TableColumn<?, ?> price_column;
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
    }
    
    public void setOrderPurchaseTable(){
        orderid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public static void updateOrderPurchaseList(DAOFactory msabase){
        orderpurchase_list.setAll(msabase.getOrderPurchaseDAO().list());
    }
    
}
