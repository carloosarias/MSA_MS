/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Module;
import model.ProcessReport;
import msa_ms.MainApp;

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
    private ComboBox<?> employee_combo;
    @FXML
    private CheckBox datefilter_checkbox;
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
    private TableColumn<ProcessReport, Date> reportdate_column;
    @FXML
    private TableColumn<ProcessReport, String> partnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> revision_column;
    @FXML
    private TableColumn<ProcessReport, String> lotnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> tankid_column;
    @FXML
    private TableColumn<ProcessReport, String> containerid_column;
    @FXML
    private TableColumn<ProcessReport, String> process_column;
    @FXML
    private TableColumn<ProcessReport, Integer> quantity_column;
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
        setFilterHBox();
        setProcessReportTable();
        updateProcessReportTable();
        processreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProcessReport> observable, ProcessReport oldValue, ProcessReport newValue) -> {
            setProcessReportDetails(newValue);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
        });
        
    }
    public void setFilterHBox(){
        modules = msabase.getModuleEmployeeDAO().list(msabase.getEmployeeDAO().find(MainApp.employee_id));
        for(Module module : modules){
            if(module.getName().equals("Historial Producción")){
                filter_hbox.setDisable(false);
                return;
            }
        }
    }
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateProcessReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Proceso");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateProcessReportTable();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateProcessReportTable(){
        processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listEmployeeDateRange(
                msabase.getEmployeeDAO().find(MainApp.employee_id), 
                java.sql.Date.valueOf(LocalDate.now()) , 
                java.sql.Date.valueOf(LocalDate.now().plusDays(1))))
        );
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
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(msabase.getProcessReportDAO().findPartRevision(c.getValue())).getPart_number()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findPartRevision(c.getValue()).getRev()));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        tankid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findTank(c.getValue()).toString()));
        containerid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findContainer(c.getValue()).toString()));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }
    
    
    
}
