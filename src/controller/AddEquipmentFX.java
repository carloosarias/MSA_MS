/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Equipment;
import model.EquipmentType;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddEquipmentFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<EquipmentType> equipmenttype_combobox;
    @FXML
    private TextField name_field;
    @FXML
    private TextArea description_area;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipmenttype_combobox.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeDAO().list()));
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveEquipment();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
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
        if(equipmenttype_combobox.getSelectionModel().getSelectedItem().getId() == null){
            equipmenttype_combobox.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        name_field.setStyle(null);
        description_area.setStyle(null);
        equipmenttype_combobox.setStyle(null);
    }
    
    public void saveEquipment(){
        Equipment equipment = new Equipment();
        equipment.setName(name_field.getText());
        equipment.setDescription(description_area.getText());
        equipment.setNext_mantainance(Date.valueOf(LocalDate.now().plusDays(equipmenttype_combobox.getSelectionModel().getSelectedItem().getFrequency())));
        
        msabase.getEquipmentDAO().create(equipmenttype_combobox.getSelectionModel().getSelectedItem(), equipment);
    }
}
