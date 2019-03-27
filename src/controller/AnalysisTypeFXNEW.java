/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.AnalysisType;
import model.AnalysisTypeVar;
import static msa_ms.MainApp.df;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AnalysisTypeFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<AnalysisType> analysistype_tableview;
    @FXML
    private TableColumn<AnalysisType, String> name_column;
    @FXML
    private TableColumn<AnalysisType, String> description_column;
    @FXML
    private TableColumn<AnalysisType, String> minrange_column;
    @FXML
    private TableColumn<AnalysisType, String> optimal_column;
    @FXML
    private TableColumn<AnalysisType, String> maxrange_column;
    @FXML
    private Button addanalysistype_button;
    @FXML
    private Button disableanalysistype_button;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<AnalysisTypeVar> analysistypevar_tableview;
    @FXML
    private TableColumn<AnalysisTypeVar, String> varname_column;
    @FXML
    private TableColumn<AnalysisTypeVar, String> vardescription_column;
    @FXML
    private TableColumn<AnalysisTypeVar, String> default_column;
    @FXML
    private Button addvar_button;
    @FXML
    private Button disablevar_button;
    @FXML
    private TextField formula_textfield;
    
    private AnalysisType analysis_type;
    private AnalysisTypeVar analysistype_var;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setAnalysisTypeTable();
        setAnalysisTypeVarTable();
        updateAnalysisTypeTable();
        
        details_tab.disableProperty().bind(analysistype_tableview.getSelectionModel().selectedItemProperty().isNull());
        disableanalysistype_button.disableProperty().bind(analysistype_tableview.getSelectionModel().selectedItemProperty().isNull());
        disablevar_button.disableProperty().bind(analysistypevar_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        analysistype_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends AnalysisType> observable, AnalysisType oldValue, AnalysisType newValue) -> {
            try{
                updateAnalysisTypeVarTable();
            }catch(Exception e){
                analysistypevar_tableview.getItems().clear();
                formula_textfield.clear();
            }
        });
        
        addanalysistype_button.setOnAction((ActionEvent) -> {
            int current_size = analysistype_tableview.getItems().size();
            createAnalysisType();
            updateAnalysisTypeTable();
            if(current_size < analysistype_tableview.getItems().size()){
                analysistype_tableview.scrollTo(analysis_type);
                analysistype_tableview.getSelectionModel().select(analysis_type);
            }
        });
        
        disableanalysistype_button.setOnAction((ActionEvent) -> {
            disableAnalysisType();
            updateAnalysisTypeTable();
        });
        
        addvar_button.setOnAction((ActionEvent) -> {
            int current_size = analysistypevar_tableview.getItems().size();
            createAnalysisTypeVar();
            updateAnalysisTypeVarTable();
            if(current_size < analysistypevar_tableview.getItems().size()){
                analysistypevar_tableview.scrollTo(analysistype_var);
                analysistypevar_tableview.getSelectionModel().select(analysistype_var);
            }
        });
        
        disablevar_button.setOnAction((ActionEvent) -> {
            AnalysisTypeVar temp = analysistypevar_tableview.getSelectionModel().getSelectedItem();
            int index = analysistypevar_tableview.getSelectionModel().getSelectedIndex();
            analysistypevar_tableview.getItems().remove(temp);
            
            if(evalExpression()){
                analysistypevar_tableview.getItems().add(index, temp);
                analysistypevar_tableview.getSelectionModel().select(temp);
                disableAnalysisTypeVar();
                updateAnalysisTypeVarTable();
            }else{
                analysistypevar_tableview.getItems().add(index, temp);
                analysistypevar_tableview.getSelectionModel().select(temp);
                formula_textfield.requestFocus();
            }
        });
        
        formula_textfield.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(evalExpression()){
                analysistype_tableview.getSelectionModel().getSelectedItem().setFormula(newValue);
                msabase.getAnalysisTypeDAO().update(analysistype_tableview.getSelectionModel().getSelectedItem());
            }
        });
    }
    
    public boolean evalExpression(){
        formula_textfield.setStyle(null);
        try{
            ExpressionBuilder builder = new ExpressionBuilder(formula_textfield.getText());
            for(AnalysisTypeVar item : analysistypevar_tableview.getItems()){
                builder.variable(item.getName());
            }
            Expression e = builder.build();
            for(AnalysisTypeVar item : analysistypevar_tableview.getItems()){
                e.setVariable(item.getName(), item.getDefault_value());
            }
            
            double result = e.evaluate();
            System.out.println(result);
            return true;
        }catch(Exception e){
            formula_textfield.setStyle("-fx-background-color: lightpink;");
            return false;
        }
    }
    
    public void createAnalysisTypeVar(){
        analysistype_var = new AnalysisTypeVar();
        int counter = 1;
        analysistype_var.setName("x"+1);
        while(analysistypevar_tableview.getItems().stream().filter(o -> o.getName().equals(analysistype_var.getName())).findFirst().isPresent()){
            analysistype_var.setName("x"+counter);
            counter++;
        }
        analysistype_var.setDescription("N/A");
        analysistype_var.setDefault_value(0.0);
        analysistype_var.setActive(true);
        msabase.getAnalysisTypeVarDAO().create(analysistype_tableview.getSelectionModel().getSelectedItem(), analysistype_var);
    }
    
    public void disableAnalysisTypeVar(){
        analysistypevar_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getAnalysisTypeVarDAO().update(analysistypevar_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void disableAnalysisType(){
        analysistype_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getAnalysisTypeDAO().update(analysistype_tableview.getSelectionModel().getSelectedItem());
        for(AnalysisTypeVar analysistype_var : msabase.getAnalysisTypeVarDAO().list(analysistype_tableview.getSelectionModel().getSelectedItem(), true)){
            analysistype_var.setActive(false);
            msabase.getAnalysisTypeVarDAO().update(analysistype_var);
        }
    }
    
    public void createAnalysisType(){
        analysis_type = new AnalysisType();
        analysis_type.setName("N/A");
        analysis_type.setDescription("N/A");
        analysis_type.setMin_range(0.0);
        analysis_type.setOptimal(0.0);
        analysis_type.setMax_range(0.0);
        analysis_type.setFormula("0.0");
        analysis_type.setActive(true);
        msabase.getAnalysisTypeDAO().create(analysis_type);
    }
    
    public void updateAnalysisTypeVarTable(){
        analysistypevar_tableview.getItems().setAll(msabase.getAnalysisTypeVarDAO().list(analysistype_tableview.getSelectionModel().getSelectedItem(), true));
        formula_textfield.setText(analysistype_tableview.getSelectionModel().getSelectedItem().getFormula());
    }
    
    public void setAnalysisTypeVarTable(){
        varname_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        varname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        varname_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisTypeVar, String> t) -> {
            for(AnalysisTypeVar analysistype_var : analysistypevar_tableview.getItems()){
                if(analysistype_var.getName().equals(t.getNewValue())){
                    analysistypevar_tableview.refresh();
                    return;
                }
            }
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getAnalysisTypeVarDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistypevar_tableview.refresh();
        });
        vardescription_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        vardescription_column.setCellFactory(TextFieldTableCell.forTableColumn());
        vardescription_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisTypeVar, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getAnalysisTypeVarDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistypevar_tableview.refresh();
        });
        default_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getDefault_value())));
        default_column.setCellFactory(TextFieldTableCell.forTableColumn());
        default_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisTypeVar, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDefault_value(getDefault_valueValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisTypeVarDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistypevar_tableview.refresh();
        });
    }
    
    public void setAnalysisTypeTable(){
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        name_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getAnalysisTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistype_tableview.refresh();
        });
        
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getAnalysisTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistype_tableview.refresh();
        });
        
        minrange_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMin_range())+" G/L"));
        minrange_column.setCellFactory(TextFieldTableCell.forTableColumn());
        minrange_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setMin_range(getMin_rangeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistype_tableview.refresh();
        });
        
        optimal_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getOptimal())+" G/L"));
        optimal_column.setCellFactory(TextFieldTableCell.forTableColumn());
        optimal_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setOptimal(getOptimalValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistype_tableview.refresh();
        });
        
        maxrange_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMax_range())+" G/L"));
        maxrange_column.setCellFactory(TextFieldTableCell.forTableColumn());
        maxrange_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setMax_range(getMax_rangeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getAnalysisTypeDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            analysistype_tableview.refresh();
        });
    }
    
    public void updateAnalysisTypeTable(){
        analysistype_tableview.getItems().setAll(msabase.getAnalysisTypeDAO().list(true));
    }
    
    public Double getOptimalValue(AnalysisType analysis_type, String optimal){
        try{
            return Double.parseDouble(optimal);
        }catch(Exception e){
            return analysis_type.getOptimal();
        }
    }
    
    public Double getMax_rangeValue(AnalysisType analysis_type, String max_range){
        try{
            return Double.parseDouble(max_range);
        }catch(Exception e){
            return analysis_type.getMax_range();
        }
    }
    
    public Double getMin_rangeValue(AnalysisType analysis_type, String min_range){
        try{
            return Double.parseDouble(min_range);
        }catch(Exception e){
            return analysis_type.getMin_range();
        }
    }
    
    public Double getDefault_valueValue(AnalysisTypeVar analysistype_var, String default_value){
        try{
            return Double.parseDouble(default_value);
        }catch(Exception e){
            return analysistype_var.getDefault_value();
        }
    }
}
