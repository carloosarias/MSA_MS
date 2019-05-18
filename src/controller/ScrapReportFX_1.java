/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Company;
import model.Employee;
import model.IncomingReport_1;
import model.ScrapReport;
import model.ScrapReport_1;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ScrapReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private Button delete_button;
    @FXML
    private TableView<ScrapReport_1> scrapreport_tableview;
    @FXML
    private TableColumn<ScrapReport_1, String> date_column;
    @FXML
    private TableColumn<ScrapReport_1, Employee> employee_column;
    @FXML
    private TableColumn<ScrapReport_1, IncomingReport_1> details_column;
    @FXML
    private TableColumn<ScrapReport_1, Integer> qtyscrap_column;
    @FXML
    private TableColumn<ScrapReport_1, String> comments_column;
    @FXML
    private DatePicker start_datepicker1;
    @FXML
    private DatePicker end_datepicker1;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private TextField id_field1;
    @FXML
    private TextField packing_field1;
    @FXML
    private TextField po_field1;
    @FXML
    private TextField line_field1;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField lot_field1;
    @FXML
    private TextField rev_field1;
    @FXML
    private Button reset_button;
    @FXML
    private TextField packing_field2;
    @FXML
    private TextField po_field2;
    @FXML
    private TextField line_field2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField lot_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private ComboBox<IncomingReport_1> incomingreport_combo2;
    @FXML
    private Button save_button2;
    @FXML
    private TextField qtyscrap_field2;
    @FXML
    private DatePicker start_datepicker2;
    @FXML
    private DatePicker end_datepicker2;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private TextField id_field2;
    
    private ObjectProperty<Integer> qty_scrap = new SimpleObjectProperty();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setScrapReportTable();
        updateScrapReportTable();
        updateIncomingReportCombo();
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.itemsProperty().bind(company_combo1.itemsProperty());
        company_combo1.setOnAction((ActionEvent) -> {
            updateScrapReportTable();
        });
        start_datepicker1.setOnAction(company_combo1.getOnAction());
        end_datepicker1.setOnAction(company_combo1.getOnAction());
        id_field1.setOnAction(company_combo1.getOnAction());
        packing_field1.setOnAction(company_combo1.getOnAction());
        po_field1.setOnAction(company_combo1.getOnAction());
        line_field1.setOnAction(company_combo1.getOnAction());
        partnumber_field1.setOnAction(company_combo1.getOnAction());
        lot_field1.setOnAction(company_combo1.getOnAction());
        rev_field1.setOnAction(company_combo1.getOnAction());
        reset_button.setOnAction((ActionEvent) -> {
            clearSearchFields();
        });
    incomingreport_combo2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingReport_1> observable, IncomingReport_1 oldValue, IncomingReport_1 newValue) -> {
        try{
            qty_scrap.setValue(null);
            qtyscrap_field2.setText(newValue.getQty_ava()+"");
        }catch(Exception e){
            qty_scrap.setValue(null);
            incomingreport_combo2.getSelectionModel().clearSelection();
            qtyscrap_field2.clear();
        }
    });
        company_combo2.setOnAction((ActionEvent) -> {
            updateIncomingReportCombo();
        });
        start_datepicker2.setOnAction(company_combo2.getOnAction());
        end_datepicker2.setOnAction(company_combo2.getOnAction());
        id_field2.setOnAction(company_combo2.getOnAction());
        packing_field2.setOnAction(company_combo2.getOnAction());
        po_field2.setOnAction(company_combo2.getOnAction());
        line_field2.setOnAction(company_combo2.getOnAction());
        partnumber_field2.setOnAction(company_combo2.getOnAction());
        lot_field2.setOnAction(company_combo2.getOnAction());
        rev_field2.setOnAction(company_combo2.getOnAction());
        qtyscrap_field2.setOnAction((ActionEvent) -> {
            try{
                qty_scrap.setValue(null);
                qtyscrap_field2.setStyle(null);
                qty_scrap.setValue(Integer.parseInt(qtyscrap_field2.getText().trim()));
                if(qty_scrap.getValue() < 1 || qty_scrap.getValue() > incomingreport_combo2.getValue().getQty_ava()) throw new Exception();
                save_button2.requestFocus();
            }
            catch(Exception e){
                qty_scrap.setValue(null);
                qtyscrap_field2.setStyle("-fx-background-color: lightpink;");
                qtyscrap_field2.requestFocus();
                qtyscrap_field2.selectAll();
            }
        });
        save_button2.setOnAction((ActionEvent) -> {
            try{
                qty_scrap.setValue(null);
                qtyscrap_field2.setStyle(null);
                qty_scrap.setValue(Integer.parseInt(qtyscrap_field2.getText().trim()));
                if(qty_scrap.getValue() < 1 || qty_scrap.getValue() > incomingreport_combo2.getValue().getQty_ava()) throw new Exception();
            }catch(Exception e){
                qty_scrap.setValue(null);
                qtyscrap_field2.setStyle("-fx-background-color: lightpink;");
                qtyscrap_field2.requestFocus();
                qtyscrap_field2.selectAll();
                return;
            }
            createScrapReport();
            clearSearchFields();
            updateScrapReportTable();
            clearCreateFields();
            qty_scrap.setValue(null);
        });
        delete_button.setOnAction((ActionEvent) -> {
            msabase.getScrapReport_1DAO().delete(scrapreport_tableview.getSelectionModel().getSelectedItem());
            updateScrapReportTable();
            updateIncomingReportCombo();
        });
        incomingreport_combo2.disableProperty().bind(Bindings.size(incomingreport_combo2.getItems()).isEqualTo(0));
        qtyscrap_field2.disableProperty().bind(incomingreport_combo2.valueProperty().isNull());
        save_button2.disableProperty().bind(qty_scrap.isNull());
        delete_button.disableProperty().bind(scrapreport_tableview.getSelectionModel().selectedItemProperty().isNull());  
    }    
    
    public void setScrapReportTable(){
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        details_column.setCellValueFactory(new PropertyValueFactory<>("incoming_report"));
        qtyscrap_column.setCellValueFactory(new PropertyValueFactory<>("qty_scrap"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<ScrapReport_1, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getScrapReport_1DAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            scrapreport_tableview.refresh();
        });
    }
    
    public void updateScrapReportTable(){
        try{
            scrapreport_tableview.getItems().setAll(msabase.getScrapReport_1DAO().list(Integer.parseInt(id_field1.getText().trim()), DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                    partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }catch(Exception e){
            scrapreport_tableview.getItems().setAll(msabase.getScrapReport_1DAO().list(null, DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }
    }
    
    public void clearSearchFields(){
        start_datepicker1.setValue(null);
        end_datepicker1.setValue(null);
        company_combo1.setValue(null);
        id_field1.clear();
        partnumber_field1.clear();
        rev_field1.clear();
        lot_field1.clear();
        packing_field1.clear();
        po_field1.clear();
        line_field1.clear();
    }
    
    public void updateIncomingReportCombo(){
        try{
            incomingreport_combo2.getItems().setAll(msabase.getIncomingReport_1DAO().listAva(Integer.parseInt(id_field2.getText().trim()), DAOUtil.toUtilDate(start_datepicker2.getValue()), DAOUtil.toUtilDate(end_datepicker2.getValue()), company_combo2.getValue(), 
                    partnumber_field2.getText(), rev_field2.getText(), lot_field2.getText(), packing_field2.getText(), po_field2.getText(), line_field2.getText()));
        }catch(Exception e){
            incomingreport_combo2.getItems().setAll(msabase.getIncomingReport_1DAO().listAva(null, DAOUtil.toUtilDate(start_datepicker2.getValue()), DAOUtil.toUtilDate(end_datepicker2.getValue()), company_combo2.getValue(), 
                partnumber_field2.getText(), rev_field2.getText(), lot_field2.getText(), packing_field2.getText(), po_field2.getText(), line_field2.getText()));
        }
    }
    
    public void createScrapReport(){
        ScrapReport_1 scrap_report = new ScrapReport_1();
        scrap_report.setDate(DAOUtil.toUtilDate(LocalDate.now()));
        scrap_report.setEmployee(MainApp.current_employee);
        scrap_report.setIncoming_report(incomingreport_combo2.getValue());
        scrap_report.setQty_scrap(qty_scrap.getValue());
        scrap_report.setComments("");
        
        msabase.getScrapReport_1DAO().create(scrap_report);
    }
    
    public void clearCreateFields(){
        incomingreport_combo2.getSelectionModel().clearSelection();
        qtyscrap_field2.setStyle(null);
        qtyscrap_field2.clear();
        updateIncomingReportCombo();
    }
}
