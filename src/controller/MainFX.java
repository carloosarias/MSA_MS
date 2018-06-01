/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Module;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MainFX implements Initializable {

    @FXML
    private BorderPane root_pane;
    @FXML
    private Tab hr_tab;
    @FXML
    private BorderPane hr_root_pane;
    @FXML
    private ComboBox<?> filter_combo;
    @FXML
    private ListView<?> emp_listview;
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
    @FXML
    private Tab other;

    private List<Module> modules;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        modules = msabase.getModuleEmployeeDAO().list(MainApp.employee);
        for(Module module : modules){
            switch(module.getName()){
                default:
                    hr_tab.setDisable(true);
                case "Recursos Humanos":
                    hr_tab.setDisable(false);
                
            }
        }
    }    
    
}
