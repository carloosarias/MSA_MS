/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Container;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateContainerFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<String> type_combo;
    @FXML
    private TextField containername_field;
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
        type_combo.setItems(FXCollections.observableArrayList(MainApp.container_type_list));
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            else{
                saveContainer();
                Stage stage = (Stage) root_hbox.getScene().getWindow();
                stage.close();
            }
        });
        
    }    
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(containername_field.getText().replace(" ", "").equals("")){
            containername_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(type_combo.getSelectionModel().isEmpty()){
            type_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(description_area.getText().replace(" ", "").equals("")){
            description_area.setText("n/a");
        }
        return b;
    }
    
    public void clearStyle(){
        containername_field.setStyle(null);
        type_combo.setStyle(null);
        description_area.setStyle(null);
    }
    
    public void saveContainer(){
        Container container = new Container();
        container.setType(type_combo.getSelectionModel().getSelectedItem());
        container.setContainer_name(containername_field.getText());
        container.setDescription(description_area.getText());
        msabase.getContainerDAO().create(container);
    }
}
