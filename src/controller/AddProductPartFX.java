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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddProductPartFX implements Initializable {
    
    @FXML
    private HBox root_hbox;
    @FXML
    private TextField partnumber_field;
    @FXML
    private TextArea description_field;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveProductPart();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(partnumber_field.getText().replace(" ", "").equals("")){
            partnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(description_field.getText().replace(" ", "").equals("")){
            description_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        partnumber_field.setStyle(null);
        description_field.setStyle(null);
    }
    
    public void saveProductPart(){
        ProductPart item = new ProductPart();
        item.setPart_number(partnumber_field.getText());
        item.setDescription(description_field.getText());
        item.setActive(true);
        msabase.getProductPartDAO().create(item);
    }
}
