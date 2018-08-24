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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyContact;
import model.Invoice;
import model.InvoiceItem;
import model.PartRevision;
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
    private TableColumn<Invoice, String> shippingaddress_column;
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
    private TableColumn<InvoiceItem, String> remision_column;
    @FXML
    private TableColumn<InvoiceItem, String> part_column;
    @FXML
    private TableColumn<InvoiceItem, String> revision_column;
    @FXML
    private TableColumn<InvoiceItem, String> lot_column;
    @FXML
    private TableColumn<InvoiceItem, String> comments_column;
    @FXML
    private TableColumn<InvoiceItem, String> lot_qty;
    @FXML
    private TableColumn<InvoiceItem, String> lot_boxqty_column;
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
        
        invoice_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Invoice> observable, Invoice oldValue, Invoice newValue) -> {
            setInvoiceDetails(newValue);
            setTotal();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                buildPDF(invoice_tableview.getSelectionModel().getSelectedItem());
                MainApp.openPDF("./src/pdf/InvoicePDF.pdf");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    public void setTotal(){
        double total = 0;
        for(InvoiceItem invoice_item : invoiceitem_tableview.getItems()){
            total += Double.parseDouble(lotprice_column.getCellData(invoice_item));
        }
        total_field.setText(total+"");
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateInvoiceFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Factura");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateInvoiceTable();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
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
        client_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getInvoiceDAO().findCompany(c.getValue()).toString()));
        billingaddress_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getInvoiceDAO().findBillingAddress(c.getValue()).toString()));
        shippingaddress_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getInvoiceDAO().findShippingAddress(c.getValue()).toString()));
        pending_column.setCellValueFactory(c -> new SimpleStringProperty(pendingToString(c.getValue().isPending())));
    }
    
    public String pendingToString(boolean pending){
        if(pending){
            return "Pendiente";
        }else{
            return "Pagada";
        }
    }
    
    public void setInvoiceItemTable(){
        remision_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().findDepartReport(msabase.getInvoiceItemDAO().findDepartLot(c.getValue())).getId()));
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
            msabase.getPartRevisionDAO().findProductPart(
                    msabase.getDepartLotDAO().findPartRevision(
                            msabase.getInvoiceItemDAO().findDepartLot(c.getValue()))).getPart_number())
        );
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(
                    msabase.getDepartLotDAO().findPartRevision(
                            msabase.getInvoiceItemDAO().findDepartLot(c.getValue())).getRev())
        );
        lot_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getInvoiceItemDAO().findDepartLot(c.getValue()).getLot_number()));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        lot_qty.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceItemDAO().findDepartLot(c.getValue()).getQuantity()));
        lot_boxqty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceItemDAO().findDepartLot(c.getValue()).getBox_quantity()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getInvoiceItemDAO().findQuote(c.getValue()).getUnit_price()));
        lotprice_column.setCellValueFactory(c -> new SimpleStringProperty(
                msabase.getInvoiceItemDAO().findQuote(c.getValue()).getUnit_price()*msabase.getInvoiceItemDAO().findDepartLot(c.getValue()).getQuantity()+""));
    }

    private void buildPDF(Invoice invoice) throws IOException {
            PdfDocument pdf = new PdfDocument(
                new PdfReader(new File("./src/template/InvoiceTemplate.pdf")),
                new PdfWriter(new File("./src/pdf/InvoicePDF.pdf"))
            );
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            fields.get("invoice_id").setValue(""+invoice.getId());
            fields.get("date").setValue(invoice.getInvoice_date().toString());
            fields.get("client").setValue(msabase.getInvoiceDAO().findCompany(invoice).getName());
            fields.get("billing_address").setValue(msabase.getInvoiceDAO().findBillingAddress(invoice).getAddress());
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(msabase.getInvoiceDAO().findCompany(invoice));
            if(company_contact.isEmpty()){
                fields.get("contact").setValue("n/a");
                fields.get("contact_email").setValue("n/a");
                fields.get("contact_number").setValue("n/a");
            }else{
                fields.get("contact").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_number").setValue(company_contact.get(0).getPhone_number());
            }
            fields.get("shipping_address").setValue(msabase.getInvoiceDAO().findShippingAddress(invoice).getAddress());
            fields.get("payment_terms").setValue(invoice.getTerms());
            fields.get("shipping_method").setValue(invoice.getShipping_method());
            fields.get("fob").setValue(invoice.getFob());
            List<PartRevision> a = msabase.getInvoiceItemDAO().listPartRevision(invoice);
            for(PartRevision part_revision : a){
                System.out.println(part_revision.getId());
            }
            form.flattenFields();
            pdf.close();
    }
}