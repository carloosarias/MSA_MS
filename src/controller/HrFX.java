/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Employee;
import model.Module;

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
    private TextField pass_field;
    @FXML
    private ListView<Module> module_list;
    @FXML
    private ListView<Module> invmodule_list;
    @FXML
    private Button move_button;
    
    ObservableList<String> filter_list = 
    FXCollections.observableArrayList(
        "Empleados Activos",
        "Empleados Inactivos"
    );
    DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter_combo.setItems(filter_list);
        emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(true)));
        filter_combo.getSelectionModel().selectFirst();
        
        entry_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,0));
        entry_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0));
        end_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,0));
        end_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0));
        
        filter_combo.setOnAction((ActionEvent) -> {
            switch (filter_combo.getSelectionModel().getSelectedItem()){
                case "Empleados Activos":
                    emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(true)));
                    break;
                case "Empleados Inactivos":
                    emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(false)));
                    break;
            }
        });
        
        emp_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) -> {
            Employee employee = emp_listview.getSelectionModel().getSelectedItem();
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
                clearFields();
            }
        });
    }
    
    public void setFields(Employee employee){
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
    }
    public void clearFields(){
        fname_field.clear();
        lname_field.clear();
        dob_picker.setValue(null);
        hire_picker.setValue(null);
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
}
