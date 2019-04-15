/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.DepartReport;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateDepartReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private ComboBox<CompanyAddress> address_combo;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column;
    @FXML
    private TableColumn<DepartLot, String> rev_column;
    @FXML
    private TableColumn<DepartLot, String> process_column;
    @FXML
    private TableColumn<DepartLot, String> ponumber_column;
    @FXML
    private TableColumn<DepartLot, String> linenumber_column;
    @FXML
    private TableColumn<DepartLot, String> quantity_column;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    @FXML
    private Button save_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    public static ObservableList<DepartLot> departlot_queue = FXCollections.observableArrayList(new ArrayList<DepartLot>());
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        departlot_queue.clear();
        setDepartLotTable();
        updateDepartLotTable();
        
        departlot_tableview.disableProperty().bind(Bindings.size(departlot_queue).isEqualTo(0));
        disable_button.disableProperty().bind(departlot_tableview.getSelectionModel().selectedItemProperty().isNull());
        address_combo.disableProperty().bind(company_combo.getSelectionModel().selectedItemProperty().isNull());
        save_button.disableProperty().bind(Bindings.size(departlot_queue).isEqualTo(0));
        
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit(
            (TableColumn.CellEditEvent<DepartLot, String> t) ->
                (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setComments(t.getNewValue())
        );
        
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        reportdate_picker.setValue(LocalDate.now());
        setDatePicker(reportdate_picker);
        
        company_combo.setOnAction((ActionEvent) -> {
            address_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(company_combo.getSelectionModel().getSelectedItem(), true)));
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            departlot_queue.remove(departlot_tableview.getSelectionModel().getSelectedItem());
            updateDepartLotTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
           showAdd_stage();
           updateDepartLotTable();
        });
       
        save_button.setOnAction((ActionEvent) -> {
            if(!testSaveFields()){
                return;
            }
            saveDepartReport();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/AddDepartLotFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Agregar caja a Remisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setDepartLotTable(){
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getProduct_part().getPart_number().toString()));
        rev_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getRev()));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        ponumber_column.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        linenumber_column.setCellValueFactory(new PropertyValueFactory<>("line_number"));  
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public boolean testSaveFields(){
        boolean b = true;
        clearStyle();
        if(departlot_queue.isEmpty()){
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
        //msabase.getDepartReportDAO().create(MainApp.current_employee, company_combo.getSelectionModel().getSelectedItem(), address_combo.getSelectionModel().getSelectedItem(), depart_report);
        saveDepartLots(depart_report);
    }
    
    public void saveDepartLots(DepartReport depart_report){
        for(DepartLot depart_lot : departlot_queue){
            msabase.getDepartLotDAO().create(depart_report, depart_lot.getPart_revision(), depart_lot);
        }
    }
    
    public void updateDepartLotTable(){
        departlot_tableview.setItems(departlot_queue);
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        address_combo.setStyle(null);
    }
}
