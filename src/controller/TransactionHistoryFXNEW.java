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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class TransactionHistoryFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<?> partnumber_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<?> incominglot_tableview;
    @FXML
    private TableColumn<?, ?> id_column1;
    @FXML
    private TableColumn<?, ?> date_column1;
    @FXML
    private TableColumn<?, ?> part_column1;
    @FXML
    private TableColumn<?, ?> rev_column1;
    @FXML
    private TableColumn<?, ?> lot_column1;
    @FXML
    private TableColumn<?, ?> quantity_column1;
    @FXML
    private TableColumn<?, ?> boxquantity_column1;
    @FXML
    private TableColumn<?, ?> status_column1;
    @FXML
    private TableView<?> departlot_tableview;
    @FXML
    private TableColumn<?, ?> id_column2;
    @FXML
    private TableColumn<?, ?> date_column2;
    @FXML
    private TableColumn<?, ?> part_column2;
    @FXML
    private TableColumn<?, ?> rev_column2;
    @FXML
    private TableColumn<?, ?> lot_column2;
    @FXML
    private TableColumn<?, ?> quantity_column2;
    @FXML
    private TableColumn<?, ?> boxquantity_column2;
    @FXML
    private TableColumn<?, ?> process_column2;
    @FXML
    private TableColumn<?, ?> status_column2;
    @FXML
    private TableView<?> process_tableview;
    @FXML
    private TableColumn<?, ?> id_column3;
    @FXML
    private TableColumn<?, ?> date_column3;
    @FXML
    private TableColumn<?, ?> part_column3;
    @FXML
    private TableColumn<?, ?> rev_column3;
    @FXML
    private TableColumn<?, ?> lot_column3;
    @FXML
    private TableColumn<?, ?> quantity_column3;
    @FXML
    private TableColumn<?, ?> process_column3;
    @FXML
    private TableColumn<?, ?> status_column3;
    @FXML
    private TableView<?> weekly_tableview;
    @FXML
    private TableColumn<?, ?> startdate_column;
    @FXML
    private TableColumn<?, ?> enddate_column;
    @FXML
    private TableColumn<?, ?> virgin_column;
    @FXML
    private TableColumn<?, ?> rework_column;
    @FXML
    private TableColumn<?, ?> incominglotquantity_column;
    @FXML
    private TableColumn<?, ?> departlotquantity_column;
    @FXML
    private TableColumn<?, ?> departlotrejected_column;
    @FXML
    private TableColumn<?, ?> departlotbalance_column;
    @FXML
    private TableColumn<?, ?> quality_column1;
    @FXML
    private TableColumn<?, ?> quality_column2;
    @FXML
    private TableColumn<?, ?> processquantity_column;
    @FXML
    private Label virgin_label;
    @FXML
    private Label rework_label;
    @FXML
    private Label incominglotquantity_label;
    @FXML
    private Label departlotbalance_label;
    @FXML
    private Label departlotrejected_label;
    @FXML
    private Label departlotquantity_label;
    @FXML
    private Label quality_label1;
    @FXML
    private Label quality_label2;
    @FXML
    private Label processquantity_label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
