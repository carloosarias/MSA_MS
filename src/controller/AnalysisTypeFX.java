/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.AnalysisType;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class AnalysisTypeFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<AnalysisType> analysistype_tableview;
    @FXML
    private TableColumn<AnalysisType, String> name_column;
    @FXML
    private TableColumn<AnalysisType, String> description_column;
    @FXML
    private TableColumn<AnalysisType, String> factor_column;
    @FXML
    private TableColumn<AnalysisType, String> optimal_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private AnalysisType analysis_type;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setAnalysisTypeTable();
        updateAnalysisTypeTable();
        
        disable_button.disableProperty().bind(analysistype_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = analysistype_tableview.getItems().size();
            createAnalysisType();
            updateAnalysisTypeTable();
            if(current_size < analysistype_tableview.getItems().size()){
                analysistype_tableview.scrollTo(analysis_type);
                analysistype_tableview.getSelectionModel().select(analysis_type);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableAnalysisType();
            updateAnalysisTypeTable();
        });
    }
    
    public void createAnalysisType(){
        analysis_type = new AnalysisType();
        analysis_type.setName("N/A");
        analysis_type.setDescription("N/A");
        analysis_type.setFactor(0.0);
        analysis_type.setOptimal(0.0);
        analysis_type.setActive(true);
        
        msabase.getAnalysisTypeDAO().create(analysis_type);
    }
    
    public void disableAnalysisType(){
        analysistype_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getAnalysisTypeDAO().update(analysistype_tableview.getSelectionModel().getSelectedItem());
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
        
        factor_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getFactor())));
        factor_column.setCellFactory(TextFieldTableCell.forTableColumn());
        factor_column.setOnEditCommit((TableColumn.CellEditEvent<AnalysisType, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFactor(getFactorValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
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
    
    public Double getFactorValue(AnalysisType analysis_type, String factor){
        try{
            return Double.parseDouble(factor);
        }catch(Exception e){
            return analysis_type.getFactor();
        }
    }
}
