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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Specification;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class SpecificationFX implements Initializable {

    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<Specification> specification_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextField specificationnumber_field;
    @FXML
    private TextArea details_field;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Especificaciones Activas",
        "Especificaciones Inactivas"
    );
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        updateList();
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        specification_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Specification> observable, Specification oldValue, Specification newValue) -> {
            setFieldValues(specification_listview.getSelectionModel().getSelectedItem());
        });
                
    }    
    
    public void updateList(){
        specification_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Especificaciones Activas":
                specification_listview.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list(true)));
                break;
            case "Especificaciones Inactivas":
                specification_listview.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list(false)));
                break;
        }        
    }
    
    public void setFieldValues(Specification specification){
        if(specification != null){
            id_field.setText(""+specification.getId());
            specificationnumber_field.setText(specification.getSpecification_number());
            details_field.setText(specification.getDetails());
            active_check.setSelected(!specification.isActive());
        } else{
            id_field.clear();
            specificationnumber_field.clear();
            details_field.clear();
            active_check.setSelected(false);
        }
        clearStyle();        
    }
    
    public void clearStyle(){
        specificationnumber_field.setStyle(null);
        details_field.setStyle(null);
    }
    
}
