/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.SpecificationFX.specification;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Metal;
import model.SpecificationItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddSpecificationItemFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Metal> metal_combo;
    @FXML
    private Button submit_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    public static SpecificationItem specification_item;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metal_combo.setItems(FXCollections.observableArrayList(msabase.getMetalDAO().list()));
        
        submit_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            createSpecificationItem();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public void createSpecificationItem(){
        specification_item = new SpecificationItem();
        specification_item.setMinimum_thickness(0.0);
        specification_item.setMaximum_thickness(0.0);
        specification_item.setActive(true);
        msabase.getSpecificationItemDAO().create(specification, metal_combo.getSelectionModel().getSelectedItem(), specification_item);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(metal_combo.getSelectionModel().isEmpty()){
            metal_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        metal_combo.setStyle(null);
    }
    
}
