/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        loadValues();
        driver_field.setText(db_driver);
        url_field.setText(db_url);
        user_field.setText(db_user);
        pass_field.setText(db_pass);
        
        edit_button.setOnAction((ActionEvent) -> {
            driver_field.setDisable(false);
            url_field.setDisable(false);
            user_field.setDisable(false);
            pass_field.setDisable(false);
            config_field.setDisable(false);
            current_field.setDisable(false);
            edit_button.setDisable(true);
            save_button.setDisable(false);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(current_field.getText().equals(config_pass)){
                saveValues(driver_field.getText(), url_field.getText(),
                user_field.getText(), pass_field.getText(), config_field.getText());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Los cambios fueron guardados con éxito.");
                alert.setHeaderText(null);
                alert.setTitle(null);
                alert.showAndWait();
                Stage stage = (Stage) root_pane.getScene().getWindow();
                stage.close();
            }else{
                current_field.setStyle("-fx-border-color: red ;");
            }
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
        config_field.setText(config_pass);
    }
    
    public void saveValues(String driver, String url, String user, String pass, String config){
        DAOUtil.setProperty("dao.properties", "msabase.jdbc.driver", driver);
        DAOUtil.setProperty("dao.properties", "msabase.jdbc.url", url);
        DAOUtil.setProperty("dao.properties", "msabase.jdbc.username", user);
        DAOUtil.setProperty("dao.properties", "msabase.jdbc.password", pass);
        DAOUtil.setProperty("dao.properties", "config.password", config);
    }
}
