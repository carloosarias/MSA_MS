/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.Employee;
import model.IncomingItem;
import model.IncomingLot;
import model.IncomingReport;
import model.PartRevision;
import model.ProductPart;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private TextField ponumber_field;
    @FXML
    private TextField packinglist_field;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> partnumber_column;
    @FXML
    private TableColumn<PartRevision, String> revision_column;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private Button item_add_button;
    @FXML
    private Button item_delete_button;
    @FXML
    private HBox lot_hbox;
    @FXML
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, String> lotnumber_column;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column;
    @FXML
    private TableColumn<IncomingLot, Integer> boxquantity_column;
    @FXML
    private TableColumn<IncomingLot, String> status_column;
    @FXML
    private TableColumn<IncomingLot, String> comments_column;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private TextField boxquantity_field;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button lot_add_button;
    @FXML
    private Button lot_delete_button;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private List<PartRevision> part_revisions = new ArrayList<PartRevision>();

    private List<IncomingLot> incoming_lots = new ArrayList<IncomingLot>();
    
    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        status_combo.setItems(FXCollections.observableArrayList(MainApp.status_list));
        revision_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(c.getValue()).toString()));
        
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        
        part_combo.setOnAction((ActionEvent) -> {
            List<PartRevision> list = msabase.getPartRevisionDAO().list(part_combo.getSelectionModel().getSelectedItem(), true);
            partrev_combo.setItems(FXCollections.observableArrayList(list));
            partrev_combo.getItems().removeAll(part_revisions);
            partrev_combo.disableProperty().bind(Bindings.isEmpty(partrev_combo.getItems()));
        });
        
        partrev_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            item_add_button.setDisable((newValue == null));
        });
        
        item_add_button.setOnAction((ActionEvent) -> {
            part_revisions.add(partrev_combo.getSelectionModel().getSelectedItem());
            partrev_combo.getItems().remove(partrev_combo.getSelectionModel().getSelectedItem());
            partrevision_tableview.setItems(FXCollections.observableArrayList(part_revisions));
            partrevision_tableview.disableProperty().bind(Bindings.isEmpty(partrevision_tableview.getItems()));
        });
        
        item_delete_button.setOnAction((ActionEvent) -> {
            part_revisions.remove(partrevision_tableview.getSelectionModel().getSelectedItem());
            partrev_combo.getItems().add(partrevision_tableview.getSelectionModel().getSelectedItem());
            partrevision_tableview.setItems(FXCollections.observableArrayList(part_revisions));
            clearLots(partrevision_tableview.getSelectionModel().getSelectedItem());
            partrevision_tableview.disableProperty().bind(Bindings.isEmpty(partrevision_tableview.getItems()));
        });
        
        partrevision_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            updateLotListview();
            item_delete_button.setDisable((newValue == null));
            lot_hbox.setDisable((newValue == null));
        });
        
        
        lot_add_button.setOnAction((ActionEvent) -> {
            if(!testLotFields()){
                return;
            }
            IncomingLot incoming_lot = new IncomingLot();
            incoming_lot.setLot_number(lotnumber_field.getText());
            incoming_lot.setQuantity(Integer.parseInt(quantity_field.getText()));
            incoming_lot.setBox_quantity(Integer.parseInt(boxquantity_field.getText()));
            incoming_lot.setStatus(status_combo.getSelectionModel().getSelectedItem());
            incoming_lot.setComments(comments_area.getText());
            incoming_lot.setPartrevision_index(partrevision_tableview.getSelectionModel().getSelectedItem().getId());
            incoming_lots.add(incoming_lot);
            clearLotFields();
            updateLotListview();
        });
        
        lot_delete_button.setOnAction((ActionEvent) -> {
            incoming_lots.remove(incominglot_tableview.getSelectionModel().getSelectedItem());
            updateLotListview();
        });
       
        save_button.setOnAction((ActionEvent) -> {
            if(!testSaveFields()){
                return;
            }
            saveIncomingReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }   
    
    public boolean testSaveFields(){
        boolean b = true;
        clearStyle();
        if(part_revisions.isEmpty()){
            partrevision_tableview.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(incoming_lots.isEmpty()){
            incominglot_tableview.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(packinglist_field.getText().replace(" ","").equals("")){
            packinglist_field.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        
        if(ponumber_field.getText().replace(" ", "").equals("")){
            ponumber_field.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(company_combo.getSelectionModel().isEmpty()){
            company_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
        }
        return b;
    }
    
    public void saveIncomingReport(){
        IncomingReport incoming_report = new IncomingReport();
        incoming_report.setReport_date(java.sql.Date.valueOf(reportdate_picker.getValue()));
        incoming_report.setPo_number(ponumber_field.getText());
        incoming_report.setPacking_list(packinglist_field.getText());
        msabase.getIncomingReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), incoming_report);
        saveIncomingItems(incoming_report);
    }
    
    public void saveIncomingItems(IncomingReport incoming_report){
        for(PartRevision part_revision : part_revisions){
            IncomingItem incoming_item = new IncomingItem();
            msabase.getIncomingItemDAO().create(incoming_report, part_revision, incoming_item);
            saveIncomingLots(part_revision, incoming_item);
        }
    }
    
    public void saveIncomingLots(PartRevision part_revision, IncomingItem incoming_item){
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getPartRevision_index().equals(part_revision.getId())){
                msabase.getIncomingLotDAO().create(incoming_item, incoming_lot);
            }
        }
    }
    
    public void clearLots(PartRevision part_revision){
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getPartRevision_index().equals(part_revision.getId())){
                incoming_lots.remove(incoming_lot);
            }
        }
    }
    
    public void updateLotListview(){
        List<IncomingLot> incoming_lots_filtered = new ArrayList<IncomingLot>();
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getPartRevision_index().equals(partrevision_tableview.getSelectionModel().getSelectedItem().getId())){
                incoming_lots_filtered.add(incoming_lot);
            }
        }
        incominglot_tableview.setItems(FXCollections.observableArrayList(incoming_lots_filtered));
        incominglot_tableview.disableProperty().bind(Bindings.isEmpty(incominglot_tableview.getItems()));
    }
    
    public void clearLotFields(){
        lotnumber_field.setText(null);
        status_combo.getSelectionModel().clearSelection();
        comments_area.setText(null);
        quantity_field.setText(null);
        boxquantity_field.setText(null);
    }
    
    public boolean testLotFields(){
        boolean b = true;
        clearStyle();
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(status_combo.getSelectionModel().isEmpty()){
            status_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }        
        try{
            Double.parseDouble(boxquantity_field.getText());
        }catch(Exception e){
            boxquantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        status_combo.setStyle(null);
        comments_area.setStyle(null);
        quantity_field.setStyle(null);
        boxquantity_field.setStyle(null);
    }
}
