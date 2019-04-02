/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.CreateDepartReportFX.departlot_queue;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import model.DepartLot;
import model.PartRevision;
import model.ProductPart;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class AddDepartLotFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> rev_combo;
    @FXML
    private ComboBox<String> process_combo;
    @FXML
    private TextField ponumber_field;
    @FXML
    private TextField linenumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private Button save_button;
    
    private ProductPart partcombo_selection;
    private String partcombo_text;
    
    private PartRevision partrevcombo_selection;
    private String partrevcombo_text;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        process_combo.setItems(FXCollections.observableArrayList(MainApp.process_list));
        
        rev_combo.disableProperty().bind(rev_combo.itemsProperty().isNull());
        
        lotnumber_field.setOnAction((ActionEvent) -> {
            part_combo.requestFocus();
        });
        
        part_combo.setOnAction((ActionEvent) -> {
            partcombo_text = part_combo.getEditor().textProperty().getValue().replace(" ", "").toUpperCase();
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
                updateRev_combo();
                rev_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        rev_combo.setOnAction((ActionEvent) -> {
            if(partcombo_selection == null){
                ActionEvent.consume();
                return;
            }
            partrevcombo_text = rev_combo.getEditor().textProperty().getValue().replace(" ", "").toUpperCase();
            partrevcombo_selection = null;
            for(PartRevision part_revision : rev_combo.getItems()){
                if(partrevcombo_text.equals(part_revision.getRev())){
                    partrevcombo_selection = part_revision;
                    break;
                }
            }
            if(partrevcombo_selection == null){
                rev_combo.getEditor().selectAll();
            }
            else{
                process_combo.getSelectionModel().select(partrevcombo_selection.getSpecification_process());
                quantity_field.requestFocus();
            }            
        });
        
        quantity_field.setOnAction((ActionEvent) -> {
            ponumber_field.requestFocus();
        });
        
        ponumber_field.setOnAction((ActionEvent) -> {
            linenumber_field.requestFocus();
        });
        
        linenumber_field.setOnAction((ActionEvent) -> {
            save_button.fireEvent(new ActionEvent());
        });
        

        
        save_button.setOnKeyPressed((KeyEvent ke) -> {
           if(ke.getCode().equals(KeyCode.ENTER)){
               save_button.fireEvent(new ActionEvent());
           }
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testLotFields()){
                return;
            }
            DepartLot depart_lot = new DepartLot();
            depart_lot.setLot_number(lotnumber_field.getText().replace(" ", "").toUpperCase());
            depart_lot.setProcess(process_combo.getSelectionModel().getSelectedItem());
            depart_lot.setPo_number(ponumber_field.getText().replace(" ", "").toUpperCase());
            depart_lot.setLine_number(linenumber_field.getText().replace(" ", "").toUpperCase());
            depart_lot.setQuantity(Integer.parseInt(quantity_field.getText()));
            depart_lot.setBox_quantity(1);
            depart_lot.setComments("N/A");
            depart_lot.setTemp_partrevision(partrevcombo_selection);
            depart_lot.setTemp_productpart(partcombo_selection);
            depart_lot.setPending(true);
            departlot_queue.add(depart_lot);
            clearFields();
            lotnumber_field.requestFocus();
        });
    }
    
    public void updateRev_combo(){
        try{
            rev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection, true)));
        } catch(Exception e) {
            rev_combo.getItems().clear();
        }
    }
    
        public void clearFields(){
        lotnumber_field.setText(null);
        part_combo.getSelectionModel().clearSelection();
        part_combo.getEditor().setText(null);
        process_combo.getSelectionModel().clearSelection();
        ponumber_field.setText(null);
        linenumber_field.setText(null);
        rev_combo.getSelectionModel().clearSelection();
        rev_combo.getEditor().setText(null);
        quantity_field.setText(null);
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
        if(ponumber_field.getText().replace(" ", "").equals("")){
            ponumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(linenumber_field.getText().replace(" ", "").equals("")){
            linenumber_field.setStyle("-fx-background-color: lightpink;");
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
            rev_combo.setStyle("-fx-background-color: lightpink;");
            rev_combo.getSelectionModel().select(null);
            rev_combo.getEditor().setText(null);
            b = false;
        }
        
        return b;
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        process_combo.setStyle(null);
        ponumber_field.setStyle(null);
        linenumber_field.setStyle(null);
        quantity_field.setStyle(null);
        part_combo.setStyle(null);
        rev_combo.setStyle(null);
    }
    
}
