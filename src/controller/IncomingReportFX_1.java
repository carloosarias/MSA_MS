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
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import model.PartRevision;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class IncomingReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<IncomingReport_1> incomingreport_tableview;
    @FXML
    private TableColumn<IncomingReport_1, String> date_column;
    @FXML
    private TableColumn<IncomingReport_1, Employee> employee_column;
    @FXML
    private TableColumn<IncomingReport_1, String> details_column;
    @FXML
    private TableColumn<IncomingReport_1, Integer> qtyin_column;
    @FXML
    private TableColumn<IncomingReport_1, String> comments_column;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private DatePicker start_datepicker1;
    @FXML
    private DatePicker end_datepicker1;
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
    private TextField partnumber_field2;
    @FXML
    private TextField rev_field2;
    @FXML
    private TextField lot_field2;
    @FXML
    private TextField qtyin_field2;
    @FXML
    private TextField packing_field2;
    @FXML
    private TextField po_field2;
    @FXML
    private TextField line_field2;
    @FXML
    private Button save_button2;
    @FXML
    private Button delete_button;
    
    private IncomingReport_1 incoming_report;
    private ObjectProperty<PartRevision> part_revision = new SimpleObjectProperty();
    private ObjectProperty<Integer> qty_in = new SimpleObjectProperty();
    private BooleanProperty open_property = new SimpleBooleanProperty(false);
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIncomingReportTable();
        updateIncomingReportTable();
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo1.setOnAction((ActionEvent) -> {
            updateIncomingReportTable();
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
        incomingreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingReport_1> observable, IncomingReport_1 oldValue, IncomingReport_1 newValue) -> {
            try{
                open_property.setValue(newValue.isOpen());
            }catch(Exception e){
                open_property.setValue(false);
            }
        });
        reset_button.setOnAction((ActionEvent) -> {
            clearSearchFields();
        });             
        save_button2.setOnAction((ActionEvent) -> {
            if(save_button2.isDisabled()) return;
            createIncomingReport();
            clearSearchFields();
            updateIncomingReportTable();
            clearCreateFields();
            part_revision.setValue(null);
            qty_in.setValue(null);
        });
        delete_button.setOnAction((ActionEvent) -> {
            msabase.getIncomingReport_1DAO().delete(incomingreport_tableview.getSelectionModel().getSelectedItem());
            updateIncomingReportTable();
        });
        packing_field2.setOnAction((ActionEvent) -> {
            po_field2.requestFocus();
        });
        po_field2.setOnAction((ActionEvent) -> {
           line_field2.requestFocus(); 
        });
        line_field2.setOnAction((ActionEvent) -> {
           partnumber_field2.requestFocus();
        });
        partnumber_field2.setOnAction((ActionEvent) -> {
            if(rev_field2.textProperty().isEmpty().not().getValue()){
                setPartRevision();
            }
            else{
                rev_field2.requestFocus();
            }
        });
        rev_field2.setOnAction((ActionEvent) -> {
            setPartRevision();
            lot_field2.requestFocus();
        });
        lot_field2.setOnAction((ActionEvent) -> {
            qtyin_field2.requestFocus();
        });
        qtyin_field2.setOnAction((ActionEvent) -> {
            qty_in.setValue(null);
            qtyin_field2.setStyle(null);
            try{
                qty_in.setValue(Integer.parseInt(qtyin_field2.getText().trim()));
                if(qty_in.getValue() < 1) throw new Exception();
                save_button2.fire();
            }catch(Exception e){
                qty_in.setValue(null);
                qtyin_field2.setStyle("-fx-background-color: lightpink;");
                qtyin_field2.requestFocus();
                qtyin_field2.selectAll();
            }
        });
        partnumber_field2.disableProperty().bind(packing_field2.textProperty().isEmpty().or(po_field2.textProperty().isEmpty()).or(line_field2.textProperty().isEmpty()));
        rev_field2.disableProperty().bind(partnumber_field2.textProperty().isEmpty());
        lot_field2.disableProperty().bind(part_revision.isNull());
        qtyin_field2.disableProperty().bind(part_revision.isNull());
        delete_button.disableProperty().bind(open_property.not());  
        save_button2.disableProperty().bind(part_revision.isNull().or(lot_field2.textProperty().isEmpty()).or(qty_in.isNull())
                .or(packing_field2.textProperty().isEmpty()).or(po_field2.textProperty().isEmpty()).or(line_field2.textProperty().isEmpty()));
    }
    
    public void setIncomingReportTable(){
        //counter_column.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getTableView().getItems().indexOf(c.getValue())+1)));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        details_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().toString()));
        //rev_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getRev()));
        //lot_column.setCellValueFactory(new PropertyValueFactory<>("lot"));
        //packing_column.setCellValueFactory(new PropertyValueFactory<>("packing"));
        //po_column.setCellValueFactory(new PropertyValueFactory<>("po"));
        //line_column.setCellValueFactory(new PropertyValueFactory<>("line"));
        qtyin_column.setCellValueFactory(new PropertyValueFactory<>("qty_in"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<IncomingReport_1, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getIncomingReport_1DAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            incomingreport_tableview.refresh();
        });
    }
    
    public void updateIncomingReportTable() {
        try{
            incomingreport_tableview.getItems().setAll(msabase.getIncomingReport_1DAO().list(Integer.parseInt(id_field1.getText().trim()), DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                    partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }catch(Exception e){
            incomingreport_tableview.getItems().setAll(msabase.getIncomingReport_1DAO().list(null, DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
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
    
    public void createIncomingReport(){
        incoming_report = new IncomingReport_1();
        incoming_report.setDate(DAOUtil.toUtilDate(LocalDate.now()));
        incoming_report.setEmployee(MainApp.current_employee);
        incoming_report.setPart_revision(part_revision.getValue());
        incoming_report.setLot(lot_field2.getText().trim());
        incoming_report.setQty_in(qty_in.getValue());
        incoming_report.setPacking(packing_field2.getText().trim());
        incoming_report.setPo(po_field2.getText().trim());
        incoming_report.setLine(line_field2.getText().trim());
        incoming_report.setComments("");
        
        msabase.getIncomingReport_1DAO().create(incoming_report);
    }
    
    public void setPartRevision(){
        partnumber_field2.setStyle("-fx-background-color: lightgreen;");
        rev_field2.setStyle("-fx-background-color: lightgreen;");
        part_revision.setValue(msabase.getPartRevisionDAO().find(partnumber_field2.getText().trim(), rev_field2.getText().trim()));
        if(part_revision.isNull().getValue()){
            clearCreateFields();
            partnumber_field2.setStyle("-fx-background-color: lightpink;");
            rev_field2.setStyle("-fx-background-color: lightpink;");
        }
    }
    
    public void clearCreateFields(){
        partnumber_field2.setStyle(null);
        rev_field2.setStyle(null);
        partnumber_field2.clear();
        rev_field2.clear();
        lot_field2.clear();
        qtyin_field2.clear();
        partnumber_field2.requestFocus();
    }
    
}
