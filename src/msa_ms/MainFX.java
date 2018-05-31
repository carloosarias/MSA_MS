/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msa_ms;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import model.Module;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MainFX implements Initializable {

    @FXML
    private Tab hr_tab;
    @FXML
    private Tab other;

    List<Module> modules;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        modules = msabase.getModuleEmployeeDAO().list(MainApp.employee);
        for(Module module : modules){
            switch(module.getName()){
                case "Recursos Humanos":
                    hr_tab.setDisable(false);
                    break;
            }
        }
    }    
    
}
