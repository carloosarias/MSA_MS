/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Equipment;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EquipmentFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Equipment> equipment_tableview;
    @FXML
    private TableColumn<Equipment, Integer> id_column;
    @FXML
    private TableColumn<Equipment, String> equipmenttype_column;
    @FXML
    private TableColumn<Equipment, String> name_column;
    @FXML
    private TableColumn<Equipment, String> description_column;
    @FXML
    private TableColumn<Equipment, Date> nextmantainance_column;
    @FXML
    private Button add_button;
    @FXML
    private Tab equipmenttype_tab;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    private Stage add_stage = new Stage();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTypeTab();
        setEquipmentTableview();
        setEquipmentItems();
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAddStage();
        });
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddEquipmentFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar Nuevo Equipo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            setEquipmentItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setEquipmentTypeTab(){
        try {
            equipmenttype_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/EquipmentTypeFX.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(EquipmentFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setEquipmentTableview(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        name_column.setCellValueFactory(new PropertyValueFactory("name"));
        equipmenttype_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getEquipmentDAO().findEquipmentType(c.getValue()).getName()));
        description_column.setCellValueFactory(new PropertyValueFactory("description"));
        nextmantainance_column.setCellValueFactory(new PropertyValueFactory("next_mantainance"));
    }
    
    public void setEquipmentItems(){
        equipment_tableview.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().list()));
    }
}
