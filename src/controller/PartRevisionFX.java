/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.PartRevision;
import model.Specification;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PartRevisionFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<PartRevision> revision_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private DatePicker revdate_picker;
    @FXML
    private TextField finalprocess_field;
    @FXML
    private TextField initialweight_field;
    @FXML
    private Button specification_button;
    @FXML
    private TextField rev_field;
    @FXML
    private TextField basemetal_field;
    @FXML
    private TextField area_field;
    @FXML
    private TextField finalweight_field;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Revisiones Activas",
        "Revisiones Inactivas"
    );
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    @FXML
    private ComboBox<Specification> specification_combo;

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
        
        revision_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            setFieldValues(revision_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            filter_combo.getOnAction();
            setFieldValues(revision_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
           /* if(!testFields()){
                return;
            }*/
            if(revision_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getPartRevisionDAO().update(revision_listview.getSelectionModel().getSelectedItem());
            } else{
                msabase.getPartRevisionDAO().create(ProductPartFX.getPart(), specification_combo.getSelectionModel().getSelectedItem(), new PartRevision());
            }
            setFieldValues(revision_listview.getSelectionModel().getSelectedItem());
            updateList();
            disableFields(true);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(revision_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
            }
        });        
    }
    
    public void disableFields (boolean value){
        rev_field.setDisable(value);
        revdate_picker.setDisable(value);
        basemetal_field.setDisable(value);
        finalprocess_field.setDisable(value);
        area_field.setDisable(value);
        initialweight_field.setDisable(value);
        finalweight_field.setDisable(value);
        active_check.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        edit_button.setDisable(!value);
        add_button.setDisable(!value);
        filter_combo.setDisable(!value);
        revision_listview.setDisable(!value);
        
    }
    
    public void setFieldValues(PartRevision revision){
        if(revision != null){
            id_field.setText(""+revision.getId());
            rev_field.setText(revision.getRev());
            revdate_picker.setValue(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(revision.getRev_date())));
            basemetal_field.setText(revision.getBase_metal());
            finalprocess_field.setText(revision.getFinal_process());
            area_field.setText(""+revision.getArea());
            initialweight_field.setText(""+revision.getBase_weight());
            finalweight_field.setText(""+revision.getFinal_weight());
            active_check.setSelected(!revision.isActive());
        } else{
            id_field.clear();
            rev_field.clear();
            revdate_picker.setValue(null);
            basemetal_field.clear();
            finalprocess_field.clear();
            area_field.clear();
            initialweight_field.clear();
            finalweight_field.clear();
            active_check.setSelected(false);
        }
        clearStyle();
    }
    
    public void clearStyle(){
        rev_field.setStyle(null);
        revdate_picker.setStyle(null);
        basemetal_field.setStyle(null);
        finalprocess_field.setStyle(null);
        area_field.setStyle(null);
        initialweight_field.setStyle(null);
        finalweight_field.setStyle(null);
    }
    
    public void updateList(){
        revision_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Revisiones Activas":
                revision_listview.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(ProductPartFX.getPart(), true)));
                break;
            case "Revisiones Inactivas":
                revision_listview.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(ProductPartFX.getPart(), false)));
                break;
        }
       
    }
}
