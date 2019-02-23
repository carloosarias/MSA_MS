/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
import model.Specification;
import static msa_ms.MainApp.dateFormat;
import static msa_ms.MainApp.setDatePicker;

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
    private ComboBox<ProductPart> productpart_combo;
    @FXML
    private ComboBox<Metal> metal_combo;
    @FXML
    private ComboBox<Specification> specification_combo;
    @FXML
    private Button save_button;
    
    public static PartRevision part_revision;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productpart_combo.getItems().setAll(msabase.getProductPartDAO().listActive(true));
        metal_combo.getItems().setAll(msabase.getMetalDAO().list(true));
        specification_combo.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list(true)));
        revdate_picker.setValue(LocalDate.now());
        setDatePicker(revdate_picker);
        
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
        part_revision = new PartRevision();
        part_revision.setRev_date(DAOUtil.toUtilDate(revdate_picker.getValue()));
        part_revision.setRev("N/A");
        part_revision.setArea(0.0);
        part_revision.setBase_weight(0.0);
        part_revision.setFinal_weight(0.0);
        part_revision.setActive(true);
        
        msabase.getPartRevisionDAO().create(productpart_combo.getSelectionModel().getSelectedItem(), specification_combo.getSelectionModel().getSelectedItem(), metal_combo.getSelectionModel().getSelectedItem(), part_revision);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(revdate_picker.getValue() == null){
            revdate_picker.setStyle("-fx-background-color: lightpink;");
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
        return b;
    }
    
    public void clearStyle(){
        revdate_picker.setStyle(null);
        productpart_combo.setStyle(null);
        metal_combo.setStyle(null);
        specification_combo.setStyle(null);
    }
}
