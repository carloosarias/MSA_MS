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
import javafx.stage.Stage;
import model.Tank;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class TankFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<Tank> tank_tableview;
    @FXML
    private TableColumn<Tank, Integer> id_column;
    @FXML
    private TableColumn<Tank, String> tankname_column;
    @FXML
    private TableColumn<Tank, String> description_column;
    @FXML
    private TableColumn<Tank, String> volume_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private Tank tank;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTankTable();        
        updateTankTable();        
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = tank_tableview.getItems().size();
            createTank();
            updateTankTable();
            if(current_size < tank_tableview.getItems().size()){
                tank_tableview.scrollTo(tank);
                tank_tableview.getSelectionModel().select(tank);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableTank();
            updateTankTable();
        });
    }
    
    public void disableTank(){
        tank_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getTankDAO().update(tank_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createTank(){
        tank = new Tank();
        tank.setTank_name("N/A");
        tank.setDescription("N/A");
        tank.setVolume(0.0);
        tank.setActive(true);
        
        msabase.getTankDAO().create(tank);
    }
    
    public void updateTankTable(){
        tank_tableview.getItems().setAll(msabase.getTankDAO().list(true));
    }
        
    public void setTankTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        tankname_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        tankname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        tankname_column.setOnEditCommit((TableColumn.CellEditEvent<Tank, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setTank_name(t.getNewValue());
            msabase.getTankDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            tank_tableview.refresh();
        });
        
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<Tank, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getTankDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            tank_tableview.refresh();
        });
        
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getVolume())+" LT³"));
        volume_column.setCellFactory(TextFieldTableCell.forTableColumn());
        volume_column.setOnEditCommit((TableColumn.CellEditEvent<Tank, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setVolume(getVolumeValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getTankDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            tank_tableview.refresh();
        });
    }
    
    public Double getVolumeValue(Tank tank, String volume){
        try{
            return Double.parseDouble(volume);
        }catch(Exception e){
            return tank.getVolume();
        }
    }
}    
