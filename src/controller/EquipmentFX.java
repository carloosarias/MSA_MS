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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
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
    private GridPane root_gridpane;
    @FXML
    private TableView<Equipment> equipment_tableview;
    @FXML
    private TableColumn<Equipment, String> type_column;
    @FXML
    private TableColumn<Equipment, String> name_column;
    @FXML
    private TableColumn<Equipment, String> description_column;
    @FXML
    private TableColumn<Equipment, String> serialnumber_column;
    @FXML
    private TableColumn<Equipment, String> physicallocation_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private Stage add_stage = new Stage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTable();
        updateEquipmentTable();
        
        disable_button.disableProperty().bind(equipment_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = equipment_tableview.getItems().size();
            showAdd_stage();
            updateEquipmentTable();
            if(current_size < equipment_tableview.getItems().size()){
                equipment_tableview.scrollTo(AddEquipmentFX.equipment);
                equipment_tableview.getSelectionModel().select(AddEquipmentFX.equipment);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
           disableEquipment();
           updateEquipmentTable();
        });
    }
    
    public void disableEquipment(){
        equipment_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getEquipmentDAO().update(equipment_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/AddEquipmentFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Registrar Equipo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(EquipmentFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setEquipmentTable(){
        type_column.setCellValueFactory(new PropertyValueFactory("equipmenttype_name"));
        
        name_column.setCellValueFactory(new PropertyValueFactory("name"));
        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        name_column.setOnEditCommit((TableColumn.CellEditEvent<Equipment, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getEquipmentDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipment_tableview.refresh();
        });
        
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        serialnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        serialnumber_column.setOnEditCommit((TableColumn.CellEditEvent<Equipment, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setSerial_number(t.getNewValue());
            msabase.getEquipmentDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipment_tableview.refresh();
        });
        
        description_column.setCellValueFactory(new PropertyValueFactory("description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<Equipment, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getEquipmentDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipment_tableview.refresh();
        });
        physicallocation_column.setCellValueFactory(new PropertyValueFactory("physical_location"));
        physicallocation_column.setCellFactory(TextFieldTableCell.forTableColumn());
        physicallocation_column.setOnEditCommit((TableColumn.CellEditEvent<Equipment, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhysical_location(t.getNewValue());
            msabase.getEquipmentDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipment_tableview.refresh();
        });
        
    }
    
    public void updateEquipmentTable(){
        equipment_tableview.getItems().setAll(msabase.getEquipmentDAO().list(true));
    }
}
