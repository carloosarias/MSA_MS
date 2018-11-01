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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    
    private Stage addanalysistype_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
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
    }
    
    public void setAnalysisTypeTable(){
        analysistypeid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        analysistypename_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        analysistypedescription_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        analysistypefactor_column.setCellValueFactory(new PropertyValueFactory<>("factor"));
        analysistypeoptimal_column.setCellValueFactory(new PropertyValueFactory<>("optimal"));
    }
    
    public void setAnalysisTypeItems(){
        analysistype_tableview.setItems(FXCollections.observableArrayList(msabase.getAnalysisTypeDAO().list()));
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
}
