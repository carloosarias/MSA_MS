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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PartRevisionFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<?> filter_combo;
    @FXML
    private ListView<?> revision_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private DatePicker revdate_picker;
    @FXML
    private TextField finalprocess_field;
    @FXML
    private TextField initialweight_field;
    @FXML
    private Button specification_button;
    @FXML
    private TextField rev_field;
    @FXML
    private TextField basemetal_field;
    @FXML
    private TextField area_field;
    @FXML
    private TextField finalweight_field;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
