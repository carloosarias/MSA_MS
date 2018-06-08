/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Product;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductPartFX implements Initializable {
    @FXML
    private HBox root_hbox;
    @FXML
    private TextField id_field;
    @FXML
    private Button revision_button;
    @FXML
    private TextField partnumber_field;
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
        setFieldValues(ProductFX.getProduct());
    }
    
    public void setFieldValues(Product product){
        ProductPart part = msabase.getProductPartDAO().find(product);
        if(part != null){
            id_field.setText(""+part.getId());
            partnumber_field.setText(part.getPart_number());
        }else{
            id_field.clear();
            partnumber_field.clear();
        }
    }
    
    
}
