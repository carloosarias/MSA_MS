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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.CompanyAddress;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddressFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<CompanyAddress> address_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_field;
    @FXML
    private TextArea address_field;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    @FXML
    private Button edit_button;
    
    private ObservableList<String> filter_list = FXCollections.observableArrayList(
        "Direcciones Activas",
        "Direcciones Inactivas"
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
        
        address_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CompanyAddress> observable, CompanyAddress oldValue, CompanyAddress newValue) -> {
            setFieldValues(address_listview.getSelectionModel().getSelectedItem());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            setFieldValues(null);
            disableFields(false);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            if(address_listview.getSelectionModel().getSelectedItem() != null){
                disableFields(false);
            }
        });
        
        cancel_button.setOnAction((ActionEvent) -> {
            updateList();
            setFieldValues(address_listview.getSelectionModel().getSelectedItem());
            disableFields(true);
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            if(id_field.getText().replace(" ", "").equals("")){
                msabase.getCompanyAddressDAO().create(msabase.getCompanyDAO().find(CompanyFX.getCompanyId()),mapAddress(new CompanyAddress()));
            } else{
                msabase.getCompanyAddressDAO().update(mapAddress(address_listview.getSelectionModel().getSelectedItem()));
            }
            updateList();
            disableFields(true);
        });
        
    }
    
    public CompanyAddress mapAddress(CompanyAddress address){
        address.setAddress(address_field.getText());
        address.setActive(!active_check.isSelected());
        return address;
    }
    
    public void disableFields(boolean value){
        active_check.setDisable(value);
        address_field.setDisable(value);
        save_button.setDisable(value);
        cancel_button.setDisable(value);
        add_button.setDisable(!value);
        edit_button.setDisable(!value);
        filter_combo.setDisable(!value);
        address_listview.setDisable(!value);
    }
    
    public void setFieldValues(CompanyAddress address){
        if(address != null){
            id_field.setText(""+address.getId());
            address_field.setText(address.getAddress());
            active_check.setSelected(!address.isActive());
        } else{
            id_field.clear();
            address_field.clear();
            active_check.setSelected(false);   
        }
        clearStyle();
    }
    
    public void clearStyle(){
        address_field.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        if(address_field.getText().replace(" ", "").equals("")){
            address_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        } else{
            address_field.setStyle(null);
        }
        return b;
    }
    
    public void updateList(){
        address_listview.getItems().clear();
        switch (filter_combo.getSelectionModel().getSelectedItem()){
            case "Direcciones Activas":
                address_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(msabase.getCompanyDAO().find(CompanyFX.getCompanyId()), true)));
                break;
            case "Direcciones Inactivas":
                address_listview.setItems(FXCollections.observableArrayList(msabase.getCompanyAddressDAO().listActive(msabase.getCompanyDAO().find(CompanyFX.getCompanyId()), false)));
                break;
        }
    }
}
