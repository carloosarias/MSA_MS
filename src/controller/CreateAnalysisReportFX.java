/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.AnalysisType;
import model.Employee;
import model.Tank;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateAnalysisReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private TextField volume_field;
    @FXML
    private ComboBox<AnalysisType> analysistype_combo;
    @FXML
    private TextField factor_field;
    @FXML
    private TextField optimal_field;
    @FXML
    private TextField quantityused_field;
    @FXML
    private TextField result_field;
    @FXML
    private TextField estimatedadjust_field;
    @FXML
    private TextField appliedadjust_field;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportdate_picker.setValue(LocalDate.now());
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        employee_combo.getSelectionModel().selectFirst();
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list()));
        analysistype_combo.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list()));
        
        tank_combo.setOnAction((ActionEvent) -> {
            volume_field.setText(tank_combo.getSelectionModel().getSelectedItem().getVolume()+"");
        });
        
        analysistype_combo.setOnAction((ActionEvent) -> {
           factor_field.setText(analysistype_combo.getSelectionModel().getSelectedItem().getFactor()+"");
           optimal_field.setText(analysistype_combo.getSelectionModel().getSelectedItem().getOptimal()+"");
        });
    }    
    
}
