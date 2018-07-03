/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import model.DepartItem;
import model.DepartLot;
import model.DepartReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartReport> depart_report_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> report_id_column;
    @FXML
    private TableColumn<DepartReport, String> report_employee_column;
    @FXML
    private TableColumn<DepartReport, Date> report_date_column;
    @FXML
    private TableColumn<DepartReport, String> report_client_column;
    @FXML
    private TableView<DepartItem> departitem_tableview;
    @FXML
    private TableColumn<DepartItem, String> part_column;
    @FXML
    private TableColumn<DepartItem, String> revision_column;
    @FXML
    private TableColumn<DepartItem, String> item_qty_column;
    @FXML
    private TableColumn<DepartItem, String> item_boxqty_column;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> lot_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<DepartLot, String> lot_process_column;
    @FXML
    private TableColumn<DepartLot, String> lot_comments_column;
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
