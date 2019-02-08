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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.OrderPurchase;
import model.PurchaseItem;
import msa_ms.MainApp;


/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseFX implements Initializable {
    @FXML
    private TableView<OrderPurchase> orderpurchase_tableview;
    @FXML
    private TableColumn<OrderPurchase, Integer> orderid_column;
    @FXML
    private TableColumn<OrderPurchase, Date> orderdate_column;
    @FXML
    private TableColumn<OrderPurchase, String> company_column;
    @FXML
    private TableColumn<OrderPurchase, String> companyaddress_column;
    @FXML
    private TableColumn<OrderPurchase, String> exchangerate_column;
    @FXML
    private TableColumn<OrderPurchase, String> ivarate_column;
    @FXML
    private TableColumn<OrderPurchase, String> status_column;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private Button changestatus_button;
    @FXML
    private Button pdf_button;
    @FXML
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, String> serialnumber_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasureprice_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitprice_column;
    @FXML
    private TableColumn<PurchaseItem, Integer> unitsordered_column;
    @FXML
    private TableColumn<PurchaseItem, String> price_column;
    @FXML
    private TextField subtotal_field;
    @FXML
    private TextField iva_field;
    @FXML
    private TextField total_field;
    @FXML
    private TextField mxnsubtotal_field;
    @FXML
    private TextField mxniva_field;
    @FXML
    private TextField mxntotal_field;
    @FXML
    private TextArea comments_area;
    
    private static ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    DecimalFormat df = new DecimalFormat("#");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(4);
        
        setOrderPurchaseTable();
        updateOrderPurchaseList(msabase);
        orderpurchase_tableview.setItems(orderpurchase_list);
        
        status_combo.disableProperty().bind(orderpurchase_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button.disableProperty().bind(orderpurchase_tableview.getSelectionModel().selectedItemProperty().isNull());
        changestatus_button.disableProperty().bind(status_combo.getSelectionModel().selectedItemProperty().isNull());
        
        setPurchaseItemTable();
        
        status_combo.setItems(FXCollections.observableArrayList(MainApp.orderpurchase_status));
        
        orderpurchase_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends OrderPurchase> observable, OrderPurchase oldValue, OrderPurchase newValue) -> {
            purchaseitem_tableview.setItems(FXCollections.observableArrayList(msabase.getPurchaseItemDAO().list(newValue)));
            calculateTotals();
            comments_area.setText(newValue.getComments());
            status_combo.getSelectionModel().clearSelection();
        });
        
        changestatus_button.setOnAction((ActionEvent) -> {
            orderpurchase_tableview.getSelectionModel().getSelectedItem().setStatus(status_combo.getSelectionModel().getSelectedItem());
            msabase.getOrderPurchaseDAO().update(orderpurchase_tableview.getSelectionModel().getSelectedItem());
            updateOrderPurchaseList(msabase);
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(orderpurchase_tableview.getSelectionModel().getSelectedItem()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    public void setOrderPurchaseTable(){
        orderid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        company_column.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        companyaddress_column.setCellValueFactory(new PropertyValueFactory<>("companyaddress_address"));
        exchangerate_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getExchange_rate())+" MXN"));
        ivarate_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getIva_rate())+"%"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setPurchaseItemTable(){
        serialnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProductsupplier_serialnumber()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct_description()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getProductsupplier_quantity())));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct_unitmeasure()));
        unitmeasureprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getPrice_unit()/c.getValue().getProductsupplier_quantity()))+" USD"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_unit()+" USD"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        price_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_total()+" USD"));
    }
    
    public static void updateOrderPurchaseList(DAOFactory msabase){
        orderpurchase_list.setAll(msabase.getOrderPurchaseDAO().list());
    }
    
    public void setSubtotal(){
        Double subtotal = 0.0;
        
        for(PurchaseItem item : purchaseitem_tableview.getItems()){
            subtotal += item.getPrice_total();
        }
        
        subtotal_field.setText(df.format(subtotal));
        mxnsubtotal_field.setText(df.format(subtotal*orderpurchase_tableview.getSelectionModel().getSelectedItem().getExchange_rate()));
    }
    public void calculateTotals(){
        setSubtotal();
        setIva();
        setTotal();
    }
    public void setIva(){
        iva_field.setText(df.format(Double.parseDouble(subtotal_field.getText())*orderpurchase_tableview.getSelectionModel().getSelectedItem().getIva_rate()/100));
        mxniva_field.setText(df.format(Double.parseDouble(iva_field.getText())*orderpurchase_tableview.getSelectionModel().getSelectedItem().getExchange_rate()));
    }
    
    public void setTotal(){
        total_field.setText(df.format(Double.parseDouble(subtotal_field.getText())+Double.parseDouble(iva_field.getText())));
        mxntotal_field.setText(df.format(Double.parseDouble(total_field.getText())*orderpurchase_tableview.getSelectionModel().getSelectedItem().getExchange_rate()));
    }
    
    public File buildPDF(OrderPurchase order_purchase) throws IOException{
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
        fields.get("id").setValue(""+order_purchase.getId());
        fields.get("report_date").setValue(order_purchase.getReport_date().toString());
        fields.get("employee_email").setValue("cmartinez@maquilasales.com");
        fields.get("company_name").setValue(order_purchase.getCompany_name());
        fields.get("company_address").setValue(order_purchase.getCompanyaddress_address());
        fields.get("iva_rate").setValue("%"+order_purchase.getIva_rate());
        fields.get("exchange_rate").setValue("$1 USD = $"+order_purchase.getExchange_rate()+" MXN");
        fields.get("comments").setValue(order_purchase.getComments());

        List<PurchaseItem> purchaseitem_list = purchaseitem_tableview.getItems();
        int i = 0;
        for(PurchaseItem purchase_item : purchaseitem_list){
            int current_row = i+1;
            fields.get("serial_number"+current_row).setValue(purchase_item.getProductsupplier_serialnumber());
            fields.get("quantity_ordered"+current_row).setValue("x"+purchase_item.getUnits_ordered());
            fields.get("description"+current_row).setValue(purchase_item.getProduct_description());
            fields.get("quantity"+current_row).setValue(df.format(purchase_item.getProductsupplier_quantity()));
            fields.get("unit_measure"+current_row).setValue(purchase_item.getProduct_unitmeasure());
            fields.get("unitmeasure_price"+current_row).setValue("$"+df.format(purchase_item.getPrice_unit()/purchase_item.getProductsupplier_quantity())+" USD");
            fields.get("unit_price"+current_row).setValue("$"+purchase_item.getPrice_unit()+" USD");
            fields.get("price_lot"+current_row).setValue("$ "+purchase_item.getPrice_total()+" USD");
            i++;
        }
        fields.get("subtotal").setValue("$ "+subtotal_field.getText()+" USD");
        fields.get("iva").setValue("$ "+iva_field.getText()+" USD");
        fields.get("total").setValue("$ "+total_field.getText()+" USD");
        fields.get("mxn_subtotal").setValue("$ "+mxnsubtotal_field.getText()+" MXN");
        fields.get("mxn_iva").setValue("$ "+mxniva_field.getText()+" MXN");
        fields.get("mxn_total").setValue("$ "+mxntotal_field.getText()+" MXN");

        form.flattenFields();
        pdf.close();

        return output.toFile();
    }
    
}
