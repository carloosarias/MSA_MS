/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductPartFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> productpart_tableview;
    @FXML
    private TableColumn<?, ?> productpartid_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private TableColumn<?, ?> productpartstatus_column;
    @FXML
    private Tab partrevision_tableview;
    @FXML
    private TableColumn<?, ?> partrevisionid_column;
    @FXML
    private TableColumn<?, ?> rev_column;
    @FXML
    private TableColumn<?, ?> revdate_column;
    @FXML
    private TableColumn<?, ?> basemetal_column;
    @FXML
    private TableColumn<?, ?> finalprocess_column;
    @FXML
    private TableColumn<?, ?> specificationnumber_column;
    @FXML
    private TableColumn<?, ?> area_column;
    @FXML
    private TableColumn<?, ?> baseweight_column;
    @FXML
    private TableColumn<?, ?> finalweight_column;
    @FXML
    private TableColumn<?, ?> partrevisionstatus_column;
    @FXML
    private Button addproductpart_button;
    @FXML
    private Button addpartrevision_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
