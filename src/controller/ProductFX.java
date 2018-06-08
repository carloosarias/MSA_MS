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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {

    @FXML
    private ComboBox<?> typefilter_combo;
    @FXML
    private ComboBox<?> filter_combo;
    @FXML
    private ListView<?> product_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_button;
    @FXML
    private TextField name_field;
    @FXML
    private ComboBox<?> type_combo;
    @FXML
    private CheckBox active_check;
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
        // TODO
    }    
    
}
