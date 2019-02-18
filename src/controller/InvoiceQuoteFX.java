/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Invoice;
import model.InvoiceItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoiceQuoteFX implements Initializable {
    
    @FXML
    private CheckBox processfilter_checkbox;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<InvoiceItem> invoicequote_tableview;
    @FXML
    private TableColumn<InvoiceItem, String> partnumber_column;
    @FXML
    private TableColumn<InvoiceItem, String> rev_column;
    @FXML
    private TableColumn<InvoiceItem, String> process_column;
    @FXML
    private TableColumn<InvoiceItem, String> quoteid_column;
    @FXML
    private TableColumn<InvoiceItem, String> quotedate_column;
    @FXML
    private TableColumn<InvoiceItem, String> unitprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> totalinvoiced_column;
    @FXML
    private TableColumn<InvoiceItem, String> totalvalue_column;
    
    private List<InvoiceItem> invoiceitem_list = new ArrayList();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvoiceQuoteTableView();
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        setInvoiceQuoteItems();
        
        processfilter_checkbox.setOnAction((ActionEvent) -> {
            startdate_picker.fireEvent(ActionEvent);
        });
        
        process_combo.setOnAction((ActionEvent) -> {
            if(processfilter_checkbox.isSelected()){
                filter_list();
            }
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            setInvoiceQuoteItems();
            if(processfilter_checkbox.isSelected()){
                filter_list();
            }
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            startdate_picker.fireEvent(ActionEvent);
        });
    }    
    
    public void setInvoiceQuoteTableView(){
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_number()));
        rev_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_number()));
        process_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSpec_process()));
        quoteid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuote_id()));
        quotedate_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuote().getQuote_date().toString()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuote().getEstimated_total()));
        totalinvoiced_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTotal_invoiced()));
        totalvalue_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTotal_value()));
    }
    
    public void filter_list(){
        if(process_combo.getSelectionModel().isEmpty()){
            return;
        }
        List<InvoiceQuote> filtered_list = new ArrayList();
        for(InvoiceQuote item : invoicequote_list){
            if(item.getQuote().getSpec_process().equalsIgnoreCase(process_combo.getSelectionModel().getSelectedItem())){
                filtered_list.add(item);
            }
        }
        
        invoicequote_tableview.setItems(FXCollections.observableArrayList(filtered_list));
    }
    
    public void setInvoiceQuoteItems(){
        invoiceitem_list.clear();
        for(Invoice invoice : msabase.getInvoiceDAO().listDateRange(DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()))){
            for(InvoiceItem invoiceitem : msabase.getInvoiceItemDAO().list(invoice)){
                invoiceitem_list.add(item);
            }
        }
        
        for(int i = 0; i < invoiceitem_list.size(); i++){
            for(int j = 0; j < invoiceitem_list.size(); j++){
                if(invoiceitem_list.get(i).getQuote().equals(invoiceitem_list.get(j).getQuote()) && i != j){
                    invoiceitem_list.get(i).setTotal_invoiced(invoiceitem_list.get(i).getTotal_invoiced() + invoiceitem_list.get(j).getTotal_invoiced());
                    invoiceitem_list.remove(j);
                }
            }
        }
        
        invoicequote_tableview.setItems(FXCollections.observableArrayList(invoiceitem_list));
    }
}
