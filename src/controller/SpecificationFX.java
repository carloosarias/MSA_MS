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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Specification;
import model.SpecificationItem;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.process_list;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class SpecificationFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<Specification> specification_tableview;
    @FXML
    private TableColumn<Specification, String> specificationnumber_column;
    @FXML
    private TableColumn<Specification, String> specificationname_column;
    @FXML
    private TableColumn<Specification, String> process_column;
    @FXML
    private TableView<SpecificationItem> specificationitem_tableview;
    @FXML
    private TableColumn<SpecificationItem, String> metal_column;
    @FXML
    private TableColumn<SpecificationItem, String> minimumthickness_column;
    @FXML
    private TableColumn<SpecificationItem, String> maximumthickness_column;
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
        setSpecificationTable();
        setSpecificationItemTable();
        updateSpecificationTable();
        
        details_tab.disableProperty().bind(specification_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        specification_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Specification> observable, Specification oldValue, Specification newValue) -> {
            try{
                updateSpecificationItemTable();
            }catch(Exception e){
                specificationitem_tableview.getItems().clear();
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = specification_tableview.getItems().size();
            showAdd_stage();
            updateSpecificationItemTable();
            if(current_size < specification_tableview.getItems().size()){
                specification_tableview.requestFocus();
                //specification_tableview.getSelectionModel().select(AddSpecificationFX.specification);
            }
        });
        
    }
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateSpecificationFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Especificación");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateSpecificationItemTable(){
        specificationitem_tableview.getItems().setAll(msabase.getSpecificationItemDAO().list(specification_tableview.getSelectionModel().getSelectedItem()));
    }
    
    public void updateSpecificationTable(){
        specification_tableview.getItems().setAll(msabase.getSpecificationDAO().list());
    }
    
    public void setSpecificationTable(){
        specificationnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_number"));
        specificationnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        specificationnumber_column.setOnEditCommit((TableColumn.CellEditEvent<Specification, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setSpecification_number(t.getNewValue());
            msabase.getSpecificationDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            specification_tableview.refresh();
        });
        
        specificationname_column.setCellValueFactory(new PropertyValueFactory<>("specification_name"));
        specificationname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        specificationname_column.setOnEditCommit((TableColumn.CellEditEvent<Specification, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setSpecification_name(t.getNewValue());
            msabase.getSpecificationDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            specification_tableview.refresh();
        });
        
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        process_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(process_list)));
        process_column.setOnEditCommit((TableColumn.CellEditEvent<Specification, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setProcess(t.getNewValue());
            msabase.getSpecificationDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            specification_tableview.refresh();
        });
    }
    
    public void setSpecificationItemTable(){
        metal_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMetal_name()));
        minimumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMinimum_thickness())+" IN"));
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMaximum_thickness())+" IN"));
    }
    
}
