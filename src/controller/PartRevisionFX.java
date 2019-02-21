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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PartRevision;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PartRevisionFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> partnumber_column;
    @FXML
    private TableColumn<PartRevision, String> rev_column;
    @FXML
    private TableColumn<PartRevision, String> revdate_column;
    @FXML
    private TableColumn<PartRevision, String> basemetal_column;
    @FXML
    private TableColumn<PartRevision, String> finalprocess_column;
    @FXML
    private TableColumn<PartRevision, String> specnumber_column;
    @FXML
    private TableColumn<PartRevision, String> area_column;
    @FXML
    private TableColumn<PartRevision, String> baseweight_column;
    @FXML
    private TableColumn<PartRevision, String> finalweight_column;
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
        setPartRevisionTable();
        updatePartRevisionTable();
        
        disable_button.disableProperty().bind(partrevision_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = partrevision_tableview.getItems().size();
            showAdd_stage();
            updatePartRevisionTable();
            if(current_size < partrevision_tableview.getItems().size()){
                partrevision_tableview.requestFocus();
                partrevision_tableview.getSelectionModel().select(AddPartRevisionFX.part_revision);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disablePartRevision();
            updatePartRevisionTable();
        });
    }    
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddPartRevisionFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Registrar Revisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disablePartRevision(){
        partrevision_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getPartRevisionDAO().update(partrevision_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void updatePartRevisionTable(){
        partrevision_tableview.getItems().setAll(msabase.getPartRevisionDAO().list(true));
    }
    
    public void setPartRevisionTable(){
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revdate_column.setCellValueFactory(c -> new SimpleStringProperty(dateFormat.format(c.getValue().getRev_date()).toUpperCase()));
        finalprocess_column.setCellValueFactory(new PropertyValueFactory<>("specification_process"));
        specnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_specificationnumber"));
        basemetal_column.setCellValueFactory(new PropertyValueFactory<>("metal_metalname"));
        
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        rev_column.setCellFactory(TextFieldTableCell.forTableColumn());
        rev_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRev(t.getNewValue());
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        
        area_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getArea())+" IN²"));
        area_column.setCellFactory(TextFieldTableCell.forTableColumn());
        area_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setArea(getAreaValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        
        baseweight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getBase_weight())+" KG"));
        baseweight_column.setCellFactory(TextFieldTableCell.forTableColumn());
        baseweight_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setBase_weight(getBase_weightValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        
        finalweight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getFinal_weight())+" KG"));
        finalweight_column.setCellFactory(TextFieldTableCell.forTableColumn());
        finalweight_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFinal_weight(getFinal_weightValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
    }
    
    public Double getAreaValue(PartRevision revision, String area){
        try{
            return Double.parseDouble(area);
        }catch(Exception e){
            return revision.getArea();
        }
    }
    
    public Double getBase_weightValue(PartRevision revision, String base_weight){
        try{
            return Double.parseDouble(base_weight);
        }catch(Exception e){
            return revision.getBase_weight();
        }
    }
    
    public Double getFinal_weightValue(PartRevision revision, String final_weight){
        try{
            return Double.parseDouble(final_weight);
        }catch(Exception e){
            return revision.getFinal_weight();
        }
    }
}
