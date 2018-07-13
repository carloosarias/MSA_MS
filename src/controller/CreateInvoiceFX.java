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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.InvoiceItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateInvoiceFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker invoicedate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private ComboBox<CompanyAddress> billingaddress_combo;
    @FXML
    private ComboBox<CompanyAddress> shippingaddress_combo;
    @FXML
    private TextField terms_field;
    @FXML
    private TextField fob_field;
    @FXML
    private TextField shippingmethod_field;
    @FXML
    private TableView<InvoiceItem> invoiceitem_tableview;
    @FXML
    private TableColumn<InvoiceItem, String> remision_column;
    @FXML
    private TableColumn<InvoiceItem, String> part_column;
    @FXML
    private TableColumn<InvoiceItem, String> revision_column;
    @FXML
    private TableColumn<InvoiceItem, String> lot_column;
    @FXML
    private TableColumn<InvoiceItem, String> unitprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> lot_qty;
    @FXML
    private TableColumn<InvoiceItem, String> lot_boxqty_column;
    @FXML
    private TableColumn<InvoiceItem, String> lotprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private TextField total_field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
