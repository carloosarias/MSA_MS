/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Product;
import model.ProductType;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<ProductType> typefilter_combo;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<Product> product_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextField name_field;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    ObservableList<ProductType> type_list = FXCollections.observableArrayList(
        msabase.getProductTypeDAO().listActive(true)
    );
    
    ObservableList<String> filter_list = FXCollections.observableArrayList(
            "Activos",
            "Inactivos"
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typefilter_combo.setItems(type_list);
        typefilter_combo.getSelectionModel().selectFirst();
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst(); 
        updateList();
        
        typefilter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        product_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
            setFieldValues(product_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            filter_combo.getOnAction();
            setFieldValues(product_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            if(product_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getProductDAO().update(mapProduct(product_listview.getSelectionModel().getSelectedItem()));
            } else{
                msabase.getProductDAO().create(typefilter_combo.getSelectionModel().getSelectedItem(), mapProduct(new Product()));
            }
            
            setFieldValues(product_listview.getSelectionModel().getSelectedItem());
            updateList();
            disableFields(true);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(product_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
            }
        });
        
    }
    public void disableFields(boolean value){
        name_field.setDisable(value);
        active_check.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        edit_button.setDisable(!value);
        add_button.setDisable(!value);
        filter_combo.setDisable(!value);
        typefilter_combo.setDisable(!value);
        product_listview.setDisable(!value);
    }
    public Product mapProduct(Product product){
        product.setName(name_field.getText());
        product.setActive(!active_check.isSelected());
        return product;
    }
    
    public boolean testFields(){
        return true;
    }
    
    public void setFieldValues(Product product){
        if(product != null){
            id_field.setText(""+product.getId());
            name_field.setText(product.getName());
            active_check.setSelected(!product.isActive());
        }else{
            id_field.clear();
            name_field.clear();
            active_check.setSelected(false);
        }
    }
    
    public void updateList(){
        boolean active = true;
        product_listview.getItems().clear();
        switch(filter_combo.getSelectionModel().getSelectedItem()){
            case "Activos":
                active = true;
                break;
            case "Inactivos":
                active = false;
                break;
        }
        product_listview.setItems(FXCollections.observableArrayList(msabase.getProductDAO().listActive(typefilter_combo.getSelectionModel().getSelectedItem(), active)));
    }
}
