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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.DepartReport;
import model.Employee;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartReport> departreport_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> reportid_column;
    @FXML
    private TableColumn<DepartReport, Employee> employee_column;
    @FXML
    private TableColumn<DepartReport, String> reportdate_column;
    @FXML
    private TableColumn<DepartReport, Company> client_column;
    @FXML
    private TableColumn<DepartReport, CompanyAddress> address_column;
    @FXML
    private TableColumn<DepartReport, Integer> totalqty_column;
    @FXML
    private TableColumn<DepartReport, Integer> totalbox_column;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private DatePicker start_datepicker;
    @FXML
    private DatePicker end_datepicker;
    @FXML
    private Button reset_button;
    @FXML
    private Button save_button;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private Button delete_button1;
    @FXML
    private Tab details_tab;
    @FXML
    private Button pdf_button;
    @FXML
    private ComboBox<?> pdf_combo;
    @FXML
    private TextField partnumber_field;
    @FXML
    private TextField rev_field;
    @FXML
    private ComboBox<?> po_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private Button save_button2;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column3;
    @FXML
    private TableColumn<DepartLot, String> partrevision_column;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column2;
    @FXML
    private TableColumn<DepartLot, String> process_column2;
    @FXML
    private TableColumn<DepartLot, String> ponumber_column2;
    @FXML
    private TableColumn<DepartLot, String> linenumber_column2;
    @FXML
    private TableColumn<DepartLot, String> quantity_column3;
    @FXML
    private TableColumn<DepartLot, String> boxquantity_column3;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button delete_button2;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        updateComboItems();
        
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            updateDepartLotTable();
        });
        
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        client_column.setCellValueFactory(new PropertyValueFactory<>("company"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("company"));
        totalqty_column.setCellValueFactory(new PropertyValueFactory<>("total_qty"));
        totalbox_column.setCellValueFactory(new PropertyValueFactory<>("total_box"));
    }
    
    public void setDepartLotTable(){
        partnumber_column3.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partrevision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column2.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        lotnumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLot_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        ponumber_column2.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        ponumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        ponumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPo_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        linenumber_column2.setCellValueFactory(new PropertyValueFactory<>("line_number"));
        linenumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        linenumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLine_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        quantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column3.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        boxquantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getBox_quantity()));
        boxquantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        boxquantity_column3.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setBox_quantity(getBox_quantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
    }
    
    public void updateDepartReportTable(){
        System.out.println(start_datepicker.getValue());
        departreport_tableview.getItems().setAll(msabase.getDepartReportDAO().list(company_combo1.getValue(), DAOUtil.toUtilDate(start_datepicker.getValue()), DAOUtil.toUtilDate(end_datepicker.getValue())));
    }
    
    public void updateDepartLotTable(){
        departlot_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().list(departreport_tableview.getSelectionModel().getSelectedItem())));
    }
    
    public void updateComboItems(){
        setDatePicker(start_datepicker);
        setDatePicker(end_datepicker);
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.itemsProperty().bind(company_combo1.itemsProperty());
    }
    
    public Integer getQuantityValue(DepartLot depart_lot, String quantity){
        try{
            return Integer.parseInt(quantity);
        }catch(Exception e){
            return depart_lot.getQuantity();
        }
    }
    
    public Integer getBox_quantityValue(DepartLot depart_lot, String box_quantity){
        try{
            return Integer.parseInt(box_quantity);
        }catch(Exception e){
            return depart_lot.getBox_quantity();
        }
    }
    
}
