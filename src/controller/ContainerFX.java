/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
        container_tableview.setItems(FXCollections.observableArrayList(msabase.getContainerDAO().list()));
    }
    
    public void setContainerTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
    }
    
}
