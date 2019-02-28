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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Equipment;
import model.EquipmentType;
import model.MantainanceItem;
import model.MantainanceReport;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MantainanceReportFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<MantainanceReport> mantainancereport_tableview;
    @FXML
    private TableColumn<MantainanceReport, String> reportdate_column;
    @FXML
    private TableColumn<MantainanceReport, String> employee_column;
    @FXML
    private TableColumn<MantainanceReport, String> name_column;
    @FXML
    private TableColumn<MantainanceReport, String> serialnumber_column;
    @FXML
    private TableColumn<MantainanceReport, String> type_column;
    @FXML
    private TableColumn<MantainanceReport, String> location_column;
    @FXML
    private ComboBox<EquipmentType> type_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private CheckBox type_filter;
    @FXML
    private CheckBox equipment_filter;
    @FXML
    private Button pdf_button;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<MantainanceItem> mantainanceitem_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> checkdescription_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMantainanceReportTable();
        updateMantainanceReportTable();
    }
    
    public void setMantainanceReportTable(){
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        employee_column.setCellValueFactory(new PropertyValueFactory("employee"));
        name_column.setCellValueFactory(new PropertyValueFactory("equipment_name"));
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("serial_number"));
        type_column.setCellValueFactory(new PropertyValueFactory("equipment_type"));
        location_column.setCellValueFactory(new PropertyValueFactory("physical_location"));
    }
    
    public void updateMantainanceReportTable(){
        try{
        mantainancereport_tableview.getItems().setAll(msabase.getMantainanceReportDAO().list(type_combo.getSelectionModel().getSelectedItem(), equipment_combo.getSelectionModel().getSelectedItem(), type_filter.isSelected(), equipment_filter.isSelected(), true));
        }catch(Exception e) {
            mantainancereport_tableview.getItems().clear();
        }
    }
}
