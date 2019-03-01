/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Equipment;
import model.EquipmentType;
import model.MantainanceItem;
import model.MantainanceReport;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MantainanceReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<MantainanceReport> mantainancereport_tableview;
    @FXML
    private TableColumn<MantainanceReport, String> reportdate_column;
    @FXML
    private TableColumn<MantainanceReport, String> employee_column;
    @FXML
    private TableColumn<MantainanceReport, String> name_column;
    @FXML
    private TableColumn<MantainanceReport, String> serialnumber_column;
    @FXML
    private TableColumn<MantainanceReport, String> type_column;
    @FXML
    private TableColumn<MantainanceReport, String> location_column;
    @FXML
    private ComboBox<EquipmentType> type_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private CheckBox type_filter;
    @FXML
    private CheckBox equipment_filter;
    @FXML
    private Button pdf_button;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<MantainanceItem> mantainanceitem_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> checkdescription_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMantainanceReportTable();
        updateMantainanceReportTable();
        
        type_combo.disableProperty().bind(type_filter.selectedProperty().not());
        equipment_combo.disableProperty().bind(equipment_filter.selectedProperty().not().or(type_combo.getSelectionModel().selectedItemProperty().isNull()));
        details_tab.disableProperty().bind(mantainancereport_tableview.getSelectionModel().selectedItemProperty().isNull());
        disable_button.disableProperty().bind(mantainancereport_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button.disableProperty().bind(mantainancereport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        type_combo.getItems().setAll(msabase.getEquipmentTypeDAO().list(true));
        
        type_combo.setOnAction((ActionEvent) -> {
            try{
                equipment_combo.getItems().setAll(msabase.getEquipmentDAO().list(type_combo.getSelectionModel().getSelectedItem(), true));
            }catch(Exception e){
                equipment_combo.getItems().clear();
            }
            updateMantainanceReportTable();
        });
        
        equipment_combo.setOnAction((ActionEvent) -> {
            updateMantainanceReportTable();
        });
        
        type_filter.setOnAction((ActionEvent) -> {
            updateMantainanceReportTable();
        });
        
        equipment_filter.setOnAction((ActionEvent) -> {
           updateMantainanceReportTable(); 
        });
        
        add_button.setOnAction((ActionEvent) -> {
           int current_size = mantainancereport_tableview.getItems().size();
           showAdd_stage(); 
           updateMantainanceReportTable();
           if(current_size < mantainancereport_tableview.getItems().size()){
               mantainancereport_tableview.scrollTo(CreateMantainanceReportFX.mantainance_report);
               mantainancereport_tableview.getSelectionModel().select(CreateMantainanceReportFX.mantainance_report);
           }
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/CreateMantainanceReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Generar Reporte de Mantenimiento");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MantainanceReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setMantainanceReportTable(){
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        employee_column.setCellValueFactory(new PropertyValueFactory("employee_name"));
        name_column.setCellValueFactory(new PropertyValueFactory("equipment_name"));
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        type_column.setCellValueFactory(new PropertyValueFactory("equipment_type"));
        location_column.setCellValueFactory(new PropertyValueFactory("physical_location"));
    }
    
    public void updateMantainanceReportTable(){
        try{
            mantainancereport_tableview.getItems().setAll(msabase.getMantainanceReportDAO().list(type_combo.getSelectionModel().getSelectedItem(), equipment_combo.getSelectionModel().getSelectedItem(), type_filter.isSelected(), equipment_filter.isSelected(), true));
        }catch(Exception e) {
            mantainancereport_tableview.getItems().clear();
        }
    }
}
