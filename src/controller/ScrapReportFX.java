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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ProcessReport;
import model.ScrapReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ScrapReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<ScrapReport> scrapreport_tableview;
    @FXML
    private TableColumn<ScrapReport, Integer> id_column;
    @FXML
    private TableColumn<ScrapReport, String> employee_column;
    @FXML
    private TableColumn<ScrapReport, Date> reportdate_column;
    @FXML
    private TableColumn<ScrapReport, String> partnumber_column;
    @FXML
    private TableColumn<ScrapReport, String> revision_column;
    @FXML
    private TableColumn<ScrapReport, String> lotnumber_column;
    @FXML
    private TableColumn<ScrapReport, Integer> quantity_column;
    @FXML
    private TableColumn<ScrapReport, String> comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setScrapReportTable();
        updateScrapReportTable();
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
        });
        
    }    
    
    public void updateScrapReportTable(){
        scrapreport_tableview.setItems(FXCollections.observableArrayList(msabase.getScrapReportDAO().list()));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateScrapReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Scrap");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
            updateScrapReportTable();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setScrapReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getScrapReportDAO().findEmployee(c.getValue()).toString()));
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(msabase.getScrapReportDAO().findPartRevision(c.getValue())).getPart_number()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getScrapReportDAO().findPartRevision(c.getValue()).getRev()));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
}
