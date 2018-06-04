/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private MenuItem logout;
    
    
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
                    try {
                        hr_tab.setContent((BorderPane) FXMLLoader.load(getClass().getResource("/fxml/HrFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        }
        
        logout.setOnAction((ActionEvent) ->{
            MainApp.employee = null;
            showLogin();
        });
    }
    
    public void showLogin(){
        try {
            Stage stage = (Stage) root_pane.getScene().getWindow();
            stage.close();
            stage = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("MSA Manager");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
