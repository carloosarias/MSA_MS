/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Equipment;
import model.MantainanceItem;
import model.MantainanceReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MantainanceReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Equipment> equipment_tableview;
    @FXML
    private TableColumn<Equipment, Integer> id_column;
    @FXML
    private TableColumn<Equipment, String> name_column;
    @FXML
    private TableColumn<Equipment, String> serialnumber_column;
    @FXML
    private TableColumn<Equipment, String> description_column;
    @FXML
    private TableColumn<Equipment, String> type_column;
    @FXML
    private TableColumn<Equipment, String> physicallocation_column;
    @FXML
    private TableColumn<Equipment, Date> nextmantainance_column;
    @FXML
    private TableView<MantainanceReport> mantainancereport_tableview;
    @FXML
    private TableColumn<MantainanceReport, Integer> reportid_column;
    @FXML
    private TableColumn<MantainanceReport, Date> reportdate_column;
    @FXML
    private TableColumn<MantainanceReport, String> employeeid_column;
    @FXML
    private TableColumn<MantainanceReport, String> employeename_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmentid_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmentname_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmenttype_column;
    @FXML
    private TableView<MantainanceItem> mantainanceitem_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> checkdescription_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    @FXML
    private TableColumn<MantainanceItem, Boolean> checkvalue_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private static Equipment equipment_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipment_selection = new Equipment();
        setEquipmentTableview();
        setMantainanceReportTableview();
        setMantainanceItemTableview();
        
        setEquipmentItems();
        setMantainanceReportItems();
        
        equipment_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Equipment> observable, Equipment oldValue, Equipment newValue) -> {
            equipment_selection = equipment_tableview.getSelectionModel().getSelectedItem();
            add_button.setDisable(equipment_tableview.getSelectionModel().isEmpty());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            equipment_tableview.setDisable(true);
            add_button.setDisable(true);
            showAdd_stage();
        });
        
        mantainancereport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue <? extends MantainanceReport> observable, MantainanceReport oldValue, MantainanceReport newValue) -> {
            setMantainanceItemItems();
        });
    }
    
    
    public void setMantainanceItemItems(){
        mantainanceitem_tableview.setItems(FXCollections.observableArrayList(msabase.getMantainanceItemDAO().list(mantainancereport_tableview.getSelectionModel().getSelectedItem())));
    }
    
    public void setMantainanceReportItems(){
        mantainancereport_tableview.setItems(FXCollections.observableArrayList(msabase.getMantainanceReportDAO().list()));
    }
    
    public void setEquipmentItems(){
        equipment_tableview.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().listPending(Date.valueOf(LocalDate.now()))));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateMantainanceReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Mantenimiento");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            equipment_tableview.setDisable(true);
            setEquipmentItems();
            setMantainanceReportItems();
            setMantainanceItemItems();
        } catch (IOException ex) {
            Logger.getLogger(ProcessReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setEquipmentTableview(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        name_column.setCellValueFactory(new PropertyValueFactory("name"));
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        description_column.setCellValueFactory(new PropertyValueFactory("description"));
        type_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getEquipmentDAO().findEquipmentType(c.getValue()).getName()));
        physicallocation_column.setCellValueFactory(new PropertyValueFactory("physical_location"));
        nextmantainance_column.setCellValueFactory(new PropertyValueFactory("next_mantainance"));
    }
    
    public void setMantainanceReportTableview(){
        reportid_column.setCellValueFactory(new PropertyValueFactory("id"));
        reportdate_column.setCellValueFactory(new PropertyValueFactory("report_date"));
        employeeid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceReportDAO().findEmployee(c.getValue()).getId()));
        employeename_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getMantainanceReportDAO().findEmployee(c.getValue()).toString()));
        equipmentid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceReportDAO().findEquipment(c.getValue()).getId()));
        equipmentname_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getMantainanceReportDAO().findEquipment(c.getValue()).getName()));
        equipmenttype_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getEquipmentDAO().findEquipmentType(msabase.getMantainanceReportDAO().findEquipment(c.getValue())).getName()));
    }
    
    public void setMantainanceItemTableview(){
        checkdescription_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceItemDAO().findEquipmentTypeCheck(c.getValue()).getDescription()));
        details_column.setCellValueFactory(new PropertyValueFactory("details"));
        checkvalue_column.setCellValueFactory(new PropertyValueFactory("check_value"));
        checkvalue_column.setCellFactory(column -> new CheckBoxTableCell<>());
        checkvalue_column.setCellValueFactory(cellData -> {
            MantainanceItem cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isCheck_value());
            return property;
        });
    }
    
    public static Equipment getEquipment_selection(){
        return equipment_selection;
    }
}
