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
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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
import model.AnalysisReportVar;
import model.AnalysisType;
import model.AnalysisTypeVar;
import model.Employee;
import model.Tank;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AnalysisReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<Tank> tank_combo1;
    @FXML
    private ComboBox<AnalysisType> analysistype_combo1;
    @FXML
    private DatePicker start_datepicker;
    @FXML
    private DatePicker end_datepicker;
    @FXML
    private ComboBox<Tank> tank_combo2;
    @FXML
    private ComboBox<AnalysisType> analysistype_combo2;
    @FXML
    private Button reset_button;
    @FXML
    private Button save_button;
    @FXML
    private TableView<AnalysisReport> analysisreport_tableview;
    @FXML
    private TableColumn<AnalysisReport, Integer> id_column;
    @FXML
    private TableColumn<AnalysisReport, String> date_column;
    @FXML
    private TableColumn<AnalysisReport, String> time_column;
    @FXML
    private TableColumn<AnalysisReport, Employee> employee_column;
    @FXML
    private TableColumn<AnalysisReport, Tank> tank_column;
    @FXML
    private TableColumn<AnalysisReport, String> volume_column;
    @FXML
    private TableColumn<AnalysisReport, AnalysisType> type_column;
    @FXML
    private TableColumn<AnalysisReport, String> optimal_column;
    @FXML
    private TableColumn<AnalysisReport, String> estimatedadjust_column;
    @FXML
    private TableColumn<AnalysisReport, String> ph_column;
    @FXML
    private TableColumn<AnalysisReport, String> appliedadjust_column;
    @FXML
    private Button delete_button;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<AnalysisReportVar> analysisreportvar_tableview;
    @FXML
    private TableColumn<AnalysisReportVar, String> var_column;
    @FXML
    private TableColumn<AnalysisReportVar, String> description_column;
    @FXML
    private TableColumn<AnalysisReportVar, String> value_column;
    @FXML
    private Label resultformula_label;
    @FXML
    private Label result_label;
    @FXML
    private Label estimatedadjustformula_label;
    @FXML
    private Label estimatedadjust_label;
    @FXML
    private Label tank_label;
    @FXML
    private Label volume_label;
    @FXML
    private Label analysistype_label;
    @FXML
    private Label range_label;
    @FXML
    private Label optimal_label;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        updateComboItems();
        
        details_tab.disableProperty().bind(analysisreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        delete_button.disableProperty().bind(analysisreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        setAnalysisReportTable();
        setAnalysisReportVarTable();
        
        updateAnalysisReportTable();
        analysisreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends AnalysisReport> observable, AnalysisReport oldValue, AnalysisReport newValue) -> {
                updateAnalysisReportVarTable();
                analysisreport_tableview.refresh();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            int current_size = analysisreport_tableview.getItems().size();
            showAdd_stage();
            updateAnalysisReportTable();
            if(current_size < analysisreport_tableview.getItems().size()){
                analysisreport_tableview.scrollTo(CreateAnalysisReportFX.analysis_report);
                analysisreport_tableview.getSelectionModel().select(CreateAnalysisReportFX.analysis_report);
            }
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            analysisreport_tableview.getSelectionModel().getSelectedItem().setActive(false);
            msabase.getAnalysisReportDAO().update(analysisreport_tableview.getSelectionModel().getSelectedItem());
            updateAnalysisReportTable();
        });

        tank_combo1.setOnAction((ActionEvent) -> {updateAnalysisReportTable();});
        analysistype_combo1.setOnAction(tank_combo1.getOnAction());
        start_datepicker.setOnAction(tank_combo1.getOnAction());
        end_datepicker.setOnAction(tank_combo1.getOnAction());
        
    }
    
    public void updateComboItems(){
        tank_combo1.getItems().setAll(msabase.getTankDAO().list(true));
        tank_combo2.itemsProperty().bind(tank_combo1.itemsProperty());
        analysistype_combo1.getItems().setAll(msabase.getAnalysisTypeDAO().list(true));
        analysistype_combo2.itemsProperty().bind(analysistype_combo1.itemsProperty());
        setDatePicker(start_datepicker);
        setDatePicker(end_datepicker);
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateAnalysisReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Generar Reporte de Análisis");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void evalResult(){
        try{
            ExpressionBuilder builder = new ExpressionBuilder(analysisreport_tableview.getSelectionModel().getSelectedItem().getFormula_timestamp());
            for(AnalysisReportVar item : analysisreportvar_tableview.getItems()){
                builder.variable(item.getAnalysistypevar_name());
            }
            Expression e = builder.build();
            for(AnalysisReportVar item : analysisreportvar_tableview.getItems()){
                e.setVariable(item.getAnalysistypevar_name(), item.getValue());
            }
            
            analysisreport_tableview.getSelectionModel().getSelectedItem().setResult(e.evaluate());
            msabase.getAnalysisReportDAO().update(analysisreport_tableview.getSelectionModel().getSelectedItem());
            analysisreport_tableview.refresh();
        }catch(Exception e){
            analysisreport_tableview.getSelectionModel().getSelectedItem().setResult(0.0);
            msabase.getAnalysisReportDAO().update(analysisreport_tableview.getSelectionModel().getSelectedItem());
            analysisreport_tableview.refresh();
        }
    }
    
    
    public void updateAnalysisReportVarTable(){
        try{
            analysisreportvar_tableview.getItems().setAll(msabase.getAnalysisReportVarDAO().list(analysisreport_tableview.getSelectionModel().getSelectedItem()));
            resultformula_label.setText(analysisreport_tableview.getSelectionModel().getSelectedItem().getFormula_timestamp());
            evalResult();
            result_label.setText(df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getResult()));
            estimatedadjustformula_label.setText("((Óptimo - Resultado) * Volumen) / 1000");
            estimatedadjust_label.setText(df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getEstimated_adjust()));
            tank_label.setText(analysisreport_tableview.getSelectionModel().getSelectedItem().getTank().getTank_name());
            volume_label.setText(df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getTank().getVolume())+" L");
            analysistype_label.setText(analysisreport_tableview.getSelectionModel().getSelectedItem().getAnalysis_type().getName());
            optimal_label.setText(df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getAnalysis_type().getOptimal())+" G/L");
            range_label.setText(df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getAnalysis_type().getMin_range())+" G/L - "+df.format(analysisreport_tableview.getSelectionModel().getSelectedItem().getAnalysis_type().getMax_range())+" G/L");
        }catch(Exception e){
            analysisreportvar_tableview.getItems().clear();
        }
    }
    
    public void setAnalysisReportVarTable(){
        var_column.setCellValueFactory(new PropertyValueFactory<>("analysistypevar_name"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("analysistypevar_description"));
        value_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getValue())));
        value_column.setCellFactory(TextFieldTableCell.forTableColumn());
        value_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisReportVar, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(getValueValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisReportVarDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateAnalysisReportVarTable();
        });
    }
    
    public void updateAnalysisReportTable(){
        try{
            analysisreport_tableview.getItems().setAll(msabase.getAnalysisReportDAO().list(tank_combo1.getSelectionModel().getSelectedItem(), analysistype_combo1.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(start_datepicker.getValue()), DAOUtil.toUtilDate(end_datepicker.getValue())));
        }catch(Exception e){
            analysisreport_tableview.getItems().clear();
        }
    }
    
    public void setAnalysisReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        time_column.setCellValueFactory(new PropertyValueFactory<>("report_time"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank"));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getTank().getVolume())+" L"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("analysis_type"));
        optimal_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getAnalysis_type().getOptimal())+" G/L"));
        estimatedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getEstimated_adjust())+ " KG"));
        appliedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getApplied_adjust())+" KG"));
        appliedadjust_column.setCellFactory(TextFieldTableCell.forTableColumn());
        appliedadjust_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setApplied_adjust(getApplied_adjustValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysisreport_tableview.refresh();
        });
        ph_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getPh())));
        ph_column.setCellFactory(TextFieldTableCell.forTableColumn());
        ph_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPh(getPhValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysisreport_tableview.refresh();
        });
    }
    
    public Double getApplied_adjustValue(AnalysisReport analysis_report, String applied_adjust){
        try{
            return Double.parseDouble(applied_adjust);
        }catch(Exception e){
            return analysis_report.getApplied_adjust();
        }
    }
    
    public Double getPhValue(AnalysisReport analysis_report, String ph){
        try{
            return Double.parseDouble(ph);
        }catch(Exception e){
            return analysis_report.getPh();
        }
    }
    
    public Double getValueValue(AnalysisReportVar analysisreport_var, String value){
        try{
            return Double.parseDouble(value);
        }catch(Exception e){
            return analysisreport_var.getValue();
        }
    }
}
