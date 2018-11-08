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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Tank;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class TankFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Tank> tank_tableview;
    @FXML
    private TableColumn<Tank, Integer> id_column;
    @FXML
    private TableColumn<Tank, String> tankname_column;
    @FXML
    private TableColumn<Tank, String> description_column;
    @FXML
    private TableColumn<Tank, Double> volume_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTankTable();        
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
        });
    }
    
    public void updateContainerTable(){
        tank_tableview.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list()));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateTankFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Tanque");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateContainerTable();
        } catch (IOException ex) {
            Logger.getLogger(TankFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void setTankTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        tankname_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        volume_column.setCellValueFactory(new PropertyValueFactory<>("volume"));
    }
}    
