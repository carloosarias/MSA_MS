/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
import model.QuoteItem;



/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class QuoteFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private CheckBox status_checkbox;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, Integer> id_column;
    @FXML
    private TableColumn<Quote, Date> quotedate_column;
    @FXML
    private TableColumn<Quote, String> contact_column;
    @FXML
    private TableColumn<Quote, Integer> eau_column;
    @FXML
    private TableColumn<Quote, String> comments_column;
    @FXML
    private TableColumn<Quote, String> status_column;
    @FXML
    private TableView<QuoteItem> quoteitem_tableview;
    @FXML
    private TableColumn<QuoteItem, String> listnumber_column;
    @FXML
    private TableColumn<QuoteItem, String> metal_name_column;
    @FXML
    private TableColumn<QuoteItem, String> density_column;
    @FXML
    private TableColumn<QuoteItem, Double> unitprice_column;
    @FXML
    private TableColumn<QuoteItem, String> maximumthickness_column;
    @FXML
    private TableColumn<QuoteItem, String> volume_column;
    @FXML
    private TableColumn<QuoteItem, String> weight_column;
    @FXML
    private TableColumn<QuoteItem, Double> estimatedprice_column;
    @FXML
    private TextField specificationnumber_field;
    @FXML
    private TextField specificationprocess_field;
    @FXML
    private TextField revisionarea_field;
    @FXML
    private TextField quotemargin_field;
    @FXML
    private TextField estimatedtotal_field;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Aprovado", "Descartado", "Pendiente");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        status_combo.setItems(FXCollections.observableArrayList(status_items));
        status_combo.getSelectionModel().selectFirst();
        
        setQuoteTable();
    }
    
    public void setQuoteTableItems(){
        if(partrev_combo.getSelectionModel().getSelectedItem() == null){
            quote_tableview.getItems().clear();
        }else{
            if(status_checkbox.isSelected()){
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem())));
            }else{
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(partrev_combo.getSelectionModel().getSelectedItem())));
            }
        }
    }
    
    public void setQuoteTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        contact_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getQuoteDAO().findCompanyContact(c.getValue()).getName()));
        quotedate_column.setCellValueFactory(new PropertyValueFactory<>("quote_date"));
        eau_column.setCellValueFactory(new PropertyValueFactory<>("estimated_annual_usage"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        status_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        status_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setApproved(t.getNewValue());
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
             setQuoteTableItems();
        });
    }

}
