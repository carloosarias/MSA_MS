/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Invoice;
import model.InvoiceItem;
import model.Quote;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoiceQuoteFX implements Initializable {
    
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<InvoiceQuote> invoicequote_tableview;
    @FXML
    private TableColumn<InvoiceQuote, String> partnumber_column;
    @FXML
    private TableColumn<InvoiceQuote, String> rev_column;
    @FXML
    private TableColumn<InvoiceQuote, String> process_column;
    @FXML
    private TableColumn<InvoiceQuote, String> quoteid_column;
    @FXML
    private TableColumn<InvoiceQuote, String> quotedate_column;
    @FXML
    private TableColumn<InvoiceQuote, String> unitprice_column;
    @FXML
    private TableColumn<InvoiceQuote, String> totalinvoiced_column;
    @FXML
    private TableColumn<InvoiceQuote, String> totalvalue_column;
    
    private List<InvoiceQuote> invoicequote_list = new ArrayList();
    
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
        
        process_combo.setOnAction((ActionEvent) -> {
            filter_list();
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            setInvoiceQuoteItems();
            if(!process_combo.getSelectionModel().isEmpty()){
                filter_list();
            }
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            startdate_picker.fireEvent(ActionEvent);
        });
    }    
    
    public void setInvoiceQuoteTableView(){
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(
                msabase.getPartRevisionDAO().findProductPart(
                        msabase.getQuoteDAO().findPartRevision(
                                c.getValue().getQuote()
                        )
                ).getPart_number()));
        rev_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getQuoteDAO().findPartRevision(c.getValue().getQuote()).getRev()));
        process_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findSpecification(msabase.getQuoteDAO().findPartRevision(c.getValue().getQuote())).getProcess()));
        quoteid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuote().getId()));
        quotedate_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuote().getQuote_date().toString()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuote().getEstimated_total()));
        totalinvoiced_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTotal_invoiced()));
        totalvalue_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTotal_value()));
    }
    
    public void filter_list(){
        List<InvoiceQuote> filtered_list = new ArrayList();
        for(InvoiceQuote item : invoicequote_list){
            if(msabase.getPartRevisionDAO().findSpecification(msabase.getQuoteDAO().findPartRevision(item.getQuote())).getProcess().equalsIgnoreCase(process_combo.getSelectionModel().getSelectedItem())){
                filtered_list.add(item);
            }
        }
        
        invoicequote_tableview.setItems(FXCollections.observableArrayList(filtered_list));
    }
    
    public void setInvoiceQuoteItems(){
        invoicequote_list.clear();
        for(Invoice item : msabase.getInvoiceDAO().listDateRange(Date.valueOf(startdate_picker.getValue()), Date.valueOf(enddate_picker.getValue()))){
            for(InvoiceItem invoiceitem : msabase.getInvoiceItemDAO().list(item)){
                InvoiceQuote invquote = new InvoiceQuote();
                invquote.setQuote(msabase.getInvoiceItemDAO().findQuote(invoiceitem));
                invquote.setTotal_invoiced(msabase.getInvoiceItemDAO().findDepartLot(invoiceitem).getQuantity());
                invoicequote_list.add(invquote);
            }
        }
        
        for(int i = 0; i < invoicequote_list.size(); i++){
            for(int j = 0; j < invoicequote_list.size(); j++){
                if(invoicequote_list.get(i).getQuote().equals(invoicequote_list.get(j).getQuote()) && i != j){
                    invoicequote_list.get(i).setTotal_invoiced(invoicequote_list.get(i).getTotal_invoiced() + invoicequote_list.get(j).getTotal_invoiced());
                    invoicequote_list.remove(j);
                }
            }
        }
        
        invoicequote_tableview.setItems(FXCollections.observableArrayList(invoicequote_list));
    }
    
    public class InvoiceQuote{
        private Quote quote;
        private Integer total_invoiced;

        public Quote getQuote() {
            return quote;
        }

        public void setQuote(Quote quote) {
            this.quote = quote;
        }

        public Integer getTotal_invoiced() {
            return total_invoiced;
        }

        public void setTotal_invoiced(Integer total_invoiced) {
            this.total_invoiced = total_invoiced;
        }

        public Double getTotal_value() {
            return quote.getEstimated_total() * total_invoiced;
        }
        
        
        
    }
}
