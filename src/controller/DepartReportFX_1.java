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
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Company;
import model.CompanyAddress;
import model.DepartLot_1;
import model.DepartReport_1;
import model.Employee;
import model.IncomingReport_1;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartReport_1> departreport_tableview;
    @FXML
    private TableColumn<DepartReport_1, Integer> reportid_column;
    @FXML
    private TableColumn<DepartReport_1, String> date_column;
    @FXML
    private TableColumn<DepartReport_1, Employee> employee_column;
    @FXML
    private TableColumn<DepartReport_1, String> client_column;
    @FXML
    private TableColumn<DepartReport_1, CompanyAddress> companyaddress_column;
    @FXML
    private TableColumn<DepartReport_1, Integer> qtytotal_column;
    @FXML
    private TableColumn<DepartReport_1, Integer> count_column;
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
    private Button reset_button1;
    @FXML
    private Button save_button2;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private ComboBox<CompanyAddress> companyaddress_combo2;
    @FXML
    private Button delete_button1;
    @FXML
    private Button pdf_button1;
    @FXML
    private ComboBox<String> pdf_combo1;
    @FXML
    private Tab details_tab;
    @FXML
    private TextField packing_field3;
    @FXML
    private TextField po_field3;
    @FXML
    private TextField line_field3;
    @FXML
    private TextField partnumber_field3;
    @FXML
    private TextField lot_field3;
    @FXML
    private TextField rev_field3;
    @FXML
    private ComboBox<IncomingReport_1> incomingreport_combo3;
    @FXML
    private Button save_button3;
    @FXML
    private TextField qtyout_field3;
    @FXML
    private DatePicker start_datepicker3;
    @FXML
    private DatePicker end_datepicker3;
    @FXML
    private ComboBox<Company> company_combo3;
    @FXML
    private TextField id_field3;
    @FXML
    private TableView<DepartLot_1> departlot_tableview;
    @FXML
    private TableColumn<DepartLot_1, String> date_column2;
    @FXML
    private TableColumn<DepartLot_1, String> employee_column2;
    @FXML
    private TableColumn<DepartLot_1, Integer> incomingreportid_column;
    @FXML
    private TableColumn<DepartLot_1, String> partnumber_column;
    @FXML
    private TableColumn<DepartLot_1, String> lot_column;
    @FXML
    private TableColumn<DepartLot_1, String> po_column;
    @FXML
    private TableColumn<DepartLot_1, String> line_column;
    @FXML
    private TableColumn<DepartLot_1, String> packing_column;
    @FXML
    private TableColumn<DepartLot_1, Integer> quantity_column;
    @FXML
    private TableColumn<DepartLot_1, String> comments_column;
    @FXML
    private Button delete_button2;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    private ObjectProperty<Integer> qty_out = new SimpleObjectProperty();
    private BooleanProperty open_property = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //SETUP
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        
        //UPDATE TABLE ON FILTER CHANGE
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.getItems().setAll(company_combo1.getItems());
        company_combo1.setOnAction((ActionEvent) -> {
            updateDepartReportTable();
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
        reset_button1.setOnAction((ActionEvent) -> {
            clearSearchFields();
        });
        
        //CHECK IF EDITING ALLOWED ON TABLE ITEM SELECTION
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport_1> observable, DepartReport_1 oldValue, DepartReport_1 newValue) -> {
            updateDepartLotTable();
            try{
                
                open_property.setValue(newValue.isOpen());
            }catch(Exception e){
                open_property.setValue(false);
            }
        });
        
        //CREATE NEW REPORT
        company_combo2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            try{
                companyaddress_combo2.getItems().clear();
                companyaddress_combo2.getItems().setAll(msabase.getCompanyAddressDAO().list(newValue));
            }catch(Exception e){
                companyaddress_combo2.getItems().clear();
            }
        });
        save_button2.setOnAction((ActionEvent) -> {
            createDepartReport();
            clearSearchFields();
            updateDepartReportTable();
            company_combo2.getSelectionModel().clearSelection();
        });
        
        //LIMIT MAX-QTY ON COMBO ITEM SELECTION
        incomingreport_combo3.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingReport_1> observable, IncomingReport_1 oldValue, IncomingReport_1 newValue) -> {
            try{
                qty_out.setValue(null);
                qtyout_field3.setText(newValue.getQty_ava()+"");
            }catch(Exception e){
                qty_out.setValue(null);
                incomingreport_combo3.getSelectionModel().clearSelection();
                qtyout_field3.clear();
            }
        });
        details_tab.disableProperty().bind(Bindings.size(departlot_tableview.getItems()).isEqualTo(0));
        departreport_tableview.editableProperty().bind(open_property);
        companyaddress_combo2.disableProperty().bind(Bindings.size(companyaddress_combo2.getItems()).isEqualTo(0));
        save_button2.disableProperty().bind(companyaddress_combo2.valueProperty().isNull());
        delete_button1.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull().and(open_property.not()));  
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany_address().getCompany().toString()));
        companyaddress_column.setCellValueFactory(new PropertyValueFactory<>("company_address"));
        qtytotal_column.setCellValueFactory(new PropertyValueFactory<>("qty_total"));
        count_column.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
    
    public void setDepartLotTable(){
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        employee_column2.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        incomingreportid_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_partnumber"));
        lot_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_lot"));
        po_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_po"));
        line_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_line"));
        packing_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_packing"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("qty_out"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void updateDepartReportTable(){
        try{
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(Integer.parseInt(id_field1.getText().trim()), DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                    partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }catch(Exception e){
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(null, DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                partnumber_field1.textProperty().getValue(), rev_field1.textProperty().getValue(), lot_field1.textProperty().getValue(), packing_field1.textProperty().getValue(), po_field1.textProperty().getValue(), line_field1.textProperty().getValue()));
        }
    }
    
    public void updateDepartLotTable(){
        departlot_tableview.getItems().setAll(msabase.getDepartLot_1DAO().list(departreport_tableview.getSelectionModel().getSelectedItem()));
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
    
    public void createDepartReport(){
        DepartReport_1 depart_report = new DepartReport_1();
        depart_report.setDate(DAOUtil.toUtilDate(LocalDate.now()));
        depart_report.setEmployee(MainApp.current_employee);
        depart_report.setCompany_address(companyaddress_combo2.getValue());
        depart_report.setComments("");
        
        msabase.getDepartReport_1DAO().create(depart_report);
    }
    
    
}
