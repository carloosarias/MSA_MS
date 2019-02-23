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
        setDatePicker(reportdate_picker);
        
        employee_combo.setItems(FXCollections.observableArrayList(MainApp.current_employee));
        employee_combo.getSelectionModel().selectFirst();
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list()));
        analysistype_combo.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list(true)));
        setResult();
        
        tank_combo.setOnAction((ActionEvent) -> {
            volume_field.setText(tank_combo.getSelectionModel().getSelectedItem().getVolume()+"");
            setResult();
        });
        
        analysistype_combo.setOnAction((ActionEvent) -> {
           factor_field.setText(analysistype_combo.getSelectionModel().getSelectedItem().getFactor()+"");
           optimal_field.setText(analysistype_combo.getSelectionModel().getSelectedItem().getOptimal()+"");
            setResult();
        });
        
        quantityused_field.setOnAction((ActionEvent) -> {
            try{
                Double.parseDouble(quantityused_field.getText());
            }catch(Exception e){
                quantityused_field.setText("0.0");
            }
            setResult();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveAnalysisReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void setResult(){
        result_field.setText(""+(Double.parseDouble(quantityused_field.getText())*Double.parseDouble(factor_field.getText())));
        setEstimatedAdjust();
    }
    
    public void setEstimatedAdjust(){
        estimatedadjust_field.setText(""+(((Double.parseDouble(result_field.getText())*Double.parseDouble(optimal_field.getText()))*Double.parseDouble(volume_field.getText()))/1000));
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(employee_combo.getSelectionModel().isEmpty()){
            employee_combo.setStyle("-fx-background-color: lightpink;");
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
        
        try{
            Double.parseDouble(quantityused_field.getText());
        }catch(Exception e){
            quantityused_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(result_field.getText());
        }catch(Exception e){
            result_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(estimatedadjust_field.getText());
        }catch(Exception e){
            estimatedadjust_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(appliedadjust_field.getText());
        }catch(Exception e){
            appliedadjust_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        employee_combo.setStyle(null);
        tank_combo.setStyle(null);
        analysistype_combo.setStyle(null);
        quantityused_field.setStyle(null);
        result_field.setStyle(null);
        estimatedadjust_field.setStyle(null);
        appliedadjust_field.setStyle(null);
    }
        
    public void saveAnalysisReport(){
        AnalysisReport item = new AnalysisReport();
        item.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        item.setQuantity_used(Double.parseDouble(quantityused_field.getText()));
        item.setResult(Double.parseDouble(result_field.getText()));
        item.setEstimated_adjust(Double.parseDouble(estimatedadjust_field.getText()));
        item.setApplied_adjust(Double.parseDouble(appliedadjust_field.getText()));
        msabase.getAnalysisReportDAO().create(tank_combo.getSelectionModel().getSelectedItem(), analysistype_combo.getSelectionModel().getSelectedItem(), employee_combo.getSelectionModel().getSelectedItem(), item);
    }
}
