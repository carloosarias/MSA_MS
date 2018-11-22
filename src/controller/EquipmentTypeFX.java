/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.EquipmentType;
import model.EquipmentTypeCheck;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EquipmentTypeFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<EquipmentType> equipmenttype_tableview;
    @FXML
    private TableColumn<EquipmentType, Integer> id_column;
    @FXML
    private TableColumn<EquipmentType, String> equipmenttypename_column;
    @FXML
    private TableColumn<EquipmentType, String> equipmenttypedescription_column;
    @FXML
    private TableColumn<EquipmentType, Integer> frequency_column;
    @FXML
    private Button add_button;
    @FXML
    private TableView<EquipmentTypeCheck> equipmenttypecheck_tableview;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> equipmenttypecheckname_column;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> equipmenttypecheckdescription_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    private Stage add_stage = new Stage();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTypeTableview();
        setEquipmentTypeItems();
        setEquipmentTypeCheckTableview();
        
        equipmenttype_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EquipmentType> observable, EquipmentType oldValue, EquipmentType newValue) -> {
            setEquipmentTypeCheckItems(equipmenttype_tableview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAddStage();
        });
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateEquipmentTypeFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar Check de Equipo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            setEquipmentTypeItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setEquipmentTypeTableview(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        equipmenttypename_column.setCellValueFactory(new PropertyValueFactory("name"));
        equipmenttypedescription_column.setCellValueFactory(new PropertyValueFactory("description"));
        frequency_column.setCellValueFactory(new PropertyValueFactory("frequency"));
    }
    
    public void setEquipmentTypeCheckTableview(){
        equipmenttypecheckname_column.setCellValueFactory(new PropertyValueFactory("name"));
        equipmenttypecheckdescription_column.setCellValueFactory(new PropertyValueFactory("description"));
    }
    
    public void setEquipmentTypeItems(){
        equipmenttype_tableview.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeDAO().list()));
    }
    
    public void setEquipmentTypeCheckItems(EquipmentType equipment_type){
        equipmenttypecheck_tableview.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeCheckDAO().list(equipment_type)));
    }
}
