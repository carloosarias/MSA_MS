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
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateMantainanceReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker date_picker;
    @FXML
    private ComboBox<?> employee_combo;
    @FXML
    private ComboBox<?> equipment_combo;
    @FXML
    private TableView<?> mantainancecheck_tableview;
    @FXML
    private TableColumn<?, ?> name_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private TableColumn<?, ?> details_column;
    @FXML
    private TableColumn<?, ?> checkvalue_column;
    @FXML
    private Button submit_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
