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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Product;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddProductFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField description_field;
    @FXML
    private ComboBox<String> unitmeasure_combo;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        unitmeasure_combo.setItems(FXCollections.observableArrayList(MainApp.unit_measure));
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            Product product = new Product();
            product.setDescription(description_field.getText());
            product.setUnit_measure(unitmeasure_combo.getSelectionModel().getSelectedItem());
            product.setActive(true);
            msabase.getProductDAO().create(product);
            Stage current_stage = (Stage) root_hbox.getScene().getWindow();
            current_stage.close();
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(unitmeasure_combo.getSelectionModel().isEmpty()){
            unitmeasure_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(description_field.getText().replace(" ", "").equals("")){
            description_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        unitmeasure_combo.setStyle(null);
        description_field.setStyle(null);
    }
}
