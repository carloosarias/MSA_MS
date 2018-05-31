/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msa_ms;

import dao.DAOUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class LoginFX implements Initializable {

    @FXML
    private BorderPane root_pane;
    @FXML
    private ImageView logo_image;
    @FXML
    private VBox center_vbox;
    @FXML
    private Label user_label;
    @FXML
    private TextField user_field;
    @FXML
    private Label pass_label;
    @FXML
    private PasswordField pass_field;
    @FXML
    private ButtonBar bottom_bar;
    @FXML
    private Button options_button;
    @FXML
    private Button enter_button;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        options_button.setOnAction((ActionEvent e) -> {
            options_button.setDisable(true);
            showConfig();
        });
        
    }
    
    public void showConfig(){
        try {
            Stage configStage = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("ConfigFX.fxml"));
            Scene scene = new Scene(root);
            
            configStage.setTitle("Opciones");
            configStage.setResizable(false);
            configStage.initStyle(StageStyle.UTILITY);
            configStage.setScene(scene);
            configStage.showAndWait();
            options_button.setDisable(false);
        } catch (IOException ex) {
            Logger.getLogger(LoginFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
