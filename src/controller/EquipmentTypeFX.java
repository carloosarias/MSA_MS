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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EquipmentTypeFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> equipmenttype_tableview;
    @FXML
    private TableColumn<?, ?> id_column;
    @FXML
    private TableColumn<?, ?> equipmenttypename_column;
    @FXML
    private TableColumn<?, ?> equipmenttypedescription_column;
    @FXML
    private TableColumn<?, ?> frequency_column;
    @FXML
    private Button add_button;
    @FXML
    private TableView<?> equipmenttypecheck_tableview;
    @FXML
    private TableColumn<?, ?> equipmenttypecheckname_column;
    @FXML
    private TableColumn<?, ?> equipmenttypecheckdescription_column;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
