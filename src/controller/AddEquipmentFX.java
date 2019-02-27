/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
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
    private GridPane root_gridpane;
    @FXML
    private ComboBox<EquipmentType> equipmenttype_combobox;
    @FXML
    private Button save_button;
    
    public static Equipment equipment;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipmenttype_combobox.getItems().setAll(msabase.getEquipmentTypeDAO().list(true));
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            createEquipment();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }    
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(equipmenttype_combobox.getSelectionModel().getSelectedItem().getId() == null){
            equipmenttype_combobox.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        equipmenttype_combobox.setStyle(null);
    }
        
    public void createEquipment(){
        equipment = new Equipment();
        equipment.setName("N/A");
        equipment.setSerial_number("N/A");
        equipment.setDescription("N/A");
        equipment.setPhysical_location("N/A");
        equipment.setNext_mantainance(DAOUtil.toUtilDate(LocalDate.now().plusDays(equipmenttype_combobox.getSelectionModel().getSelectedItem().getFrequency())));
        equipment.setActive(true);
        msabase.getEquipmentDAO().create(equipmenttype_combobox.getSelectionModel().getSelectedItem(), equipment);
    }
}
