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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.DepartLot;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddInvoiceItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, Integer> remision_column;
    @FXML
    private TableColumn<DepartLot, String> part_column;
    @FXML
    private TableColumn<DepartLot, String> revision_column;
    @FXML
    private TableColumn<DepartLot, String> lot_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private ComboBox<DepartLot> departlot_combo;
    @FXML
    private TextField unitprice_field;
    @FXML
    private TextField comments_field;
    @FXML
    private Button save_button;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
