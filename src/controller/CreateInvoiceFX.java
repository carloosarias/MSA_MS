 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.Invoice;
import model.InvoiceItem;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateInvoiceFX implements Initializable {
 
    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker invoicedate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private TextField terms_field;
    @FXML
    private TableView<InvoiceItem> invoiceitem_tableview;
    @FXML
    private TableColumn<InvoiceItem, Integer> departreportid_column;
    @FXML
    private TableColumn<InvoiceItem, String> partnumber_column;
    @FXML
    private TableColumn<InvoiceItem, String> revision_column;
    @FXML
    private TableColumn<InvoiceItem, String> unitprice_column;
    @FXML
    private TableColumn<InvoiceItem, Integer> lot_qty;
    @FXML
    private TableColumn<InvoiceItem, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<InvoiceItem, String> lotprice_column;
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
    @FXML
    private Label total_label;
    @FXML
    private Button create_button;
    
    private static List<DepartLot> departlot_list = new ArrayList<DepartLot>();
    
    private static List<InvoiceItem> invoiceitem_queue = new ArrayList<InvoiceItem>();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private Stage add_stage = new Stage();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        invoicedate_picker.setValue(LocalDate.now());
        setDatePicker(invoicedate_picker);
        client_combo.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        setInvoiceItemTable();
        
        add_button.disableProperty().bind(client_combo.getSelectionModel().selectedItemProperty().isNull());
        delete_button.disableProperty().bind(invoiceitem_tableview.getSelectionModel().selectedItemProperty().isNull());
        create_button.disableProperty().bind(invoiceitem_tableview.itemsProperty().isNull().or(client_combo.getSelectionModel().selectedItemProperty().isNull()));
        
        client_combo.setOnAction((ActionEvent) -> {
            updateClient_combo();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            removeInvoiceItems();
            updateInvoiceItemTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateInvoiceItemTable();
        });
        
        create_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            createInvoice();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public void removeInvoiceItems(){
        ArrayList<InvoiceItem> remove_queue = new ArrayList();
        for(InvoiceItem invoice_item : invoiceitem_queue){
            if(invoice_item.getDepartreport_id().equals(invoiceitem_tableview.getSelectionModel().getSelectedItem().getDepartreport_id()) && invoice_item.getPart_revision().equals(invoiceitem_tableview.getSelectionModel().getSelectedItem().getPart_revision())){
                departlot_list.add(invoice_item.getTemp_departlot());
                remove_queue.add(invoice_item);
            }
        }
        invoiceitem_queue.removeAll(remove_queue);
    }
    
    public void sortQueues(){
        Collections.sort(departlot_list, new Comparator<DepartLot>(){
            public int compare(DepartLot lot1, DepartLot lot2) {
               return lot1.getDepartreport_id().compareTo(lot2.getDepartreport_id());
            }
        });
        
        Collections.sort(invoiceitem_queue, new Comparator<InvoiceItem>(){
            public int compare(InvoiceItem item1, InvoiceItem item2) {
                return item1.getDepartreport_id().compareTo(item2.getDepartreport_id());
            } 
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
        if(terms_field.getText().replace(" ", "").equals("")){
            terms_field.setStyle("-fx-background-color: lightpink;");
            terms_field.setText("N/A");
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
        terms_field.setStyle(null);
        invoiceitem_tableview.setStyle(null);
    }
    
    public void createInvoice(){
        Invoice invoice = new Invoice();
        invoice.setInvoice_date(DAOUtil.toUtilDate(invoicedate_picker.getValue()));
        invoice.setTerms(terms_field.getText());
        invoice.setPayment_date(null);
        invoice.setCheck_number("N/A");
        invoice.setQuantity_paid(0.0);
        invoice.setPending(true);
        msabase.getInvoiceDAO().create(client_combo.getSelectionModel().getSelectedItem(), invoice);
        createInvoiceItems(invoice);
    }
    
    public void createInvoiceItems(Invoice invoice){
        for(InvoiceItem invoice_item : invoiceitem_queue){
            invoice_item.getTemp_departlot().setPending(false);
            msabase.getDepartLotDAO().update(invoice_item.getTemp_departlot());
            msabase.getInvoiceItemDAO().create(invoice, invoice_item.getTemp_departlot(), invoice_item.getTemp_quote(), invoice_item);
        }
    }
    public void updateInvoiceItemTable(){
        invoiceitem_tableview.getItems().setAll(mergeByDepartreport_Partnumber(invoiceitem_queue));
        setTotal();
        sortQueues();
    }
    
    public void setTotal(){
        double total = 0;
        for(InvoiceItem invoice_item : invoiceitem_queue){
            total += (invoice_item.getQuote_estimatedtotal()*invoice_item.getQuantity());
        }
        total_label.setText("$ "+df.format(total)+" USD");
    }
    
    public void updateClient_combo(){
        invoiceitem_queue.clear();
        try{
            departlot_list = msabase.getDepartLotDAO().list(client_combo.getSelectionModel().getSelectedItem(), true, false);
        }catch(Exception e){
            departlot_list.clear();
        }
        updateInvoiceItemTable();
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddInvoiceItemFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Seleccionar Remisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            updateInvoiceItemTable();

        } catch (IOException ex) {
            Logger.getLogger(CreateInvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setInvoiceItemTable(){
        departreportid_column.setCellValueFactory(new PropertyValueFactory<>("departreport_id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_quote().toString()));
        lotprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getTemp_quote().getEstimated_total()*c.getValue().getQuantity()))+" USD"));
    }
    
    public static List<DepartLot> getDepartlot_list(){
        return departlot_list;
    }
    
    public static List<InvoiceItem> getInvoiceitem_queue(){
        return invoiceitem_queue;
    }
    
    public List<InvoiceItem> mergeByDepartreport_Partnumber(List<InvoiceItem> unfilteredList){
        //find all part_number
        ArrayList<Integer> departreport_id = new ArrayList();
        ArrayList<String> partnumber = new ArrayList();
        ArrayList<String> part_revision = new ArrayList();
        ArrayList<InvoiceItem> mergedList = new ArrayList();
        for(InvoiceItem invoice_item : unfilteredList){
            if(departreport_id.contains(invoice_item.getDepartreport_id()) && partnumber.contains(invoice_item.getPart_number()) && part_revision.contains(invoice_item.getPart_revision())){
                for(InvoiceItem listitem : mergedList){
                    if(invoice_item.getDepartreport_id().equals(listitem.getDepartreport_id()) && invoice_item.getPart_number().equals(listitem.getPart_number()) && invoice_item.getPart_revision().equals(listitem.getPart_revision())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + invoice_item.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + invoice_item.getBox_quantity());
                        break;
                    }
                }
            }
            else{
                departreport_id.add(invoice_item.getDepartreport_id());
                partnumber.add(invoice_item.getPart_number());
                part_revision.add(invoice_item.getPart_revision());
                
                InvoiceItem item = new InvoiceItem();
                item.setPart_revision(invoice_item.getPart_revision());
                item.setDepartreport_id(invoice_item.getDepartreport_id());
                item.setPart_number(invoice_item.getPart_number());
                item.setPart_revision(invoice_item.getPart_revision());
                item.setQuantity(invoice_item.getQuantity());
                item.setBox_quantity(invoice_item.getBox_quantity());
                item.setTemp_quote(invoice_item.getTemp_quote());
                item.setQuote_id(invoice_item.getTemp_quote().getId());
                item.setQuote_estimatedtotal(invoice_item.getTemp_quote().getEstimated_total());
                mergedList.add(item);
            }
        }
        
        return mergedList;
    }
}
