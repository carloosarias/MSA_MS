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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Container;
import model.Employee;
import model.PartRevision;
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
    private Spinner<Integer> starthour_spinner =  new Spinner<Integer>(0, 23, 1);
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
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getContainerDAO().listType("Tanque")));
        containertype_combo.setItems(FXCollections.observableArrayList("Barril", "Rack"));
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        
        employee_combo.getSelectionModel().selectFirst();
        reportdate_picker.setValue(LocalDate.now());
    }
    
}
