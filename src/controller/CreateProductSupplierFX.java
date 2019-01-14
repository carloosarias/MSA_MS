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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.Product;
import model.ProductSupplier;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateProductSupplierFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Product> product_combo;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private ComboBox<String> unitmeasure_combo;
    @FXML
    private TextField unitprice_field;
    @FXML
    private Button cancel_button;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        product_combo.setItems(FXCollections.observableArrayList(msabase.getProductDAO().list(true)));
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listSupplier(true)));
        
        product_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
            unitmeasure_combo.setItems(FXCollections.observableArrayList(newValue.getUnit_measure()));
            unitmeasure_combo.getSelectionModel().selectFirst();
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveProductSupplier();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void clearStyle(){
        product_combo.setStyle(null);
        company_combo.setStyle(null);
        unitprice_field.setStyle(null);
        quantity_field.setStyle(null);        
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();

        if(product_combo.getSelectionModel().isEmpty()){
            product_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(company_combo.getSelectionModel().isEmpty()){
            company_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try {
            Double.parseDouble(unitprice_field.getText());
        }catch(Exception e){
            unitprice_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        return b;
    }
    
    public void saveProductSupplier(){
        ProductSupplier product_supplier = new ProductSupplier();
        product_supplier.setUnit_price(Double.parseDouble(unitprice_field.getText()));
        product_supplier.setQuantity(Double.parseDouble(quantity_field.getText()));
        product_supplier.setActive(true);
        
        msabase.getProductSupplierDAO().create(product_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), product_supplier);
    }
    
}
