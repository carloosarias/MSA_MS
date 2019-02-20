/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Equipment;
import model.EquipmentType;
import model.MantainanceItem;
import model.MantainanceReport;
import msa_ms.MainApp;

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
    private ComboBox<EquipmentType> equipmenttype_combo;
    @FXML
    private ComboBox<Equipment> equipment_combo;
    @FXML
    private Button search_button;
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
    private TableView<MantainanceItem> mantainanceitem_tableview;
    @FXML
    private TableColumn<MantainanceItem, String> checkdescription_column;
    @FXML
    private TableColumn<MantainanceItem, String> details_column;
    @FXML
    private TableColumn<MantainanceItem, Boolean> checkvalue_column;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();
    
    private static Equipment equipment_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipment_selection = new Equipment();
        equipmenttype_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentTypeDAO().list()));
        setEquipmentTableview();
        setMantainanceReportTableview();
        setMantainanceItemTableview();
        
        updateEquipmentTable();
        
        add_button.disableProperty().bind(equipment_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        equipment_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Equipment> observable, Equipment oldValue, Equipment newValue) -> {
            equipment_selection = equipment_tableview.getSelectionModel().getSelectedItem();
        });
        
        equipmenttype_combo.setOnAction((ActionEvent) -> {
            equipment_combo.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().list(equipmenttype_combo.getSelectionModel().getSelectedItem())));
            equipment_combo.setDisable(equipment_combo.getItems().isEmpty());
        });
        
        equipment_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Equipment> observable, Equipment oldValue, Equipment newValue) -> {
            search_button.setDisable(equipment_combo.getSelectionModel().isEmpty());
        });
        
        search_button.setOnAction((ActionEvent) -> {
            updateMantainanceReportTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateEquipmentTable();
        });
        
        mantainancereport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue <? extends MantainanceReport> observable, MantainanceReport oldValue, MantainanceReport newValue) -> {
            updateMantainanceItemTable();
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(equipment_tableview.getItems()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    
    public void updateMantainanceItemTable(){
        mantainanceitem_tableview.setItems(FXCollections.observableArrayList(msabase.getMantainanceItemDAO().list(mantainancereport_tableview.getSelectionModel().getSelectedItem())));
    }
    
    public void updateMantainanceReportTable(){
        mantainancereport_tableview.setItems(FXCollections.observableArrayList(msabase.getMantainanceReportDAO().listEquipment(equipment_combo.getSelectionModel().getSelectedItem())));
    }
    
    public void updateEquipmentTable(){
        equipment_tableview.setItems(FXCollections.observableArrayList(msabase.getEquipmentDAO().listPending(DAOUtil.toUtilDate(LocalDate.now().plusWeeks(1)))));
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateMantainanceReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Mantenimiento");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProcessReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    }
    
    public void setMantainanceItemTableview(){
        checkdescription_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getMantainanceItemDAO().findEquipmentTypeCheck(c.getValue()).getDescription()));
        details_column.setCellValueFactory(new PropertyValueFactory("details"));
        checkvalue_column.setCellValueFactory(new PropertyValueFactory("check_value"));
        checkvalue_column.setCellFactory(column -> new CheckBoxTableCell<>());
        checkvalue_column.setCellValueFactory(cellData -> {
            MantainanceItem cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isCheck_value());
            return property;
        });
    }
    
    public static Equipment getEquipment_selection(){
        return equipment_selection;
    }
    
    public File buildPDF(List<Equipment> equipment_list) throws IOException{
        
        Path template = Files.createTempFile("MantainanceTemplate", ".pdf");
        template.toFile().deleteOnExit();
        try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/MantainanceTemplate.pdf")) {
            Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
        }
            
        Path output = Files.createTempFile("ActividadesDeMantenimientoPDF", ".pdf");
        template.toFile().deleteOnExit();  
        
        PdfDocument pdf = new PdfDocument(
            new PdfReader(template.toFile()),
            new PdfWriter(output.toFile())
        );

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        fields.get("report_date").setValue(LocalDate.now().toString());
        fields.get("employee").setValue(MainApp.current_employee.toString());

        int i = 0;
        for(Equipment equipment : equipment_list){
            int current_row = i+1;
            fields.get("id"+current_row).setValue(""+equipment.getId());
            fields.get("name"+current_row).setValue(equipment.getName());
            fields.get("serial_number"+current_row).setValue(equipment.getSerial_number());
            fields.get("description"+current_row).setValue(equipment.getDescription());
            fields.get("type"+current_row).setValue(msabase.getEquipmentDAO().findEquipmentType(equipment).getName());
            fields.get("physical_location"+current_row).setValue(equipment.getPhysical_location());
            fields.get("next_mantainance"+current_row).setValue(equipment.getNext_mantainance().toString());
            i++;
        }
        
        form.flattenFields();
        pdf.close();
        
        return output.toFile();
    }
}
