/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
    private TableColumn<DepartLot, String> remision_column;
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
        departlot_tableview.setItems(FXCollections.observableArrayList(CreateInvoiceFX.getDepartlot_list()));
        departlot_combo.setItems(departlot_tableview.getItems());
        
        departlot_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartLot> observable, DepartLot oldValue, DepartLot newValue) -> {
            departlot_combo.getSelectionModel().select(newValue);
            save_button.setDisable(newValue == null);
            setQuoteCombo(newValue);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            CreateInvoiceFX.getInvoiceitem_queue().add(mapInvoiceItem());
            CreateInvoiceFX.getDepartlot_list().remove(departlot_combo.getSelectionModel().getSelectedItem());
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
        return b;
    }
    
    public void setQuoteCombo(DepartLot depart_lot){
        if(depart_lot == null){
            quote_combo.getItems().clear();
        }else{
            quote_combo.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(msabase.getDepartLotDAO().findPartRevision(depart_lot), true)));
            if(!quote_combo.getItems().isEmpty()){
                quote_combo.getSelectionModel().selectFirst();
            }
        }
    }
    
    public void clearStyle(){
        departlot_tableview.setStyle(null);
        quote_combo.setStyle(null);
    }
    
    public InvoiceItem mapInvoiceItem(){
        InvoiceItem invoice_item = new InvoiceItem();
        invoice_item.setDepart_lot_id(departlot_combo.getSelectionModel().getSelectedItem().getId());
        invoice_item.setQuote_id(quote_combo.getSelectionModel().getSelectedItem().getId());
        invoice_item.setComments(comments_field.getText());
        if(invoice_item.getComments().equals("")){
            invoice_item.setComments("n/a");
        }
        return invoice_item;
    }
    
    public void setDepartLotTable(){
        remision_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().findDepartReport(c.getValue()).getId()));
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
                msabase.getPartRevisionDAO().findProductPart(
                        msabase.getDepartLotDAO().findPartRevision(c.getValue())).getPart_number()
        ));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findPartRevision(c.getValue()).getRev()));
        lot_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
    }
    
}
