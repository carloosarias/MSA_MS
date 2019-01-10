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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.ProductSupplier;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductSupplierFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<ProductSupplier> productsupplier_tableview;
    @FXML
    private TableColumn<ProductSupplier, Integer> id_column;
    @FXML
    private TableColumn<ProductSupplier, String> description_column;
    @FXML
    private TableColumn<ProductSupplier, String> supplier_column;
    @FXML
    private TableColumn<ProductSupplier, Double> quantity_column;
    @FXML
    private TableColumn<ProductSupplier, String> unitmeasure_column;
    @FXML
    private TableColumn<ProductSupplier, Double> unitprice_column;
    @FXML
    private Button delete_button;
    @FXML
    private Button edit_button;
    @FXML
    private Button add_button;
    @FXML
    private Button addtocart_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductSupplierTable();
        updateProductSupplierTable();
    }    
    public void updateProductSupplierTable(){
        productsupplier_tableview.setItems(FXCollections.observableArrayList(msabase.getProductSupplierDAO().list()));
    }
    
    public void setProductSupplierTable(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(c.getValue()).getDescription()));
        supplier_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findCompany(c.getValue()).getName()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProductSupplierDAO().findProduct(c.getValue()).getDescription()));
        quantity_column.setCellValueFactory(new PropertyValueFactory("quantity"));
        unitprice_column.setCellValueFactory(new PropertyValueFactory("unitprice"));
    }
    
}
