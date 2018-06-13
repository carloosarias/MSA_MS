/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PartRevision;
import model.ProductPart;
import model.Specification;
import msa_ms.MainApp;

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
    private ComboBox<String> process_combo;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private DatePicker revdate_picker;
    @FXML
    private TextField initialweight_field;
    @FXML
    private ComboBox<Specification> specification_combo;
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
    
    private Stage specificationStage;
    
    private ProductPart part;
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Revisiones Activas",
        "Revisiones Inactivas"
    );
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        part  = ProductPartFX.getPart();
        System.out.println(ProductPartFX.getPart());
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        specification_combo.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list(true)));
        
        updateList();
        
        specification_button.setOnAction((ActionEvent) -> {
           specification_button.setDisable(true);
           showSpecification(); 
        });
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });
        
        revision_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            setFieldValues(revision_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
            specification_combo.setDisable(false);
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            setFieldValues(revision_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            System.out.println(part.getPart_number());
            System.out.println(part.getId());
           if(!testFields()){
                return;
            }
            if(revision_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getPartRevisionDAO().update(mapPartRevision(revision_listview.getSelectionModel().getSelectedItem()));
            } else{
                msabase.getPartRevisionDAO().create(part, specification_combo.getSelectionModel().getSelectedItem(),mapPartRevision(new PartRevision()));
            }
                            System.out.println(part.getPart_number());
                System.out.println(part.getId());
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
    
    public void showSpecification(){
        try {
            specificationStage = new Stage();
            specificationStage.initOwner((Stage) root_hbox.getScene().getWindow());
            
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/SpecificationFX.fxml"));
            Scene scene = new Scene(root);
            
            specificationStage.setTitle("Detalles de Especificación");
            specificationStage.setResizable(false);
            specificationStage.initStyle(StageStyle.UTILITY);
            specificationStage.setScene(scene);
            specificationStage.showAndWait();
            specification_button.setDisable(!edit_button.isDisabled());        
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PartRevision mapPartRevision(PartRevision revision){
        revision.setRev(rev_field.getText());
        revision.setRev_date(java.sql.Date.valueOf(revdate_picker.getValue()));
        revision.setFinal_process(process_combo.getSelectionModel().getSelectedItem());
        revision.setBase_metal(basemetal_field.getText());
        revision.setArea(Double.parseDouble(area_field.getText()));
        revision.setBase_weight(Double.parseDouble(initialweight_field.getText()));
        revision.setFinal_weight(Double.parseDouble(finalweight_field.getText()));
        revision.setActive(!active_check.isSelected());
        return revision;
    }
    public void disableFields (boolean value){
        specification_combo.setDisable(true);
        rev_field.setDisable(value);
        revdate_picker.setDisable(value);
        process_combo.setDisable(value);
        basemetal_field.setDisable(value);
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
            area_field.setText(""+revision.getArea());
            initialweight_field.setText(""+revision.getBase_weight());
            finalweight_field.setText(""+revision.getFinal_weight());
            active_check.setSelected(!revision.isActive());
            specification_combo.getSelectionModel().select(msabase.getPartRevisionDAO().findSpecification(revision));
            process_combo.getSelectionModel().select(revision.getFinal_process());
        } else{
            id_field.clear();
            rev_field.clear();
            revdate_picker.setValue(null);
            basemetal_field.clear();
            area_field.clear();
            initialweight_field.clear();
            finalweight_field.clear();
            active_check.setSelected(false);
            specification_combo.getSelectionModel().clearSelection();
            process_combo.getSelectionModel().clearSelection();
        }
        clearStyle();
    }
    
    public void clearStyle(){
        rev_field.setStyle(null);
        revdate_picker.setStyle(null);
        basemetal_field.setStyle(null);
        area_field.setStyle(null);
        initialweight_field.setStyle(null);
        finalweight_field.setStyle(null);
        specification_combo.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        if(rev_field.getText().replace(" ", "").equals("")){
            rev_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            rev_field.setStyle(null);
        }
        if(revdate_picker.getValue() == null){
            revdate_picker.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            revdate_picker.setStyle(null);
        }
        if(basemetal_field.getText().replace(" ", "").equals("")){
            basemetal_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            basemetal_field.setStyle(null);
        }
        try{
            Double.parseDouble(area_field.getText());
            area_field.setStyle(null);
        }catch(Exception e){
            area_field.setStyle("-fx-border-color: red ;");
            b = false;
        }
        try{
            Double.parseDouble(initialweight_field.getText());
            initialweight_field.setStyle(null);
        }catch(Exception e){
            initialweight_field.setStyle("-fx-border-color: red ;");
            b = false;
        }
        try{
            Double.parseDouble(finalweight_field.getText());
            finalweight_field.setStyle(null);
        }catch(Exception e){
            finalweight_field.setStyle("-fx-border-color: red ;");
            b = false;
        }
        if(specification_combo.getSelectionModel().getSelectedItem() == null){
            specification_combo.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            specification_combo.setStyle(null);
        }
        return b;
    }
    
    public void updateList(){
        specification_combo.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list(true)));
        revision_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Revisiones Activas":
                System.out.println(msabase.getPartRevisionDAO().list(part, true).isEmpty());
                System.out.println(part.getPart_number());
                System.out.println(part.getId());
                revision_listview.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(part, true)));
                break;
            case "Revisiones Inactivas":
                revision_listview.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(part, false)));
                break;
        }
       
    }
}
