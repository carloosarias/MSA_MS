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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.DepartReport;
import model.Employee;
import model.PartRevision;
import model.ProductPart;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateDepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private ComboBox<CompanyAddress> address_combo;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column;
    @FXML
    private TableColumn<DepartLot, String> revision_column;
    @FXML
    private TableColumn<DepartLot, String> quantity_column;
    @FXML
    private TableColumn<DepartLot, String> process_column;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button lot_add_button;
    @FXML
    private Button lot_delete_button;
    @FXML
    private Button save_button;
    
    private ProductPart partcombo_selection;
    private String partcombo_text;
    
    private PartRevision partrevcombo_selection;
    private String partrevcombo_text;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private List<DepartLot> depart_lots = new ArrayList<DepartLot>();
    
    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        departlot_tableview.setDisable(departlot_tableview.getItems().isEmpty());
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productpart().toString()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_partrevision().getRev()));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit(
            (TableColumn.CellEditEvent<DepartLot, String> t) ->
                (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setComments(t.getNewValue())
        );
        
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        reportdate_picker.setValue(LocalDate.now());
        
        lotnumber_field.setOnAction((ActionEvent) -> {
            part_combo.requestFocus();
            ActionEvent.consume();
        });
        
        company_combo.setOnAction((ActionEvent) -> {
            if(!company_combo.getSelectionModel().isEmpty()){
                address_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(company_combo.getSelectionModel().getSelectedItem(), true)));
            }
            address_combo.setDisable(address_combo.getItems().isEmpty());
        });
        
        part_combo.setOnAction((ActionEvent) -> {
            partcombo_text = part_combo.getEditor().textProperty().getValue();
            partcombo_selection = null;
            for(ProductPart product_part : part_combo.getItems()){
                if(partcombo_text.equals(product_part.getPart_number())){
                    partcombo_selection = product_part;
                    break;
                }
            }
           
            if(partcombo_selection == null){
                part_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                partrev_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        partrev_combo.setOnAction((ActionEvent) -> {
            if(partcombo_selection == null){
                ActionEvent.consume();
                return;
            }
            partrevcombo_text = partrev_combo.getEditor().textProperty().getValue();
            partrevcombo_selection = null;
            for(PartRevision part_revision : partrev_combo.getItems()){
                if(partrevcombo_text.equals(part_revision.getRev())){
                    partrevcombo_selection = part_revision;
                    break;
                }
            }
            if(partrevcombo_selection == null){
                partrev_combo.getEditor().selectAll();
            }
            else{
                process_combo.getSelectionModel().select(msabase.getPartRevisionDAO().findSpecification(partrevcombo_selection).getProcess());
                quantity_field.requestFocus();
                ActionEvent.consume();
            }            
        });
        
        quantity_field.setOnAction((ActionEvent) -> {
            lot_add_button.fireEvent(new ActionEvent());
        });
        
        lot_add_button.setOnKeyPressed((KeyEvent ke) -> {
           if(ke.getCode().equals(KeyCode.ENTER)){
               lot_add_button.fireEvent(new ActionEvent());
           }
        });
        
        lot_add_button.setOnAction((ActionEvent) -> {
            if(!testLotFields()){
                return;
            }
            DepartLot depart_lot = new DepartLot();
            depart_lot.setLot_number(lotnumber_field.getText());
            depart_lot.setQuantity(Integer.parseInt(quantity_field.getText()));
            depart_lot.setBox_quantity(1);
            depart_lot.setProcess(process_combo.getSelectionModel().getSelectedItem());
            depart_lot.setComments("N/A");
            depart_lot.setTemp_partrevision(partrevcombo_selection);
            depart_lot.setTemp_productpart(partcombo_selection);
            depart_lot.setPending(true);
            depart_lots.add(depart_lot);
            clearFields();
            updateLotListview();
            lotnumber_field.requestFocus();
        });
    
        departlot_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartLot> observable, DepartLot oldValue, DepartLot newValue) -> {
            lot_delete_button.setDisable(departlot_tableview.getSelectionModel().isEmpty());
        });
        
        lot_delete_button.setOnAction((ActionEvent) -> {
            depart_lots.remove(departlot_tableview.getSelectionModel().getSelectedItem());
            updateLotListview();
        });
       
        save_button.setOnAction((ActionEvent) -> {
            if(!testSaveFields()){
                return;
            }
            saveDepartReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public boolean testSaveFields(){
        boolean b = true;
        clearStyle();
        if(depart_lots.isEmpty()){
            departlot_tableview.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(company_combo.getSelectionModel().isEmpty()){
            company_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(address_combo.getSelectionModel().isEmpty()){
            address_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        return b;
    }
    
    public void saveDepartReport(){
        DepartReport depart_report = new DepartReport();
        depart_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        msabase.getDepartReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), address_combo.getSelectionModel().getSelectedItem(), depart_report);
        saveDepartLots(depart_report);
    }
    
    public void saveDepartLots(DepartReport depart_report){
        for(DepartLot depart_lot : depart_lots){
            msabase.getDepartLotDAO().create(depart_report, depart_lot.getTemp_partrevision(), depart_lot);
        }
    }
    
    public void updatePartrev_combo(){
        try{
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection)));
        } catch(Exception e) {
            partrev_combo.getItems().clear();
        }
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
    public void updateLotListview(){
        departlot_tableview.setItems(FXCollections.observableArrayList(depart_lots));
        departlot_tableview.disableProperty().bind(Bindings.isEmpty(departlot_tableview.getItems()));
    }
    
    public void clearFields(){
        lotnumber_field.setText(null);
        part_combo.getSelectionModel().select(null);
        part_combo.getEditor().setText(null);
        partrev_combo.getSelectionModel().clearSelection();
        partrev_combo.getEditor().setText(null);
        quantity_field.setText(null);
    }
    
    public boolean testLotFields(){
        boolean b = true;
        clearStyle();
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(process_combo.getSelectionModel().isEmpty()){
            process_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            partcombo_selection.getId();
        }
        catch(Exception e){
            part_combo.setStyle("-fx-background-color: lightpink;");
            part_combo.getSelectionModel().select(null);
            part_combo.getEditor().setText(null);
            b = false;
        }
        try {
            partrevcombo_selection.getId();
        }
        catch(Exception e){
            partrev_combo.setStyle("-fx-background-color: lightpink;");
            partrev_combo.getSelectionModel().select(null);
            partrev_combo.getEditor().setText(null);
            b = false;
        }
        
        return b;
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        process_combo.setStyle(null);
        quantity_field.setStyle(null);
        part_combo.setStyle(null);
        partrev_combo.setStyle(null);
        address_combo.setStyle(null);
    }
}
