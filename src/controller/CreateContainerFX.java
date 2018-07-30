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
    private ComboBox<String> process_combo;
    @FXML
    private TextArea details_area;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        type_combo.setItems(FXCollections.observableArrayList(MainApp.container_type_list));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        
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
        if(process_combo.getSelectionModel().isEmpty()){
            process_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(type_combo.getSelectionModel().isEmpty()){
            type_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(details_area.getText().replace(" ", "").equals("")){
            details_area.setText("n/a");
        }
        return b;
    }
    
    public void clearStyle(){
        process_combo.setStyle(null);
        type_combo.setStyle(null);
        details_area.setStyle(null);
    }
    
    public void saveContainer(){
        Container container = new Container();
        container.setType(type_combo.getSelectionModel().getSelectedItem());
        container.setProcess(process_combo.getSelectionModel().getSelectedItem());
        container.setDetails(details_area.getText());
        msabase.getContainerDAO().create(container);
    }
}
