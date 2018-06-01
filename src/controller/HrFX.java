/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Employee;

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
    private ComboBox<?> entry_combo;
    @FXML
    private TextField curp_field;
    @FXML
    private TextField lname_field;
    @FXML
    private DatePicker hire_picker;
    @FXML
    private ComboBox<?> end_combo;
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
    private ListView<?> module_list;
    @FXML
    private ListView<?> invmodule_list;
    @FXML
    private Button move_button;
    
    ObservableList<String> filter_list = 
    FXCollections.observableArrayList(
        "Empleados Activos",
        "Empleados Inactivos"
    );
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        filter_combo.setItems(filter_list);
        emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(true)));
        filter_combo.getSelectionModel().selectFirst();
        
        filter_combo.setOnAction((ActionEvent) -> {
            switch (filter_combo.getSelectionModel().getSelectedItem()){
                case "Empleados Activos":
                    emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(true)));
                    break;
                case "Empleados Inactivos":
                    emp_listview.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().listActive(false)));
            }
        });
    }
}
