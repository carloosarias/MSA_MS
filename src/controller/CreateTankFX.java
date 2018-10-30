/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Tank;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateTankFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField tankname_field;
    @FXML
    private TextField volume_field;
    @FXML
    private TextArea description_area;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            else{
                saveTank();
                Stage stage = (Stage) root_hbox.getScene().getWindow();
                stage.close();
            }
        });
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(tankname_field.getText().replace(" ", "").equals("")){
            tankname_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(volume_field.getText());
        } catch(Exception e){
            volume_field.setText("0.0");
            volume_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(description_area.getText().replace(" ", "").equals("")){
            description_area.setText("n/a");
        }
        return b;
    }
    
    public void clearStyle(){
        tankname_field.setStyle(null);
        volume_field.setStyle(null);
        description_area.setStyle(null);
    }
    
    public void saveTank(){
        Tank tank = new Tank();
        tank.setTank_name(tankname_field.getText());
        tank.setVolume(Double.parseDouble(volume_field.getText()));
        tank.setDescription(description_area.getText());
        msabase.getTankDAO().create(tank);
    }
}    
