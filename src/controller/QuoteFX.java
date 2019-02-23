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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyAddress;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
import model.QuoteItem;
import msa_ms.MainApp;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.df;



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
    private CheckBox status_checkbox;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, Integer> id_column;
    @FXML
    private TableColumn<Quote, String> quotedate_column;
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
    private ComboBox<String> specification_combo;
    @FXML
    private TextField specificationprocess_field;
    @FXML
    private TextField revisionarea_field;
    @FXML
    private TextField quotemargin_field;
    @FXML
    private TextField actualprice_field;
    @FXML
    private TextField estimatedtotal_field;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    
    private static PartRevision partrevision_selection;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Aprovado", "Descartado", "Pendiente");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        add_button.disableProperty().bind(partrev_combo.getSelectionModel().selectedItemProperty().isNull());
        partrev_combo.disableProperty().bind(partrev_combo.itemsProperty().isNull());
        status_combo.disableProperty().bind(status_checkbox.selectedProperty().not());
        pdf_button.disableProperty().bind(quote_tableview.getSelectionModel().selectedItemProperty().isNull());
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        status_combo.setItems(FXCollections.observableArrayList(status_items));
        status_combo.getSelectionModel().selectFirst();
        
        setQuoteTableView();
        setQuoteItemTableView();
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            setPartRevisionItems(newValue);
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
           setQuoteItems();
           partrevision_selection = partrev_combo.getSelectionModel().getSelectedItem();
        });
        
        status_combo.setOnAction((ActionEvent) -> {
            setQuoteItems();
        });
        
        status_checkbox.setOnAction((ActionEvent) -> {
            setQuoteItems();
        });
        
        quote_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Quote> observable, Quote oldValue, Quote newValue) -> {
            setQuoteItemItems();
            setQuoteFields();
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
    
    public void showAddStage(){
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
    
    public void setPartRevisionItems(ProductPart product_part){
        partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part, true)));
    }
    
    public void setQuoteFields(){
        if(quote_tableview.getSelectionModel().getSelectedItem() == null){
            specification_combo.getItems().clear();
            specificationprocess_field.clear();
            revisionarea_field.clear();
            quotemargin_field.clear();
            estimatedtotal_field.clear();
        }else{
            specification_combo.getItems().setAll(quote_tableview.getSelectionModel().getSelectedItem().getSpec_number());
            specification_combo.getSelectionModel().selectFirst();
            specificationprocess_field.setText(quote_tableview.getSelectionModel().getSelectedItem().getSpec_process());
            revisionarea_field.setText(df.format(quote_tableview.getSelectionModel().getSelectedItem().getPartrev_area()));
            quotemargin_field.setText(df.format(quote_tableview.getSelectionModel().getSelectedItem().getMargin()));
            actualprice_field.setText(df.format(quote_tableview.getSelectionModel().getSelectedItem().getActualprice()));
            estimatedtotal_field.setText(df.format(quote_tableview.getSelectionModel().getSelectedItem().getEstimated_total()));
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
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+(quoteitem_tableview.getItems().indexOf(c.getValue())+1)));
        metal_name_column.setCellValueFactory(new PropertyValueFactory<>("metal_name"));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMetal_densityGMM())+" G/MM³"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getUnit_price())+" USD"));
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getSpecitem_maximumthicknessMM())+" MM"));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getVolume())+" MM³"));
        weight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getWeight())+" G"));
        estimatedprice_column.setCellValueFactory(c -> new SimpleStringProperty("$"+df.format(c.getValue().getEstimatedPrice())+" USD"));
    }
    
    public void setQuoteTableView(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        contact_column.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        quotedate_column.setCellValueFactory(c -> new SimpleStringProperty(dateFormat.format(c.getValue().getQuote_date())));
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
    
    public static PartRevision getPartrevision_selection(){
        return partrevision_selection;
    }
}
