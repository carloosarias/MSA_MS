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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyContact;
import model.Invoice;
import model.InvoiceItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoiceFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Invoice> invoice_tableview;
    @FXML
    private TableColumn<Invoice, Integer> id_column;
    @FXML
    private TableColumn<Invoice, Date> invoicedate_column;
    @FXML
    private TableColumn<Invoice, String> client_column;
    @FXML
    private TableColumn<Invoice, String> billingaddress_column;
    @FXML
    private TableColumn<Invoice, String> pending_column;
    @FXML
    private TextField terms_field;
    @FXML
    private TextField shippingmethod_field;
    @FXML
    private TextField fob_field;
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
    private TableColumn<InvoiceItem, Integer> lot_qty;
    @FXML
    private TableColumn<InvoiceItem, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<InvoiceItem, String> unitprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> lotprice_column;
    @FXML
    private TextField total_field;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvoiceTable();
        setInvoiceItemTable();
        updateInvoiceTable();
        pdf_button.disableProperty().bind(invoice_tableview.getSelectionModel().selectedItemProperty().isNull());
        invoice_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Invoice> observable, Invoice oldValue, Invoice newValue) -> {
            setInvoiceDetails(newValue);
            setTotal();
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
    
    public void setTotal(){
        double total = 0;
        for(InvoiceItem invoice_item : invoiceitem_tableview.getItems()){
            total += (invoice_item.getQuote_estimatedtotal()*invoice_item.getDepartlot_quantity());
        }
        total_field.setText(total+"");
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateInvoiceFX.fxml"));
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
    
    public void setInvoiceDetails(Invoice invoice){
        if(invoice == null){
            invoiceitem_tableview.getItems().clear();
            terms_field.setText(null);
            shippingmethod_field.setText(null);
            fob_field.setText(null);
        }else{
            invoiceitem_tableview.setItems(FXCollections.observableArrayList(msabase.getInvoiceItemDAO().list(invoice)));
            terms_field.setText(invoice.getTerms());
            shippingmethod_field.setText(invoice.getShipping_method());
            fob_field.setText(invoice.getFob());
        }
    }
    
    public void setInvoiceTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        invoicedate_column.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
        client_column.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        billingaddress_column.setCellValueFactory(new PropertyValueFactory<>("billing_address"));
        pending_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().pendingToString()));
    }
    
    public void setInvoiceItemTable(){
        departreportid_column.setCellValueFactory(new PropertyValueFactory<>("departreport_id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("departlot_quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("departlot_boxquantity"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getQuote_estimatedtotal()+" USD"));
        lotprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+(c.getValue().getQuote_estimatedtotal()*c.getValue().getDepartlot_quantity())+" USD"));
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
            fields.get("date").setValue(invoice.getInvoice_date().toString());
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
            
            List<InvoiceItem> invoice_item_list = msabase.getInvoiceItemDAO().list(invoice);
            int i = 0;
            double total_price = 0;
            for(InvoiceItem invoice_item : invoice_item_list){
                int current_row = i+1;
                double lot_price = 0;
                double quantity = invoice_item.getDepartlot_quantity();
                double unit_price = invoice_item.getQuote_estimatedtotal();
                fields.get("depart_report_id"+current_row).setValue(""+invoice_item.getDepartreport_id());
                fields.get("part_number"+current_row).setValue(invoice_item.getPart_number());
                fields.get("quantity_box"+current_row).setValue(""+invoice_item.getDepartlot_boxquantity());
                fields.get("quantity"+current_row).setValue(""+quantity);
                fields.get("unit_price"+current_row).setValue("$ "+unit_price+" USD");
                lot_price = unit_price * quantity;
                fields.get("lot_price"+current_row).setValue("$ "+lot_price+" USD");
                total_price += lot_price;
                i++;
            }
            fields.get("total_price").setValue("$ "+total_price);
            

            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
}