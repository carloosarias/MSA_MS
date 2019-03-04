/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.DepartLot;
import model.InvoiceItem;
import model.Quote;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddInvoiceItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> departreportid_column;
    @FXML
    private TableColumn<DepartLot, String> part_column;
    @FXML
    private TableColumn<DepartLot, String> revision_column;
    @FXML
    private TableColumn<DepartLot, String> lot_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private ComboBox<DepartLot> departlot_combo;
    @FXML
    private ComboBox<Quote> quote_combo;
    @FXML
    private TextField comments_field;
    @FXML
    private Button save_button;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartLotTable();
        updateDepartLotTable();
        
        
        save_button.disableProperty().bind(departlot_tableview.getSelectionModel().selectedItemProperty().isNull());

        departlot_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartLot> observable, DepartLot oldValue, DepartLot newValue) -> {
            updateQuote_combo();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            CreateInvoiceFX.getDepartlot_list().remove(departlot_combo.getSelectionModel().getSelectedItem());
            CreateInvoiceFX.getInvoiceitem_queue().add(mapInvoiceItem());
            Stage current_stage = (Stage) root_hbox.getScene().getWindow();
            current_stage.close();
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(departlot_combo.getSelectionModel().isEmpty()){
            departlot_tableview.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(quote_combo.getSelectionModel().isEmpty()){
            quote_combo.setStyle("-fx-background-color: lightpink");
            b = false;
        }
        if(comments_field.getText().replace(" ", "").equals("")){
            comments_field.setText("N/A");
        }
        return b;
    }
    
    public void updateQuote_combo(){
        try{
            quote_combo.getItems().setAll(msabase.getQuoteDAO().getLatestQuote(departlot_tableview.getSelectionModel().getSelectedItem().getDepartreport_id(), departlot_tableview.getSelectionModel().getSelectedItem().getPartrevision_id(), true));
            quote_combo.getSelectionModel().selectFirst();
        }catch(Exception e){
            quote_combo.getItems().clear();
        }
    }
    
    public void clearStyle(){
        departlot_tableview.setStyle(null);
        quote_combo.setStyle(null);
    }
    
    public InvoiceItem mapInvoiceItem(){
        InvoiceItem invoice_item = new InvoiceItem();
        invoice_item.setTemp_departlot(departlot_combo.getSelectionModel().getSelectedItem());
        invoice_item.setTemp_quote(quote_combo.getSelectionModel().getSelectedItem());
        invoice_item.setComments(comments_field.getText());
        invoice_item.setDepartreport_id(departlot_combo.getSelectionModel().getSelectedItem().getDepartreport_id());
        invoice_item.setPart_number(departlot_combo.getSelectionModel().getSelectedItem().getPart_number());
        invoice_item.setPart_revision(departlot_combo.getSelectionModel().getSelectedItem().getPart_revision());
        invoice_item.setLot_number(departlot_combo.getSelectionModel().getSelectedItem().getLot_number());
        invoice_item.setDepartlot_quantity(departlot_combo.getSelectionModel().getSelectedItem().getQuantity());
        invoice_item.setDepartlot_boxquantity(departlot_combo.getSelectionModel().getSelectedItem().getBox_quantity());
        invoice_item.setQuote_estimatedtotal(quote_combo.getSelectionModel().getSelectedItem().getEstimated_total());
        return invoice_item;
    }
    
    public void setDepartLotTable(){
        departreportid_column.setCellValueFactory(new PropertyValueFactory<>("departreport_id"));
        part_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
    }
    
    public void updateDepartLotTable(){
        departlot_tableview.getItems().setAll(mergeByDepartreport_Partnumber(CreateInvoiceFX.getDepartlot_list()));
    }
    
    public List<DepartLot> mergeByDepartreport_Partnumber(List<DepartLot> unfilteredList){
        //find all part_number
        ArrayList<Integer> departreport_id = new ArrayList();
        ArrayList<String> partnumber = new ArrayList();
        ArrayList<String> part_revision = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        for(DepartLot depart_lot : unfilteredList){
            if(departreport_id.contains(depart_lot.getDepartreport_id()) && partnumber.contains(depart_lot.getPart_number()) && part_revision.contains(depart_lot.getPart_revision())){
                mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).setQuantity(mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).getQuantity() + depart_lot.getQuantity());
                mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).setBox_quantity(mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).getBox_quantity() + depart_lot.getBox_quantity());
            }
            else{
                departreport_id.add(depart_lot.getDepartreport_id());
                partnumber.add(depart_lot.getPart_number());
                part_revision.add(depart_lot.getPart_revision());
                
                DepartLot item = new DepartLot();
                item.setPartrevision_id(depart_lot.getPartrevision_id());
                item.setDepartreport_id(depart_lot.getDepartreport_id());
                item.setPart_number(depart_lot.getPart_number());
                item.setPart_revision(depart_lot.getPart_revision());
                item.setQuantity(depart_lot.getQuantity());
                item.setBox_quantity(depart_lot.getBox_quantity());
                mergedList.add(item);
            }
        }
        
        return mergedList;
    }
}
