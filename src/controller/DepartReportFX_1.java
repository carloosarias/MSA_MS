/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import model.DepartReport_1;
import model.Employee;
import model.IncomingReport_1;
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
    private ComboBox<?> pdf_combo1;
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
    private TableView<?> departlot_tableview;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private Button delete_button2;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartReportTable();
        updateDepartReportTable();
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
    
    public void updateDepartReportTable(){
        try{
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(Integer.parseInt(id_field1.getText().trim()), DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                    partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }catch(Exception e){
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(null, DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }
    }
}
