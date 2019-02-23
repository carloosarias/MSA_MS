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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.AnalysisReport;
import model.AnalysisType;
import model.Tank;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.getFormattedDate;


/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AnalysisReportFX implements Initializable {
    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<AnalysisType> analysistype_tableview;
    @FXML
    private TableColumn<AnalysisType, Integer> analysistypeid_column;
    @FXML
    private TableColumn<AnalysisType, String> analysistypename_column;
    @FXML
    private TableColumn<AnalysisType, String> analysistypedescription_column;
    @FXML
    private TableColumn<AnalysisType, Double> analysistypefactor_column;
    @FXML
    private TableColumn<AnalysisType, Double> analysistypeoptimal_column;
    @FXML
    private Button addanalysistype_button;
    
    @FXML
    private TableView<AnalysisReport> analysisreport_tableview;
    @FXML
    private TableColumn<AnalysisReport, Integer> analysisreportid_column;
    @FXML
    private TableColumn<AnalysisReport, String> analysisreportdate_column;
    @FXML
    private TableColumn<AnalysisReport, String> analysisreportemployee_column;
    @FXML
    private TableColumn<AnalysisReport, String> analysisreporttank_column;
    @FXML
    private TableColumn<AnalysisReport, String> analysisreporttype_column;
    @FXML
    private TableColumn<AnalysisReport, Double> analysisreportquantityused_column;
    @FXML
    private TableColumn<AnalysisReport, Double> analysisreportresult_column;
    @FXML
    private TableColumn<AnalysisReport, Double> analysisreportestimatedadjust_column;
    @FXML
    private TableColumn<AnalysisReport, Double> analysisreportappliedadjust_column;
    @FXML
    private Button addanalysisreport_button;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    
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
        
        setAnalysisTypeTable();
        updateAnalysisTypeTable();
        
        addanalysistype_button.setOnAction((ActionEvent) -> {
            showAddAnalysisTypeStage();
            updateAnalysisTypeTable();
        });
        
        setAnalysisReportTable();
        updateAnalysisReportTable();
        
        addanalysisreport_button.setOnAction((ActionEvent) -> {
            showAddAnalysisReportStage();
            updateAnalysisReportTable();
        });
    }
    
    public void setAnalysisReportTable(){
        analysisreportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        analysisreportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        analysisreportemployee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getAnalysisReportDAO().findEmployee(c.getValue()).toString()));
        analysisreporttank_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getAnalysisReportDAO().findTank(c.getValue()).toString()));
        analysisreporttype_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getAnalysisReportDAO().findAnalysisType(c.getValue()).getName()));
        analysisreportquantityused_column.setCellValueFactory(new PropertyValueFactory<>("quantity_used"));
        analysisreportresult_column.setCellValueFactory(new PropertyValueFactory<>("result"));
        analysisreportestimatedadjust_column.setCellValueFactory(new PropertyValueFactory<>("estimated_adjust"));
        analysisreportappliedadjust_column.setCellValueFactory(new PropertyValueFactory<>("applied_adjust"));
    }
    
    public void setAnalysisTypeTable(){
        analysistypeid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        analysistypename_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        analysistypedescription_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        analysistypefactor_column.setCellValueFactory(new PropertyValueFactory<>("factor"));
        analysistypeoptimal_column.setCellValueFactory(new PropertyValueFactory<>("optimal"));
    }
    
    public void updateAnalysisTypeTable(){
        analysistype_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list()));
    }
    
    public void updateAnalysisReportTable(){
        if(tank_combo.getSelectionModel().isEmpty()){
            return;
        }
        analysisreport_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisReportDAO().listTankDateRange(tank_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(startdate_picker.getValue()), DAOUtil.toUtilDate(enddate_picker.getValue()))));
    }
    
    public void showAddAnalysisTypeStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddAnalysisTypeFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Tipo de Análisis");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddAnalysisReportStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
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
