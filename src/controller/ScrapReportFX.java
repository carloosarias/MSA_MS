/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.POQuery;
import model.PartRevision;
import model.ScrapReport;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ScrapReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField ponumber_field1;
    @FXML
    private Button reset_button;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private ComboBox<POQuery> po_combo;
    @FXML
    private TextField quantity_field2;
    @FXML
    private Button save_button;
    @FXML
    private TableView<ScrapReport> scrapreport_tableview;
    @FXML
    private TableColumn<ScrapReport, Integer> id_column;
    @FXML
    private TableColumn<ScrapReport, String> company_column;
    @FXML
    private TableColumn<ScrapReport, String> employee_column;
    @FXML
    private TableColumn<ScrapReport, String> reportdate_column;
    @FXML
    private TableColumn<ScrapReport, String> partnumber_column;
    @FXML
    private TableColumn<ScrapReport, String> revision_column;
    @FXML
    private TableColumn<ScrapReport, String> ponumber_column;
    @FXML
    private TableColumn<ScrapReport, String> quantity_column;
    @FXML
    private TableColumn<ScrapReport, String> comments_column;
    @FXML
    private Button delete_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.getItems().setAll(company_combo1.getItems());
        setScrapReportTable();
        updateScrapReportTable();
        updatePOQueryCombo();
        
        po_combo.disableProperty().bind(Bindings.isEmpty(po_combo.getItems()));
        delete_button.disableProperty().bind(scrapreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        save_button.setOnAction((ActionEvent) -> {
            createScrapReport();
            updateScrapReportTable();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            deleteScrapReport();
            updateScrapReportTable();
        });
        
        company_combo1.setOnAction((ActionEvent) -> {updateScrapReportTable();});
        partnumber_field1.setOnAction(company_combo1.getOnAction());
        ponumber_field1.setOnAction(company_combo1.getOnAction());
        
        company_combo2.setOnAction((ActionEvent) -> {updatePOQueryCombo();});
        partnumber_field2.setOnAction(company_combo2.getOnAction());
        rev_field2.setOnAction(company_combo2.getOnAction());
    }    
    
    public void updatePOQueryCombo(){
        po_combo.getItems().setAll(msabase.getPOQueryDAO().listAvailable(company_combo2.getValue(), partnumber_field2.getText(), rev_field2.getText()));
    }
    
    public void deleteScrapReport(){
        msabase.getScrapReportDAO().delete(scrapreport_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void updateScrapReportTable(){
        scrapreport_tableview.getItems().setAll(msabase.getScrapReportDAO().list(company_combo1.getValue(), partnumber_field1.getText(), ponumber_field1.getText(), null, null));
    }
    
    public void createScrapReport(){
        
    }
    
    public void setScrapReportTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column.setOnEditCommit((TableColumn.CellEditEvent<ScrapReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getScrapReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            scrapreport_tableview.refresh();
        });
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<ScrapReport, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getScrapReportDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            scrapreport_tableview.refresh();
        });
    }
    
    public Integer getQuantityValue(ScrapReport scrap_report, String quantity){
        try{
            return Integer.parseInt(quantity);
        }catch(Exception e){
            return scrap_report.getQuantity();
        }
    }
}
