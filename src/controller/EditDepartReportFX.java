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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.DepartLot;
import model.DepartReport;
import model.PartRevision;
import model.ProductPart;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class EditDepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private Button save_button;
    
    public static DepartReport departreport_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ProductPart partcombo_selection;
    private String partcombo_text;
    
    private PartRevision partrevcombo_selection;
    private String partrevcombo_text;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        
        lotnumber_field.setOnAction((ActionEvent) -> {
            part_combo.requestFocus();
            ActionEvent.consume();
        });
        
        part_combo.setOnAction((ActionEvent) -> {
            partcombo_text = part_combo.getEditor().textProperty().getValue();
            partcombo_selection = null;
            for(ProductPart product_part : part_combo.getItems()){
                if(partcombo_text.equals(product_part.getPart_number())){
                    partcombo_selection = product_part;
                    break;
                }
            }
           
            if(partcombo_selection == null){
                part_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                partrev_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        partrev_combo.setOnAction((ActionEvent) -> {
            if(partcombo_selection == null){
                ActionEvent.consume();
                return;
            }
            partrevcombo_text = partrev_combo.getEditor().textProperty().getValue();
            partrevcombo_selection = null;
            for(PartRevision part_revision : partrev_combo.getItems()){
                if(partrevcombo_text.equals(part_revision.getRev())){
                    partrevcombo_selection = part_revision;
                    break;
                }
            }
            if(partrevcombo_selection == null){
                partrev_combo.getEditor().selectAll();
            }
            else{
                process_combo.getSelectionModel().select(partrevcombo_selection.getSpecification().getProcess());
                quantity_field.requestFocus();
                ActionEvent.consume();
            }            
        });
        
        quantity_field.setOnAction((ActionEvent) -> {
            save_button.requestFocus();
            ActionEvent.consume();
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testLotFields()){
                return;
            }
            DepartLot depart_lot = new DepartLot();
            depart_lot.setLot_number(lotnumber_field.getText().toUpperCase());
            depart_lot.setQuantity(Integer.parseInt(quantity_field.getText()));
            depart_lot.setBox_quantity(1);
            depart_lot.setProcess(process_combo.getSelectionModel().getSelectedItem());
            depart_lot.setPo_number("N/A");
            depart_lot.setLine_number(("N/A"));
            depart_lot.setComments("N/A");
            depart_lot.setTemp_partrevision(partrevcombo_selection);
            depart_lot.setTemp_productpart(partcombo_selection);
            depart_lot.setPending(true);
            msabase.getDepartLotDAO().create(departreport_selection, partrevcombo_selection, depart_lot);
            
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        process_combo.setStyle(null);
        quantity_field.setStyle(null);
        part_combo.setStyle(null);
        partrev_combo.setStyle(null);
    }
    
    public boolean testLotFields(){
        boolean b = true;
        clearStyle();
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(process_combo.getSelectionModel().isEmpty()){
            process_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            partcombo_selection.getId();
        }
        catch(Exception e){
            part_combo.setStyle("-fx-background-color: lightpink;");
            part_combo.getSelectionModel().select(null);
            part_combo.getEditor().setText(null);
            b = false;
        }
        try {
            partrevcombo_selection.getId();
        }
        catch(Exception e){
            partrev_combo.setStyle("-fx-background-color: lightpink;");
            partrev_combo.getSelectionModel().select(null);
            partrev_combo.getEditor().setText(null);
            b = false;
        }
        
        return b;
    }
    
    public void updatePartrev_combo(){
        try{
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection, true)));
        } catch(Exception e) {
            partrev_combo.getItems().clear();
        }
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
}
