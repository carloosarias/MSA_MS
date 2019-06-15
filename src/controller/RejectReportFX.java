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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class RejectReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private Button delete_button;
    @FXML
    private TableView<?> rejectreport_tableview;
    @FXML
    private TableColumn<?, ?> date_column;
    @FXML
    private TableColumn<?, ?> employee_column;
    @FXML
    private TableColumn<?, ?> departid_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> lot_column;
    @FXML
    private TableColumn<?, ?> po_column;
    @FXML
    private TableColumn<?, ?> line_column;
    @FXML
    private TableColumn<?, ?> packing_column;
    @FXML
    private TableColumn<?, ?> qtyreject_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private DatePicker start_datepicker1;
    @FXML
    private DatePicker end_datepicker1;
    @FXML
    private ComboBox<?> company_combo1;
    @FXML
    private TextField id_field1;
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
    private TextField packing_field2;
    @FXML
    private TextField po_field2;
    @FXML
    private TextField line_field2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField lot_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private ComboBox<?> departlot_combo2;
    @FXML
    private Button save_button2;
    @FXML
    private TextField qtysreject_field2;
    @FXML
    private DatePicker start_datepicker2;
    @FXML
    private DatePicker end_datepicker2;
    @FXML
    private ComboBox<?> company_combo2;
    @FXML
    private TextField id_field2;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
