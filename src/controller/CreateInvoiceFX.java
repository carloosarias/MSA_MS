/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.Invoice;
import model.InvoiceItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateInvoiceFX implements Initializable {
 
    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker invoicedate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private ComboBox<CompanyAddress> billingaddress_combo;
    @FXML
    private ComboBox<CompanyAddress> shippingaddress_combo;
    @FXML
    private TextField terms_field;
    @FXML
    private TextField fob_field;
    @FXML
    private TextField shippingmethod_field;
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
    private TableColumn<InvoiceItem, Double> unitprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> lot_qty;
    @FXML
    private TableColumn<InvoiceItem, String> lot_boxqty_column;
    @FXML
    private TableColumn<InvoiceItem, String> lotprice_column;
    @FXML
    private TableColumn<InvoiceItem, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
    @FXML
    private TextField total_field;
    @FXML
    private Button save_button;
    
    private static List<DepartLot> departlot_list = new ArrayList<DepartLot>();
    
    private static List<InvoiceItem> invoiceitem_queue = new ArrayList<InvoiceItem>();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private Stage add_stage = new Stage();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvoiceItemTable();
        client_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        
        client_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            setClientList(newValue);
        });
        
        invoiceitem_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends InvoiceItem> observable, InvoiceItem oldValue, InvoiceItem newValue) ->{
            delete_button.setDisable(invoiceitem_tableview.getSelectionModel().isEmpty());
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            invoiceitem_queue.remove(invoiceitem_tableview.getSelectionModel().getSelectedItem());
            departlot_list.add(msabase.getDepartLotDAO().find(invoiceitem_tableview.getSelectionModel().getSelectedItem().getDepart_lot_id()));
            updateInvoiceItemTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveInvoice();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(invoicedate_picker.getValue() == null){
            invoicedate_picker.setStyle("-fx-background-color: lightpink;");
        }
        if(client_combo.getSelectionModel().isEmpty()){
            client_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(billingaddress_combo.getSelectionModel().isEmpty()){
            billingaddress_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(shippingaddress_combo.getSelectionModel().isEmpty()){
            shippingaddress_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(terms_field.getText().replace(" ", "").equals("")){
            terms_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(fob_field.getText().replace(" ", "").equals("")){
            fob_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(shippingmethod_field.getText().replace(" ", "").equals("")){
            shippingmethod_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(invoiceitem_tableview.getItems().isEmpty()){
            invoiceitem_tableview.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        return b;
    }
    
    public void clearStyle(){
        invoicedate_picker.setStyle(null);
        client_combo.setStyle(null);
        billingaddress_combo.setStyle(null);
        shippingaddress_combo.setStyle(null);
        terms_field.setStyle(null);
        fob_field.setStyle(null);
        shippingmethod_field.setStyle(null);
        invoiceitem_tableview.setStyle(null);
    }
    
    public void saveInvoice(){
        Invoice invoice = new Invoice();
        invoice.setInvoice_date(java.sql.Date.valueOf(invoicedate_picker.getValue()));
        invoice.setTerms(terms_field.getText());
        invoice.setShipping_method(shippingmethod_field.getText());
        invoice.setFob(fob_field.getText());
        invoice.setPending(true);
        msabase.getInvoiceDAO().create(client_combo.getSelectionModel().getSelectedItem(), billingaddress_combo.getSelectionModel().getSelectedItem(), shippingaddress_combo.getSelectionModel().getSelectedItem(), invoice);
        saveInvoiceItems(invoice);
    }
    
    public void saveInvoiceItems(Invoice invoice){
        for(InvoiceItem invoice_item : invoiceitem_queue){
            DepartLot depart_lot = msabase.getDepartLotDAO().find(invoice_item.getDepart_lot_id());
            depart_lot.setPending(false);
            msabase.getDepartLotDAO().update(depart_lot);
            System.out.println(depart_lot.isPending());
            msabase.getInvoiceItemDAO().create(invoice, depart_lot, msabase.getQuoteDAO().find(invoice_item.getQuote_id()), invoice_item);
        }
    }
    public void updateInvoiceItemTable(){
        invoiceitem_tableview.setItems(FXCollections.observableArrayList(invoiceitem_queue));
        save_button.setDisable(invoiceitem_queue.isEmpty());
        setTotal();
    }
    
    public void setTotal(){
        double total = 0;
        for(InvoiceItem invoice_item : invoiceitem_queue){
            total += Double.parseDouble(lotprice_column.getCellData(invoice_item));
        }
        total_field.setText(total+"");
    }
    
    public void setClientList(Company company){
        if(company == null){
            invoiceitem_queue.clear();
            departlot_list.clear();
            departlot_list = null;
            billingaddress_combo.getItems().clear();
            shippingaddress_combo.getItems().clear();
            invoiceitem_tableview.getItems().clear();
        }else{
            invoiceitem_queue.clear();
            departlot_list.clear();
            departlot_list = msabase.getDepartLotDAO().list(company, true, false);
            billingaddress_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(company, true)));
            shippingaddress_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(company, true)));
        }
        updateInvoiceItemTable();
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddInvoiceItemFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Seleccionar Remisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateInvoiceItemTable();

        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setInvoiceItemTable(){
        remision_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().findDepartReport(msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id())).getId()));
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
            msabase.getPartRevisionDAO().findProductPart(
                    msabase.getDepartLotDAO().findPartRevision(
                            msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()))).getPart_number()
        ));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findPartRevision(msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id())).getRev()));
        lot_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()).getLot_number()));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        lot_qty.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()).getQuantity()));
        lot_boxqty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()).getBox_quantity()));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        lotprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+(msabase.getQuoteDAO().find(c.getValue().getQuote_id()).getUnit_price()*msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()).getQuantity())*msabase.getDepartLotDAO().find(c.getValue().getDepart_lot_id()).getBox_quantity()));
    }
    
    public static List<DepartLot> getDepartlot_list(){
        return departlot_list;
    }
    
    public static List<InvoiceItem> getInvoiceitem_queue(){
        return invoiceitem_queue;
    }
}
