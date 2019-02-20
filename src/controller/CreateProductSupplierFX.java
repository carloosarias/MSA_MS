/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Company;
import model.Product;
import model.ProductSupplier;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateProductSupplierFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<Product> product_combo;
    @FXML
    private ComboBox<Company> company_combo;
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
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveProductSupplier();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }
    
    public void clearStyle(){
        product_combo.setStyle(null);
        company_combo.setStyle(null);
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
        
        return b;
    }
    
    public void saveProductSupplier(){
        ProductSupplier product_supplier = new ProductSupplier();
        product_supplier.setUnit_price(0.0);
        product_supplier.setSerial_number("N/A");
        product_supplier.setQuantity(0.0);
        product_supplier.setActive(true);
        msabase.getProductSupplierDAO().create(product_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), product_supplier);
    }
    
}
