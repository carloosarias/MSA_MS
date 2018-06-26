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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<?> employee_combo;
    @FXML
    private ComboBox<?> company_combo;
    @FXML
    private TextField ponumber_field;
    @FXML
    private TextField packinglist_field;
    @FXML
    private ListView<?> incomingitem_listview;
    @FXML
    private ComboBox<?> part_combo;
    @FXML
    private ComboBox<?> partrev_combo;
    @FXML
    private Button item_add_button;
    @FXML
    private Button item_delete_button;
    @FXML
    private ListView<?> incominglot_listview;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private TextField boxquantity_field;
    @FXML
    private ComboBox<?> status_combo;
    @FXML
    private TextArea comments_area;
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
