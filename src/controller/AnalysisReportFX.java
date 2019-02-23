/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import model.AnalysisReport;
import model.Tank;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;


/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AnalysisReportFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private CheckBox tank_filter;
    @FXML
    private CheckBox date_filter;
    @FXML
    private TableView<AnalysisReport> analysisreport_tableview;
    @FXML
    private TableColumn<AnalysisReport, Integer> id_column;
    @FXML
    private TableColumn<AnalysisReport, String> reportdate_column;
    @FXML
    private TableColumn<AnalysisReport, String> employee_column;
    @FXML
    private TableColumn<AnalysisReport, String> tank_column;
    @FXML
    private TableColumn<AnalysisReport, String> type_column;
    @FXML
    private TableColumn<AnalysisReport, String> quantityused_column;
    @FXML
    private TableColumn<AnalysisReport, String> result_column;
    @FXML
    private TableColumn<AnalysisReport, String> estimatedadjust_column;
    @FXML
    private TableColumn<AnalysisReport, String> appliedadjust_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list()));
        tank_combo.getSelectionModel().selectFirst();
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        setAnalysisReportTable();
        updateAnalysisReportTable();
        
        add_button.setOnAction((ActionEvent) -> {
            showAddAnalysisReportStage();
            updateAnalysisReportTable();
        });
    }
    
    public void setAnalysisReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("analysis_type"));
        quantityused_column.setCellValueFactory(new PropertyValueFactory<>("quantity_used"));
        result_column.setCellValueFactory(new PropertyValueFactory<>("result"));
        estimatedadjust_column.setCellValueFactory(new PropertyValueFactory<>("estimated_adjust"));
        appliedadjust_column.setCellValueFactory(new PropertyValueFactory<>("applied_adjust"));
    }
    
    public void updateAnalysisReportTable(){
        try{
            analysisreport_tableview.getItems().setAll(msabase.getAnalysisReportDAO().list(tank_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()), true, tank_filter.isSelected(), date_filter.isSelected()));
        } catch(Exception e){
            analysisreport_tableview.getItems().clear();
        }
    }
    
    public void showAddAnalysisReportStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateAnalysisReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Análisis");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
