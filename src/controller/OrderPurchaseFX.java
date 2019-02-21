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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private TextArea comments_area;
    @FXML
    private ComboBox<String> exchangetype_combo;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    
    private double exchangetype_multiplier = 1;
    
    private static ObservableList<OrderPurchase> orderpurchase_list = FXCollections.observableArrayList();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    DecimalFormat df = new DecimalFormat("#");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exchangetype_combo.getItems().setAll("USD","MXN");
        exchangetype_combo.getSelectionModel().selectFirst();
        
        exchangetype_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            setExchangeType();
            calculateTotals();
            purchaseitem_tableview.refresh();
        });
        
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
            if(!orderpurchase_tableview.getSelectionModel().isEmpty()){
                purchaseitem_tableview.setItems(FXCollections.observableArrayList(msabase.getPurchaseItemDAO().list(newValue)));
                calculateTotals();
                comments_area.setText(newValue.getComments());
                setExchangeType();
                status_combo.getSelectionModel().clearSelection();
            }else{
                purchaseitem_tableview.getItems().clear();
                subtotal_field.clear();
                iva_field.clear();
                total_field.clear();
                comments_area.clear();
                status_combo.getSelectionModel().clearSelection();
                setExchangeType();
            }
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
    public void setExchangeType(){
        label1.setText(exchangetype_combo.getSelectionModel().getSelectedItem());
        label2.setText(exchangetype_combo.getSelectionModel().getSelectedItem());
        label3.setText(exchangetype_combo.getSelectionModel().getSelectedItem());
        if(exchangetype_combo.getSelectionModel().getSelectedItem().equals("MXN")){
            exchangetype_multiplier = orderpurchase_tableview.getSelectionModel().getSelectedItem().getExchange_rate();
        }else{
            exchangetype_multiplier = 1;
        }
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
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("productsupplier_quantity"));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct_unitmeasure()));
        unitmeasureprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getPrice_unit()/c.getValue().getProductsupplier_quantity())*exchangetype_multiplier)+" "+exchangetype_combo.getSelectionModel().getSelectedItem()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getPrice_unit()*exchangetype_multiplier)+" "+exchangetype_combo.getSelectionModel().getSelectedItem()));
        unitprice_column.setCellFactory(TextFieldTableCell.forTableColumn());
        unitprice_column.setOnEditCommit((TableColumn.CellEditEvent<PurchaseItem, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice_updated(getPrice_Unitvalue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPurchaseItemDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            purchaseitem_tableview.refresh();
            calculateTotals();
        });
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        price_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getPrice_total()*exchangetype_multiplier)+" "+exchangetype_combo.getSelectionModel().getSelectedItem()));
    }
    
    public static void updateOrderPurchaseList(DAOFactory msabase){
        orderpurchase_list.setAll(msabase.getOrderPurchaseDAO().list());
    }
    
    public void setSubtotal(){
        Double subtotal = 0.0;
        
        for(PurchaseItem item : purchaseitem_tableview.getItems()){
            subtotal += item.getPrice_total();
        }
        
        subtotal_field.setText(df.format(subtotal*exchangetype_multiplier));
    }
    public void calculateTotals(){
        setSubtotal();
        setIva();
        setTotal();
    }
    public void setIva(){
        iva_field.setText(df.format((Double.parseDouble(subtotal_field.getText())*orderpurchase_tableview.getSelectionModel().getSelectedItem().getIva_rate()/100)));
    }
    
    public void setTotal(){
        total_field.setText(df.format(Double.parseDouble(subtotal_field.getText())+Double.parseDouble(iva_field.getText())));
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
        fields.get("employee_email").setValue("alberto.nunez@maquilasales.com");
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
            fields.get("unitmeasure_price"+current_row).setValue("$"+df.format((purchase_item.getPrice_unit()/purchase_item.getProductsupplier_quantity())*exchangetype_multiplier)+" "+exchangetype_combo.getSelectionModel().getSelectedItem());
            fields.get("unit_price"+current_row).setValue("$"+(df.format(purchase_item.getPrice_unit()*exchangetype_multiplier))+" "+exchangetype_combo.getSelectionModel().getSelectedItem());
            fields.get("price_lot"+current_row).setValue("$ "+(df.format(purchase_item.getPrice_total()*exchangetype_multiplier))+" "+exchangetype_combo.getSelectionModel().getSelectedItem());
            i++;
        }
        fields.get("subtotal").setValue("$ "+subtotal_field.getText()+" "+exchangetype_combo.getSelectionModel().getSelectedItem());
        fields.get("iva").setValue("$ "+iva_field.getText()+" "+exchangetype_combo.getSelectionModel().getSelectedItem());
        fields.get("total").setValue("$ "+total_field.getText()+" "+exchangetype_combo.getSelectionModel().getSelectedItem());

        form.flattenFields();
        pdf.close();

        return output.toFile();
    }
    
    public Double getPrice_Unitvalue(PurchaseItem item, String unit_price){
        try{
            return Double.parseDouble(unit_price);
        }catch(Exception e){
            return item.getPrice_unit();
        }
    }
}
