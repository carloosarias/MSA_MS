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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Employee;
import model.PartRevision;
import model.ProcessReport;
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
    private HBox root_hbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private ComboBox<PartRevision> revision_combo;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private TextArea comments_area;
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
        employee_combo.setItems(FXCollections.observableArrayList(MainApp.current_employee));
        partnumber_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        employee_combo.getSelectionModel().selectFirst();
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
            revisioncombo_selection = msabase.getPartRevisionDAO().find(partnumbercombo_selection, revisioncombo_text);
            if(revisioncombo_selection == null){
                revision_combo.getEditor().selectAll();
            }
            else{
                lotnumber_field.requestFocus();
                ActionEvent.consume();
            }            
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            saveScrapReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    public void saveScrapReport(){
        ScrapReport scrap_report = new ScrapReport();
        scrap_report.setReport_date(DAOUtil.toUtilDate(reportdate_picker.getValue()));
        scrap_report.setLot_number(lotnumber_field.getText());
        scrap_report.setQuantity(Integer.parseInt(quantity_field.getText()));
        scrap_report.setComments(comments_area.getText());
        
        msabase.getScrapReportDAO().create(
            employee_combo.getSelectionModel().getSelectedItem(),
            revisioncombo_selection,
            scrap_report);
    }
    
    public void clearStyle(){
        reportdate_picker.setStyle(null);
        lotnumber_field.setStyle(null);        
        quantity_field.setStyle(null);        
        partnumber_combo.setStyle(null);        
        revision_combo.setStyle(null);
        comments_area.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
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
        
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setText("n/a");
        }
        
        return b;
    }
    
    public void updatePartrev_combo(){
        try{
            revision_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partnumbercombo_selection, true)));
        } catch(Exception e) {
            revision_combo.getItems().clear();
        }
        revision_combo.setDisable(revision_combo.getItems().isEmpty());
    }    
    
}
