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
import java.util.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.InvoicePaymentItem;
import model.InvoicePaymentReport;
import static msa_ms.MainApp.getFormattedDate;

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
    private TableColumn<InvoicePaymentReport, String> reportdate_column;
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
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvoicePaymentReportTable();
        setInvoicePaymentItemTable();
        updateInvoicePaymentItems();
        
        invoicepayment_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends InvoicePaymentReport> observable, InvoicePaymentReport oldValue, InvoicePaymentReport newValue) -> {
            setInvoicePaymentReportDetails(newValue);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateInvoicePaymentItems();
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateInvoicePaymentReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de pago.");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoicePaymentReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateInvoicePaymentItems(){
        invoicepayment_tableview.setItems(FXCollections.observableArrayList(msabase.getInvoicePaymentReportDAO().list()));
    }
    
    public void setInvoicePaymentItemTable(){
        invoiceid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getInvoice_id()));
        invoicedate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getInvoice_date()))));
        invoicetotal_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceDAO().findTotal(msabase.getInvoiceDAO().find(c.getValue().getInvoice_id()))));
        terms_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getInvoice_terms()));
    }
    
    
    public void setInvoicePaymentReportDetails(InvoicePaymentReport invoice_payment_report){
        if(invoice_payment_report == null){
            invoiceitem_tableview.getItems().clear();
            ammountpaid_field.setText(null);
            caculated_field.setText(null);
            balance_field.setText(null);
        }else{
            invoiceitem_tableview.setItems(FXCollections.observableArrayList(msabase.getInvoicePaymentItemDAO().list(invoice_payment_report)));
            ammountpaid_field.setText(""+ammountpaid_column.getCellData(invoice_payment_report));
            setTotal();
        }
    }
    
    public void setTotal(){
        double total = 0;
        for(InvoicePaymentItem invoice_payment_item: invoiceitem_tableview.getItems()){
            total += Double.parseDouble(invoicetotal_column.getCellData(invoice_payment_item));
        }
        caculated_field.setText(total+"");
        balance_field.setText(""+(Double.parseDouble(ammountpaid_field.getText()) -  total));
    }
    
    public void setInvoicePaymentReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany_name()));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        ammountpaid_column.setCellValueFactory(new PropertyValueFactory<>("ammount_paid"));
        checknumber_column.setCellValueFactory(new PropertyValueFactory<>("check_number"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
}
