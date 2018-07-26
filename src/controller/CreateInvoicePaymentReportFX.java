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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateInvoicePaymentReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> invoicepaymentitem_tableview;
    @FXML
    private TableColumn<?, ?> invoiceid_column;
    @FXML
    private TableColumn<?, ?> invoicedate_column;
    @FXML
    private TableColumn<?, ?> invoicetotal_column;
    @FXML
    private Button add_button;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<?> client_combo;
    @FXML
    private TextField checknumber_field;
    @FXML
    private TextField ammountpaid_field;
    @FXML
    private TextField calculated_field;
    @FXML
    private TextField balance_field;
    @FXML
    private TextArea comments_field;
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
