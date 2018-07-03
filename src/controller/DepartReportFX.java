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
public class DepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> depart_report_tableview;
    @FXML
    private TableColumn<?, ?> report_id_column;
    @FXML
    private TableColumn<?, ?> report_employee_column;
    @FXML
    private TableColumn<?, ?> report_date_column;
    @FXML
    private TableColumn<?, ?> report_client_column;
    @FXML
    private TableView<?> departitem_tableview;
    @FXML
    private TableColumn<?, ?> part_column;
    @FXML
    private TableColumn<?, ?> revision_column;
    @FXML
    private TableColumn<?, ?> item_qty_column;
    @FXML
    private TableColumn<?, ?> item_boxqty_column;
    @FXML
    private TableView<?> departlot_tableview;
    @FXML
    private TableColumn<?, ?> lot_column;
    @FXML
    private TableColumn<?, ?> lot_qty;
    @FXML
    private TableColumn<?, ?> lot_boxqty_column;
    @FXML
    private TableColumn<?, ?> lot_status_column;
    @FXML
    private TableColumn<?, ?> lot_comments_column;
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
