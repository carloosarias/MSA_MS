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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddInvoiceItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> departlot_tableview;
    @FXML
    private TableColumn<?, ?> remision_column;
    @FXML
    private TableColumn<?, ?> part_column;
    @FXML
    private TableColumn<?, ?> revision_column;
    @FXML
    private TableColumn<?, ?> lot_column;
    @FXML
    private TableColumn<?, ?> lot_qty;
    @FXML
    private TableColumn<?, ?> lot_boxqty_column;
    @FXML
    private TextField terms_field;
    @FXML
    private TextField fob_field;
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
