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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.AnalysisReport;
import model.AnalysisType;
import model.Employee;
import model.Tank;
import msa_ms.MainApp;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.setDatePicker;

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
    private ComboBox<Tank> tank_combo;
    @FXML
    private ComboBox<AnalysisType> analysistype_combo;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportdate_picker.setValue(LocalDate.now());
        setDatePicker(reportdate_picker);
        
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list(true)));
        analysistype_combo.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list(true)));
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            createAnalysisReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(tank_combo.getSelectionModel().isEmpty()){
            tank_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(analysistype_combo.getSelectionModel().isEmpty()){
            analysistype_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        tank_combo.setStyle(null);
        analysistype_combo.setStyle(null);
    }
        
    public void createAnalysisReport(){
        AnalysisReport item = new AnalysisReport();
        item.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        item.setQuantity_used(0.0);
        item.setApplied_adjust(0.0);
        item.setActive(true);
        msabase.getAnalysisReportDAO().create(tank_combo.getSelectionModel().getSelectedItem(), analysistype_combo.getSelectionModel().getSelectedItem(), MainApp.current_employee, item);
    }
}
