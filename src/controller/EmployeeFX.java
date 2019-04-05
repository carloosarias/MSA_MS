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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.ActivityReport;
import model.Employee;
import model.Equipment;
import model.Invoice;
import model.Module;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.timeFormat;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EmployeeFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<Employee> employee_tableview1;
    @FXML
    private TableColumn<Employee, String> firstname_column;
    @FXML
    private TableColumn<Employee, String> lastname_column;
    @FXML
    private TableColumn<Employee, String> birthdate_column;
    @FXML
    private TableColumn<Employee, String> curp_column;
    @FXML
    private TableColumn<Employee, String> phone_column;
    @FXML
    private TableColumn<Employee, String> email_column;
    @FXML
    private TableColumn<Employee, String> address_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private TableView<Employee> employee_tableview2;
    @FXML
    private TableColumn<Employee, String> employeename_column1;
    @FXML
    private TableColumn<Employee, String> hiredate_column;
    @FXML
    private TableColumn<Employee, String> entrytime_column;
    @FXML
    private TableColumn<Employee, String> endtime_column;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column1;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column2;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column3;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column4;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column5;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column6;
    @FXML
    private TableColumn<Employee, Boolean> schedule_column7;
    @FXML
    private Button password_button;
    @FXML
    private TableView<Employee> employee_tableview3;
    @FXML
    private TableColumn<Employee, String> employeename_column2;
    @FXML
    private TableColumn<Employee, String> user_column;
    @FXML
    private TableColumn<Employee, Boolean> admin_column;
    @FXML
    private TableView<Module> module_tableview;
    @FXML
    private TableColumn<Module, String> modulename_column;
    @FXML
    private TableColumn<Module, Boolean> access_column;
    
    private ObservableList<Module> employeemodule_list = FXCollections.<Module>emptyObservableList();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEmployeeTable();
        setModuleTable();
        updateModuleList();
        updateEmployeeTable();
        
        disable_button.disableProperty().bind(employee_tableview1.getSelectionModel().selectedItemProperty().isNull());
        password_button.disableProperty().bind(employee_tableview3.getSelectionModel().selectedItemProperty().isNull());
        module_tableview.disableProperty().bind(employee_tableview3.getSelectionModel().selectedItemProperty().isNull());
        
        employee_tableview1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            employee_tableview2.getSelectionModel().select(newValue);
            employee_tableview3.getSelectionModel().select(newValue);
            //updateEmployeeModuleList();
            module_tableview.refresh();
        });
        
        employee_tableview2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            employee_tableview1.getSelectionModel().select(newValue);
            employee_tableview3.getSelectionModel().select(newValue);
            //updateEmployeeModuleList();
            module_tableview.refresh();
        });
        
        employee_tableview3.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            employee_tableview1.getSelectionModel().select(newValue);
            employee_tableview2.getSelectionModel().select(newValue);
            updateEmployeeModuleList();
            module_tableview.refresh();
        });
    }
    
    public void updateEmployeeModuleList(){
        employeemodule_list = FXCollections.observableArrayList(msabase.getModuleDAO().list(employee_tableview3.getSelectionModel().getSelectedItem()));
    }
    
    public void updateModuleList(){
        try{
            module_tableview.getItems().setAll(msabase.getModuleDAO().list());
        }catch(Exception e){
            module_tableview.getItems().clear();
        }
    }
    
    public void setModuleTable(){
        modulename_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        access_column.setCellValueFactory(c -> new SimpleBooleanProperty(getAccessValue(c.getValue())));
        access_column.setCellFactory(tc -> new CheckBoxTableCell<>());
    }
    
    public boolean getAccessValue(Module module){
        try{
            return employeemodule_list.contains(module);
        }catch(Exception e){
            return false;
        }
    }
    
    public void setEmployeeTable(){
        firstname_column.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        firstname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        firstname_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirst_name(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        lastname_column.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        lastname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        lastname_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLast_name(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        birthdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getBirth_date()))));
        birthdate_column.setCellFactory(TextFieldTableCell.forTableColumn());
        birthdate_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setBirth_date(getBirth_dateValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        curp_column.setCellValueFactory(new PropertyValueFactory<>("curp"));
        curp_column.setCellFactory(TextFieldTableCell.forTableColumn());
        curp_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCurp(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        phone_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phone_column.setCellFactory(TextFieldTableCell.forTableColumn());
        phone_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        email_column.setCellFactory(TextFieldTableCell.forTableColumn());
        email_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        address_column.setCellFactory(TextFieldTableCell.forTableColumn());
        address_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        
        employeename_column1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()+" "+c.getValue().getLast_name()));
        hiredate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getHire_date()))));
        birthdate_column.setCellFactory(TextFieldTableCell.forTableColumn());
        birthdate_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setHire_date(getHire_dateValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        entrytime_column.setCellValueFactory(new PropertyValueFactory<>("entry_time"));
        entrytime_column.setCellFactory(TextFieldTableCell.forTableColumn());
        entrytime_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEntry_time(getEntry_timeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        endtime_column.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        endtime_column.setCellFactory(TextFieldTableCell.forTableColumn());
        endtime_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEnd_time(getEnd_timeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        schedule_column1.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getSchedule().get(2)));
        schedule_column1.setCellFactory(c -> new CheckBoxTableCell<>());
        
        schedule_column2.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getSchedule().get(1)));
        schedule_column2.setCellFactory(c -> new CheckBoxTableCell<>());
        /*
        schedule_column3.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getScheduleValue(2)));
        schedule_column3.setCellFactory(tc -> new CheckBoxTableCell<>());
        schedule_column4.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getScheduleValue(3)));
        schedule_column4.setCellFactory(tc -> new CheckBoxTableCell<>());
        schedule_column5.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getScheduleValue(4)));
        schedule_column5.setCellFactory(tc -> new CheckBoxTableCell<>());
        schedule_column6.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getScheduleValue(5)));
        schedule_column6.setCellFactory(tc -> new CheckBoxTableCell<>());
        schedule_column7.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getScheduleValue(6)));
        schedule_column7.setCellFactory(tc -> new CheckBoxTableCell<>());*/
        
        employeename_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()+" "+c.getValue().getLast_name()));
        user_column.setCellValueFactory(new PropertyValueFactory<>("user"));
        admin_column.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAdmin()));
        admin_column.setCellFactory(tc -> new CheckBoxTableCell<>());
    }
    
    public void updateEmployeeTable(){
        try{
            employee_tableview1.getItems().setAll(msabase.getEmployeeDAO().listActive(true));
            employee_tableview2.getItems().setAll(employee_tableview1.getItems());
            employee_tableview3.getItems().setAll(employee_tableview1.getItems());
        }catch(Exception e){
            employee_tableview1.getItems().clear();
            employee_tableview2.getItems().clear();
            employee_tableview3.getItems().clear();
        }
    }
    
    public Date getBirth_dateValue(Employee employee, String date){
        try{
            return DAOUtil.toUtilDate(LocalDate.parse(date, MainApp.dateFormat));
        }catch(Exception e){
            return employee.getBirth_date();
        }
    }
    
    public Date getHire_dateValue(Employee employee, String date){
        try{
            return DAOUtil.toUtilDate(LocalDate.parse(date, MainApp.dateFormat));
        }catch(Exception e){
            return employee.getHire_date();
        }
    }
    public String getEntry_timeValue(Employee employee, String entry_time){
        try{
            timeFormat.parse(entry_time.toUpperCase());
        }catch(Exception e){
            return employee.getEntry_time();
        }
        return entry_time;
    }
    public String getEnd_timeValue(Employee employee, String end_time){
        try{
            timeFormat.parse(end_time.toUpperCase());
        }catch(Exception e){
            return employee.getEnd_time();
        }
        return end_time;
    }
}
