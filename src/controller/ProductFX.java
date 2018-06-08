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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Product;
import model.ProductType;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<ProductType> typefilter_combo;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private ListView<Product> product_listview;
    @FXML
    private Button add_button;
    @FXML
    private TextField id_button;
    @FXML
    private TextField name_field;
    @FXML
    private CheckBox active_check;
    @FXML
    private Button edit_button;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    ObservableList<ProductType> type_list = FXCollections.observableArrayList(
        msabase.getProductTypeDAO().listActive(true)
    );
    
    ObservableList<String> filter_list = FXCollections.observableArrayList(
            "Activos",
            "Inactivos"
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typefilter_combo.setItems(type_list);
        typefilter_combo.getSelectionModel().selectFirst();
        
        filter_combo.setItems(filter_list);
        filter_combo.getSelectionModel().selectFirst();
        
        updateList();
    }    
    
    public void updateList(){
        boolean active = true;
        product_listview.getItems().clear();
        switch(filter_combo.getSelectionModel().getSelectedItem()){
            case "Activos":
                active = true;
                break;
            case "Inactivos":
                active = false;
                break;
        }
        
        product_listview.setItems(FXCollections.observableArrayList(msabase.getProductDAO().listActive(typefilter_combo.getSelectionModel().getSelectedItem(), active)));
    }
}
