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
import model.Metal;
import model.SpecificationItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddSpecificationItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Metal> metal_combo;
    @FXML
    private TextField minimumthickness_field;
    @FXML
    private TextField maximumthickness_field;
    @FXML
    private Button submit_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metal_combo.setItems(FXCollections.observableArrayList(msabase.getMetalDAO().list()));
        
        submit_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            CreateSpecificationFX.addToQueue(getSpecificationItem());
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public SpecificationItem getSpecificationItem(){
        SpecificationItem item = new SpecificationItem();
        
        item.setTemp_metal(metal_combo.getSelectionModel().getSelectedItem());
        item.setMinimum_thickness(Double.parseDouble(minimumthickness_field.getText()));
        item.setMaximum_thickness(Double.parseDouble(maximumthickness_field.getText()));
        
        return item;
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(metal_combo.getSelectionModel().isEmpty()){
            metal_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        try{
            Double.parseDouble(minimumthickness_field.getText());
        } catch(Exception e){
            minimumthickness_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(maximumthickness_field.getText());
        } catch(Exception e){
            maximumthickness_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        metal_combo.setStyle(null);
        minimumthickness_field.setStyle(null);
        maximumthickness_field.setStyle(null);
    }
    
}
