/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Employee;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateActivityReportFX implements Initializable {

    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private Spinner<Integer> starthour_spinner;
    @FXML
    private Spinner<Integer> startminute_spinner;
    @FXML
    private Spinner<Integer> endhour_spinner;
    @FXML
    private Spinner<Integer> endminute_spinner;
    @FXML
    private TextField location_field;
    @FXML
    private TextArea description_area;
    @FXML
    private TextArea actiontaken_area;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button cancel_button;
    @FXML
    private Button save_button;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private SpinnerValueFactory starthour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
    private SpinnerValueFactory startminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
    private SpinnerValueFactory endhour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
    private SpinnerValueFactory endminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee_combo.getItems().setAll(msabase.getModuleEmployeeDAO().list(msabase.getModuleDAO().find("Mantenimiento")));
        employee_combo.getSelectionModel().selectFirst();
        reportdate_picker.setValue(LocalDate.now());
        starthour_spinner.setValueFactory(starthour_factory);
        startminute_spinner.setValueFactory(startminute_factory);
        endhour_spinner.setValueFactory(endhour_factory);
        endminute_spinner.setValueFactory(endminute_factory);
        
        starthour_spinner.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            endhour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 23, endhour_spinner.getValueFactory().getValue());
            endhour_spinner.setValueFactory(endhour_factory);
            if(endhour_spinner.getValueFactory().getValue() < newValue){
                endhour_spinner.getValueFactory().setValue(newValue);
            }
            setEndminute_spinner(startminute_spinner.getValueFactory().getValue());
        });
        
        startminute_spinner.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            setEndminute_spinner(newValue);
        });
        
        endhour_spinner.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            setEndminute_spinner(startminute_spinner.getValueFactory().getValue()); 
        });
        
    }
    
    public void setEndminute_spinner(Integer newValue){
        if(endhour_spinner.getValueFactory().getValue().equals(starthour_spinner.getValueFactory().getValue())){
            endminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(newValue, 59, endminute_spinner.getValueFactory().getValue());
            endminute_spinner.setValueFactory(endminute_factory);
            if(endminute_spinner.getValueFactory().getValue() < newValue){
                endminute_spinner.getValueFactory().setValue(newValue);
            }
        }
        else{
            endminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, endminute_spinner.getValueFactory().getValue());
            endminute_spinner.setValueFactory(endminute_factory);
        }
    }
    
}
