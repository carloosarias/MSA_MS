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
import java.util.Arrays;
import java.util.Date;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DepartLot;
import model.PartRevision;
import model.ProductPart;
import model.Quote;

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
    private ComboBox<String> status_combo;
    @FXML
    private Button add_button;
    @FXML
    private TableView<Quote> quote_tableview;
    @FXML
    private TableColumn<Quote, Integer> id_column;
    @FXML
    private TableColumn<Quote, Date> quotedate_column;
    @FXML
    private TableColumn<Quote, String> contact_column;
    @FXML
    private TableColumn<Quote, Double> unitprice_column;
    @FXML
    private TableColumn<Quote, String> comments_column;
    @FXML
    private TableColumn<Quote, String> status_column;
    @FXML
    private CheckBox status_checkbox;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Aprovado", "Descartado", "Pendiente");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        status_combo.setItems(FXCollections.observableArrayList(status_items));
        status_combo.getSelectionModel().selectFirst();
        status_combo.setDisable(!status_checkbox.isSelected());
        
        status_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        status_column.setOnEditCommit((TableColumn.CellEditEvent<Quote, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setApproved(t.getNewValue());
            msabase.getQuoteDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        });
        setQuoteTable();
        part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            setPartRevisionItems(newValue);
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
           setQuoteTableItems(newValue, status_combo.getSelectionModel().getSelectedItem());
        });
        
        status_combo.setOnAction((ActionEvent) -> {
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        });
        
        status_checkbox.setOnAction((ActionEvent) -> {
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
            status_combo.setDisable(!status_checkbox.isSelected());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAddStage();
        });
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateQuoteFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Cotización");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            setQuoteTableItems(partrev_combo.getSelectionModel().getSelectedItem(), status_combo.getSelectionModel().getSelectedItem());
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPartRevisionItems(ProductPart product_part){
        partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part)));
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
    public void setQuoteTableItems(PartRevision part_revision, String status_text){
        if(part_revision == null){
            quote_tableview.getItems().clear();
        }else{
            if(status_checkbox.isSelected()){
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(part_revision, status_text)));
            }else{
                quote_tableview.setItems(FXCollections.observableArrayList(msabase.getQuoteDAO().list(part_revision)));
            }
        }
    }
    public void setQuoteTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        quotedate_column.setCellValueFactory(new PropertyValueFactory<>("quote_date"));
        contact_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getQuoteDAO().findCompanyContact(c.getValue()).getName()));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        status_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApproved()));
    }
}
