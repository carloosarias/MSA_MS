/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.CompanyContact;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ContactFX implements Initializable {

    @FXML
    private ListView<CompanyContact> contact_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextField position_field;
    @FXML
    private TextField phone_field;
    @FXML
    private TextField name_field;
    @FXML
    private TextField mail_field;
    @FXML
    private Button delete_button;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateList();
        
        contact_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CompanyContact> observable, CompanyContact oldValue, CompanyContact newValue) -> {
            setFieldValues(contact_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(contact_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
            }
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            setFieldValues(contact_listview.getSelectionModel().getSelectedItem());
            updateList();
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            /*if(!testFields()){
                return;
            }*/
            if(contact_listview.getSelectionModel().getSelectedItem() != null){
                msabase.getCompanyContactDAO().update(mapContact(contact_listview.getSelectionModel().getSelectedItem()));
            } else{
                msabase.getCompanyContactDAO().create(msabase.getCompanyDAO().find(CompanyFX.getCompanyId()),mapContact(new CompanyContact()));
            }
            updateList();
            disableFields(true);
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            msabase.getCompanyContactDAO().delete(mapContact(contact_listview.getSelectionModel().getSelectedItem()));
            updateList();
            disableFields(true);
        });
    }
    
    public CompanyContact mapContact(CompanyContact contact){
        contact.setName(name_field.getText());
        contact.setEmail(mail_field.getText());
        contact.setPhone_number(phone_field.getText());
        contact.setPosition(position_field.getText());
        return contact;
    }
    
    public void setFieldValues(CompanyContact contact){
        if(contact != null){
            id_field.setText(""+contact.getId());
            name_field.setText(contact.getName());
            mail_field.setText(contact.getEmail());
            phone_field.setText(contact.getPhone_number());
            position_field.setText(contact.getPosition());
        } else{
            id_field.clear();
            name_field.clear();
            mail_field.clear();
            phone_field.clear();
            position_field.clear();
        }
    }
    
    public void disableFields(boolean value){
        name_field.setDisable(value);
        mail_field.setDisable(value);
        phone_field.setDisable(value);
        position_field.setDisable(value);
        delete_button.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        add_button.setDisable(!value);
        edit_button.setDisable(!value);
    }
    
    public void updateList(){
        contact_listview.getItems().clear();
        contact_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyContactDAO().list(msabase.getCompanyDAO().find(CompanyFX.getCompanyId()))));
    }
}
