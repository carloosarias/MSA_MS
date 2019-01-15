/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.EquipmentTypeCheck;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddEquipmentTypeCheckFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField name_field;
    @FXML
    private TextArea description_area;
    @FXML
    private Button save_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            
            CreateEquipmentTypeFX.equipmenttypecheck_list.add(mapEquipmentTypeCheck());
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public EquipmentTypeCheck mapEquipmentTypeCheck(){
        EquipmentTypeCheck item = new EquipmentTypeCheck();
        
        item.setName(name_field.getText());
        item.setDescription(description_area.getText());
        return item;
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(name_field.getText().replace(" ", "").equals("")){
            name_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(description_area.getText().replace(" ", "").equals("")){
            description_area.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        name_field.setStyle(null);
        description_area.setStyle(null);
    }
    
}
