/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import model.ScrapReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ScrapReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<ScrapReport> processreport_tableview;
    @FXML
    private TableColumn<ScrapReport, Integer> id_column;
    @FXML
    private TableColumn<ScrapReport, String> employee_column;
    @FXML
    private TableColumn<ScrapReport, Date> reportdate_column;
    @FXML
    private TableColumn<ScrapReport, String> partnumber_column;
    @FXML
    private TableColumn<ScrapReport, String> revision_column;
    @FXML
    private TableColumn<ScrapReport, String> lotnumber_column;
    @FXML
    private TableColumn<ScrapReport, Integer> quantity_column;
    @FXML
    private TableColumn<ScrapReport, String> comments_column;
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
