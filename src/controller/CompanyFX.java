/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CompanyFX implements Initializable {
    @FXML
    private HBox root_hbox;
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
    
    private Stage addressStage = new Stage();
    private Stage contactStage = new Stage();
    
    private static Integer company_id;
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Compañías Activas",
        "Compañías Inactivas",
        "Compañías Clientes",
        "Compañías Proveedoras"
    );
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        updateList();
        
        
        
        filter_combo.setOnAction((ActionEvent) -> {
            updateList();
        });    
        
        comp_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            setFieldValues(comp_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            filter_combo.getOnAction();
            setFieldValues(comp_listview.getSelectionModel().getSelectedItem());
            closeOther();
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            if(comp_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getCompanyDAO().update(mapCompany(comp_listview.getSelectionModel().getSelectedItem()));
            } else{
                msabase.getCompanyDAO().create(mapCompany(new Company()));
            }
            setFieldValues(comp_listview.getSelectionModel().getSelectedItem());
            updateList();
            closeOther();
            disableFields(true);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(comp_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
                contact_button.setDisable(false);
                address_button.setDisable(false);
            }
        });
        
        address_button.setOnAction((ActionEvent) -> {
            address_button.setDisable(true);
            showAddress();
        });
        
        contact_button.setOnAction((ActionEvent) -> {
            contact_button.setDisable(true);
            showContact();
        });
    }
    
    public void closeOther(){
        if(addressStage != null){
            addressStage.close();
        }
        if(contactStage != null){
            contactStage.close();
        }
    }
    
    public void showAddress(){
        try {
            addressStage = new Stage();
            addressStage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddressFX.fxml"));
            Scene scene = new Scene(root);
            
            addressStage.setTitle("Opciones");
            addressStage.setResizable(false);
            addressStage.initStyle(StageStyle.UTILITY);
            addressStage.setScene(scene);
            addressStage.showAndWait();
            address_button.setDisable(!edit_button.isDisabled());
        } catch (IOException ex) {
            Logger.getLogger(LoginFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showContact(){
        try {
            contactStage = new Stage();
            contactStage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/ContactFX.fxml"));
            Scene scene = new Scene(root);
            
            contactStage.setTitle("Opciones");
            contactStage.setResizable(false);
            contactStage.initStyle(StageStyle.UTILITY);
            contactStage.setScene(scene);
            contactStage.showAndWait();
            contact_button.setDisable(!edit_button.isDisabled());
        } catch (IOException ex) {
            Logger.getLogger(LoginFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Company mapCompany(Company company){
        
        company.setName(name_field.getText());
        company.setRfc(rfc_field.getText());
        company.setTax_id(tax_field.getText());
        company.setPayment_terms(payterm_field.getText());
        company.setSupplier(supplier_check.isSelected());
        company.setClient(client_check.isSelected());
        company.setActive(!active_check.isSelected());
        
        return company;
    }
    
    public boolean testFields(){
        boolean b = true;
        if(name_field.getText().replace(" ", "").equals("")){
            name_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            name_field.setStyle(null);
        }
        if(rfc_field.getText().replace(" ", "").equals("")){
            rfc_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            rfc_field.setStyle(null);
        }
        if(tax_field.getText().replace(" ", "").equals("")){
            tax_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            tax_field.setStyle(null);
        }
        if(payterm_field.getText().replace(" ", "").equals("")){
            payterm_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            payterm_field.setStyle(null);
        }
        return b;
    }
    
    public void disableFields(boolean value){
        name_field.setDisable(value);
        rfc_field.setDisable(value);
        tax_field.setDisable(value);
        payterm_field.setDisable(value);
        supplier_check.setDisable(value);
        client_check.setDisable(value);
        active_check.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        add_button.setDisable(!value);
        edit_button.setDisable(!value);
        filter_combo.setDisable(!value);
        comp_listview.setDisable(!value);
        address_button.setDisable(!edit_button.isDisabled());
        contact_button.setDisable(!edit_button.isDisabled());
    }
    
    
    public void setFieldValues(Company company){
        if(company != null){
            company_id = company.getId();
            id_field.setText(""+company.getId());
            name_field.setText(company.getName());
            rfc_field.setText(company.getRfc());
            tax_field.setText(company.getTax_id());
            payterm_field.setText(company.getPayment_terms());
            supplier_check.setSelected(company.isSupplier());
            client_check.setSelected(company.isClient());
            active_check.setSelected(!company.isActive());
        } else{
            company_id = null;
            id_field.clear();
            name_field.clear();
            rfc_field.clear();
            tax_field.clear();
            payterm_field.clear();
            supplier_check.setSelected(false);
            client_check.setSelected(false);   
            active_check.setSelected(false);   
        }
        clearStyle();
    }
    
    public void clearStyle(){
        name_field.setStyle(null);
        rfc_field.setStyle(null);
        tax_field.setStyle(null);
        payterm_field.setStyle(null);
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
            case "Compañías Clientes":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
                break;
            case "Compañías Proveedoras":
                comp_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listSupplier(true)));
                break;
        }
       
    }
    
    public static Integer getCompanyId(){
        return company_id;
    }
    
}
