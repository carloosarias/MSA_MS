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
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class QuoteFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<?> part_combo;
    @FXML
    private ComboBox<?> partrev_combo;
    @FXML
    private ComboBox<?> status_combo;
    @FXML
    private Button add_quote;
    @FXML
    private TableView<?> quote_tableview;
    @FXML
    private TableColumn<?, ?> id_column;
    @FXML
    private TableColumn<?, ?> quotedate_column;
    @FXML
    private TableColumn<?, ?> contact_column;
    @FXML
    private TableColumn<?, ?> unitprice_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private TableColumn<?, ?> status_column;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    
}
