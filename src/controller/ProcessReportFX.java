/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProcessReportTable();
        processreport_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listEmployeeDateRange(msabase.getEmployeeDAO().find(MainApp.employee_id), new Date() , new Date())));
        
        processreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProcessReport> observable, ProcessReport oldValue, ProcessReport newValue) -> {
            setProcessReportDetails(newValue);
        });
        
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
