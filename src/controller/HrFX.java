/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Employee;
import model.Module;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class HrFX implements Initializable {

    @FXML
    private BorderPane root_pane;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<Employee> emp_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField fname_field;
    @FXML
    private DatePicker dob_picker;
    @FXML
    private TextField curp_field;
    @FXML
    private TextField lname_field;
    @FXML
    private DatePicker hire_picker;
    @FXML
    private Spinner<Integer> entry_hour;
    @FXML
    private Spinner<Integer> entry_min;
    @FXML
    private Spinner<Integer> end_hour;
    @FXML
    private Spinner<Integer> end_min;
    @FXML
    private TextArea address_area;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextField user_field;
    @FXML
    private PasswordField pass_field;
    @FXML
    private ListView<Module> module_list;
    @FXML
    private ListView<Module> invmodule_list;
    @FXML
    private Button move_button;
    
    ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Activos",
        "Inactivos"
    );
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        updateList();

        entry_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,0));
        entry_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0));
        end_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,0));
        end_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0));
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        emp_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            setFieldValues(emp_listview.getSelectionModel().getSelectedItem());
        });
        
        module_list.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Module> observable, Module oldValue, Module newValue) -> {
            switchModuleList();
        });
        
        invmodule_list.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Module> observable, Module oldValue, Module newValue) -> {
            switchModuleList();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(emp_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
                move_button.setDisable(false);
                module_list.setDisable(false);
                invmodule_list.setDisable(false);
            }
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            filter_combo.getOnAction();
            setFieldValues(emp_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            if(emp_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getEmployeeDAO().update(mapEmployee(emp_listview.getSelectionModel().getSelectedItem()));
                if(!pass_field.getText().equals("")){
                    msabase.getEmployeeDAO().changePassword(mapEmployee(emp_listview.getSelectionModel().getSelectedItem()));
                }
            } else{
                    msabase.getEmployeeDAO().create(mapEmployee(new Employee()));
            }
            setFieldValues(emp_listview.getSelectionModel().getSelectedItem());
            updateList();
            disableFields(true);
        });
        
        move_button.setOnAction((ActionEvent) -> {
            if(module_list.getSelectionModel().getSelectedItem() != null){
                if(msabase.getEmployeeDAO().find(MainApp.employee_id).equals(emp_listview.getSelectionModel().getSelectedItem()) && module_list.getSelectionModel().getSelectedItem().getName().equals("Recursos Humanos")){
                    return;
                }
                msabase.getModuleEmployeeDAO().delete(module_list.getSelectionModel().getSelectedItem(), emp_listview.getSelectionModel().getSelectedItem());
            }
            if(invmodule_list.getSelectionModel().getSelectedItem() != null){
                msabase.getModuleEmployeeDAO().create(invmodule_list.getSelectionModel().getSelectedItem(), emp_listview.getSelectionModel().getSelectedItem());
            }
            module_list.setItems(FXCollections.observableArrayList(msabase.getModuleEmployeeDAO().list(emp_listview.getSelectionModel().getSelectedItem())));
            invmodule_list.setItems(FXCollections.observableArrayList(msabase.getModuleEmployeeDAO().listInverse(emp_listview.getSelectionModel().getSelectedItem())));
        });
    }
    
    public Employee mapEmployee(Employee employee){
        employee.setFirst_name(fname_field.getText());
        employee.setLast_name(lname_field.getText());
        employee.setBirth_date(Date.valueOf(dob_picker.getValue()));
        employee.setEnd_time(LocalTime.of(end_hour.getValueFactory().getValue(), end_min.getValueFactory().getValue()));
        employee.setEntry_time(LocalTime.of(entry_hour.getValueFactory().getValue(),entry_min.getValueFactory().getValue()));
        employee.setHire_date(Date.valueOf(hire_picker.getValue()));
        employee.setUser(user_field.getText());
        employee.setPassword(pass_field.getText());
        employee.setCurp(curp_field.getText());
        employee.setAddress(address_area.getText());
        employee.setActive(!active_check.isSelected());
        
        return employee;
    }
    
    public void updateList(){
        boolean active = true;
        emp_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Activos":
                active = true;
                break;
            case "Inactivos":
                active = false;
                break;
        }
        emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(active)));
    }

    public void switchModuleList(){
        if(invmodule_list.getSelectionModel().getSelectedItem() != null){
            module_list.getSelectionModel().clearSelection();
        }
        if(invmodule_list.getSelectionModel().getSelectedItem() != null){
            module_list.getSelectionModel().clearSelection();
        }
    }
    
    public void disableFields(boolean value){
        fname_field.setDisable(value);
        lname_field.setDisable(value);
        dob_picker.setDisable(value);
        hire_picker.setDisable(value);
        entry_hour.setDisable(value);
        entry_min.setDisable(value);
        end_hour.setDisable(value);
        end_min.setDisable(value);
        user_field.setDisable(value);
        curp_field.setDisable(value);
        address_area.setDisable(value);
        active_check.setDisable(value);
        pass_field.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        emp_listview.setDisable(!value);
        filter_combo.setDisable(!value);
        edit_button.setDisable(!value);
        add_button.setDisable(!value);
        if(value){
            move_button.setDisable(value);
            module_list.setDisable(value);
            invmodule_list.setDisable(value);
        }

    }
    
    public void setFieldValues(Employee employee){
        if(employee != null){
            fname_field.setText(employee.getFirst_name());
            lname_field.setText(employee.getLast_name());
            dob_picker.setValue(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(employee.getBirth_date())));
            hire_picker.setValue(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(employee.getHire_date())));
            entry_hour.getValueFactory().setValue(employee.getEntry_time().getHour());
            entry_min.getValueFactory().setValue(employee.getEntry_time().getMinute());
            end_hour.getValueFactory().setValue(employee.getEnd_time().getHour());
            end_min.getValueFactory().setValue(employee.getEnd_time().getMinute());
            id_field.setText(""+employee.getId());
            user_field.setText(employee.getUser());
            curp_field.setText(employee.getCurp());
            address_area.setText(employee.getAddress());
            active_check.setSelected(!employee.isActive());
            module_list.setItems(FXCollections.observableArrayList(msabase.getModuleEmployeeDAO().list(employee)));
            invmodule_list.setItems(FXCollections.observableArrayList(msabase.getModuleEmployeeDAO().listInverse(employee)));
        } else{
            fname_field.clear();
            pass_field.clear();
            lname_field.clear();
            dob_picker.setValue(null);
            hire_picker.setValue(null);
            emp_listview.getSelectionModel().clearSelection();
            entry_hour.getValueFactory().setValue(0);
            entry_min.getValueFactory().setValue(0);
            end_hour.getValueFactory().setValue(0);
            end_min.getValueFactory().setValue(0);
            id_field.clear();
            user_field.clear();
            curp_field.clear();
            address_area.clear();
            active_check.setSelected(false);
            module_list.getItems().clear();
            invmodule_list.getItems().clear();     
        }
        clearStyle();
    }
    
    public void clearStyle(){
        fname_field.setStyle(null);
        lname_field.setStyle(null);
        dob_picker.setStyle(null);
        hire_picker.setStyle(null);
        user_field.setStyle(null);
        curp_field.setStyle(null);
        address_area.setStyle(null);
        pass_field.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        if(fname_field.getText().replace(" ", "").equals("")){
            fname_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            fname_field.setStyle(null);
        }
        if(lname_field.getText().replace(" ", "").equals("")){
            lname_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            lname_field.setStyle(null);
        }
        if(dob_picker.getValue() == null){
            dob_picker.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            dob_picker.setStyle(null);
        }
        if(hire_picker.getValue() == null){
            hire_picker.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            hire_picker.setStyle(null);
        }
        if(user_field.getText().replace(" ", "").equals("")){
            user_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            user_field.setStyle(null);
        }
        if(curp_field.getText().replace(" ", "").equals("")){
            curp_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            curp_field.setStyle(null);
        }
        if(address_area.getText().replace(" ", "").equals("")){
            address_area.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            address_area.setStyle(null);
        }
        if(id_field.getText().replace(" ", "").equals("") && pass_field.getText().replace(" ", "").equals("")){
            pass_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            pass_field.setStyle(null);
        }
        return b;
    }
}
