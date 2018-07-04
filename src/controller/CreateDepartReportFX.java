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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateDepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<?> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<?> company_combo;
    @FXML
    private ComboBox<?> address_combo;
    @FXML
    private ComboBox<?> process_combo;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private ComboBox<?> part_combo;
    @FXML
    private ComboBox<?> partrev_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private TableView<?> departlot_tableview;
    @FXML
    private TableColumn<?, ?> lotnumber_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> revision_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> status_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private Button lot_add_button;
    @FXML
    private Button lot_delete_button;
    @FXML
    private Button save_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
