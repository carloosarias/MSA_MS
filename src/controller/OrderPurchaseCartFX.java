/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Company;
import model.PurchaseItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseCartFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, String> productid_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> supplier_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
    @FXML
    private TableColumn<PurchaseItem, Double> unitprice_column;
    @FXML
    private TableColumn<PurchaseItem, Integer> unitsordered_column;
    @FXML
    private Button delete_button;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private Button create_button;
    
    public static ObservableList<PurchaseItem> cart_list = FXCollections.observableArrayList();
    private ObservableList<Company> company_list = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPurchaseItemTable();
        purchaseitem_tableview.setItems(cart_list);
        company_combo.setItems(company_list);
        
        delete_button.disableProperty().bind(purchaseitem_tableview.getSelectionModel().selectedItemProperty().isNull());
        company_combo.disableProperty().bind(Bindings.size(company_combo.getItems()).isEqualTo(0));
        create_button.disableProperty().bind(company_combo.getSelectionModel().selectedItemProperty().isNull());
        
        //CART_LIST LISTENER SETUP
        if(MainApp.OrderPurchaseCartFX_setup){
            cart_list.addListener((ListChangeListener) c -> {
                updateCompanyList();
            });
            MainApp.OrderPurchaseCartFX_setup = false;
        }
        
        
        delete_button.setOnAction((ActionEvent) -> {
            cart_list.remove(purchaseitem_tableview.getSelectionModel().getSelectedItem());
        });
    }
    
    public void updateCompanyList(){
        ObservableSet<Company> company_set = FXCollections.observableSet();
        for(PurchaseItem item : cart_list){
            company_set.add(item.getTemp_productsupplier().getCompany());
        }
        
        company_list.setAll(FXCollections.observableArrayList(company_set));
    }
    
    public void setPurchaseItemTable(){
        productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_productsupplier().getProduct().getId()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getProduct().getDescription()));
        supplier_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getCompany().getName()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_productsupplier().getQuantity()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getProduct().getUnit_measure()));
        unitprice_column.setCellValueFactory(new PropertyValueFactory("price_timestamp"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
    }
    
    
}
