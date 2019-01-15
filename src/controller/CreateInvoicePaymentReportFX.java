/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.Invoice;
import model.InvoicePaymentItem;
import model.InvoicePaymentReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateInvoicePaymentReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<InvoicePaymentItem> invoicepaymentitem_tableview;
    @FXML
    private TableColumn<InvoicePaymentItem, Integer> invoiceid_column;
    @FXML
    private TableColumn<InvoicePaymentItem, String> invoicedate_column;
    @FXML
    private TableColumn<InvoicePaymentItem, String> invoicetotal_column;
    @FXML
    private Button add_button;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private TextField checknumber_field;
    @FXML
    private TextField ammountpaid_field;
    @FXML
    private TextField calculated_field;
    @FXML
    private TextArea comments_field;
    @FXML
    private Button save_button;
    
    private Stage add_stage = new Stage();
    
    private static Company clientcombo_selection;
    
    private static List<Invoice> invoice_list;
    
    private static List<InvoicePaymentItem> invoicepaymentitem_queue;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportdate_picker.setValue(LocalDate.now());
        clientcombo_selection = new Company();
        invoice_list = new ArrayList();
        invoicepaymentitem_queue = new ArrayList();
        getInvoiceList();
        setInvoicePaymentItemTable();
        client_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        
        client_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            clientcombo_selection = newValue;
            clearInvoicePaymentItemQueue();
        });
        
       add_button.setOnAction((ActionEvent) -> {
           showAdd_stage();
           updateTableList();
       });
       
       save_button.setOnAction((ActionEvent) -> {
          if(!testFields()){
              return;
          }
          else{
              saveInvoicePaymentReport();
              Stage stage = (Stage) root_hbox.getScene().getWindow();
              stage.close();
          }
       });
               
    }
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(invoicepaymentitem_queue.isEmpty()){
            invoicepaymentitem_tableview.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(client_combo.getSelectionModel().isEmpty()){
            client_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(checknumber_field.getText().replace(" ", "").equals("")){
            checknumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(comments_field.getText().replace(" ", "").equals("")){
            comments_field.setText("n/a");
        }
        try{
            Double.parseDouble(ammountpaid_field.getText());
        }catch(Exception e){
            ammountpaid_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        invoicepaymentitem_tableview.setStyle(null);
        reportdate_picker.setStyle(null);
        client_combo.setStyle(null);
        checknumber_field.setStyle(null);
        ammountpaid_field.setStyle(null);
    }
    
    public void saveInvoicePaymentReport(){
        InvoicePaymentReport invoice_payment_report = new InvoicePaymentReport();
        invoice_payment_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        invoice_payment_report.setAmmount_paid(Double.parseDouble(ammountpaid_field.getText()));
        invoice_payment_report.setCheck_number(checknumber_field.getText());
        invoice_payment_report.setComments(comments_field.getText());
        msabase.getInvoicePaymentReportDAO().create(clientcombo_selection, invoice_payment_report);
        saveInvoicePaymentItems(invoice_payment_report);
    }
    
    public void saveInvoicePaymentItems(InvoicePaymentReport invoice_payment_report){
        for(InvoicePaymentItem item: invoicepaymentitem_queue){
            Invoice invoice = msabase.getInvoiceDAO().find(item.getInvoice_id());
            msabase.getInvoicePaymentItemDAO().create(msabase.getInvoiceDAO().find(item.getInvoice_id()), invoice_payment_report, item);
            invoice.setPending(false);
            msabase.getInvoiceDAO().update(invoice);
        }
    }
    
    public void updateTableList(){
        invoicepaymentitem_tableview.setItems(FXCollections.observableArrayList(invoicepaymentitem_queue));
        double total = 0;
        for(InvoicePaymentItem item: invoicepaymentitem_tableview.getItems()){
            total += Double.parseDouble(invoicetotal_column.getCellData(item));
        }
        calculated_field.setText(""+total);
    };
    public void getInvoiceList(){
        invoice_list = msabase.getInvoiceDAO().listPending(true);
    }
    public void clearInvoicePaymentItemQueue(){
        getInvoiceList();
        invoicepaymentitem_queue.clear();
        updateTableList();
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddInvoicePaymentItemFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar Factura");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(CreateInvoicePaymentReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setInvoicePaymentItemTable(){
        invoiceid_column.setCellValueFactory(new PropertyValueFactory<>("invoice_id"));
        invoicedate_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceDAO().find(c.getValue().getInvoice_id()).getInvoice_date()));
        invoicetotal_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceDAO().findTotal(msabase.getInvoiceDAO().find(c.getValue().getInvoice_id()))));
    }

    public static List<Invoice> getInvoice_list() {
        return invoice_list;
    }

    public static List<InvoicePaymentItem> getInvoicepaymentitem_queue() {
        return invoicepaymentitem_queue;
    }
    
    public static Company getClientcombo_selection(){
        return clientcombo_selection;
    }
}
