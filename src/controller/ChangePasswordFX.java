/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ChangePasswordFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private PasswordField password_field;
    @FXML
    private PasswordField confirm_field;
    @FXML
    private Button save_button;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        password_field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue.equals(confirm_field.getText()) && !newValue.equals("")){
                confirm_field.setStyle(null);
                save_button.setDisable(false);
            }else{
                confirm_field.setStyle("-fx-background-color: lightpink;");
                save_button.setDisable(true);
            }
        });
        
        confirm_field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue.equals(password_field.getText()) && !newValue.equals("")){
                confirm_field.setStyle(null);
                save_button.setDisable(false);
            }else{
                confirm_field.setStyle("-fx-background-color: lightpink;");
                save_button.setDisable(true);
            }
        });
        
        save_button.setOnAction((ActionEvent) -> {
            EmployeeFX.employee.setPassword(confirm_field.getText());
            msabase.getEmployeeDAO().changePassword(EmployeeFX.employee);
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }
}
