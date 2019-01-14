/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ProductSupplier;
import model.PurchaseItem;

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
    private Button add_button;
    @FXML
    private Button addtocart_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductSupplierTable();
        updateProductSupplierTable();
        delete_button.disableProperty().bind(productsupplier_tableview.getSelectionModel().selectedItemProperty().isNull());
        addtocart_button.disableProperty().bind(productsupplier_tableview.getSelectionModel().selectedItemProperty().isNull());

        add_button.setOnAction((ActionEvent) -> {
           showAdd_stage();
           updateProductSupplierTable();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
           ProductSupplier product_supplier = productsupplier_tableview.getSelectionModel().getSelectedItem();
           product_supplier.setActive(false);
           
           msabase.getProductSupplierDAO().update(product_supplier);
           
           updateProductSupplierTable();
        });
        
        addtocart_button.setOnAction((ActionEvent) -> {
            PurchaseItem item = new PurchaseItem();
            item.setTemp_productsupplier(productsupplier_tableview.getSelectionModel().getSelectedItem());
            item.setPrice_timestamp(productsupplier_tableview.getSelectionModel().getSelectedItem().getUnit_price());
            item.setUnits_ordered(1);
            OrderPurchaseCartFX.getCart_list().add(item);
        });
    }
    
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateProductSupplierFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Cotización de Producto");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateProductSupplierTable(){
        productsupplier_tableview.setItems(FXCollections.observableArrayList(msabase.getProductSupplierDAO().list(true)));
    }
    
    public void setProductSupplierTable(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct().getDescription()));
        supplier_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany().getName()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct().getUnit_measure()));
        quantity_column.setCellValueFactory(new PropertyValueFactory("quantity"));
        unitprice_column.setCellValueFactory(new PropertyValueFactory("unit_price"));
    }
    
}
