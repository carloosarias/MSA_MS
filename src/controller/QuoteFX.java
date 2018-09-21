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
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyAddress;
import model.CompanyContact;
import model.DepartLot;
import model.Invoice;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
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
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private Button add_button;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, Integer> id_column;
    @FXML
    private TableColumn<Quote, Date> quotedate_column;
    @FXML
    private TableColumn<Quote, String> contact_column;
    @FXML
    private TableColumn<Quote, Double> unitprice_column;
    @FXML
    private TableColumn<Quote, String> comments_column;
    @FXML
    private TableColumn<Quote, String> status_column;
    @FXML
    private TableColumn<Quote, Integer> eau_column;
    @FXML
    private TableColumn<Quote, String> process_column;
    @FXML
    private CheckBox status_checkbox;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Aprovado", "Descartado", "Pendiente");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        status_combo.setItems(FXCollections.observableArrayList(status_items));
        status_combo.getSelectionModel().selectFirst();
        status_combo.setDisable(!status_checkbox.isSelected());
        
        status_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        status_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setApproved(t.getNewValue());
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        });
        setQuoteTable();
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            setPartRevisionItems(newValue);
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
           setQuoteTableItems(newValue, status_combo.getSelectionModel().getSelectedItem());
        });
        
        status_combo.setOnAction((ActionEvent) -> {
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        });
        
        status_checkbox.setOnAction((ActionEvent) -> {
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
            status_combo.setDisable(!status_checkbox.isSelected());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAddStage();
        });
        
        quote_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Quote> observable, Quote oldValue, Quote newValue) -> {
            pdf_button.setDisable(quote_tableview.getSelectionModel().isEmpty());
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                buildPDF(quote_tableview.getSelectionModel().getSelectedItem());
                MainApp.openPDF("./src/pdf/QuotePDF.pdf");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateQuoteFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Cotización");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        } catch (IOException ex) {
            Logger.getLogger(QuoteFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPartRevisionItems(ProductPart product_part){
        partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part)));
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
    public void setQuoteTableItems(PartRevision part_revision, String status_text){
        if(part_revision == null){
            quote_tableview.getItems().clear();
        }else{
            if(status_checkbox.isSelected()){
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(part_revision, status_text)));
            }else{
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(part_revision)));
            }
        }
    }
    public void setQuoteTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        quotedate_column.setCellValueFactory(new PropertyValueFactory<>("quote_date"));
        contact_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getQuoteDAO().findCompanyContact(c.getValue()).getName()));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        status_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApproved()));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        eau_column.setCellValueFactory(new PropertyValueFactory<>("eau"));
    }
    
    private void buildPDF(Quote quote) throws IOException {
            PdfDocument pdf = new PdfDocument(
                new PdfReader(new File("./src/template/QuoteTemplate.pdf")),
                new PdfWriter(new File("./src/pdf/QuotePDF.pdf"))
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
            fields.get("process").setValue(quote.getProcess());
            fields.get("eau").setValue(""+quote.getEau());
            fields.get("unit_price").setValue(""+quote.getUnit_price());
            fields.get("comments").setValue(quote.getComments());
            form.flattenFields();
            pdf.close();
    }
}
