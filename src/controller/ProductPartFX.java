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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    
    
    private Stage detailsStage;
    
    private Product product;
    
    private static ProductPart part;
    
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        product = ProductFX.getProduct();
        System.out.println(product);
        setFieldValues();
        cancel_button.setOnAction((ActionEvent) -> {
            setFieldValues();
            disableFields(true);
        });
        
        revision_button.setOnAction((ActionEvent) -> {
            revision_button.setDisable(true);
            showRevision();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            System.out.println(msabase.getProductPartDAO().find(product));
            if(msabase.getProductPartDAO().find(product) != null){
                msabase.getProductPartDAO().update(mapProductPart(msabase.getProductPartDAO().find(product)));
            } else{
                msabase.getProductPartDAO().create(product ,mapProductPart(new ProductPart()));
            }
            
            setFieldValues();
            disableFields(true);
        });
        
        edit_button.setOnAction((ActionEvent) -> {
            disableFields(false);
        });
    }
    
    public void disableFields(boolean value){
        cancel_button.setDisable(value);
        save_button.setDisable(value);
        partnumber_field.setDisable(value);
        edit_button.setDisable(!value);
    }

    public boolean testFields(){
        boolean b = true;
        if(partnumber_field.getText().replace(" ", "").equals("")){
            partnumber_field.setStyle("-fx-border-color: red ;");
            b = false;
        } else{
            partnumber_field.setStyle(null);
        }
        return b;
    }
    
    public void clearStyle(){
        partnumber_field.setStyle(null);
    }
    
    public ProductPart mapProductPart(ProductPart part){
        part.setPart_number(partnumber_field.getText());
        return part;
    }
    
    public void setFieldValues(){
        part = msabase.getProductPartDAO().find(product);
        if(part != null){
            id_field.setText(""+part.getId());
            partnumber_field.setText(part.getPart_number());
        }else{
            id_field.clear();
            partnumber_field.clear();
        }
        clearStyle();
    }
    
    public void showRevision(){
        try {
            detailsStage = new Stage();
            detailsStage.initOwner((Stage) root_hbox.getScene().getWindow());
            
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/PartRevisionFX.fxml"));
            Scene scene = new Scene(root);
            
            detailsStage.setTitle("Detalles de Revisión");
            detailsStage.setResizable(false);
            detailsStage.initStyle(StageStyle.UTILITY);
            detailsStage.setScene(scene);
            detailsStage.showAndWait();
            revision_button.setDisable(false);        
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ProductPart getPart(){
        return part;
    }
}
