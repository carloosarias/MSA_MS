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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Company;
import model.Employee;
import model.IncomingItem;
import model.IncomingLot;
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
    private ListView<PartRevision> incomingitem_listview;
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

    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee_combo.setDisable(true);
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        
        part_combo.setOnAction((ActionEvent) -> {
            List<PartRevision> list = msabase.getPartRevisionDAO().list(part_combo.getSelectionModel().getSelectedItem(), true);
            list.removeAll(part_revisions);
            partrev_combo.setItems(FXCollections.observableArrayList(list));
        });
        
    }    
    
}
