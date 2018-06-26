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
import model.Company;
import model.Employee;
import model.IncomingItem;
import model.IncomingLot;
import model.OrderPurchase;
import model.PartRevision;
import model.ProductPart;
import model.PurchaseItem;
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
    private ListView<IncomingLot> incominglot_listview;
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
        revision_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(c.getValue()).toString()));
        employee_combo.setDisable(true);
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        
        part_combo.setOnAction((ActionEvent) -> {
            List<PartRevision> list = msabase.getPartRevisionDAO().list(part_combo.getSelectionModel().getSelectedItem(), true);
            partrev_combo.setItems(FXCollections.observableArrayList(list));
            partrev_combo.getItems().removeAll(part_revisions);
        });
        
        item_add_button.setOnAction((ActionEvent) -> {
            part_revisions.add(partrev_combo.getSelectionModel().getSelectedItem());
            partrev_combo.getItems().remove(partrev_combo.getSelectionModel().getSelectedItem());
            partrevision_tableview.setItems(FXCollections.observableArrayList(part_revisions));
        });
        
        partrevision_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PartRevision> observable, PartRevision oldValue, PartRevision newValue) -> {
            List<IncomingLot> incoming_lots_filtered = new ArrayList<IncomingLot>();
            for(IncomingLot incoming_lot : incoming_lots){
                if(incoming_lot.getPartRevision_index().equals(partrevision_tableview.getSelectionModel().getSelectedItem().getId())){
                    incoming_lots_filtered.add(incoming_lot);
                }
            }
            incominglot_listview.setItems(FXCollections.observableArrayList(incoming_lots_filtered));
        });
        
        
        lot_add_button.setOnAction();
        
    }    
    
}
