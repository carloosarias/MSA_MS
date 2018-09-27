/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Metal;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSpecificationItemTable();
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        setSpecificationItemItems();
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
