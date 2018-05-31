/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msa_ms;

import dao.DAOUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ConfigFX implements Initializable {

    @FXML
    private BorderPane root_pane;
    @FXML
    private VBox left_vbox;
    @FXML
    private Label driver_label;
    @FXML
    private TextField driver_field;
    @FXML
    private Label label_url;
    @FXML
    private TextField url_field;
    @FXML
    private Label user_label;
    @FXML
    private TextField user_field;
    @FXML
    private Label pass_label;
    @FXML
    private PasswordField pass_field;
    @FXML
    private Label config_label;
    @FXML
    private PasswordField config_field;
    @FXML
    private Label current_label;
    @FXML
    private PasswordField current_field;
    @FXML
    private Button save_button;
    @FXML
    private Button edit_button;

    private String db_driver;
    private String db_url;
    private String db_user;
    private String db_pass;
    private String config_pass;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        edit_button.setOnAction((ActionEvent) -> {
            driver_field.setEditable(true);
            url_field.setEditable(true);
            user_field.setEditable(true);
            pass_field.setEditable(true);
            config_field.setEditable(true);
            current_field.setEditable(true);
            save_button.setDisable(false);
            edit_button.setDisable(true);
        });
    }    
    
    public void loadValues(){
        db_driver = DAOUtil.getProperty("dao.properties", "msabase.jdbc.driver");
        db_url = DAOUtil.getProperty("dao.properties", "msabase.jdbc.url");
        db_user = DAOUtil.getProperty("dao.properties", "msabase.jdbc.username");
        db_pass = DAOUtil.getProperty("dao.properties", "msabase.jdbc.password");
        config_pass = DAOUtil.getProperty("dao.properties", "config.password");
        driver_field.setText(db_driver);
        url_field.setText(db_url);
        user_field.setText(db_user);
        pass_field.setText(db_pass);
    }
}
