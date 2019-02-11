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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private TableColumn<ProductSupplier, String> serialnumber_column;
    @FXML
    private TableColumn<ProductSupplier, String> description_column;
    @FXML
    private TableColumn<ProductSupplier, String> supplier_column;
    @FXML
    private TableColumn<ProductSupplier, String> quantity_column;
    @FXML
    private TableColumn<ProductSupplier, String> unitmeasure_column;
    @FXML
    private TableColumn<ProductSupplier, String> unitmeasureprice_column;
    @FXML
    private TableColumn<ProductSupplier, String> unitprice_column;
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
        
        productsupplier_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductSupplier> observable, ProductSupplier oldValue, ProductSupplier newValue) -> {
            addtocart_button.setDisable(productsupplier_tableview.getSelectionModel().isEmpty());
            isInList(productsupplier_tableview.getSelectionModel().getSelectedItem());
        });

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
            item.setProductsupplier_serialnumber(item.getTemp_productsupplier().getSerial_number());
            item.setProduct_unitmeasure(item.getTemp_productsupplier().getProduct_unitmeasure());
            item.setProduct_description(item.getTemp_productsupplier().getProduct_description());
            item.setProductsupplier_quantity(item.getTemp_productsupplier().getQuantity());
            item.setUnits_ordered(1);
            item.setPrice_updated(item.getPrice_timestamp());
            item.setDate_modified(DAOUtil.toUtilDate(LocalDate.now()));
            item.setModified(false);
            OrderPurchaseCartFX.cart_list.add(item);
            OrderPurchaseCartFX.updateCompanyList();
            isInList(productsupplier_tableview.getSelectionModel().getSelectedItem());
        });
        
    }
    
    public void isInList(ProductSupplier product_supplier){
        for(PurchaseItem item : OrderPurchaseCartFX.cart_list){
            if(item.getTemp_productsupplier().equals(product_supplier)){
                addtocart_button.setDisable(true);
            }
        }
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
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(4);
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        serialnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        serialnumber_column.setOnEditCommit((TableColumn.CellEditEvent<ProductSupplier, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setSerial_number(t.getNewValue());
            msabase.getProductSupplierDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productsupplier_tableview.refresh();
        });
        description_column.setCellValueFactory(new PropertyValueFactory("product_description"));
        supplier_column.setCellValueFactory(new PropertyValueFactory("company_name"));
        unitmeasure_column.setCellValueFactory(new PropertyValueFactory("product_unitmeasure"));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getQuantity())));
        quantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column.setOnEditCommit((TableColumn.CellEditEvent<ProductSupplier, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProductSupplierDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productsupplier_tableview.refresh();
        });
        unitmeasureprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getUnit_price()/c.getValue().getQuantity()))+" USD"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getUnit_price())+" USD"));
        unitprice_column.setCellFactory(TextFieldTableCell.forTableColumn());
        unitprice_column.setOnEditCommit((TableColumn.CellEditEvent<ProductSupplier, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnit_price(getUnit_priceValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProductSupplierDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productsupplier_tableview.refresh();
        });
    }
    
    public Double getUnit_priceValue(ProductSupplier revision, String unit_price){
        try{
            return Double.parseDouble(unit_price);
        }catch(Exception e){
            return revision.getUnit_price();
        }
    }
    
    public Double getQuantityValue(ProductSupplier revision, String quantity){
        try{
            return Double.parseDouble(quantity);
        }catch(Exception e){
            return revision.getQuantity();
        }
    }
}
