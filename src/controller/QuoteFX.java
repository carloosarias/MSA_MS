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
import dao.JDBC.DAOFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyAddress;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
import model.QuoteItem;
import model.Specification;
import msa_ms.MainApp;



/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class QuoteFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private VBox filter_vbox;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private CheckBox status_checkbox;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, Integer> id_column;
    @FXML
    private TableColumn<Quote, Date> quotedate_column;
    @FXML
    private TableColumn<Quote, String> contact_column;
    @FXML
    private TableColumn<Quote, Integer> eau_column;
    @FXML
    private TableColumn<Quote, String> comments_column;
    @FXML
    private TableColumn<Quote, String> status_column;
    @FXML
    private TableView<QuoteItem> quoteitem_tableview;
    @FXML
    private TableColumn<QuoteItem, String> listnumber_column;
    @FXML
    private TableColumn<QuoteItem, String> metal_name_column;
    @FXML
    private TableColumn<QuoteItem, String> density_column;
    @FXML
    private TableColumn<QuoteItem, Double> unitprice_column;
    @FXML
    private TableColumn<QuoteItem, String> maximumthickness_column;
    @FXML
    private TableColumn<QuoteItem, String> volume_column;
    @FXML
    private TableColumn<QuoteItem, String> weight_column;
    @FXML
    private TableColumn<QuoteItem, String> estimatedprice_column;
    @FXML
    private ComboBox<Specification> specification_combo;
    @FXML
    private TextField specificationprocess_field;
    @FXML
    private TextField revisionarea_field;
    @FXML
    private TextField quotemargin_field;
    @FXML
    private TextField estimatedtotal_field;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    @FXML
    private Tab invoicequote_tab;
    
    private static PartRevision partrevision_selection;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Aprovado", "Descartado", "Pendiente");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInvoiceQuoteTab();
        
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        status_combo.setItems(FXCollections.observableArrayList(status_items));
        status_combo.getSelectionModel().selectFirst();
        
        setQuoteTableView();
        setQuoteItemTableView();
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            setPartRevisionItems(newValue);
            partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
           setQuoteItems();
           add_button.setDisable(partrev_combo.getSelectionModel().isEmpty());
           partrevision_selection = partrev_combo.getSelectionModel().getSelectedItem();
        });
        
        status_combo.setOnAction((ActionEvent) -> {
            setQuoteItems();
        });
        
        status_checkbox.setOnAction((ActionEvent) -> {
            setQuoteItems();
            status_combo.setDisable(!status_checkbox.isSelected());
        });
        
        quote_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Quote> observable, Quote oldValue, Quote newValue) -> {
            setQuoteItemItems();
            setQuoteFields();
            pdf_button.setDisable(quote_tableview.getSelectionModel().isEmpty());
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(quote_tableview.getSelectionModel().getSelectedItem()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAddStage();
            setQuoteItems();
        });
    }
    
    public void setInvoiceQuoteTab(){
        try {
            invoicequote_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoiceQuoteFX.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(QuoteFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
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
    
    public void setPartRevisionItems(ProductPart product_part){
        partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part)));
    }
    
    public void setQuoteFields(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        if(quote_tableview.getSelectionModel().getSelectedItem() == null){
            specification_combo.getItems().clear();
            specificationprocess_field.clear();
            revisionarea_field.clear();
            quotemargin_field.clear();
            estimatedtotal_field.clear();
        }else{
            specification_combo.setItems(FXCollections.observableArrayList(
                    msabase.getPartRevisionDAO().findSpecification(
                            msabase.getQuoteDAO().findPartRevision(
                                    quote_tableview.getSelectionModel().getSelectedItem()
                            )
                    )
            ));
            specification_combo.getSelectionModel().selectFirst();
            specificationprocess_field.setText(specification_combo.getSelectionModel().getSelectedItem().getProcess());
            revisionarea_field.setText(""+df.format(msabase.getQuoteDAO().findPartRevision(quote_tableview.getSelectionModel().getSelectedItem()).getArea()));
            quotemargin_field.setText(""+quote_tableview.getSelectionModel().getSelectedItem().getMargin());
            estimatedtotal_field.setText(""+df.format(quote_tableview.getSelectionModel().getSelectedItem().getEstimated_total()));
        }
    }
    
    public void setQuoteItems(){
        if(partrev_combo.getSelectionModel().getSelectedItem() == null){
            quote_tableview.getItems().clear();
        }else{
            if(status_checkbox.isSelected()){
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem())));
            }else{
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(partrev_combo.getSelectionModel().getSelectedItem())));
            }
        }
    }
    
    public void setQuoteItemItems(){
        if(quote_tableview.getSelectionModel().getSelectedItem() == null){
            quoteitem_tableview.getItems().clear();
        }else{
            quoteitem_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteItemDAO().list(quote_tableview.getSelectionModel().getSelectedItem())));
        }
    }
    
    public void setQuoteItemTableView(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+quoteitem_tableview.getItems().indexOf(c.getValue())+1));
        metal_name_column.setCellValueFactory(c -> new SimpleStringProperty(
                msabase.getSpecificationItemDAO().findMetal(
                    msabase.getQuoteItemDAO().findSpecificationItem(
                                c.getValue()
                    )
                ).getMetal_name()));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getDensity())));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getThickness())+" in."));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getVolume())));
        weight_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getWeight())));
        estimatedprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getEstimatedPrice())));
    }
    
    public void setQuoteTableView(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        contact_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getQuoteDAO().findCompanyContact(c.getValue()).getName()));
        quotedate_column.setCellValueFactory(new PropertyValueFactory<>("quote_date"));
        eau_column.setCellValueFactory(new PropertyValueFactory<>("estimated_annual_usage"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        status_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApproved()));
        status_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        status_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setApproved(t.getNewValue());
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
             setQuoteItems();
        });
    }
    
    private File buildPDF(Quote quote) throws IOException {
        
        Path template = Files.createTempFile("OrderPurchaseTemplate", ".pdf");
        template.toFile().deleteOnExit();
        
        try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/OrderPurchaseTemplate.pdf")) {
            Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
        }

        Path output = Files.createTempFile("OrdenDeCompraPDF", ".pdf");
        template.toFile().deleteOnExit();  
        
        PdfDocument pdf = new PdfDocument(
            new PdfReader(template.toFile()),
            new PdfWriter(output.toFile())
        );
            
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        
        fields.get("quote_id").setValue(""+quote.getId());
        fields.get("date").setValue(quote.getQuote_date().toString());
        fields.get("client").setValue(msabase.getCompanyContactDAO().findCompany(msabase.getQuoteDAO().findCompanyContact(quote)).getName());
        fields.get("contact").setValue(msabase.getQuoteDAO().findCompanyContact(quote).getName());
        fields.get("contact_email").setValue(msabase.getQuoteDAO().findCompanyContact(quote).getEmail());
        fields.get("contact_number").setValue(msabase.getQuoteDAO().findCompanyContact(quote).getPhone_number());
        List<CompanyAddress> company_address = msabase.getCompanyAddressDAO().listActive(msabase.getCompanyContactDAO().findCompany(msabase.getQuoteDAO().findCompanyContact(quote)), true);
        if(company_address.isEmpty()){
            fields.get("client_address").setValue("n/a");
        }else{
            fields.get("client_address").setValue(company_address.get(0).getAddress());
        }
        fields.get("part_number").setValue(msabase.getPartRevisionDAO().findProductPart(msabase.getQuoteDAO().findPartRevision(quote)).getPart_number());
        fields.get("revision").setValue(msabase.getQuoteDAO().findPartRevision(quote).getRev());
        fields.get("description").setValue(msabase.getPartRevisionDAO().findProductPart(msabase.getQuoteDAO().findPartRevision(quote)).getDescription());
        fields.get("process").setValue(msabase.getPartRevisionDAO().findSpecification(partrevision_selection).getProcess());
        fields.get("eau").setValue(""+quote.getEstimated_annual_usage());
        fields.get("unit_price").setValue(""+quote.getEstimated_total());
        fields.get("comments").setValue(quote.getComments());
        form.flattenFields();
        pdf.close();
        
        return output.toFile();
    }
    
    public static PartRevision getPartrevision_selection(){
        return partrevision_selection;
    }
}
