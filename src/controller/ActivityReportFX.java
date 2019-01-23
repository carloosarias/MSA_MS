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
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ActivityReport;
import model.Employee;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ActivityReportFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private Button filter_button;
    @FXML
    private CheckBox employeefilter_checkbox;
    @FXML
    private CheckBox datefilter_checkbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<ActivityReport> activityreport_tableview1;
    @FXML
    private TableColumn<ActivityReport, Integer> reportid_column1;
    @FXML
    private TableColumn<ActivityReport, String> employeeid_column;
    @FXML
    private TableColumn<ActivityReport, String> name_column;
    @FXML
    private TableColumn<ActivityReport, Date> reportdate_column;
    @FXML
    private TableColumn<ActivityReport, Time> starttime_column;
    @FXML
    private TableColumn<ActivityReport, Time> endtime_column;
    @FXML
    private TableView<ActivityReport> activityreport_tableview2;
    @FXML
    private TableColumn<ActivityReport, Integer> reportid_column2;
    @FXML
    private TableColumn<ActivityReport, String> jobdescription_column;
    @FXML
    private TableColumn<ActivityReport, String> physicallocation_column;
    @FXML
    private TableColumn<ActivityReport, String> actiontaken_column;
    @FXML
    private TableColumn<ActivityReport, String> comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startdate_picker.setValue(LocalDate.now().minusWeeks(1));
        enddate_picker.setValue(LocalDate.now());
        employee_combo.getItems().setAll(msabase.getModuleEmployeeDAO().list(msabase.getModuleDAO().find("Mantenimiento")));
        employee_combo.getSelectionModel().selectFirst();
        setActivityReportTable();
        updateActivityReportTable();
        
        filter_button.setOnAction((ActionEvent) -> {
            updateActivityReportTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
           showAdd_stage(); 
        });
    }
    
    public void setActivityReportTable(){
        reportid_column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        reportid_column2.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getActivityReportDAO().findEmployee(c.getValue()).getId()));
        name_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getActivityReportDAO().findEmployee(c.getValue()).toString()));
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        starttime_column.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        endtime_column.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        jobdescription_column.setCellValueFactory(new PropertyValueFactory<>("job_description"));
        physicallocation_column.setCellValueFactory(new PropertyValueFactory<>("physical_location"));
        actiontaken_column.setCellValueFactory(new PropertyValueFactory<>("action_taken"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void updateActivityReportTable(){
        if(employeefilter_checkbox.isSelected()){
            if(datefilter_checkbox.isSelected()){
                activityreport_tableview1.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().listEmployeeDateRange(
                    employee_combo.getSelectionModel().getSelectedItem(), 
                    DAOUtil.toUtilDate(startdate_picker.getValue()), 
                    DAOUtil.toUtilDate(enddate_picker.getValue())))
                );
            }
            else{
                activityreport_tableview1.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().listEmployee(
                    employee_combo.getSelectionModel().getSelectedItem()))
                );
            }
        }
        else if(datefilter_checkbox.isSelected()){
            activityreport_tableview1.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().listDateRange(
                    DAOUtil.toUtilDate(startdate_picker.getValue()), 
                    DAOUtil.toUtilDate(enddate_picker.getValue())))
            );            
        }
        else{
            activityreport_tableview1.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().list()));            
        }
        
        activityreport_tableview2.setItems(activityreport_tableview1.getItems());
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateActivityReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Trabajo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
