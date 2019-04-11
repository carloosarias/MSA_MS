/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.CompanyAddress;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
import model.QuoteItem;
import model.Specification;
import msa_ms.MainApp;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;



/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class QuoteFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, String> quotedate_column;
    @FXML
    private TableColumn<Quote, String> company_column;
    @FXML
    private TableColumn<Quote, String> contact_column;
    @FXML
    private TableColumn<Quote, String> eau_column;
    @FXML
    private TableColumn<Quote, String> comments_column;
    @FXML
    private TableColumn<Quote, String> actualprice_column;
    @FXML
    private TableColumn<Quote, String> estimatedtotal_column;
    @FXML
    private TableView<QuoteItem> quoteitem_tableview;
    @FXML
    private TableColumn<QuoteItem, String> listnumber_column;
    @FXML
    private TableColumn<QuoteItem, String> metal_name_column;
    @FXML
    private TableColumn<QuoteItem, String> density_column;
    @FXML
    private TableColumn<QuoteItem, String> unitprice_column;
    @FXML
    private TableColumn<QuoteItem, String> maximumthickness_column;
    @FXML
    private TableColumn<QuoteItem, String> volume_column;
    @FXML
    private TableColumn<QuoteItem, String> weight_column;
    @FXML
    private TableColumn<QuoteItem, String> estimatedprice_column;
    @FXML
    private Label specification_label;
    @FXML
    private Label process_label;
    @FXML
    private Label area_label;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            
        setQuoteTable();
        setQuoteItemTable();
        
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        
        partrev_combo.disableProperty().bind(partrev_combo.itemsProperty().isNull());
        pdf_button.disableProperty().bind(quote_tableview.getSelectionModel().selectedItemProperty().isNull());
        disable_button.disableProperty().bind(quote_tableview.getSelectionModel().selectedItemProperty().isNull()); 
        
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(new Company(), new Metal(), new Specification(), newValue.getPart_number())));
            partrev_combo.getSelectionModel().selectFirst();
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            updateQuoteTable();
        });
        
        quote_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Quote> observable, Quote oldValue, Quote newValue) -> {
            updateQuoteItemTable();
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(quote_tableview.getSelectionModel().getSelectedItem()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableQuote();
            updateQuoteTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            try{
                CreateQuoteFX.quote.getId();
                part_combo.getSelectionModel().select(CreateQuoteFX.quote_part);
                partrev_combo.getSelectionModel().select(CreateQuoteFX.quote_rev);
                quote_tableview.scrollTo(CreateQuoteFX.quote);
                quote_tableview.getSelectionModel().select(CreateQuoteFX.quote);
            }catch(Exception e){
                System.out.println("quote is empty");
                updateQuoteTable();
            }
        });
    }
    
    public void disableQuote(){
        quote_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getQuoteDAO().update(quote_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateQuoteFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Cotización");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(QuoteFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateQuoteTable(){
        try{
            quote_tableview.getItems().setAll(msabase.getQuoteDAO().list(partrev_combo.getSelectionModel().getSelectedItem(), true));
        }catch(Exception e){
            quote_tableview.getItems().clear();
        }
    }
    
    public void updateQuoteItemTable(){
        try{
            quoteitem_tableview.getItems().setAll(msabase.getQuoteItemDAO().list(quote_tableview.getSelectionModel().getSelectedItem()));
            specification_label.setText(quote_tableview.getSelectionModel().getSelectedItem().getSpec_number());
            process_label.setText(quote_tableview.getSelectionModel().getSelectedItem().getSpec_process());
            area_label.setText(df.format(quote_tableview.getSelectionModel().getSelectedItem().getPartrev_area())+" IN²");
        }catch(Exception e){
            quoteitem_tableview.getItems().clear();
            specification_label.setText("N/A");
            process_label.setText("N/A");
            area_label.setText("N/A");
        }
    }
    
    public void setQuoteItemTable(){
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+(quoteitem_tableview.getItems().indexOf(c.getValue())+1)));
        metal_name_column.setCellValueFactory(new PropertyValueFactory<>("metal_name"));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMetal_densityGMM())+" G/MM³"));
        
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getUnit_price())+" USD"));
        unitprice_column.setCellFactory(TextFieldTableCell.forTableColumn());
        unitprice_column.setOnEditCommit((TableColumn.CellEditEvent<QuoteItem, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnit_price(getUnit_priceValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getQuoteItemDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            quoteitem_tableview.refresh();
            quote_tableview.refresh();
        });
        
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getSpecitem_maximumthicknessMM())+" MM"));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getVolume())+" MM³"));
        weight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getWeight())+" G"));
        estimatedprice_column.setCellValueFactory(c -> new SimpleStringProperty("$"+df.format(c.getValue().getEstimatedPrice())+" USD"));
    }
    
    public void setQuoteTable(){
        contact_column.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        quotedate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getQuote_date()))));
        actualprice_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getCalculatedPrice())+" USD"));
        
        eau_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getEstimated_annual_usage())));
        eau_column.setCellFactory(TextFieldTableCell.forTableColumn());
        eau_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEstimated_annual_usage(getEstimated_annual_usageValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            quote_tableview.refresh();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            quote_tableview.refresh();
        });
        
        estimatedtotal_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getEstimated_total())+" USD"));
        estimatedtotal_column.setCellFactory(TextFieldTableCell.forTableColumn());
        estimatedtotal_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEstimated_total(getEstimated_totalValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            quote_tableview.refresh();
        });
    }
    
    private File buildPDF(Quote quote) throws IOException {
        
        Path template = Files.createTempFile("Quote", ".pdf");
        template.toFile().deleteOnExit();
        
        try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/QuoteTemplate.pdf")) {
            Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
        }

        Path output = Files.createTempFile("CotizaciónPDF", ".pdf");
        template.toFile().deleteOnExit();  
        
        PdfDocument pdf = new PdfDocument(
            new PdfReader(template.toFile()),
            new PdfWriter(output.toFile())
        );
            
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        
        fields.get("quote_id").setValue(""+quote.getId());
        fields.get("date").setValue(quote.getQuote_date().toString());
        fields.get("client").setValue(quote.getCompany_name());
        fields.get("contact").setValue(quote.getContact_name());
        fields.get("contact_email").setValue(quote.getContact_email());
        fields.get("contact_number").setValue(quote.getContact_number());
        List<CompanyAddress> company_address = msabase.getCompanyAddressDAO().listActive(msabase.getCompanyDAO().find(quote.getCompany_id()), true);
        if(company_address.isEmpty()){
            fields.get("client_address").setValue("n/a");
        }else{
            fields.get("client_address").setValue(company_address.get(0).getAddress());
        }
        fields.get("part_number").setValue(quote.getPart_number());
        fields.get("revision").setValue(quote.getPart_rev());
        fields.get("description").setValue(quote.getPart_description());
        fields.get("process").setValue(quote.getSpec_number());
        fields.get("eau").setValue(""+quote.getEstimated_annual_usage());
        fields.get("unit_price").setValue("$ "+df.format(quote.getEstimated_total())+ " USD");
        fields.get("comments").setValue(quote.getComments());
        form.flattenFields();
        pdf.close();
        
        return output.toFile();
    }
    
    public Double getUnit_priceValue(QuoteItem quote_item, String unit_price){
        try{
            return Double.parseDouble(unit_price);
        }catch(Exception e){
            return quote_item.getUnit_price();
        }
    }
    
    public Double getEstimated_totalValue(Quote quote, String estimated_total){
        try{
            return Double.parseDouble(estimated_total);
        }catch(Exception e){
            return quote.getEstimated_total();
        }
    }
    
    public Integer getEstimated_annual_usageValue(Quote quote, String estimated_annual_usage){
        try{
            return Integer.parseInt(estimated_annual_usage);
        }catch(Exception e){
            return quote.getEstimated_annual_usage();
        }
    }
}
