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
    private CheckBox processfilter_checkbox;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, String> partnumber_column;
    @FXML
    private TableColumn<Quote, String> rev_column;
    @FXML
    private TableColumn<Quote, String> process_column;
    @FXML
    private TableColumn<Quote, String> quoteid_column;
    @FXML
    private TableColumn<Quote, String> quotedate_column;
    @FXML
    private TableColumn<Quote, String> unitprice_column;
    @FXML
    private TableColumn<Quote, String> totalinvoiced_column;
    @FXML
    private TableColumn<Quote, String> totalvalue_column;
    
    private Integer temp_totalinvoiced;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setQuoteTable();
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        updateQuoteTable();
        
        processfilter_checkbox.setOnAction((ActionEvent) -> {
            updateQuoteTable();
        });
        
        process_combo.setOnAction((ActionEvent) -> {
            updateQuoteTable();
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            updateQuoteTable();
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            updateQuoteTable();
        });
    }    
    
    public void setQuoteTable(){
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_number()));
        rev_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_number()));
        process_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSpec_process()));
        quoteid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getId()));
        quotedate_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuote_date().toString()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getEstimated_total() + " USD"));
        totalinvoiced_column.setCellValueFactory(c -> new SimpleStringProperty(""+getTotal_invoiced(c.getValue())));
        totalvalue_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+getTotal_value(c.getValue())+" USD"));
    }
    
    public void updateQuoteTable(){
        quote_tableview.getItems().setAll(msabase.getInvoiceItemDAO().listDistinctQuote_byProcessDateRange(process_combo.getSelectionModel().getSelectedItem(), processfilter_checkbox.isSelected(), DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue())));
        
    }
    
    public Integer getTotal_invoiced(Quote quote){
        temp_totalinvoiced = 0;
        for(InvoiceItem item : msabase.getInvoiceItemDAO().list(quote, DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()))){
            temp_totalinvoiced += item.getDepartlot_quantity();
        }
        return temp_totalinvoiced;
    }
    
    public Double getTotal_value(Quote quote){
        return temp_totalinvoiced*quote.getEstimated_total();
    }
}
