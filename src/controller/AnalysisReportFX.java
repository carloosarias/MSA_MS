/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
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
    
    private Stage addanalysistype_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    @FXML
    private CheckBox tankfilter_checkbox;
    @FXML
    private ComboBox<?> tank_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnalysisTypeTable();
        setAnalysisTypeItems();
        
        addanalysistype_button.setOnAction((ActionEvent) -> {
            addanalysistype_button.setDisable(true);
            showAddAnalysisTypeStage();
            setAnalysisTypeItems();
        });
        
        
        setAnalysisReportTable();
        //setAnalysisReportItems();
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
    /*
    public setAnalysisReportItems(){
        analysisreport_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisReportDAO().listTankDateRange(tank, start, end)));
    }*/
    
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
}
