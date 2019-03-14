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
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.ProcessReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;

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
    private TableColumn<?, ?> quality_column;
    @FXML
    private TableView<ProcessReport> processreport_tableview2;
    @FXML
    private TableColumn<ProcessReport, Integer> id_column2;
    @FXML
    private TableColumn<ProcessReport, String> date_column2;
    @FXML
    private TableColumn<?, ?> tank_column;
    @FXML
    private TableColumn<?, ?> equipment_column;
    @FXML
    private TableColumn<?, ?> process_column;
    @FXML
    private TableColumn<ProcessReport, String> voltage_column;
    @FXML
    private TableColumn<ProcessReport, String> amperage_column;
    @FXML
    private TableColumn<ProcessReport, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startdate_picker.setValue(LocalDate.now());
        enddate_picker.setValue(LocalDate.now().plusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        setProcessReportTable();
        updateProcessReportTable();
        
    }
    
    public void setProcessReportTable(){
        id_column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        id_column2.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column1.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        start_column.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        end_column.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        equipment_column.setCellValueFactory(new PropertyValueFactory<>("equipment_name"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        voltage_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getVoltage()));
        amperage_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getAmperage()));
        comments_column.setCellValueFactory(c -> new SimpleStringProperty("comments"));
    }
    
    public void updateProcessReportTable(){
        //processreport_tableview1.getItems().setAll(msabase.getProcessReportDAO().listEmployeeDateRange(MainApp.current_employee, DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()), datefilter_check.isSelected()));
        processreport_tableview1.getItems().setAll(msabase.getProcessReportDAO().list());
        processreport_tableview2.getItems().setAll(processreport_tableview1.getItems());
    }
    
}
