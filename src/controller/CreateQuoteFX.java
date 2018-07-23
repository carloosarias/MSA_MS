/*ny
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.CompanyContact;
import model.PartRevision;
import model.ProductPart;
import model.Quote;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateQuoteFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private ComboBox<CompanyContact> contact_combo;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private TextField unitprice_field;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       client_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
       part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
       client_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
           setCompanyContactItems(newValue);
       });
       part_combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
           setPartRevisionItems(newValue);
       });
       
       save_button.setOnAction((ActionEvent) -> {
           if(!testFields()){
               return;
           }else{
               Quote quote = new Quote();
               quote.setQuote_date(java.sql.Date.valueOf(LocalDate.now()));
               quote.setUnit_price(Double.parseDouble(unitprice_field.getText()));
               quote.setComments(comments_area.getText());
               quote.setApproved("Pendiente");
               msabase.getQuoteDAO().create(partrev_combo.getSelectionModel().getSelectedItem(), contact_combo.getSelectionModel().getSelectedItem(), quote);
               Stage stage = (Stage) root_hbox.getScene().getWindow();
               stage.close();
           }
       });  
    }

    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(partrev_combo.getSelectionModel().isEmpty()){
            partrev_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(contact_combo.getSelectionModel().isEmpty()){
            contact_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(unitprice_field.getText());
        } catch(Exception e){
            unitprice_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setText("n/a");
        }
        return b;
    }
    
    public void clearStyle(){
        partrev_combo.setStyle(null);
        contact_combo.setStyle(null);
        unitprice_field.setStyle(null);
        comments_area.setStyle(null);
    }
    
    public void setPartRevisionItems(ProductPart product_part){
        partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part)));
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    public void setCompanyContactItems(Company company){
        contact_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyContactDAO().list(company)));
        contact_combo.setDisable(contact_combo.getItems().isEmpty());
    }
}
