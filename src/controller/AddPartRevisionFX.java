/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
import model.Specification;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class AddPartRevisionFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker revdate_picker;
    @FXML
    private TextField rev_field;
    @FXML
    private ComboBox<ProductPart> productpart_combo;
    @FXML
    private ComboBox<Metal> metal_combo;
    @FXML
    private ComboBox<Specification> specification_combo;
    @FXML
    private TextField area_field;
    @FXML
    private TextField baseweight_field;
    @FXML
    private TextField finalweight_field;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productpart_combo.setItems(FXCollections.observableArrayList(ProductPartFX.getProductpart_selection()));
        metal_combo.setItems(FXCollections.observableArrayList(msabase.getMetalDAO().list()));
        specification_combo.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list()));
        revdate_picker.setValue(LocalDate.now());
        productpart_combo.getSelectionModel().selectFirst();
        area_field.setText("0.0");
        baseweight_field.setText("0.0");
        finalweight_field.setText("0.0");
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            
            savePartRevision();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
            
        });
    }
    
    public void savePartRevision(){
        PartRevision part_revision = new PartRevision();
        
        part_revision.setActive(true);
        part_revision.setRev_date(Date.valueOf(revdate_picker.getValue()));
        part_revision.setRev(rev_field.getText());
        part_revision.setArea(Double.parseDouble(area_field.getText()));
        part_revision.setBase_weight(Double.parseDouble(baseweight_field.getText()));
        part_revision.setFinal_weight(Double.parseDouble(finalweight_field.getText()));
        
        msabase.getPartRevisionDAO().create(productpart_combo.getSelectionModel().getSelectedItem(), specification_combo.getSelectionModel().getSelectedItem(), metal_combo.getSelectionModel().getSelectedItem(), part_revision);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(revdate_picker.getValue() == null){
            revdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(rev_field.getText().replace(" ", "").equals("")){
            rev_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(productpart_combo.getSelectionModel().isEmpty()){
            productpart_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(metal_combo.getSelectionModel().isEmpty()){
            metal_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(specification_combo.getSelectionModel().isEmpty()){
            specification_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        try{
            Double.parseDouble(area_field.getText());
        } catch(Exception e){
            area_field.setText("0.0");
            area_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(baseweight_field.getText());
        } catch(Exception e){
            baseweight_field.setText("0.0");
            baseweight_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(finalweight_field.getText());
        } catch(Exception e){
            finalweight_field.setText("0.0");
            finalweight_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        revdate_picker.setStyle(null);
        rev_field.setStyle(null);
        productpart_combo.setStyle(null);
        metal_combo.setStyle(null);
        specification_combo.setStyle(null);
        area_field.setStyle(null);
        baseweight_field.setStyle(null);
        finalweight_field.setStyle(null);
    }
}
