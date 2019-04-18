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
import model.POQuery;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class POQueryFX implements Initializable {

    @FXML
    private TableView<POQuery> poquery_tableview;
    @FXML
    private TableColumn<?, ?> company_column;
    @FXML
    private TableColumn<?, ?> ponumber_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> rev_column;
    @FXML
    private TableColumn<?, ?> incoming_column;
    @FXML
    private TableColumn<?, ?> depart_column;
    @FXML
    private TableColumn<?, ?> scrap_column;
    @FXML
    private TableColumn<?, ?> balance_column;
    @FXML
    private TextField ponumber_field;
    @FXML
    private ComboBox<?> company_combo;
    @FXML
    private TextField partnumber_field;
    @FXML
    private Button reset_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
