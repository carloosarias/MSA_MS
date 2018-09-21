/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Container;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ContainerFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Container> container_tableview;
    @FXML
    private TableColumn<Container, Integer> id_column;
    @FXML
    private TableColumn<Container, String> type_column;
    @FXML
    private TableColumn<Container, String> process_column;
    @FXML
    private TextArea details_area;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setContainerTable();        
        container_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Container> observable, Container oldValue, Container newValue) -> {
            setContainerDetails(newValue);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
        });
    }
   
    public void setContainerDetails(Container container){
        if(container == null){
            details_area.setText(null);
        }
        else{
            details_area.setText(container.getDetails());
        }
    }
    
    public void updateContainerTable(){
        container_tableview.setItems(FXCollections.observableArrayList(msabase.getContainerDAO().list()));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateContainerFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Contenedor");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateContainerTable();
        } catch (IOException ex) {
            Logger.getLogger(ContainerFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void setContainerTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("type"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
}
