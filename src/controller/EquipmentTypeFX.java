/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.ActivityReport;
import model.EquipmentType;
import model.EquipmentTypeCheck;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EquipmentTypeFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<EquipmentType> equipmenttype_tableview;
    @FXML
    private TableColumn<EquipmentType, String> typename_column;
    @FXML
    private TableColumn<EquipmentType, String> typedesc_column;
    @FXML
    private TableColumn<EquipmentType, String> typefreq_column;
    @FXML
    private Button addtype_button;
    @FXML
    private Button disabletype_button;
    @FXML
    private Tab check_tab;
    @FXML
    private TableView<EquipmentTypeCheck> equipmenttypecheck_tableview;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> checkname_column;
    @FXML
    private TableColumn<EquipmentTypeCheck, String> checkdesc_column;
    @FXML
    private Button addcheck_button;
    @FXML
    private Button disablecheck_button;
    
    private EquipmentType equipment_type;
    
    private EquipmentTypeCheck equipment_type_check;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTypeTable();
        setEquipmentTypeCheckTable();
        updateEquipmentTypeTable();
        
        disabletype_button.disableProperty().bind(equipmenttype_tableview.getSelectionModel().selectedItemProperty().isNull());
        check_tab.disableProperty().bind(equipmenttype_tableview.getSelectionModel().selectedItemProperty().isNull());
        disablecheck_button.disableProperty().bind(equipmenttypecheck_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        equipmenttype_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EquipmentType> observable, EquipmentType oldValue, EquipmentType newValue) -> {
            updateEquipmentTypeCheckTable();
        });
        
        addcheck_button.setOnAction((ActionEvent) -> {
            int current_size = equipmenttypecheck_tableview.getItems().size();
            createEquipmentTypeCheck();
            updateEquipmentTypeCheckTable();
            if(current_size < equipmenttypecheck_tableview.getItems().size()){
                equipmenttypecheck_tableview.scrollTo(equipment_type_check);
                equipmenttypecheck_tableview.getSelectionModel().select(equipment_type_check);
            }
        });
        
        disablecheck_button.setOnAction((ActionEvent) -> {
            disableEquipmentTypeCheck();
            updateEquipmentTypeCheckTable();
        });
        
        addtype_button.setOnAction((ActionEvent) -> {
            int current_size = equipmenttype_tableview.getItems().size();
            createEquipmentType();
            updateEquipmentTypeTable();
            if(current_size < equipmenttype_tableview.getItems().size()){
                equipmenttype_tableview.scrollTo(equipment_type);
                equipmenttype_tableview.getSelectionModel().select(equipment_type);
            }
        });
        
        disabletype_button.setOnAction((ActionEvent) -> {
            disableEquipmentType();
            updateEquipmentTypeTable();
        });
    }
    
    public void disableEquipmentTypeCheck(){
        equipmenttypecheck_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getEquipmentTypeCheckDAO().update(equipmenttypecheck_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createEquipmentTypeCheck(){
        equipment_type_check = new EquipmentTypeCheck();
        equipment_type_check.setName("N/A");
        equipment_type_check.setDescription("N/A");
        equipment_type_check.setActive(true);
        
        msabase.getEquipmentTypeCheckDAO().create(equipmenttype_tableview.getSelectionModel().getSelectedItem(), equipment_type_check);
    }
    
    public void updateEquipmentTypeCheckTable(){
        try{
            equipmenttypecheck_tableview.getItems().setAll(msabase.getEquipmentTypeCheckDAO().list(equipmenttype_tableview.getSelectionModel().getSelectedItem(), true));
        }catch(Exception e){
            equipmenttypecheck_tableview.getItems().clear();
        }
    }
    
    public void setEquipmentTypeCheckTable(){
        checkname_column.setCellValueFactory(new PropertyValueFactory("name"));
        checkname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        checkname_column.setOnEditCommit((TableColumn.CellEditEvent<EquipmentTypeCheck, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getEquipmentTypeCheckDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipmenttype_tableview.refresh();
        });
        
        checkdesc_column.setCellValueFactory(new PropertyValueFactory("description"));
        checkdesc_column.setCellFactory(TextFieldTableCell.forTableColumn());
        checkdesc_column.setOnEditCommit((TableColumn.CellEditEvent<EquipmentTypeCheck, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getEquipmentTypeCheckDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipmenttype_tableview.refresh();
        });
    }
    
    public void disableEquipmentType(){
        equipmenttype_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getEquipmentTypeDAO().update(equipmenttype_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createEquipmentType(){
        equipment_type = new EquipmentType();
        equipment_type.setName("N/A");
        equipment_type.setDescription("N/A");
        equipment_type.setFrequency(365);
        equipment_type.setActive(true);
        
        msabase.getEquipmentTypeDAO().create(equipment_type);
    }
    
    public void setEquipmentTypeTable(){
        typename_column.setCellValueFactory(new PropertyValueFactory("name"));
        typename_column.setCellFactory(TextFieldTableCell.forTableColumn());
        typename_column.setOnEditCommit((TableColumn.CellEditEvent<EquipmentType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getEquipmentTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipmenttype_tableview.refresh();
        });
        
        typedesc_column.setCellValueFactory(new PropertyValueFactory("description"));
        typedesc_column.setCellFactory(TextFieldTableCell.forTableColumn());
        typedesc_column.setOnEditCommit((TableColumn.CellEditEvent<EquipmentType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getEquipmentTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipmenttype_tableview.refresh();
        });
        
        typefreq_column.setCellValueFactory(c -> new SimpleStringProperty("Cada "+c.getValue().getFrequency()+" Días"));
        typefreq_column.setCellFactory(TextFieldTableCell.forTableColumn());
        typefreq_column.setOnEditCommit((TableColumn.CellEditEvent<EquipmentType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFrequency(getFrequencyValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getEquipmentTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            equipmenttype_tableview.refresh();
        });
    }
    
    public void updateEquipmentTypeTable(){
        equipmenttype_tableview.getItems().setAll(msabase.getEquipmentTypeDAO().list(true));
    }
    
    public Integer getFrequencyValue(EquipmentType equipment_type, String frequency){
        try{
            return Integer.parseInt(frequency);
        }catch(Exception e){
            return equipment_type.getFrequency();
        }
    }
}
