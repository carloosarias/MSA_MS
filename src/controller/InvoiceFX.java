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
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyContact;
import model.Invoice;
import model.InvoiceItem;
import msa_ms.MainApp;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoiceFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<Invoice> invoice_tableview;
    @FXML
    private TableColumn<Invoice, Integer> id_column;
    @FXML
    private TableColumn<Invoice, String> invoicedate_column;
    @FXML
    private TableColumn<Invoice, String> client_column;
    @FXML
    private TableColumn<Invoice, String> terms_column;
    @FXML
    private TableColumn<Invoice, String> shipping_column;
    @FXML
    private TableColumn<Invoice, String> fob_column;
    @FXML
    private TableColumn<Invoice, String> address_column;
    @FXML
    private TableColumn<Invoice, String> pending_column;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<InvoiceItem> invoiceitem_tableview;
    @FXML
    private TableColumn<InvoiceItem, Integer> departreportid_column;
    @FXML
    private TableColumn<InvoiceItem, String> partnumber_column;
    @FXML
    private TableColumn<InvoiceItem, String> revision_column;
    @FXML
    private TableColumn<InvoiceItem, String> lotnumber_column;
    @FXML
    private TableColumn<InvoiceItem, String> comments_column;
    @FXML
    private TableColumn<InvoiceItem, Integer> qty_column;
    @FXML
    private TableColumn<InvoiceItem, Integer> boxqty_column;
    @FXML
    private TableColumn<InvoiceItem, String> quote_column;
    @FXML
    private TableColumn<InvoiceItem, String> total_column;
    @FXML
    private Label total_label;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setInvoiceTable();
        setInvoiceItemTable();
        updateInvoiceTable();
        
        pdf_button.disableProperty().bind(invoice_tableview.getSelectionModel().selectedItemProperty().isNull());
        details_tab.disableProperty().bind(invoice_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        invoice_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Invoice> observable, Invoice oldValue, Invoice newValue) -> {
            updateInvoiceItemTable();
            total_label.setText("$ "+df.format(getTotal())+" USD");
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateInvoiceTable();
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(invoice_tableview.getSelectionModel().getSelectedItem()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    public Double getTotal(){
        double total = 0;
        for(InvoiceItem invoice_item : invoiceitem_tableview.getItems()){
            total += (invoice_item.getQuote_estimatedtotal()*invoice_item.getQuantity());
        }
        return total;
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/CreateInvoiceFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Factura");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateInvoiceTable(){
        invoiceitem_tableview.getItems().clear();
        invoice_tableview.setItems(FXCollections.observableArrayList(msabase.getInvoiceDAO().list()));
    }
    
    public void updateInvoiceItemTable(){
        try{
            invoiceitem_tableview.getItems().setAll(msabase.getInvoiceItemDAO().list(invoice_tableview.getSelectionModel().getSelectedItem()));
        }catch(Exception e){
            invoiceitem_tableview.getItems().clear();
        }
    }
    
    public void setInvoiceTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        invoicedate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getInvoice_date()))));
        client_column.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("billing_address"));
        terms_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTerms()));
        shipping_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getShipping_method()));
        fob_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFob()));
        pending_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().pendingToString()));
    }
    
    public void setInvoiceItemTable(){
        departreportid_column.setCellValueFactory(new PropertyValueFactory<>("departreport_id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        qty_column.setCellValueFactory(new PropertyValueFactory<>("departlot_quantity"));
        boxqty_column.setCellValueFactory(new PropertyValueFactory<>("departlot_boxquantity"));
        quote_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getQuote_estimatedtotal())+" USD"));
        total_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getQuote_estimatedtotal()*c.getValue().getQuantity())+" USD"));
    }

    private File buildPDF(Invoice invoice) throws IOException {
            Path template = Files.createTempFile("InvoiceTemplate", ".pdf");
            template.toFile().deleteOnExit();
            try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/InvoiceTemplate.pdf")) {
                Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
            }
            
            Path output = Files.createTempFile("FacturaPDF", ".pdf");
            template.toFile().deleteOnExit();            
            
            PdfDocument pdf = new PdfDocument(
                new PdfReader(template.toFile()),
                new PdfWriter(output.toFile())
            );
            
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            fields.get("invoice_id").setValue(""+invoice.getId());
            fields.get("date").setValue(getFormattedDate(DAOUtil.toLocalDate(invoice.getInvoice_date())));
            fields.get("client").setValue(invoice.getCompany_name());
            fields.get("billing_address").setValue(invoice.getBilling_address());
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(msabase.getInvoiceDAO().findCompany(invoice), true);
            if(company_contact.isEmpty()){
                fields.get("contact").setValue("n/a");
                fields.get("contact_email").setValue("n/a");
                fields.get("contact_number").setValue("n/a");
            }else{
                fields.get("contact").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_number").setValue(company_contact.get(0).getPhone_number());
            }
            fields.get("shipping_address").setValue(invoice.getShipping_address());
            fields.get("payment_terms").setValue(invoice.getTerms());
            fields.get("shipping_method").setValue(invoice.getShipping_method());
            fields.get("fob").setValue(invoice.getFob());
            
            List<InvoiceItem> invoiceitem_list = invoiceitem_tableview.getItems();
            int i = 0;
            double total_price = 0;
            for(InvoiceItem invoice_item : invoiceitem_list){
                int current_row = i+1;
                double lot_price = 0;
                double quantity = invoice_item.getQuantity();
                double unit_price = invoice_item.getQuote_estimatedtotal();
                fields.get("depart_report_id"+current_row).setValue(""+invoice_item.getDepartreport_id());
                fields.get("part_number"+current_row).setValue(invoice_item.getPart_number());
                fields.get("quantity_box"+current_row).setValue(""+invoice_item.getBox_quantity());
                fields.get("quantity"+current_row).setValue(""+quantity);
                fields.get("unit_price"+current_row).setValue("$ "+df.format(unit_price)+" USD");
                lot_price = unit_price * quantity;
                fields.get("lot_price"+current_row).setValue("$ "+df.format(lot_price)+" USD");
                total_price += lot_price;
                i++;
            }
            fields.get("total_price").setValue("$ "+df.format(total_price)+" USD");
            

            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
}
