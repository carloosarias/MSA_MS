/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.EquipmentType;
import model.EquipmentTypeCheck;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateEquipmentTypeFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField name_field;
    @FXML
    private Spinner<Integer> frequency_spinner;
    @FXML
    private TextArea description_area;
    @FXML
    private TableView<EquipmentTypeCheck> equipmenttypecheck_tableview;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> name_column;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> description_column;
    @FXML
    private Button add_button;
    @FXML
    private Button save_button;
    @FXML
    private Button delete_button;
    
    private Stage add_stage = new Stage();
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    public static ObservableList<EquipmentTypeCheck> equipmenttypecheck_list = FXCollections.observableArrayList();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //CLEAN LIST WHEN STARTING
        equipmenttypecheck_list.clear();
        
        setEquipmentTypeCheckTable();
        equipmenttypecheck_tableview.setItems(equipmenttypecheck_list);
        frequency_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));
        delete_button.disableProperty().bind(equipmenttypecheck_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        //EQUIPMENTTYPECHECK_LIST LISTENER SETUP
        if(MainApp.CreateEquipmentTypeFX_setup){
            //NOT IMPLEMENTED
            MainApp.CreateEquipmentTypeFX_setup = false;
        }
        
        delete_button.setOnAction((ActionEvent) -> {
            equipmenttypecheck_list.remove(equipmenttypecheck_tableview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAddStage();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            
            saveEquipmentType();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }    
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddEquipmentTypeCheckFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar Check de Equipo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if(frequency_spinner.getValue() <= 0){
            frequency_spinner.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        name_field.setStyle(null);
        description_area.setStyle(null);
        frequency_spinner.setStyle(null);
    }
    
    public void saveEquipmentType(){
        EquipmentType equipment_type = new EquipmentType();
        equipment_type.setName(name_field.getText());
        equipment_type.setDescription(description_area.getText());
        equipment_type.setFrequency(frequency_spinner.getValue());
        msabase.getEquipmentTypeDAO().create(equipment_type);
        saveEquipmentTypeCheckItems(equipment_type);
    }
    
    public void saveEquipmentTypeCheckItems(EquipmentType equipment_type){
        for(EquipmentTypeCheck item: equipmenttypecheck_list){
            msabase.getEquipmentTypeCheckDAO().create(equipment_type, item);
        }
    }
    
    public void setEquipmentTypeCheckTable(){
        name_column.setCellValueFactory(new PropertyValueFactory("name"));
        description_column.setCellValueFactory(new PropertyValueFactory("description"));
    }
    
}
