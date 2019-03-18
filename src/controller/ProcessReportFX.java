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
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;
import model.Module;
import model.ProcessReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProcessReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private HBox filter_hbox;
    @FXML
    private CheckBox employeefilter_checkbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private CheckBox datefilter_checkbox;
    @FXML
    private Button filter_button;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<ProcessReport> processreport_tableview;
    @FXML
    private TableColumn<ProcessReport, Integer> id_column;
    @FXML
    private TableColumn<ProcessReport, String> employee_column;
    @FXML
    private TableColumn<ProcessReport, String> reportdate_column;
    @FXML
    private TableColumn<ProcessReport, String> partnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> revision_column;
    @FXML
    private TableColumn<ProcessReport, String> lotnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> tankid_column;
    @FXML
    private TableColumn<ProcessReport, String> equipmentid_column;
    @FXML
    private TableColumn<ProcessReport, String> process_column;
    @FXML
    private TableColumn<ProcessReport, Integer> quantity_column;
    @FXML
    private TableColumn<ProcessReport, Boolean> quality_column;
    @FXML
    private TextField amperage_field;
    @FXML
    private TextField voltage_field;
    @FXML
    private TextField starttime_field;
    @FXML
    private TextField endtime_field;
    @FXML
    private TextArea comments_field;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private List<Module> modules;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(true)));
        employee_combo.getSelectionModel().select(MainApp.current_employee);
        startdate_picker.setValue(LocalDate.now());
        enddate_picker.setValue(LocalDate.now().plusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        filter_hbox.setDisable(!MainApp.current_employee.isAdmin());
        
        setProcessReportTable();
        updateProcessReportTable();
        
        filter_button.setOnAction((ActionEvent) -> {
            updateProcessReportTable();
        });
        
        processreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProcessReport> observable, ProcessReport oldValue, ProcessReport newValue) -> {
            setProcessReportDetails(newValue);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateProcessReportTable();
        });
        
    }

    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateProcessReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Proceso");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProcessReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateProcessReportTable(){
        if(employeefilter_checkbox.isSelected()){
            if(datefilter_checkbox.isSelected()){
                processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listEmployeeDateRange(
                    employee_combo.getSelectionModel().getSelectedItem(), 
                    DAOUtil.toUtilDate(startdate_picker.getValue()), 
                    DAOUtil.toUtilDate(enddate_picker.getValue())))
                );
            }
            else{
                processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listEmployee(
                    employee_combo.getSelectionModel().getSelectedItem()))
                );
            }
        }
        else if(datefilter_checkbox.isSelected()){
            processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listDateRange(
                    DAOUtil.toUtilDate(startdate_picker.getValue()), 
                    DAOUtil.toUtilDate(enddate_picker.getValue())))
            );            
        }
        else{
            processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().list()));            
        }
    }
    
    public void setProcessReportDetails(ProcessReport process_report){
        if(process_report == null){
            amperage_field.setText(null);
            voltage_field.setText(null);
            starttime_field.setText(null);
            endtime_field.setText(null);
            comments_field.setText(null);
        }
        else{
            amperage_field.setText(process_report.getAmperage()+"");
            voltage_field.setText(process_report.getVoltage()+"");
            starttime_field.setText(process_report.getStart_time().toString());
            endtime_field.setText(process_report.getEnd_time().toString());
            comments_field.setText(process_report.getComments());
        }
    }
    
    public void setProcessReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findEmployee(c.getValue()).toString()));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(msabase.getProcessReportDAO().findPartRevision(c.getValue())).getPart_number()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findPartRevision(c.getValue()).getRev()));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        tankid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findTank(c.getValue()).toString()));
        equipmentid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findEquipment(c.getValue()).toString()));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quality_column.setCellFactory(column -> new CheckBoxTableCell<>());
        quality_column.setCellValueFactory(cellData -> {
            ProcessReport cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isQuality_passed());
            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> {
                cellValue.setQuality_passed(newValue);
                msabase.getProcessReportDAO().update(cellValue);
            });
            return property;
        });
    }
    
}


