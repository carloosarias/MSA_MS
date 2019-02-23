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
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import model.ActivityReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.dateTimeFormatter;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ActivityReportFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private CheckBox datefilter_checkbox;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
    @FXML
    private TableView<ActivityReport> activityreport_tableview;
    @FXML
    private TableColumn<ActivityReport, Integer> reportid_column;
    @FXML
    private TableColumn<ActivityReport, String> name_column;
    @FXML
    private TableColumn<ActivityReport, String> reportdate_column;
    @FXML
    private TableColumn<ActivityReport, String> starttime_column;
    @FXML
    private TableColumn<ActivityReport, String> endtime_column;
    @FXML
    private TableColumn<ActivityReport, String> physicallocation_column;
    @FXML
    private Accordion details_accordion;
    @FXML
    private TextArea job_area;
    @FXML
    private TextArea actiontaken_area;
    @FXML
    private TextArea comments_area;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startdate_picker.setValue(LocalDate.now().minusWeeks(1));
        enddate_picker.setValue(LocalDate.now());
        
        details_accordion.disableProperty().bind(activityreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        activityreport_tableview.editableProperty().bind(delete_button.disabledProperty().not());
        job_area.editableProperty().bind(activityreport_tableview.editableProperty());
        actiontaken_area.editableProperty().bind(activityreport_tableview.editableProperty());
        comments_area.editableProperty().bind(activityreport_tableview.editableProperty());
        
        activityreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ActivityReport> observable, ActivityReport oldValue, ActivityReport newValue) -> {
            try{
                delete_button.setDisable(!newValue.getReport_date().toString().equals(LocalDate.now().toString()));
                job_area.setText(newValue.getJob_description());
                actiontaken_area.setText(newValue.getAction_taken());
                comments_area.setText(newValue.getComments());
            }catch(Exception e){
                delete_button.setDisable(true);
                job_area.setText(null);
                actiontaken_area.setText(null);
                comments_area.setText(null);
            }
        });
        
        job_area.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                activityreport_tableview.getSelectionModel().getSelectedItem().setJob_description(job_area.getText());
                msabase.getActivityReportDAO().update(activityreport_tableview.getSelectionModel().getSelectedItem());
            }
        });
        
        actiontaken_area.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                activityreport_tableview.getSelectionModel().getSelectedItem().setAction_taken(actiontaken_area.getText());
                msabase.getActivityReportDAO().update(activityreport_tableview.getSelectionModel().getSelectedItem());
            }
        });
        
        comments_area.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                activityreport_tableview.getSelectionModel().getSelectedItem().setComments(comments_area.getText());
                msabase.getActivityReportDAO().update(activityreport_tableview.getSelectionModel().getSelectedItem());
            }
        });
        
        setActivityReportTable();
        updateActivityReportTable();
        
        add_button.setOnAction((ActionEvent) -> {
           createActivityReport(); 
           updateActivityReportTable();
           activityreport_tableview.getSelectionModel().selectLast();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            deleteActivityReport();
            updateActivityReportTable();
        });
        
        datefilter_checkbox.setOnAction((ActionEvent) ->{
            updateActivityReportTable(); 
        });
        
        startdate_picker.setOnAction((ActionEvent) -> {
            updateActivityReportTable(); 
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            updateActivityReportTable();
        });
    }
    
    public void setActivityReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));   
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        
        starttime_column.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        starttime_column.setCellFactory(TextFieldTableCell.forTableColumn());
        starttime_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setStart_time(getStart_time(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
        
        endtime_column.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        endtime_column.setCellFactory(TextFieldTableCell.forTableColumn());
        endtime_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEnd_time(getEnd_time(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
        
        physicallocation_column.setCellValueFactory(new PropertyValueFactory<>("physical_location"));
        physicallocation_column.setCellFactory(TextFieldTableCell.forTableColumn());
        physicallocation_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhysical_location(t.getNewValue());
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
    }
    
    public String getStart_time(ActivityReport report, String value){
        try{
            dateTimeFormatter.parse(value);
        }catch(Exception e){
            return report.getStart_time();
        }
        return value;
    }
    
    public String getEnd_time(ActivityReport report, String value){
        try{
            dateTimeFormatter.parse(value);
        }catch(Exception e){
            return report.getEnd_time();
        }
        return value;
    }
    
    public void updateActivityReportTable(){
        if(datefilter_checkbox.isSelected()){
            activityreport_tableview.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().listDateRange(
                    DAOUtil.toUtilDate(startdate_picker.getValue()), 
                    DAOUtil.toUtilDate(enddate_picker.getValue())))
            );            
        }
        else{
            activityreport_tableview.setItems(FXCollections.observableArrayList(msabase.getActivityReportDAO().list()));            
        }
    }
    
    public void deleteActivityReport(){
        msabase.getActivityReportDAO().delete(activityreport_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createActivityReport(){
        ActivityReport activity_report = new ActivityReport();
        activity_report.setReport_date(DAOUtil.toUtilDate(LocalDate.now()));
        activity_report.setStart_time(LocalTime.now().format(dateTimeFormatter));
        activity_report.setEnd_time(LocalTime.now().format(dateTimeFormatter));
        activity_report.setJob_description("N/A");
        activity_report.setPhysical_location("N/A");
        activity_report.setAction_taken("N/A");
        activity_report.setComments("N/A");
        msabase.getActivityReportDAO().create(MainApp.current_employee, activity_report);
    }
}