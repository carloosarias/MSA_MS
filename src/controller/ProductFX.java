/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.ProductType;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {

    @FXML
    private TabPane root_tabpane;
    @FXML
    private Tab part_tab;

    private List<ProductType> product_types;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        product_types = msabase.getProductTypeDAO().listActive(true);
        
        for(ProductType product_type : product_types){
            switch(product_type.getName()){
                default:
                    part_tab.setDisable(true);
                    break;
                case "Partes":
                    part_tab.setDisable(false);
                    try {
                        part_tab.setContent((BorderPane) FXMLLoader.load(getClass().getResource("/fxml/HrFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
    }    
    
}
