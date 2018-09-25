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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddPartRevisionFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<?> productpart_combo;
    @FXML
    private DatePicker revdate_picker;
    @FXML
    private TextField rev_field;
    @FXML
    private TextField basemetal_field;
    @FXML
    private ComboBox<?> finalprocess_combo;
    @FXML
    private TextField specification_field;
    @FXML
    private TextField area_field;
    @FXML
    private TextField baseweight_field;
    @FXML
    private TextField finalprocess_field;
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
