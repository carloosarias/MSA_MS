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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Metal;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MetalFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<Metal> metal_tableview;
    @FXML
    private TableColumn<Metal, String> metalname_column;
    @FXML
    private TableColumn<Metal, String> density_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private Metal metal;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setMetalTable();
        updateMetalTable();
        
        disable_button.disableProperty().bind(metal_tableview.getSelectionModel().selectedItemProperty().isNull());
                
        add_button.setOnAction((ActionEvent) -> {
            createMetal();
            updateMetalTable();
            metal_tableview.scrollTo(metal);
            metal_tableview.getSelectionModel().select(metal);
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableMetal();
            updateMetalTable();
        });
    }
    
    public void disableMetal(){
        metal_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getMetalDAO().update(metal_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createMetal(){
        metal = new Metal();
        metal.setMetal_name("N/A");
        metal.setDensity(0.0);
        metal.setActive(true);
        msabase.getMetalDAO().create(metal);
    }
    
    public void updateMetalTable(){
        metal_tableview.getItems().setAll(msabase.getMetalDAO().list(true));
    }
    
    public void setMetalTable(){
        metalname_column.setCellValueFactory(new PropertyValueFactory<>("metal_name"));
        metalname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        metalname_column.setOnEditCommit((TableColumn.CellEditEvent<Metal, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setMetal_name(t.getNewValue());
            msabase.getMetalDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            metal_tableview.refresh();
        });
        
        density_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getDensity())+" G/CM³"));
        density_column.setCellFactory(TextFieldTableCell.forTableColumn());
        density_column.setOnEditCommit((TableColumn.CellEditEvent<Metal, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDensity(getDensityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getMetalDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            metal_tableview.refresh();
        });
    }
    
    public Double getDensityValue(Metal metal, String final_weight){
        try{
            return Double.parseDouble(final_weight);
        }catch(Exception e){
            return metal.getDensity();
        }
    }
}
