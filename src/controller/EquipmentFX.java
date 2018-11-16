/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EquipmentFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> equipment_tableview;
    @FXML
    private TableColumn<?, ?> id_column;
    @FXML
    private TableColumn<?, ?> equipmenttype_column;
    @FXML
    private TableColumn<?, ?> name_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private Button add_button;
    @FXML
    private Tab equipmenttype_tab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTypeTab();
    }
    
    public void setEquipmentTypeTab(){
        try {
            equipmenttype_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/EquipmentTypeFX.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(EquipmentFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
