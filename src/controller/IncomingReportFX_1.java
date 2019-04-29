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

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class IncomingReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<?> incominglot_tableview;
    @FXML
    private TableColumn<?, ?> reportid_column;
    @FXML
    private TableColumn<?, ?> rejected_column;
    @FXML
    private TableColumn<?, ?> reportdate_column;
    @FXML
    private TableColumn<?, ?> employee_column;
    @FXML
    private TableColumn<?, ?> client_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> partrevision_column;
    @FXML
    private TableColumn<?, ?> lotnumber_column;
    @FXML
    private TableColumn<?, ?> packinglist_column;
    @FXML
    private TableColumn<?, ?> ponumber_column;
    @FXML
    private TableColumn<?, ?> linenumber_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private ComboBox<?> company_combo1;
    @FXML
    private DatePicker start_datepicker11;
    @FXML
    private DatePicker end_datepicker1;
    @FXML
    private CheckBox rejected_checkbox1;
    @FXML
    private TextField packinglist_field1;
    @FXML
    private TextField ponumber_field1;
    @FXML
    private TextField linenumber_field1;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField lotnumber_field1;
    @FXML
    private TextField rev_field1;
    @FXML
    private Button reset_button;
    @FXML
    private ComboBox<?> company_combo2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private TextField lotnumber_field2;
    @FXML
    private TextField quantity_field2;
    @FXML
    private TextField packinglist_field2;
    @FXML
    private TextField ponumber_field2;
    @FXML
    private TextField linenumber_field2;
    @FXML
    private Button save_button2;
    @FXML
    private ComboBox<?> company_combo3;
    @FXML
    private TextField departreport_field3;
    @FXML
    private TextField partnumber_field3;
    @FXML
    private TextField rev_field3;
    @FXML
    private TextField lotnumber_field3;
    @FXML
    private TextField ponumber_field3;
    @FXML
    private TextField linenumber_field3;
    @FXML
    private ComboBox<?> departlot_combo3;
    @FXML
    private TextField quantity_field3;
    @FXML
    private Button save_button3;
    @FXML
    private Button disable_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
