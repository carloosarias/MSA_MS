/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Employee;
import model.Equipment;
import model.EquipmentType;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
import model.Tank;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateProcessReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private ComboBox<EquipmentType> equipmenttype_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private ComboBox<PartRevision> revision_combo;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private TextField amperage_field;
    @FXML
    private TextField voltage_field;
    @FXML
    private Spinner<Integer> starthour_spinner;
    @FXML
    private Spinner<Integer> startminute_spinner;
    @FXML
    private Spinner<Integer> endhour_spinner;
    @FXML
    private Spinner<Integer> endminute_spinner;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button save_button;
    
    private ProductPart partnumbercombo_selection;
    private String partnumbercombo_text;
    
    private PartRevision revisioncombo_selection;
    private String revisioncombo_text;
    
    private SpinnerValueFactory starthour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
    private SpinnerValueFactory startminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
    private SpinnerValueFactory endhour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalTime.now().getHour(), 23, LocalTime.now().getHour());
    private SpinnerValueFactory endminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(LocalTime.now().getMinute(), 59, LocalTime.now().getMinute());
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSpinnerValues();
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list()));
        equipmenttype_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeDAO().list()));
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        employee_combo.getSelectionModel().selectFirst();
        reportdate_picker.setValue(LocalDate.now());
        
        equipmenttype_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EquipmentType> observable, EquipmentType oldValue, EquipmentType newValue) -> {
            equipment_combo.getSelectionModel().clearSelection();
            equipment_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().list(newValue)));
            equipment_combo.setDisable(equipment_combo.getItems().isEmpty());
        });
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            partnumbercombo_text = partnumber_combo.getEditor().textProperty().getValue();
            partnumbercombo_selection = msabase.getProductPartDAO().find(partnumbercombo_text);
            if(partnumbercombo_selection == null){
                partnumber_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                revision_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        revision_combo.setOnAction((ActionEvent) -> {
            if(partnumbercombo_selection == null){
                ActionEvent.consume();
                return;
            }
            revisioncombo_text = revision_combo.getEditor().textProperty().getValue();
            revisioncombo_selection = msabase.getPartRevisionDAO().find(partnumbercombo_selection, revisioncombo_text);
            if(revisioncombo_selection == null){
                revision_combo.getEditor().selectAll();
            }
            else{
                lotnumber_field.requestFocus();
                ActionEvent.consume();
            }            
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveProcessReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
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
    
    public void saveProcessReport(){
        ProcessReport process_report = new ProcessReport();
        process_report.setProcess(process_combo.getSelectionModel().getSelectedItem());
        process_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        process_report.setLot_number(lotnumber_field.getText());
        process_report.setQuantity(Integer.parseInt(quantity_field.getText()));
        process_report.setAmperage(Double.parseDouble(amperage_field.getText()));
        process_report.setVoltage(Double.parseDouble(voltage_field.getText()));
        Time start_time = new Time(starthour_spinner.getValue(), startminute_spinner.getValue(), 0);
        Time end_time = new Time(endhour_spinner.getValue(), endminute_spinner.getValue(), 0);
        process_report.setStart_time(start_time);
        process_report.setEnd_time(end_time);
        process_report.setComments(comments_area.getText());
        process_report.setQuality_passed(true);
        
        msabase.getProcessReportDAO().create(
            employee_combo.getSelectionModel().getSelectedItem(),
            revisioncombo_selection,
            tank_combo.getSelectionModel().getSelectedItem(),
            equipment_combo.getSelectionModel().getSelectedItem(),
            process_report);
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        process_combo.setStyle(null);
        tank_combo.setStyle(null);   
        equipmenttype_combo.setStyle(null);
        equipment_combo.setStyle(null);
        lotnumber_field.setStyle(null);        
        quantity_field.setStyle(null);        
        partnumber_combo.setStyle(null);        
        revision_combo.setStyle(null);
        amperage_field.setStyle(null);        
        voltage_field.setStyle(null);
        comments_area.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(process_combo.getSelectionModel().isEmpty()){
            process_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(tank_combo.getSelectionModel().isEmpty()){
            tank_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(equipmenttype_combo.getSelectionModel().isEmpty()){
            equipmenttype_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(equipment_combo.getSelectionModel().isEmpty()){
            equipment_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            partnumbercombo_selection.getId();
        }
        catch(Exception e){
            partnumber_combo.setStyle("-fx-background-color: lightpink;");
            partnumber_combo.getSelectionModel().select(null);
            partnumber_combo.getEditor().setText(null);
            b = false;
        }
        
        try {
            revisioncombo_selection.getId();
        }
        catch(Exception e){
            revision_combo.setStyle("-fx-background-color: lightpink;");
            revision_combo.getSelectionModel().select(null);
            revision_combo.getEditor().setText(null);
            b = false;
        }
        
        try {
            Double.parseDouble(amperage_field.getText());
        }catch(Exception e){
            amperage_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try {
            Double.parseDouble(voltage_field.getText());
        }catch(Exception e){
            voltage_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setText("n/a");
        }
        
        return b;
    }
    
    public void setSpinnerValues(){
        starthour_spinner.setValueFactory(starthour_factory);
        startminute_spinner.setValueFactory(startminute_factory);
        endhour_spinner.setValueFactory(endhour_factory);
        endminute_spinner.setValueFactory(endminute_factory);
    }
    
    public void updatePartrev_combo(){
        try{
            revision_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partnumbercombo_selection)));
        } catch(Exception e) {
            revision_combo.getItems().clear();
        }
        revision_combo.setDisable(revision_combo.getItems().isEmpty());
    }
    
}
