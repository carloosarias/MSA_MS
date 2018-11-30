/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Equipment;
import model.MantainanceItem;
import model.MantainanceReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MantainanceReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<Equipment> equipment_tableview;
    @FXML
    private TableColumn<Equipment, Integer> id_column;
    @FXML
    private TableColumn<Equipment, String> name_column;
    @FXML
    private TableColumn<Equipment, String> serialnumber_column;
    @FXML
    private TableColumn<Equipment, String> description_column;
    @FXML
    private TableColumn<Equipment, String> type_column;
    @FXML
    private TableColumn<Equipment, String> physicallocation_column;
    @FXML
    private TableColumn<Equipment, Date> nextmantainance_column;
    @FXML
    private Button add_button;
    @FXML
    private TableView<MantainanceReport> mantainancereport_tableview;
    @FXML
    private TableColumn<MantainanceReport, Integer> reportid_column;
    @FXML
    private TableColumn<MantainanceReport, Date> reportdate_column;
    @FXML
    private TableColumn<MantainanceReport, String> employeeid_column;
    @FXML
    private TableColumn<MantainanceReport, String> employeename_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmentid_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmentname_column;
    @FXML
    private TableColumn<MantainanceReport, String> equipmenttype_column;
    @FXML
    private TableView<MantainanceItem> mantainanceitem_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> checkdescription_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    @FXML
    private TableColumn<MantainanceItem, Boolean> checkvalue_column;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setEquipmentTableview();
        setMantainanceReportTableview();
        setMantainanceItemTableview();
    }    
    
    public void setEquipmentTableview(){
        id_column.setCellValueFactory(new PropertyValueFactory("id"));
        name_column.setCellValueFactory(new PropertyValueFactory("name"));
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        description_column.setCellValueFactory(new PropertyValueFactory("description"));
        type_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getEquipmentDAO().findEquipmentType(c.getValue()).getName()));
        physicallocation_column.setCellValueFactory(new PropertyValueFactory("physical_location"));
        nextmantainance_column.setCellValueFactory(new PropertyValueFactory("next_mantainance"));
    }
    
    public void setMantainanceReportTableview(){
    reportid_column.setCellValueFactory(new PropertyValueFactory("id"));
    reportdate_column.setCellValueFactory(new PropertyValueFactory("report_date"));
    employeeid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceReportDAO().findEmployee(c.getValue()).getId()));
    employeename_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getMantainanceReportDAO().findEmployee(c.getValue()).toString()));
    equipmentid_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceReportDAO().findEquipment(c.getValue()).getId()));
    equipmentname_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getMantainanceReportDAO().findEquipment(c.getValue()).getName()));
    equipmenttype_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getEquipmentDAO().findEquipmentType(msabase.getMantainanceReportDAO().findEquipment(c.getValue())).getName()));
    }
    
    public void setMantainanceItemTableview(){
    checkdescription_column.setCellValueFactory(new PropertyValueFactory("description"));
    details_column.setCellValueFactory(new PropertyValueFactory("details"));
    checkvalue_column.setCellValueFactory(new PropertyValueFactory("check_value"));
    }
}
