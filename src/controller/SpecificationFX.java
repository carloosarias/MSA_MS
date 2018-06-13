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
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class SpecificationFX implements Initializable {

    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ComboBox<String> process_combo;
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
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        
        updateList();
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        
        specification_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Specification> observable, Specification oldValue, Specification newValue) -> {
            setFieldValues(specification_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            filter_combo.getOnAction();
            setFieldValues(specification_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            if(specification_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getSpecificationDAO().update(mapSpecification(specification_listview.getSelectionModel().getSelectedItem()));
            } else{
                msabase.getSpecificationDAO().create(mapSpecification(new Specification()));
            }
            setFieldValues(specification_listview.getSelectionModel().getSelectedItem());
            updateList();
            disableFields(true);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(specification_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
            }
        });        
    }
    
    public Specification mapSpecification(Specification specification){
        specification.setSpecification_number(specificationnumber_field.getText());
        specification.setProcess(process_combo.getSelectionModel().getSelectedItem());
        specification.setDetails(details_field.getText());
        specification.setActive(!active_check.isSelected());
        return specification;
    }
    
    public void disableFields(boolean value){
        specificationnumber_field.setDisable(value);
        details_field.setDisable(value);
        active_check.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        process_combo.setDisable(value);
        add_button.setDisable(!value);
        edit_button.setDisable(!value);
        specification_listview.setDisable(!value);
        filter_combo.setDisable(!value);
    }
    public boolean testFields(){
        boolean b = true;
        if(specificationnumber_field.getText().replace(" ", "").equals("")){
            specificationnumber_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            specificationnumber_field.setStyle(null);
        }
        if(details_field.getText().replace(" ", "").equals("")){
            details_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            details_field.setStyle(null);
        }     
        return b;
    }
    
    public void updateList(){
        specification_listview.getItems().clear();
        switch(filter_combo.getSelectionModel().getSelectedItem()){
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
            process_combo.getSelectionModel().select(specification.getProcess());
        } else{
            id_field.clear();
            specificationnumber_field.clear();
            details_field.clear();
            active_check.setSelected(false);
            specification_listview.getSelectionModel().clearSelection();
            process_combo.getSelectionModel().clearSelection();
        }
        clearStyle();
    }
    
    public void clearStyle(){
        specificationnumber_field.setStyle(null);
        details_field.setStyle(null);
    }
    
}
