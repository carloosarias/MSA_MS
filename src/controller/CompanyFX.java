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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Company;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CompanyFX implements Initializable {

    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<Company> comp_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextField rfc_field;
    @FXML
    private TextArea payterm_field;
    @FXML
    private TextField name_field;
    @FXML
    private TextField tax_field;
    @FXML
    private CheckBox supplier_check;
    @FXML
    private CheckBox client_check;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button contact_button;
    @FXML
    private Button address_button;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Compañías Activas",
        "Compañías Inactivas",
        "Compañías Clientes",
        "Compañías Proveedoras"
    );
    
    DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        updateList();
    }
    
    public void updateList(){
        comp_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Compañías Activas":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listActive(true)));
                break;
            case "Compañías Inactivas":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listActive(false)));
                break;
            case "Clientes":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
                break;
            case "Proveedores":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listSupplier(true)));
                break;
        }
    }
    
}
