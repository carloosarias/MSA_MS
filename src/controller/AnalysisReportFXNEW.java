/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import model.AnalysisReport;
import model.AnalysisReportVar;
import model.AnalysisType;
import model.AnalysisTypeVar;
import model.Tank;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AnalysisReportFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private CheckBox tank_filter;
    @FXML
    private ComboBox<AnalysisType> analysistype_combo;
    @FXML
    private CheckBox analysistype_filter;
    @FXML
    private DatePicker start_datepicker;
    @FXML
    private DatePicker end_datepicker;
    @FXML
    private CheckBox date_filter;
    @FXML
    private TableView<AnalysisReport> analysisreport_tableview;
    @FXML
    private TableColumn<AnalysisReport, Integer> id_column;
    @FXML
    private TableColumn<AnalysisReport, String> date_column;
    @FXML
    private TableColumn<AnalysisReport, String> time_column;
    @FXML
    private TableColumn<AnalysisReport, String> employee_column;
    @FXML
    private TableColumn<AnalysisReport, String> tank_column;
    @FXML
    private TableColumn<AnalysisReport, String> volume_column;
    @FXML
    private TableColumn<AnalysisReport, String> type_column;
    @FXML
    private TableColumn<AnalysisReport, String> optimal_column;
    @FXML
    private TableColumn<AnalysisReport, String> estimatedadjust_column;
    @FXML
    private TableColumn<AnalysisReport, String> appliedadjust_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
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
        tank_combo.getItems().setAll(msabase.getTankDAO().list(true));
        tank_combo.getSelectionModel().selectFirst();
        analysistype_combo.getItems().setAll(msabase.getAnalysisTypeDAO().list(true));
        analysistype_combo.getSelectionModel().selectFirst();
        
        setAnalysisReportTable();
        setAnalysisReportVarTable();
        
        updateAnalysisReportTable();
        analysisreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends AnalysisReport> observable, AnalysisReport oldValue, AnalysisReport newValue) -> {
            try{
                updateAnalysisReportVarTable();
                calculateResult();
                analysisreport_tableview.refresh();
            }catch(Exception e){
                analysisreportvar_tableview.getItems().clear();
            }
        });
        
    }
    
    public void updateAnalysisReportVarTable(){
        //
    }
    
    public void setAnalysisReportVarTable(){
        //
    }
    
    public void updateAnalysisReportTable(){
        try{
            msabase.getAnalysisReportDAO().list(tank_combo.getSelectionModel().getSelectedItem(), analysistype_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(start_datepicker.getValue().minusDays(1)), DAOUtil.toUtilDate(end_datepicker.getValue().minusDays(1)), tank_filter.isSelected(), analysistype_filter.isSelected(), date_filter.isSelected(), true);
        }catch(Exception e){
            analysisreport_tableview.getItems().clear();
        }
    }
    
    public void setAnalysisReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        time_column.setCellValueFactory(new PropertyValueFactory<>("report_time"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getTank_volume())+" L"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("analysistype_name"));
        optimal_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getAnalysistype_optimal())+" G/L"));
        estimatedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getEstimated_adjust())+ " KG"));
        appliedadjust_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getApplied_adjust())+" KG"));
        appliedadjust_column.setCellFactory(TextFieldTableCell.forTableColumn());
        appliedadjust_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setApplied_adjust(getApplied_adjustValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysisreport_tableview.refresh();
        });
    }
    
    public void calculateResult(){
        ExpressionBuilder builder = new ExpressionBuilder(analysisreport_tableview.getSelectionModel().getSelectedItem().getFormula_timestamp());
        for(AnalysisReportVar item : analysisreportvar_tableview.getItems()){
            builder.variable(item.getAnalysistypevar_name());
        }
        Expression e = builder.build();
        for(AnalysisReportVar item : analysisreportvar_tableview.getItems()){
            e.setVariable(item.getAnalysistypevar_name(), item.getValue());
        }
        
        if(e.evaluate() != analysisreport_tableview.getSelectionModel().getSelectedItem().getResult()){
            analysisreport_tableview.getSelectionModel().getSelectedItem().setEstimated_adjust(e.evaluate());
            msabase.getAnalysisReportDAO().update(analysisreport_tableview.getSelectionModel().getSelectedItem());
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
