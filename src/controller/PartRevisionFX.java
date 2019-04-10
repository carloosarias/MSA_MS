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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PartRevisionFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private Tab revision_tab;
    @FXML
    private TableView<?> partrevision_tableview;
    @FXML
    private TableColumn<?, ?> partnumber_column1;
    @FXML
    private TableColumn<?, ?> rev_column;
    @FXML
    private TableColumn<?, ?> revdate_column;
    @FXML
    private TableColumn<?, ?> basemetal_column;
    @FXML
    private TableColumn<?, ?> specnumber_column;
    @FXML
    private TableColumn<?, ?> area_column;
    @FXML
    private TableColumn<?, ?> baseweight_column;
    @FXML
    private TableColumn<?, ?> finalweight_column;
    @FXML
    private Button delete_button;
    @FXML
    private ComboBox<?> company_combo1;
    @FXML
    private ComboBox<?> metal_combo1;
    @FXML
    private ComboBox<?> spec_combo1;
    @FXML
    private Button reset_button;
    @FXML
    private TextField part_field1;
    @FXML
    private ComboBox<?> company_combo2;
    @FXML
    private ComboBox<?> part_combo2;
    @FXML
    private ComboBox<?> metal_combo2;
    @FXML
    private ComboBox<?> spec_combo2;
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
