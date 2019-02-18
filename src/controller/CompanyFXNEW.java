/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Company;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CompanyFXNEW implements Initializable {

    @FXML
    private TableView<Company> company_tableview;
    @FXML
    private TableColumn<Company, String> companyname_column;
    @FXML
    private TableColumn<Company, String> rfc_column;
    @FXML
    private TableColumn<Company, String> taxid_column;
    @FXML
    private TableColumn<Company, String> paymentterms_column;
    @FXML
    private TableColumn<Company, Boolean> client_column;
    @FXML
    private TableColumn<Company, Boolean> supplier_column;
    @FXML
    private Button addcompany_button;
    @FXML
    private ComboBox<String> companytype_combo;
    @FXML
    private Button deletecompany_button;
    @FXML
    private Tab contact_tab;
    @FXML
    private TableView<?> contact_tableview;
    @FXML
    private TableColumn<?, ?> contactname_column;
    @FXML
    private TableColumn<?, ?> position_column;
    @FXML
    private TableColumn<?, ?> email_column;
    @FXML
    private TableColumn<?, ?> phonenumber_column;
    @FXML
    private Button addcontact_button;
    @FXML
    private Button deletecontact_button;
    @FXML
    private TextField company_field1;
    @FXML
    private Tab address_tab;
    @FXML
    private TableView<?> address_tableview;
    @FXML
    private TableColumn<?, ?> address_column;
    @FXML
    private Button addaddress_button;
    @FXML
    private Button deleteaddress_button;
    @FXML
    private TextField company_field2;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Clientes & Proveedores",
        "Clientes",
        "Proveedores"
    );
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companytype_combo.setItems(filter_list);
        companytype_combo.getSelectionModel().selectFirst();
        setCompanyTable();
        updateCompanyTable();
    }
    
    public void setCompanyTable(){
        companyname_column.setCellFactory(value);
    }
    
    public void updateCompanyTable(){
        switch (companytype_combo.getSelectionModel().getSelectedIndex()){
            case 0: //Clientes y Proveedores
                company_tableview.getItems().setAll(msabase.getCompanyDAO().listActive(true));
                break;
            case 1: //Clientes
                company_tableview.getItems().setAll(msabase.getCompanyDAO().listClient(true));
                break;
            case 2: //Proveedores
                company_tableview.getItems().setAll(msabase.getCompanyDAO().listSupplier(true));
                break;
        }
       
    }
    
}
