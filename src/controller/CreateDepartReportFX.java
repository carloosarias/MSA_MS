/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import model.Company;
import model.CompanyAddress;
import model.DepartLot;
import model.Employee;
import model.IncomingLot;
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
    
    private List<PartRevision> partrev_queue = new ArrayList<PartRevision>();
    private List<IncomingLot> incoming_lots = new ArrayList<IncomingLot>();
    
    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(msabase.getPartRevisionDAO().find(c.getValue().getPartRevision_index())).toString()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().find(c.getValue().getPartRevision_index()).getRev()));
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
            partcombo_selection = msabase.getProductPartDAO().find(partcombo_text);
            if(partcombo_selection == null){
                part_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                partrev_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        partrev_combo.setOnAction((ActionEvent) -> {
            
        });
        
    }
    
    public void updatePartrev_combo(){
        try{
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection)));
        } catch(Exception e) {
            partrev_combo.getItems().clear();
        }
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
}
