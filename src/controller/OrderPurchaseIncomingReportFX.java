/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> orderpurchaseincomingreport_tableview;
    @FXML
    private TableColumn<?, ?> orderpurchaseincomingreportid_column;
    @FXML
    private TableColumn<?, ?> orderpurchaseid_column;
    @FXML
    private TableColumn<?, ?> company_column;
    @FXML
    private TableColumn<?, ?> reportdate_column;
    @FXML
    private TableColumn<?, ?> employeeid_column;
    @FXML
    private TableColumn<?, ?> employeename_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private TableView<?> orderpurchaseincomingitem_tableview;
    @FXML
    private TableColumn<?, ?> productid_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> unitmeasure_column;
    @FXML
    private TableColumn<?, ?> unitsordered_column;
    @FXML
    private TableColumn<?, ?> unitsarrived_column;
    @FXML
    private Button add_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
