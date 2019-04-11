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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.PartRevision;
import model.ProductPart;
import model.ScrapReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateScrapReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private ComboBox<PartRevision> revision_combo;
    @FXML
    private Button save_button;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ProductPart partnumbercombo_selection;
    private String partnumbercombo_text;
    
    private PartRevision revisioncombo_selection;
    private String revisioncombo_text;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        reportdate_picker.setValue(LocalDate.now());
        setDatePicker(reportdate_picker);
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            partnumbercombo_text = partnumber_combo.getEditor().textProperty().getValue();
            partnumbercombo_selection = msabase.getProductPartDAO().find(partnumbercombo_text);
            if(partnumbercombo_selection == null){
                partnumber_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                revision_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        revision_combo.setOnAction((ActionEvent) -> {
            if(partnumbercombo_selection == null){
                ActionEvent.consume();
                return;
            }
            revisioncombo_text = revision_combo.getEditor().textProperty().getValue();
            revisioncombo_selection = msabase.getPartRevisionDAO().find(null, partnumbercombo_selection.getPart_number(), revisioncombo_text);
            if(revisioncombo_selection == null){
                revision_combo.getEditor().selectAll();
            }
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveScrapReport();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
    }
    public void saveScrapReport(){
        ScrapReport scrap_report = new ScrapReport();
        scrap_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        scrap_report.setQuantity(0);
        scrap_report.setComments("N/A");
        
        msabase.getScrapReportDAO().create(MainApp.current_employee, revisioncombo_selection, scrap_report);
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);      
        partnumber_combo.setStyle(null);        
        revision_combo.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            partnumbercombo_selection.getId();
        }
        catch(Exception e){
            partnumber_combo.setStyle("-fx-background-color: lightpink;");
            partnumber_combo.getSelectionModel().select(null);
            partnumber_combo.getEditor().setText(null);
            b = false;
        }
        
        try {
            revisioncombo_selection.getId();
        }
        catch(Exception e){
            revision_combo.setStyle("-fx-background-color: lightpink;");
            revision_combo.getSelectionModel().select(null);
            revision_combo.getEditor().setText(null);
            b = false;
        }
        
        return b;
    }
    
    public void updatePartrev_combo(){
        try{
            revision_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(null, null, null, partnumbercombo_selection.getPart_number())));
        } catch(Exception e) {
            revision_combo.getItems().clear();
        }
        revision_combo.setDisable(revision_combo.getItems().isEmpty());
    }    
    
}
