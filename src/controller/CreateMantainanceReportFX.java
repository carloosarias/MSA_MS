/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Employee;
import model.Equipment;
import model.EquipmentTypeCheck;
import model.MantainanceItem;
import model.MantainanceReport;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateMantainanceReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker date_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private TableView<MantainanceItem> mantainancecheck_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> name_column;
    @FXML
    private TableColumn<MantainanceItem, String> description_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    @FXML
    private TableColumn<MantainanceItem, Boolean> checkvalue_column;
    @FXML
    private Button submit_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private List<MantainanceItem> mantainanceitem_list = new ArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillComboBoxValues();
        setMantainanceItemTableView();
        setMantainanceitem_list();
        mantainancecheck_tableview.setItems(FXCollections.observableArrayList(mantainanceitem_list));
        
        submit_button.setOnAction((ActionEvent) ->{
            saveMantainanceReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void saveMantainanceReport(){
        
        for(MantainanceItem item: mantainancecheck_tableview.getItems()){
            if(item.getDetails() == null){
                item.setDetails("N/A");
            }
        }
        
        MantainanceReport report = new MantainanceReport();
        report.setReport_date(DAOUtil.toUtilDate(date_picker.getValue()));
        msabase.getMantainanceReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), equipment_combo.getSelectionModel().getSelectedItem(), report);
        
        saveMantainanceItems(report);
    }
    
    public void saveMantainanceItems(MantainanceReport report){

        for(MantainanceItem item : mantainancecheck_tableview.getItems()){
            msabase.getMantainanceItemDAO().create(report, item.getTemp_typecheck(), item);
        }
        
        setNextMantainance();
    }
    
    public void setNextMantainance(){
        MantainanceReportFX.getEquipment_selection().setNext_mantainance(DAOUtil.toUtilDate(LocalDate.now().plusDays(msabase.getEquipmentDAO().findEquipmentType(MantainanceReportFX.getEquipment_selection()).getFrequency())));
        msabase.getEquipmentDAO().update(MantainanceReportFX.getEquipment_selection());
    }
    
    public void setMantainanceItemTableView(){
        name_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_typecheck().getName()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_typecheck().getDescription()));
        details_column.setCellValueFactory(new PropertyValueFactory<>("details"));
        checkvalue_column.setCellValueFactory(new PropertyValueFactory("check_value"));
        
        details_column.setCellFactory(TextFieldTableCell.forTableColumn());
        details_column.setOnEditCommit((TableColumn.CellEditEvent<MantainanceItem, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setDetails(t.getNewValue());
        });
        
        checkvalue_column.setCellFactory(column -> new CheckBoxTableCell<>());
        checkvalue_column.setCellValueFactory(cellData -> {
            MantainanceItem cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isCheck_value());
            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> {
                cellValue.setCheck_value(newValue);
            });
            return property;
        });
    }
    
    public void fillComboBoxValues(){
        date_picker.setValue(LocalDate.now());
        employee_combo.setItems(FXCollections.observableArrayList(msabase.getEmployeeDAO().find(MainApp.employee_id)));
        employee_combo.getSelectionModel().selectFirst();
        equipment_combo.setItems(FXCollections.observableArrayList(MantainanceReportFX.getEquipment_selection()));
        equipment_combo.getSelectionModel().selectFirst();
    }
    
    public void setMantainanceitem_list(){
        mantainanceitem_list = new ArrayList();
        for(EquipmentTypeCheck check : msabase.getEquipmentTypeCheckDAO().list(msabase.getEquipmentDAO().findEquipmentType(equipment_combo.getSelectionModel().getSelectedItem()))){
            MantainanceItem item = new MantainanceItem();
            item.setTemp_typecheck(check);
            mantainanceitem_list.add(item);
        }
    }
    
}
