/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ActivityReport;
import model.Employee;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ActivityReportFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private Button filter_button;
    @FXML
    private CheckBox employeefilter_checkbox;
    @FXML
    private CheckBox datefilter_checkbox;
    @FXML
    private HBox filter_hbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<ActivityReport> activityreport_tableview1;
    @FXML
    private TableColumn<ActivityReport, Integer> reportid_column;
    @FXML
    private TableColumn<ActivityReport, String> employeeid_column;
    @FXML
    private TableColumn<ActivityReport, String> name_column;
    @FXML
    private TableColumn<ActivityReport, Date> reportdate_column;
    @FXML
    private TableColumn<ActivityReport, Time> starttime_column;
    @FXML
    private TableColumn<ActivityReport, Time> endtime_column;
    @FXML
    private TableView<ActivityReport> activityreport_tableview2;
    @FXML
    private TableColumn<ActivityReport, String> jobdescription_column;
    @FXML
    private TableColumn<ActivityReport, String> physicallocation_column;
    @FXML
    private TableColumn<ActivityReport, String> actiontaken_column;
    @FXML
    private TableColumn<ActivityReport, String> comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        add_button.setOnAction((ActionEvent) -> {
           showAdd_stage(); 
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateActivityReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Trabajo");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
