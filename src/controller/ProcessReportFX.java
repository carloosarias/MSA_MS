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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProcessReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> processreport_tableview;
    @FXML
    private TableColumn<?, ?> id_column;
    @FXML
    private TableColumn<?, ?> employee_column;
    @FXML
    private TableColumn<?, ?> reportdate_column;
    @FXML
    private TableColumn<?, ?> partnumber_column;
    @FXML
    private TableColumn<?, ?> revision_column;
    @FXML
    private TableColumn<?, ?> lotnumber_column;
    @FXML
    private TableColumn<?, ?> tankid_column;
    @FXML
    private TableColumn<?, ?> containerid_column;
    @FXML
    private TableColumn<?, ?> process_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TextField amperage_field;
    @FXML
    private TextField voltage_field;
    @FXML
    private TextField starttime_field;
    @FXML
    private TextField endtime_field;
    @FXML
    private TextArea comments_field;
    @FXML
    private Button add_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
