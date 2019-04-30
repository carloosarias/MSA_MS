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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Company;
import model.DepartLot;
import model.Employee;
import model.IncomingReport_1;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class IncomingReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<IncomingReport_1> incomingreport_tableview;
    @FXML
    private TableColumn<IncomingReport_1, String> counter_column;
    @FXML
    private TableColumn<IncomingReport_1, String> date_column;
    @FXML
    private TableColumn<IncomingReport_1, Employee> employee_column;
    @FXML
    private TableColumn<IncomingReport_1, Company> client_column;
    @FXML
    private TableColumn<IncomingReport_1, String> partnumber_column;
    @FXML
    private TableColumn<IncomingReport_1, String> partrevision_column;
    @FXML
    private TableColumn<IncomingReport_1, String> lot_column;
    @FXML
    private TableColumn<IncomingReport_1, String> packing_column;
    @FXML
    private TableColumn<IncomingReport_1, String> po_column;
    @FXML
    private TableColumn<IncomingReport_1, String> line_column;
    @FXML
    private TableColumn<IncomingReport_1, Integer> qtyin_column;
    @FXML
    private TableColumn<IncomingReport_1, String> comments_column;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private DatePicker start_datepicker11;
    @FXML
    private DatePicker end_datepicker1;
    @FXML
    private TextField packing_field1;
    @FXML
    private TextField po_field1;
    @FXML
    private TextField line_field1;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField lot_field1;
    @FXML
    private TextField rev_field1;
    @FXML
    private Button reset_button;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private TextField lot_field2;
    @FXML
    private TextField qtyin_field2;
    @FXML
    private TextField packing_field2;
    @FXML
    private TextField po_field2;
    @FXML
    private TextField line_field2;
    @FXML
    private Button save_button2;
    @FXML
    private Button delete_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
