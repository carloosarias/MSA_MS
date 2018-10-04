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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateQuoteFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker quotedate_picker;
    @FXML
    private ComboBox<?> client_combo;
    @FXML
    private ComboBox<?> contact_combo;
    @FXML
    private ComboBox<?> part_combo;
    @FXML
    private ComboBox<?> partrev_combo;
    @FXML
    private TextField area_field;
    @FXML
    private TextField eau_field;
    @FXML
    private ComboBox<?> specificationnumber_combo;
    @FXML
    private TextField process_field;
    @FXML
    private TextArea comments_area;
    @FXML
    private TableColumn<?, ?> listnumber_column;
    @FXML
    private TableColumn<?, ?> metalname_column;
    @FXML
    private TableColumn<?, ?> density_column;
    @FXML
    private TableColumn<?, ?> unitprice_column;
    @FXML
    private TableColumn<?, ?> maximumthickness_column;
    @FXML
    private TableColumn<?, ?> volume_column;
    @FXML
    private TableColumn<?, ?> weight_column;
    @FXML
    private TableColumn<?, ?> estimatedprice_column;
    @FXML
    private TextField margin_field;
    @FXML
    private TextField estimatedtotal_field;
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
