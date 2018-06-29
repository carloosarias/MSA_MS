/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;
import msa_ms.MainApp;


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
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        enter_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                login();
            }
        });
        
        options_button.setOnAction((ActionEvent e) -> {
            options_button.setDisable(true);
            showConfig();
        });
        
        pass_field.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                login();
            }
        });
        enter_button.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                login();
            }
        });
        
    }
    
    public void login(){
        Employee employee = msabase.getEmployeeDAO().find(user_field.getText(), pass_field.getText());
        if(employee != null && employee.isActive()){
            MainApp.employee_id = employee.getId();
            showMain();
        }else{
            user_field.setStyle("-fx-background-color: lightpink;");
            pass_field.setStyle("-fx-background-color: lightpink;");
        }        
    }
    
    public void showConfig(){
        try {
            
            Stage configStage = new Stage();
            configStage.initOwner((Stage) root_pane.getScene().getWindow());
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/ConfigFX.fxml"));
            Scene scene = new Scene(root);

            configStage.setTitle("Opciones");
            configStage.setResizable(false);
            configStage.initStyle(StageStyle.UTILITY);
            configStage.setScene(scene);
            configStage.setX(root_pane.getScene().getX() + root_pane.getScene().getWidth());
            configStage.setY(root_pane.getScene().getY());
            configStage.showAndWait();
            options_button.setDisable(false);
        } catch (IOException ex) {
            Logger.getLogger(LoginFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showMain(){
        try {
            Stage stage = (Stage) root_pane.getScene().getWindow();
            stage.close();
            stage = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/MainFX.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("MSA Manager");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
