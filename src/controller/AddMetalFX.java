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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Metal;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddMetalFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField metalname_field;
    @FXML
    private TextField density_field;
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
            saveMetal();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }    
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(metalname_field.getText().replace(" ", "").equals("")){
            metalname_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(density_field.getText());
        } catch(Exception e){
            density_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        metalname_field.setStyle(null);
        density_field.setStyle(null);
    }
    
    public void saveMetal(){
        Metal item = new Metal();
        item.setMetal_name(metalname_field.getText());
        item.setDensity(Double.parseDouble(density_field.getText()));
        msabase.getMetalDAO().create(item);
    }
}
