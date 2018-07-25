/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.InvoicePaymentReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoicePaymentReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<InvoicePaymentReport> invoicepayment_tableview;
    @FXML
    private TableColumn<InvoicePaymentReport, Integer> id_column;
    @FXML
    private TableColumn<InvoicePaymentReport, String> client_column;
    @FXML
    private TableColumn<InvoicePaymentReport, Date> reportdate_column;
    @FXML
    private TableColumn<InvoicePaymentReport, Double> ammountpaid_column;
    @FXML
    private TableColumn<InvoicePaymentReport, String> checknumber_column;
    @FXML
    private TableColumn<InvoicePaymentReport, String> comments_column;
    @FXML
    private TableView<?> invoiceitem_tableview;
    @FXML
    private TableColumn<?, ?> invoiceid_column;
    @FXML
    private TableColumn<?, ?> invoicedate_column;
    @FXML
    private TableColumn<?, ?> invoicetotal_column;
    @FXML
    private TableColumn<?, ?> terms_column;
    @FXML
    private TextField caculated_field;
    @FXML
    private TextField ammountpaid_field;
    @FXML
    private TextField balance_field;
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
