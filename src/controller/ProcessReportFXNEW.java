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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ActivityReport;
import model.ProcessReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.process_list;
import static msa_ms.MainApp.setDatePicker;
import static msa_ms.MainApp.timeFormat;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProcessReportFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private CheckBox datefilter_check;
    @FXML
    private TableView<ProcessReport> processreport_tableview1;
    @FXML
    private TableColumn<ProcessReport, Integer> id_column1;
    @FXML
    private TableColumn<ProcessReport, String> date_column1;
    @FXML
    private TableColumn<ProcessReport, String> start_column;
    @FXML
    private TableColumn<ProcessReport, String> end_column;
    @FXML
    private TableColumn<ProcessReport, String> employee_column;
    @FXML
    private TableColumn<ProcessReport, String> partnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> rev_column;
    @FXML
    private TableColumn<ProcessReport, String> quantity_column;
    @FXML
    private TableColumn<ProcessReport, String> quality_column;
    @FXML
    private TableView<ProcessReport> processreport_tableview2;
    @FXML
    private TableColumn<ProcessReport, Integer> id_column2;
    @FXML
    private TableColumn<ProcessReport, String> date_column2;
    @FXML
    private TableColumn<ProcessReport, String> tank_column;
    @FXML
    private TableColumn<ProcessReport, String> equipment_column;
    @FXML
    private TableColumn<ProcessReport, String> process_column;
    @FXML
    private TableColumn<ProcessReport, String> voltage_column;
    @FXML
    private TableColumn<ProcessReport, String> amperage_column;
    @FXML
    private TableColumn<ProcessReport, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private GridPane delete_pane;
    @FXML
    private Button disable_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        startdate_picker.setValue(LocalDate.now());
        enddate_picker.setValue(LocalDate.now().plusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        setProcessReportTable();
        updateProcessReportTable();
        
        delete_pane.setDisable(!MainApp.current_employee.isAdmin());
        disable_button.disableProperty().bind(processreport_tableview1.getSelectionModel().selectedItemProperty().isNull());
        
        processreport_tableview1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProcessReport> observable, ProcessReport oldValue, ProcessReport newValue) -> {
            try{
                processreport_tableview2.getSelectionModel().select(newValue);
            }catch(Exception e){
            }
        });
        
        processreport_tableview2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProcessReport> observable, ProcessReport oldValue, ProcessReport newValue) -> {
            try{
                processreport_tableview1.getSelectionModel().select(newValue);
            }catch(Exception e){
            }
        });
        
        startdate_picker.setOnAction((ActionEvent) -> {
            updateProcessReportTable();
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            updateProcessReportTable();
        });
        
        datefilter_check.setOnAction((ActionEvent) -> {
           updateProcessReportTable(); 
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            msabase.getProcessReportDAO().delete(processreport_tableview1.getSelectionModel().getSelectedItem());
            updateProcessReportTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = processreport_tableview1.getItems().size();
            showAdd_stage();
            updateProcessReportTable();
            if(current_size < processreport_tableview1.getItems().size()){
                processreport_tableview1.scrollTo(CreateProcessReportFX.process_report);
                processreport_tableview1.getSelectionModel().select(CreateProcessReportFX.process_report);
            }
        });
        
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/CreateProcessReportFX.fxml"));
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
        
    public void setProcessReportTable(){
        id_column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        id_column2.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column1.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        start_column.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        start_column.setCellFactory(TextFieldTableCell.forTableColumn());
        start_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setStart_time(getStart_timeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        end_column.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        end_column.setCellFactory(TextFieldTableCell.forTableColumn());
        end_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEnd_time(getEnd_timeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        equipment_column.setCellValueFactory(new PropertyValueFactory<>("equipment_name"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        process_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(process_list)));
        process_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setProcess(t.getNewValue());
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        voltage_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getVoltage())));
        voltage_column.setCellFactory(TextFieldTableCell.forTableColumn());
        voltage_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setVoltage(getVoltageValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        amperage_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getAmperage())));
        amperage_column.setCellFactory(TextFieldTableCell.forTableColumn());
        amperage_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAmperage(getAmperageValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
        
        quality_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().quality_passedToString()));
        quality_column.setCellFactory(ComboBoxTableCell.forTableColumn("Bueno", "Malo"));
        quality_column.setOnEditCommit((TableColumn.CellEditEvent<ProcessReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuality_passed(t.getNewValue());
            msabase.getProcessReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateProcessReportTable();
        });
    }
    
    public void updateProcessReportTable(){
        try{
            processreport_tableview1.getItems().setAll(msabase.getProcessReportDAO().list(MainApp.current_employee, DAOUtil.toUtilDate(startdate_picker.getValue().minusDays(1)), DAOUtil.toUtilDate(enddate_picker.getValue()), datefilter_check.isSelected()));
        }catch(Exception e){
            processreport_tableview1.getItems().clear();
        }
        processreport_tableview2.getItems().setAll(processreport_tableview1.getItems());
    }
    
    public String getStart_timeValue(ProcessReport report, String start_time){
        try{
            timeFormat.parse(start_time.toUpperCase());
        }catch(Exception e){
            return report.getStart_time();
        }
        return start_time;
    }
    
    public String getEnd_timeValue(ProcessReport report, String end_time){
        try{
            timeFormat.parse(end_time.toUpperCase());
        }catch(Exception e){
            return report.getEnd_time();
        }
        return end_time;
    }
    
    public Integer getQuantityValue(ProcessReport report, String quantity){
        try{
            return Integer.parseInt(quantity);
        }catch(Exception e){
            return report.getQuantity();
        }
    }
    
    public Double getVoltageValue(ProcessReport report, String voltage){
        try{
            return Double.parseDouble(voltage);
        }catch(Exception e){
            return report.getVoltage();
        }
    }
    
    public Double getAmperageValue(ProcessReport report, String amperage){
        try{
            return Double.parseDouble(amperage);
        }catch(Exception e){
            return report.getAmperage();
        }
    }
}
