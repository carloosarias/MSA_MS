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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ActivityReport;
import model.Employee;
import model.Equipment;
import model.Invoice;
import model.Module;
import model.ProductPart;
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
    
    public static Employee employee;
    
    private Stage stage = new Stage();
    
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
        password_button.disableProperty().bind(employee_tableview3.getSelectionModel().selectedItemProperty().isNull().or(Bindings.createBooleanBinding(() -> !MainApp.current_employee.isAdmin())));
        module_tableview.disableProperty().bind(employee_tableview3.getSelectionModel().selectedItemProperty().isNull());
        
        employee_tableview1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            updateSelection(newValue);
        });
        
        employee_tableview2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            updateSelection(newValue);
        });
        
        employee_tableview3.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            updateSelection(newValue);
            updateEmployeeModuleList();
            module_tableview.refresh();
        });
        
        password_button.setOnAction((ActionEvent) -> {
            employee = employee_tableview3.getSelectionModel().getSelectedItem();
            showStage();
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableEmployee();
            updateEmployeeTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = employee_tableview1.getItems().size();
            createEmployee();
            updateEmployeeTable();
            if(current_size < employee_tableview1.getItems().size()){
                employee_tableview1.scrollTo(employee);
                employee_tableview2.scrollTo(employee);
                employee_tableview3.scrollTo(employee);
                employee_tableview1.getSelectionModel().select(employee);
            }
        });
    }
    
    public void showStage(){
        try {
            stage = new Stage();
            stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/ChangePasswordFX.fxml"));
            Scene scene = new Scene(root);
            
            stage.setTitle("Cambiar Contraseña");
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disableEmployee(){
        employee_tableview1.getSelectionModel().getSelectedItem().setActive(false);
        employee_tableview1.getSelectionModel().getSelectedItem().setUser("");
        employee_tableview1.getSelectionModel().getSelectedItem().setPassword("");
        msabase.getEmployeeDAO().update(employee_tableview1.getSelectionModel().getSelectedItem());
    }
    
    public void createEmployee(){
        employee = new Employee();
        employee.setUser("");
        employee.setPassword("");
        employee.setFirst_name("N/A");
        employee.setLast_name("N/A");
        employee.setHire_date(DAOUtil.toUtilDate(LocalDate.now()));
        employee.setEntry_time("07:30 AM");
        employee.setEnd_time("03:30 PM");
        employee.setBirth_date(DAOUtil.toUtilDate(LocalDate.now()));
        employee.setCurp("N/A");
        employee.setAddress("N/A");
        employee.setEmail("N/A");
        employee.setPhone("N/A");
        employee.setScheduleFromString("0,0,0,0,0,0,0");
        employee.setActive(true);
        employee.setAdmin(false);
        
        msabase.getEmployeeDAO().create(employee);
    }
    
    public void updateSelection(Employee newValue){
        
        employee_tableview1.getSelectionModel().select(newValue);
        employee_tableview2.getSelectionModel().select(newValue);
        employee_tableview3.getSelectionModel().select(newValue);
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
        access_column.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Module, Boolean> tableCell = new TableCell<Module, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                if(checkBox.isSelected()){
                    msabase.getModuleEmployeeDAO().delete((Module) tableCell.getTableRow().getItem(), employee_tableview3.getSelectionModel().getSelectedItem());
                }else{
                    msabase.getModuleEmployeeDAO().create((Module) tableCell.getTableRow().getItem(), employee_tableview3.getSelectionModel().getSelectedItem());
                }
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
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
        //MONDAY
        schedule_column1.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isMon()));
        schedule_column1.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setMon(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        //TUESDAY
        schedule_column2.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isTue()));
        schedule_column2.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setTue(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        //WEDNESDAY
        schedule_column3.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isWed()));
        schedule_column3.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setWed(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        //THURSDAY
        schedule_column4.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isThu()));
        schedule_column4.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setThu(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        //FRIDAY
        schedule_column5.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isFri()));
        schedule_column5.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setFri(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        schedule_column6.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isSat()));
        schedule_column6.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setSat(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        schedule_column7.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isSun()));
        schedule_column7.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setSun(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        employeename_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()+" "+c.getValue().getLast_name()));
        user_column.setCellValueFactory(new PropertyValueFactory<>("user"));
        user_column.setCellFactory(TextFieldTableCell.forTableColumn());
        user_column.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            for(Employee item : employee_tableview3.getItems()){
                if(item.getUser().equals(t.getNewValue())){
                    employee_tableview1.refresh();
                    employee_tableview2.refresh();
                    employee_tableview3.refresh();
                    return;
                }
            }
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUser(t.getNewValue());
            msabase.getEmployeeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            employee_tableview1.refresh();
            employee_tableview2.refresh();
            employee_tableview3.refresh();
        });
        admin_column.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAdmin()));
        admin_column.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Employee, Boolean> tableCell = new TableCell<Employee, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                ((Employee) tableCell.getTableRow().getItem()).setAdmin(!checkBox.isSelected());
                msabase.getEmployeeDAO().update((Employee) tableCell.getTableRow().getItem());
                checkBox.setSelected(!checkBox.isSelected());
            });
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
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
    
    private void validate(CheckBox checkBox, Employee item, Event event){
        checkBox.setSelected(!checkBox.isSelected());
    }
    
}
