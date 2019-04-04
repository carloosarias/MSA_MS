/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Company;
import model.CompanyAddress;
import model.CompanyContact;
import model.ProcessReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CompanyFX implements Initializable {

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
    private TableColumn<Company, Boolean> supplier_column;
    @FXML
    private TableColumn<Company, Boolean> client_column;
    @FXML
    private Button addcompany_button;
    @FXML
    private ComboBox<String> companytype_combo;
    @FXML
    private Button deletecompany_button;
    @FXML
    private Tab contact_tab;
    @FXML
    private TableView<CompanyContact> contact_tableview;
    @FXML
    private TableColumn<CompanyContact, String> contactname_column;
    @FXML
    private TableColumn<CompanyContact, String> position_column;
    @FXML
    private TableColumn<CompanyContact, String> email_column;
    @FXML
    private TableColumn<CompanyContact, String> phonenumber_column;
    @FXML
    private Button addcontact_button;
    @FXML
    private Button deletecontact_button;
    @FXML
    private TextField company_field1;
    @FXML
    private Tab address_tab;
    @FXML
    private TableView<CompanyAddress> address_tableview;
    @FXML
    private TableColumn<CompanyAddress, String> address_column;
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
        
        deletecompany_button.disableProperty().bind(company_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        contact_tab.disableProperty().bind(company_tableview.getSelectionModel().selectedItemProperty().isNull());
        address_tab.disableProperty().bind(company_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        deleteaddress_button.disableProperty().bind(address_tableview.getSelectionModel().selectedItemProperty().isNull());
        deletecontact_button.disableProperty().bind(contact_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        setCompanyTable();
        updateCompanyTable();
        
        setContactTable();
        setAddressTable();
        
        company_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            try{
                updateContactTable();
                updateAddressTable();
                company_field1.setText(newValue.getName());
                company_field2.setText(newValue.getName());
            }catch(Exception e){
                company_field1.setText(null);
                company_field2.setText(null);
            }
            
        });

        
        companytype_combo.setOnAction(ActionEvent -> {
            updateCompanyTable();
        });
        
        addcompany_button.setOnAction((ActionEvent -> {
            createCompany();
            updateCompanyTable();
            company_tableview.getSelectionModel().selectLast();
        }));
        
        deletecompany_button.setOnAction((ActionEvent -> {
            disableCompany();
            updateCompanyTable();
        }));
        
        addcontact_button.setOnAction((ActionEvent -> {
            createContact(company_tableview.getSelectionModel().getSelectedItem());
            updateContactTable();
            contact_tableview.getSelectionModel().selectLast();
        }));
        
        addaddress_button.setOnAction((ActionEvent) -> {
           createAddress(company_tableview.getSelectionModel().getSelectedItem());
           updateAddressTable();
           address_tableview.getSelectionModel().selectLast();
        });
        
        deletecontact_button.setOnAction((ActionEvent) -> {
           disableContact();
           updateContactTable();
        });
        
        deleteaddress_button.setOnAction((ActionEvent) -> {
           disableAddress();
           updateAddressTable();
        });
    }
    
    public void disableAddress(){
        address_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getCompanyAddressDAO().update(address_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void disableContact(){
        contact_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getCompanyContactDAO().update(contact_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createAddress(Company company){
        CompanyAddress address = new CompanyAddress();
        address.setAddress("N/A");
        address.setActive(true);
        msabase.getCompanyAddressDAO().create(company, address);
    }
    
    public void createContact(Company company){
        CompanyContact contact = new CompanyContact();
        contact.setName("N/A");
        contact.setPosition("N/A");
        contact.setEmail("N/A");
        contact.setPhone_number("N/A");
        contact.setActive(true);
        msabase.getCompanyContactDAO().create(company, contact);
    }
    
    public void updateAddressTable(){
        try{
            address_tableview.getItems().setAll(msabase.getCompanyAddressDAO().listActive(company_tableview.getSelectionModel().getSelectedItem(), true));
        }catch(Exception e){
            address_tableview.getItems().clear();
        }
    }
    
    public void updateContactTable(){
        try{
            contact_tableview.getItems().setAll(msabase.getCompanyContactDAO().list(company_tableview.getSelectionModel().getSelectedItem(), true));
        }catch(Exception e){
            contact_tableview.getItems().clear();
        }
    }
    
    public void setContactTable(){
        contactname_column.setCellValueFactory(new PropertyValueFactory("name"));
        contactname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        contactname_column.setOnEditCommit((TableColumn.CellEditEvent<CompanyContact, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getCompanyContactDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        position_column.setCellValueFactory(new PropertyValueFactory("position"));
        position_column.setCellFactory(TextFieldTableCell.forTableColumn());
        position_column.setOnEditCommit((TableColumn.CellEditEvent<CompanyContact, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPosition(t.getNewValue());
            msabase.getCompanyContactDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        email_column.setCellValueFactory(new PropertyValueFactory("email"));
        email_column.setCellFactory(TextFieldTableCell.forTableColumn());
        email_column.setOnEditCommit((TableColumn.CellEditEvent<CompanyContact, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
            msabase.getCompanyContactDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        phonenumber_column.setCellValueFactory(new PropertyValueFactory("phone_number"));
        phonenumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        phonenumber_column.setOnEditCommit((TableColumn.CellEditEvent<CompanyContact, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone_number(t.getNewValue());
            msabase.getCompanyContactDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
    }
    
    public void setAddressTable(){
        address_column.setCellValueFactory(new PropertyValueFactory("address"));
        address_column.setCellFactory(TextFieldTableCell.forTableColumn());
        address_column.setOnEditCommit((TableColumn.CellEditEvent<CompanyAddress, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue());
            msabase.getCompanyAddressDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
    }
    
    public void disableCompany(){
        company_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getCompanyDAO().update(company_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void createCompany(){
        Company company = new Company();
        company.setName("N/A");
        company.setRfc("N/A");
        company.setTax_id("N/A");
        company.setPayment_terms("N/A");
        company.setActive(true);
        company.setClient(true);
        company.setSupplier(true);
        msabase.getCompanyDAO().create(company);
    }
    
    public void setCompanyTable(){
        companyname_column.setCellValueFactory(new PropertyValueFactory("name"));
        companyname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        companyname_column.setOnEditCommit((TableColumn.CellEditEvent<Company, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            msabase.getCompanyDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        rfc_column.setCellValueFactory(new PropertyValueFactory("rfc"));
        rfc_column.setCellFactory(TextFieldTableCell.forTableColumn());
        rfc_column.setOnEditCommit((TableColumn.CellEditEvent<Company, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRfc(t.getNewValue());
            msabase.getCompanyDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        taxid_column.setCellValueFactory(new PropertyValueFactory("tax_id"));
        taxid_column.setCellFactory(TextFieldTableCell.forTableColumn());
        taxid_column.setOnEditCommit((TableColumn.CellEditEvent<Company, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setTax_id(t.getNewValue());
            msabase.getCompanyDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        paymentterms_column.setCellValueFactory(new PropertyValueFactory("payment_terms"));
        paymentterms_column.setCellFactory(TextFieldTableCell.forTableColumn());
        paymentterms_column.setOnEditCommit((TableColumn.CellEditEvent<Company, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPayment_terms(t.getNewValue());
            msabase.getCompanyDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
        });
        
        supplier_column.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        supplier_column.setCellFactory(column -> new CheckBoxTableCell<>());
        supplier_column.setCellValueFactory(cellData -> {
            Company cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isSupplier());
            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> {
                cellValue.setSupplier(newValue);
                msabase.getCompanyDAO().update(cellValue);
            });
            return property;
        });
        
        client_column.setCellValueFactory(new PropertyValueFactory<>("client"));
        client_column.setCellFactory(column -> new CheckBoxTableCell<>());
        client_column.setCellValueFactory(cellData -> {
            Company cellValue = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(cellValue.isClient());
            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> {
                cellValue.setClient(newValue);
                msabase.getCompanyDAO().update(cellValue);
            });
            return property;
        });
        
    }
    
    public void updateCompanyTable(){
        company_tableview.getItems().clear();
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
            default:
                company_tableview.getItems().setAll(msabase.getCompanyDAO().listActive(true));
                break;
        }
       
    }
    
}
