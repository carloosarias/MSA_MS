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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.AnalysisReport;
import model.Tank;
import static msa_ms.MainApp.df;
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
    private TableColumn<AnalysisReport, String> reporttime_column;
    @FXML
    private TableColumn<AnalysisReport, String> employee_column;
    @FXML
    private TableColumn<AnalysisReport, String> tank_column;
    @FXML
    private TableColumn<AnalysisReport, String> volume_column;
    @FXML
    private TableColumn<AnalysisReport, String> analysistype_column;
    @FXML
    private TableColumn<AnalysisReport, String> factor_column;
    @FXML
    private TableColumn<AnalysisReport, String> optimal_column;
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
        df.setMaximumFractionDigits(6);
        
        disable_button.disableProperty().bind(analysisreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        tank_combo.getItems().setAll(msabase.getTankDAO().list(true));
        tank_combo.getSelectionModel().selectFirst();
        
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        setAnalysisReportTable();
        updateAnalysisReportTable();
        
        tank_combo.setOnAction((ActionEvent) -> {
            updateAnalysisReportTable();
        });
        
        startdate_picker.setOnAction((ActionEvent) -> {
            updateAnalysisReportTable();
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            updateAnalysisReportTable();
        });
        
        tank_filter.setOnAction((ActionEvent) -> {
            updateAnalysisReportTable();
        });
        
        date_filter.setOnAction((ActionEvent) -> {
            updateAnalysisReportTable();
        });
        
        
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = analysisreport_tableview.getItems().size();
            showAdd_stage();
            updateAnalysisReportTable();
            if(current_size < analysisreport_tableview.getItems().size()){
                analysisreport_tableview.scrollTo(CreateAnalysisReportFX.analysis_report);
                analysisreport_tableview.getSelectionModel().select(CreateAnalysisReportFX.analysis_report);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            analysisreport_tableview.getSelectionModel().getSelectedItem().setActive(false);
            msabase.getAnalysisReportDAO().update(analysisreport_tableview.getSelectionModel().getSelectedItem());
            updateAnalysisReportTable();
        });
    }
    
    public void setAnalysisReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        reporttime_column.setCellValueFactory(new PropertyValueFactory<>("report_time"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank"));
        analysistype_column.setCellValueFactory(new PropertyValueFactory<>("analysis_type"));
        
        result_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getResult())+" ML"));
        estimatedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getResult())+" KG"));
        
        appliedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getApplied_adjust())+" KG"));
        appliedadjust_column.setCellFactory(TextFieldTableCell.forTableColumn());
        appliedadjust_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setApplied_adjust(getApplied_adjustValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysisreport_tableview.refresh();
        });
    }
    
    public void updateAnalysisReportTable(){
        try{
            //analysisreport_tableview.getItems().setAll(msabase.getAnalysisReportDAO().list(tank_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()), true, tank_filter.isSelected(), date_filter.isSelected()));
        } catch(Exception e){
            analysisreport_tableview.getItems().clear();
        }
    }
    
    public void showAdd_stage(){
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
    
    public Double getApplied_adjustValue(AnalysisReport analysis_report, String applied_adjust){
        try{
            return Double.parseDouble(applied_adjust);
        }catch(Exception e){
            return analysis_report.getApplied_adjust();
        }
    }
}
