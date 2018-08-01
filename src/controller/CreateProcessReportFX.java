/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
import model.Container;
import model.Employee;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
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
    private ComboBox<Container> tank_combo;
    @FXML
    private ComboBox<String> containertype_combo;
    @FXML
    private ComboBox<Container> container_combo;
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
    private Spinner<Integer> startminute_spinner =  new Spinner<Integer>(0, 59, 1);
    @FXML
    private Spinner<Integer> timerhour_spinner =  new Spinner<Integer>(0, 23, 1);
    @FXML
    private Spinner<Integer> timerminute_spinner =  new Spinner<Integer>(0, 59, 1);
    @FXML
    private TextArea comments_area;
    @FXML
    private Button save_button;
    
    private ProductPart partnumbercombo_selection;
    private String partnumbercombo_text;
    
    private PartRevision revisioncombo_selection;
    private String revisioncombo_text;
    
    private SpinnerValueFactory starthour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
    private SpinnerValueFactory startminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
    private SpinnerValueFactory timerhour_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
    private SpinnerValueFactory timerminute_factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSpinnerValues();
        
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getContainerDAO().listType("Tanque")));
        containertype_combo.setItems(FXCollections.observableArrayList("Barril", "Rack"));
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        employee_combo.getSelectionModel().selectFirst();
        reportdate_picker.setValue(LocalDate.now());
        
        containertype_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            container_combo.setItems(FXCollections.observableArrayList(msabase.getContainerDAO().listType(newValue)));
            container_combo.setDisable(container_combo.getItems().isEmpty());
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
                process_combo.getSelectionModel().select(revisioncombo_selection.getFinal_process());
                lotnumber_field.requestFocus();
                ActionEvent.consume();
            }            
        });
        
    }
    
    public void setSpinnerValues(){
        starthour_spinner.setValueFactory(starthour_factory);
        startminute_spinner.setValueFactory(startminute_factory);
        timerhour_spinner.setValueFactory(timerhour_factory);
        timerminute_spinner.setValueFactory(timerminute_factory);
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
