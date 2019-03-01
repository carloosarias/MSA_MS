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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Employee;
import model.Equipment;
import model.EquipmentTypeCheck;
import model.MantainanceItem;
import model.MantainanceReport;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateMantainanceReportFX implements Initializable {

    @FXML
    private HBox root_gridpane;
    @FXML
    private DatePicker date_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private Button save_button;
    
    public static MantainanceReport mantainance_report;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDatePicker(date_picker);
        date_picker.setValue(LocalDate.now());
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            createMantainanceReport();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(date_picker.getValue() == null){
            date_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(employee_combo.getSelectionModel().isEmpty()){
            employee_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(equipment_combo.getSelectionModel().isEmpty()){
            equipment_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        date_picker.setStyle(null);
        employee_combo.setStyle(null);
        equipment_combo.setStyle(null);
    }
    
    public void createMantainanceReport(){
        
        mantainance_report = new MantainanceReport();
        mantainance_report.setReport_date(DAOUtil.toUtilDate(date_picker.getValue()));
        mantainance_report.setActive(true);
        msabase.getMantainanceReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), equipment_combo.getSelectionModel().getSelectedItem(), mantainance_report);
        
        createMantainanceItems();
        setNextMantainance();
    }
    
    public void createMantainanceItems(){

        for(EquipmentTypeCheck equipment_type_check : msabase.getEquipmentTypeCheckDAO().list(equipment_combo.getSelectionModel().getSelectedItem(), true)){
            MantainanceItem mantainance_item = new MantainanceItem();
            mantainance_item.setDetails("N/A");
            mantainance_item.setActive(true);
            msabase.getMantainanceItemDAO().create(mantainance_report, equipment_type_check, mantainance_item);
        }
    }
    
    public void setNextMantainance(){
        equipment_combo.getSelectionModel().getSelectedItem().setNext_mantainance(DAOUtil.toUtilDate(date_picker.getValue().plusDays(equipment_combo.getSelectionModel().getSelectedItem().getFrequecy())));
        msabase.getEquipmentDAO().update(equipment_combo.getSelectionModel().getSelectedItem());
    }
    
}
