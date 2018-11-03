/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.AnalysisReport;
import model.AnalysisType;
import model.Tank;


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
    
    private Stage addanalysistype_stage = new Stage();
    private Stage addanalysisreport_stage = new Stage();
    
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
        setAnalysisTypeItems();
        
        addanalysistype_button.setOnAction((ActionEvent) -> {
            addanalysistype_button.setDisable(true);
            showAddAnalysisTypeStage();
        });
        
        addanalysisreport_button.setOnAction((ActionEvent) -> {
            addanalysisreport_button.setDisable(true);
            showAddAnalysisReportStage();
        });
        
        setAnalysisReportTable();
        setAnalysisReportItems();
    }
    
    public void setAnalysisReportTable(){
        analysisreportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        analysisreportdate_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReport_date().toString()));
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
    
    public void setAnalysisTypeItems(){
        analysistype_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list()));
    }
    
    public void setAnalysisReportItems(){
        if(tank_combo.getSelectionModel().isEmpty()){
            return;
        }
        analysisreport_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisReportDAO().listTankDateRange(tank_combo.getSelectionModel().getSelectedItem(), Date.valueOf(startdate_picker.getValue()), Date.valueOf(enddate_picker.getValue()))));
    }
    
    public void showAddAnalysisTypeStage(){
        try {
            addanalysistype_stage = new Stage();
            addanalysistype_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddAnalysisTypeFX.fxml"));
            Scene scene = new Scene(root);
            
            addanalysistype_stage.setTitle("Nuevo Tipo de Análisis");
            addanalysistype_stage.setResizable(false);
            addanalysistype_stage.initStyle(StageStyle.UTILITY);
            addanalysistype_stage.setScene(scene);
            addanalysistype_stage.showAndWait();
            addanalysistype_button.setDisable(false);
            setAnalysisTypeItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddAnalysisReportStage(){
        try {
            addanalysisreport_stage = new Stage();
            addanalysisreport_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateAnalysisReportFX.fxml"));
            Scene scene = new Scene(root);
            
            addanalysisreport_stage.setTitle("Nuevo Reporte de Análisis");
            addanalysisreport_stage.setResizable(false);
            addanalysisreport_stage.initStyle(StageStyle.UTILITY);
            addanalysisreport_stage.setScene(scene);
            addanalysisreport_stage.showAndWait();
            addanalysisreport_button.setDisable(false);
            setAnalysisReportItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
