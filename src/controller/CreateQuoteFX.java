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
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.CompanyContact;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
import model.QuoteItem;
import model.SpecificationItem;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateQuoteFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker quotedate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private ComboBox<CompanyContact> contact_combo;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private Button save_button;
    
    public static ProductPart quote_part;
    public static PartRevision quote_rev;
    public static Quote quote;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quote = null;
        quotedate_picker.setValue(LocalDate.now());
        setDatePicker(quotedate_picker);
        
        client_combo.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        part_combo.getItems().setAll(msabase.getProductPartDAO().listActive(true));
        
        contact_combo.disableProperty().bind(client_combo.getSelectionModel().selectedItemProperty().isNull());
        partrev_combo.disableProperty().bind(part_combo.getSelectionModel().selectedItemProperty().isNull());
        
        client_combo.setOnAction((ActionEvent) -> {
            contact_combo.getItems().setAll(msabase.getCompanyContactDAO().list(client_combo.getSelectionModel().getSelectedItem(), true));
        });
        
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            partrev_combo.getItems().setAll(msabase.getPartRevisionDAO().list(newValue, true));
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }else{
                createQuote();
                Stage stage = (Stage) root_hbox.getScene().getWindow();
                stage.close();
            }
        });
    }
    
    public void createQuote(){
        quote = new Quote();
        quote.setQuote_date(DAOUtil.toUtilDate(quotedate_picker.getValue()));
        quote.setEstimated_annual_usage(0);
        quote.setComments("N/A");
        quote.setEstimated_total(0);
        quote.setActive(true);
        quote_part = part_combo.getSelectionModel().getSelectedItem();
        quote_rev = partrev_combo.getSelectionModel().getSelectedItem();
        
        msabase.getQuoteDAO().create(partrev_combo.getSelectionModel().getSelectedItem(), contact_combo.getSelectionModel().getSelectedItem(), quote);
        
        createQuoteItems();
    }
    
    public void createQuoteItems(){
        for(SpecificationItem specification_item : msabase.getSpecificationItemDAO().list(quote, true)){
            QuoteItem quote_item = new QuoteItem();
            quote_item.setUnit_price(0.0);
            msabase.getQuoteItemDAO().create(specification_item, quote, quote_item);
        }
    }
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(quotedate_picker.getValue() == null){
            quotedate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(contact_combo.getSelectionModel().isEmpty()){
            contact_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(partrev_combo.getSelectionModel().isEmpty()){
            partrev_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        quotedate_picker.setStyle(null);
        contact_combo.setStyle(null);
    }
}
