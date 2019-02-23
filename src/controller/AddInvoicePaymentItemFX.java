/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Invoice;
import model.InvoicePaymentItem;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddInvoicePaymentItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Invoice> invoice_tableview;
    @FXML
    private TableColumn<Invoice, Integer> id_column;
    @FXML
    private TableColumn<Invoice, String> invoicedate_column;
    @FXML
    private TableColumn<Invoice, String> client_column;
    @FXML
    private TableColumn<Invoice, String> shippingaddress_column;
    @FXML
    private ComboBox<Invoice> invoice_combo;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save_button.disableProperty().bind(invoice_combo.getSelectionModel().selectedItemProperty().isNull());
        setInvoiceTable();
        setInvoiceListItems();
        invoice_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Invoice> observable, Invoice oldValue, Invoice newValue) -> {
            invoice_combo.getSelectionModel().select(newValue);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            InvoicePaymentItem item = new InvoicePaymentItem();
            item.setTemp_invoice(invoice_combo.getSelectionModel().getSelectedItem());
            item.setInvoice_date(invoice_combo.getSelectionModel().getSelectedItem().getInvoice_date());
            item.setInvoice_id(invoice_combo.getSelectionModel().getSelectedItem().getId());
            item.setInvoice_terms(invoice_combo.getSelectionModel().getSelectedItem().getTerms());
            CreateInvoicePaymentReportFX.getInvoice_list().remove(invoice_combo.getSelectionModel().getSelectedItem());
            CreateInvoicePaymentReportFX.getInvoicepaymentitem_queue().add(item);
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void setInvoiceTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        invoicedate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getInvoice_date()))));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany_name()));
        shippingaddress_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getShipping_address()));
    }
    
    public void setInvoiceListItems(){
        invoice_tableview.setItems(FXCollections.observableArrayList(msabase.getInvoiceDAO().filterListByCompany(CreateInvoicePaymentReportFX.getInvoice_list(), CreateInvoicePaymentReportFX.getClientcombo_selection())));
        invoice_combo.setItems(invoice_tableview.getItems());
    }
}
