/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.InvoicePaymentItem;
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
    private TableView<InvoicePaymentItem> invoiceitem_tableview;
    @FXML
    private TableColumn<InvoicePaymentItem, String> invoiceid_column;
    @FXML
    private TableColumn<InvoicePaymentItem, String> invoicedate_column;
    @FXML
    private TableColumn<InvoicePaymentItem, String> invoicetotal_column;
    @FXML
    private TableColumn<InvoicePaymentItem, String> terms_column;
    @FXML
    private TextField caculated_field;
    @FXML
    private TextField ammountpaid_field;
    @FXML
    private TextField balance_field;
    @FXML
    private Button add_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvoicePaymentReportTable();
    }

    public void setInvoicePaymentReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getInvoicePaymentReportDAO().findCompany(c.getValue()).toString()));
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        ammountpaid_column.setCellValueFactory(new PropertyValueFactory<>("ammount_paid"));
        checknumber_column.setCellValueFactory(new PropertyValueFactory<>("check_number"));
        ammountpaid_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void setInvoicePaymentItemTable(){
        
    }
    
}
