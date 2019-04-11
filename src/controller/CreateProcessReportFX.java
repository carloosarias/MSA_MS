/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Equipment;
import model.EquipmentType;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
import model.Tank;
import msa_ms.MainApp;
import static msa_ms.MainApp.setDatePicker;
import static msa_ms.MainApp.timeFormat;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateProcessReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Tank> tank_combo;
    @FXML
    private ComboBox<EquipmentType> equipmenttype_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private ComboBox<PartRevision> revision_combo;
    @FXML
    private Button save_button;
    
    private ProductPart partnumbercombo_selection;
    private String partnumbercombo_text;
    
    private PartRevision revisioncombo_selection;
    private String revisioncombo_text;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    public static ProcessReport process_report;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tank_combo.setItems(FXCollections.observableArrayList(msabase.getTankDAO().list(true)));
        equipmenttype_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeDAO().list(true)));
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        reportdate_picker.setValue(LocalDate.now());
        setDatePicker(reportdate_picker);
        
        equipmenttype_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EquipmentType> observable, EquipmentType oldValue, EquipmentType newValue) -> {
            equipment_combo.getSelectionModel().clearSelection();
            equipment_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().list(newValue, true)));
            equipment_combo.setDisable(equipment_combo.getItems().isEmpty());
        });
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            partnumbercombo_text = partnumber_combo.getEditor().textProperty().getValue().replace(" ", "").toUpperCase();
            partnumbercombo_selection = null;
            for(ProductPart product_part : partnumber_combo.getItems()){
                if(partnumbercombo_text.equals(product_part.getPart_number())){
                    partnumbercombo_selection = product_part;
                    break;
                }
            }
           
            if(partnumbercombo_selection == null){
                partnumber_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                revision_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        revision_combo.setOnAction((ActionEvent) -> {
            System.out.println(revisioncombo_selection);
            revisioncombo_text = revision_combo.getEditor().textProperty().getValue().replace(" ", "").toUpperCase();
            revisioncombo_selection = null;
            for(PartRevision part_revision : revision_combo.getItems()){
                if(revisioncombo_text.equals(part_revision.getRev())){
                    revisioncombo_selection = part_revision;
                    break;
                }
            }
            if(revisioncombo_selection == null){
                revision_combo.getEditor().selectAll();
            }
            else{
                ActionEvent.consume();
            }            
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveProcessReport();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public void saveProcessReport(){
        process_report = new ProcessReport();
        process_report.setProcess("N/A");
        process_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        process_report.setLot_number("N/A");
        process_report.setQuantity(0);
        process_report.setAmperage(0.0);
        process_report.setVoltage(0.0);
        process_report.setStart_time(LocalTime.now().format(timeFormat));
        process_report.setEnd_time(process_report.getStart_time());
        process_report.setComments("N/A");
        process_report.setQuality_passed(true);
        
        msabase.getProcessReportDAO().create(
            MainApp.current_employee,
            revisioncombo_selection,
            tank_combo.getSelectionModel().getSelectedItem(),
            equipment_combo.getSelectionModel().getSelectedItem(),
            process_report);
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        tank_combo.setStyle(null);   
        equipmenttype_combo.setStyle(null);
        equipment_combo.setStyle(null);   
        partnumber_combo.setStyle(null);        
        revision_combo.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(tank_combo.getSelectionModel().isEmpty()){
            tank_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(equipmenttype_combo.getSelectionModel().isEmpty()){
            equipmenttype_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(equipment_combo.getSelectionModel().isEmpty()){
            equipment_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            partnumbercombo_selection.getId();
        }
        catch(Exception e){
            partnumber_combo.setStyle("-fx-background-color: lightpink;");
            partnumber_combo.getSelectionModel().select(null);
            partnumber_combo.getEditor().setText(null);
            b = false;
        }
        
        try {
            revisioncombo_selection.getId();
        }
        catch(Exception e){
            revision_combo.setStyle("-fx-background-color: lightpink;");
            revision_combo.getSelectionModel().select(null);
            revision_combo.getEditor().setText(null);
            b = false;
        }
        
        return b;
    }

    public void updatePartrev_combo(){
        try{
            revision_combo.getItems().setAll(msabase.getPartRevisionDAO().list(null, null, null, partnumbercombo_selection.getPart_number()));
        } catch(Exception e) {
            revision_combo.getItems().clear();
        }
        revision_combo.setDisable(revision_combo.getItems().isEmpty());
    }
    
}
