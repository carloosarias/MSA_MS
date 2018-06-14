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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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
    private TableView<OrderPurchase> orderpurchase_table;
    @FXML
    private TableColumn<OrderPurchase, Integer> id_column;
    @FXML
    private TableColumn<OrderPurchase, String> description_column;
    @FXML
    private TableColumn<OrderPurchase, Date> orderdate_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList(
        msabase.getOrderPurchaseDAO().list()
    );
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        orderdate_column.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        orderpurchase_table.setItems(orderpurchase_list);
    }
    
}
