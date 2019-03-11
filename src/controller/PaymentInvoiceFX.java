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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static msa_ms.MainApp.df;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PaymentInvoiceFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private DatePicker date_picker;
    @FXML
    private TextField checknumber_field;
    @FXML
    private TextField amountpaid_field;
    @FXML
    private TextArea comments_field;
    @FXML
    private Button save_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        try{
            date_picker.setValue(DAOUtil.toLocalDate(InvoiceFX.payment_invoice.getPayment_date()));
        }catch(Exception e){
            date_picker.setValue(LocalDate.now());
        }
        checknumber_field.setText(InvoiceFX.payment_invoice.getCheck_number());
        amountpaid_field.setText(df.format(InvoiceFX.payment_invoice.getQuantity_paid()));
        comments_field.setText(InvoiceFX.payment_invoice.getComments());
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }
            updateInvoice();
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
        });
        
    }   
    
    public void updateInvoice(){
        InvoiceFX.payment_invoice.setPayment_date(DAOUtil.toUtilDate(date_picker.getValue()));
        InvoiceFX.payment_invoice.setCheck_number(checknumber_field.getText());
        InvoiceFX.payment_invoice.setQuantity_paid(Double.parseDouble(amountpaid_field.getText()));
        InvoiceFX.payment_invoice.setComments(comments_field.getText());
        
        msabase.getInvoiceDAO().update(InvoiceFX.payment_invoice);
    }
    
    public void clearStyle(){
        date_picker.setStyle(null);
        checknumber_field.setStyle(null);
        amountpaid_field.setStyle(null);
        comments_field.setStyle(null);
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(date_picker.getValue() == null){
            date_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(checknumber_field.getText().replace(" ", "").equals("")){
            checknumber_field.setStyle("-fx-background-color: lightpink;");
            checknumber_field.setText("N/A");
            b = false;
        }
        try{
            Double.parseDouble(amountpaid_field.getText());
        } catch(Exception e){
            amountpaid_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }   
        if(comments_field.getText().replace(" ", "").equals("")){
            comments_field.setStyle("-fx-background-color: lightpink;");
            comments_field.setText("N/A");
            b = false;
        }        
        return b;
    }
    
}
