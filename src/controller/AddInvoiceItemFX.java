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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DepartLot;
import model.InvoiceItem;
import model.Quote;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddInvoiceItemFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> departreportid_column;
    @FXML
    private TableColumn<DepartLot, String> part_column;
    @FXML
    private TableColumn<DepartLot, String> revision_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private ComboBox<Quote> quote_combo;
    @FXML
    private Button save_button;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setDepartLotTable();
        updateDepartLotTable();
        
        departlot_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartLot> observable, DepartLot oldValue, DepartLot newValue) -> {
            try{
                quote_combo.getItems().clear();
                quote_combo.getItems().setAll(getQuote(newValue));
                quote_combo.getSelectionModel().selectFirst();
                quote_combo.getSelectionModel().getSelectedItem().getEstimated_total();
                save_button.setDisable(false);
            }catch(Exception e){
                quote_combo.getItems().clear();
                save_button.setDisable(true);
            }
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            mapInvoiceItems();
            
            Stage current_stage = (Stage) root_gridpane.getScene().getWindow();
            current_stage.close();
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(departlot_tableview.getSelectionModel().isEmpty()){
            departlot_tableview.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public Quote getQuote(DepartLot depart_lot){
        try{
            return msabase.getQuoteDAO().findLatest(depart_lot.getDepartreport_id(), depart_lot.getPartrevision_id(), true);
        }catch(Exception e){
            return new Quote();
        }
    }
    
    public void clearStyle(){
        departlot_tableview.setStyle(null);
    }
    
    public void mapInvoiceItems(){
        ArrayList<DepartLot> remove_queue = new ArrayList();
        for(DepartLot depart_lot : CreateInvoiceFX.getDepartlot_list()){
            if(depart_lot.getDepartreport_id().equals(departlot_tableview.getSelectionModel().getSelectedItem().getDepartreport_id()) && depart_lot.getPartrevision_id().equals(departlot_tableview.getSelectionModel().getSelectedItem().getPartrevision_id())){
                InvoiceItem invoice_item = new InvoiceItem();
                invoice_item.setTemp_departlot(depart_lot);
                invoice_item.setTemp_quote(quote_combo.getSelectionModel().getSelectedItem());
                invoice_item.setComments("");
                invoice_item.setDepartreport_id(depart_lot.getDepartreport_id());
                invoice_item.setPart_number(depart_lot.getPart_number());
                invoice_item.setPart_revision(depart_lot.getPart_revision());
                invoice_item.setLot_number(depart_lot.getLot_number());
                invoice_item.setQuantity(depart_lot.getQuantity());
                invoice_item.setBox_quantity(depart_lot.getBox_quantity());
                invoice_item.setQuote_estimatedtotal(invoice_item.getTemp_quote().getEstimated_total());
                CreateInvoiceFX.getInvoiceitem_queue().add(invoice_item);
                remove_queue.add(depart_lot);
            }
        }
        CreateInvoiceFX.getDepartlot_list().removeAll(remove_queue);
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
                for(DepartLot listitem : mergedList){
                    if(depart_lot.getDepartreport_id().equals(listitem.getDepartreport_id()) && depart_lot.getPart_number().equals(listitem.getPart_number()) && depart_lot.getPart_revision().equals(listitem.getPart_revision())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + depart_lot.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + depart_lot.getBox_quantity());
                        break;
                    }
                }
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
