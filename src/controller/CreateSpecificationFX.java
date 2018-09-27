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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateSpecificationFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField specificationnumber_field;
    @FXML
    private ComboBox<?> process_combo;
    @FXML
    private TextArea specificationname_area;
    @FXML
    private TableView<?> specificationitem_tableview;
    @FXML
    private TableColumn<?, ?> listnumber_column;
    @FXML
    private TableColumn<?, ?> metal_column;
    @FXML
    private TableColumn<?, ?> minimumthickness_column;
    @FXML
    private TableColumn<?, ?> maximumthickness_column;
    @FXML
    private Button addspecificationitem_button;
    @FXML
    private Button delete_button;
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
