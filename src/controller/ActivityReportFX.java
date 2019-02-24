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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.ActivityReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.timeFormat;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ActivityReportFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private CheckBox date_filter;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private TableView<ActivityReport> activityreport_tableview;
    @FXML
    private TableColumn<ActivityReport, Integer> id_column;
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
    private TableView<ActivityReport> activityreport_tableview1;
    @FXML
    private TableColumn<ActivityReport, Integer> id_column1;
    @FXML
    private TableColumn<ActivityReport, String> description_column;
    @FXML
    private TableColumn<ActivityReport, String> action_column;
    @FXML
    private TableColumn<ActivityReport, String> comments_column;
    
    private ActivityReport activity_report;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startdate_picker.setValue(LocalDate.now().minusWeeks(1));
        enddate_picker.setValue(LocalDate.now());
        
        activityreport_tableview.editableProperty().bind(disable_button.disabledProperty().not());
        activityreport_tableview1.editableProperty().bind(activityreport_tableview.disableProperty().not());
        
        activityreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ActivityReport> observable, ActivityReport oldValue, ActivityReport newValue) -> {
            try{
                activityreport_tableview1.getSelectionModel().select(newValue);
                disable_button.setDisable(!newValue.getReport_date().toString().equals(LocalDate.now().toString()));
            }catch(Exception e){
                disable_button.setDisable(true);
            }
        });
        
        activityreport_tableview1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ActivityReport> observable, ActivityReport oldValue, ActivityReport newValue) -> {
            try{
                activityreport_tableview.getSelectionModel().select(newValue);
                disable_button.setDisable(!newValue.getReport_date().toString().equals(LocalDate.now().toString()));
            }catch(Exception e){
                disable_button.setDisable(true);
            }
        });
        
        setActivityReportTable();
        updateActivityReportTable();
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = activityreport_tableview.getItems().size();
            createActivityReport();
            updateActivityReportTable();
            if(current_size < activityreport_tableview.getItems().size()){
                activityreport_tableview.scrollTo(activity_report);
                activityreport_tableview.getSelectionModel().select(activity_report);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            deleteActivityReport();
            updateActivityReportTable();
        });
        
        date_filter.setOnAction((ActionEvent) ->{
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
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        id_column1.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        
        description_column.setCellValueFactory(new PropertyValueFactory<>("job_description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setJob_description(t.getNewValue());
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
        
        action_column.setCellValueFactory(new PropertyValueFactory<>("physical_location"));
        action_column.setCellFactory(TextFieldTableCell.forTableColumn());
        action_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAction_taken(t.getNewValue());
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("action_taken"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<ActivityReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getActivityReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            activityreport_tableview.refresh();
        });
    }
    
    public void updateActivityReportTable(){
        activityreport_tableview.getItems().setAll(msabase.getActivityReportDAO().list(DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()), date_filter.isSelected(), true));
        activityreport_tableview1.getItems().setAll(activityreport_tableview.getItems());
    }
    
    public void deleteActivityReport(){
        activityreport_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getActivityReportDAO().update(activityreport_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createActivityReport(){
        activity_report = new ActivityReport();
        activity_report.setReport_date(DAOUtil.toUtilDate(LocalDate.now()));
        activity_report.setStart_time(LocalTime.now().format(timeFormat));
        activity_report.setEnd_time(LocalTime.now().format(timeFormat));
        activity_report.setJob_description("N/A");
        activity_report.setPhysical_location("N/A");
        activity_report.setAction_taken("N/A");
        activity_report.setComments("N/A");
        activity_report.setActive(true);
        msabase.getActivityReportDAO().create(MainApp.current_employee, activity_report);
    }
    
    public String getStart_time(ActivityReport report, String value){
        try{
            timeFormat.parse(value.toUpperCase());
        }catch(Exception e){
            return report.getStart_time();
        }
        return value;
    }
    
    public String getEnd_time(ActivityReport report, String value){
        try{
            timeFormat.parse(value.toUpperCase());
        }catch(Exception e){
            return report.getEnd_time();
        }
        return value;
    }
}