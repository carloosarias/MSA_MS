/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DepartLot;
import model.DepartReport;
import model.PartRevision;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartReport> depart_report_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> report_id_column;
    @FXML
    private TableColumn<DepartReport, String> report_employee_column;
    @FXML
    private TableColumn<DepartReport, Date> report_date_column;
    @FXML
    private TableColumn<DepartReport, String> report_client_column;
    @FXML
    private TableColumn<DepartReport, String> address_column;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> part_column;
    @FXML
    private TableColumn<PartRevision, String> revision_column;
    @FXML
    private TableColumn<PartRevision, String> item_qty_column;
    @FXML
    private TableColumn<PartRevision, String> item_boxqty_column;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> lot_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<DepartLot, String> lot_process_column;
    @FXML
    private TableColumn<DepartLot, String> lot_comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setReportTable();
        setItemTable();
        setLotTable();
        depart_report_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartReportDAO().list()));
        
        depart_report_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            partrevision_tableview.getItems().clear();
            departlot_tableview.getItems().clear();
            if(newValue != null){
                departlot_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().list(newValue)));
                partrevision_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().listPartRevision(newValue)));
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
            depart_report_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartReportDAO().list()));
        });        
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateDepartReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Remisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setReportTable(){
        report_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        report_employee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findEmployee(c.getValue()).toString()));
        report_date_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        report_client_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findCompany(c.getValue()).toString()));
        address_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findCompanyAddress(c.getValue()).toString()));
    }
    
    public void setItemTable(){
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
            msabase.getPartRevisionDAO().findProductPart(c.getValue()).toString())
        );
        revision_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        item_qty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().getPartRevisionQuantity(depart_report_tableview.getSelectionModel().getSelectedItem(), c.getValue())));
        item_boxqty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().getPartRevisionBoxQuantity(depart_report_tableview.getSelectionModel().getSelectedItem(), c.getValue())));
    }
    
    public void setLotTable(){
        lot_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        lot_process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        lot_comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
}
