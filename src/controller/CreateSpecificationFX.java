/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Metal;
import model.Specification;
import model.SpecificationItem;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateSpecificationFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField specificationnumber_field;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private TextArea specificationname_area;
    @FXML
    private TableView<SpecificationItem> specificationitem_tableview;
    @FXML
    private TableColumn<SpecificationItem, String> listnumber_column;
    @FXML
    private TableColumn<SpecificationItem, Metal> metal_column;
    @FXML
    private TableColumn<SpecificationItem, Double> minimumthickness_column;
    @FXML
    private TableColumn<SpecificationItem, Double> maximumthickness_column;
    @FXML
    private Button addspecificationitem_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button save_button;
    
    private Stage add_stage = new Stage();
    
    private static ArrayList<SpecificationItem> item_list;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSpecificationItemTable();
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        setSpecificationItemItems();
        
        specificationitem_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends SpecificationItem> observable, SpecificationItem oldValue, SpecificationItem newValue) -> {
            delete_button.setDisable(specificationitem_tableview.getSelectionModel().isEmpty());
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            deleteFromQueue(specificationitem_tableview.getSelectionModel().getSelectedItem());
            setSpecificationItemItems();
        });
        
        addspecificationitem_button.setOnAction((ActionEvent) -> {
            addspecificationitem_button.setDisable(true);
            showAddStage();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveSpecification();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void saveSpecification(){
        Specification specification = new Specification();
        specification.setProcess(process_combo.getSelectionModel().getSelectedItem());
        specification.setSpecification_number(specificationnumber_field.getText());
        specification.setSpecification_name(specificationname_area.getText());
        msabase.getSpecificationDAO().create(specification);
        saveSpecificationItems(specification);
    }
    
    public void saveSpecificationItems(Specification specification){
        for(SpecificationItem item: item_list){
            msabase.getSpecificationItemDAO().create(specification, item.getTemp_metal(), item);
        }
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(specificationnumber_field.getText().replace(" ", "").equals("")){
            specificationnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(specificationname_area.getText().replace(" ", "").equals("")){
            specificationname_area.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(process_combo.getSelectionModel().isEmpty()){
            process_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        specificationnumber_field.setStyle(null);
        specificationname_area.setStyle(null);
        process_combo.setStyle(null);
    }
    
    public void showAddStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddSpecificationItemFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar detalles de especificación");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            addspecificationitem_button.setDisable(false);
            setSpecificationItemItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setSpecificationItemTable(){
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+item_list.indexOf(c.getValue())));
        metal_column.setCellValueFactory(new PropertyValueFactory("temp_metal"));
        minimumthickness_column.setCellValueFactory(new PropertyValueFactory<>("minimum_thickness"));
        maximumthickness_column.setCellValueFactory(new PropertyValueFactory<>("maximum_thickness"));
    }
    
    public void setSpecificationItemItems(){
        specificationitem_tableview.setItems(FXCollections.observableArrayList(item_list));
    }
    
    public static void addToQueue(SpecificationItem item){
        item_list.add(item);
    }
    
    public static void deleteFromQueue(SpecificationItem item){
        item_list.remove(item);
    }
}
