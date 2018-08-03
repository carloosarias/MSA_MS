/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Module;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MainFX implements Initializable {

    @FXML
    private BorderPane root_pane;
    @FXML
    private Tab employee_tab;
    @FXML
    private Tab company_tab;
    @FXML
    private Tab product_tab;
    @FXML
    private Tab orderpurchase_tab;
    @FXML
    private Tab incoming_tab;
    @FXML
    private Tab depart_tab;
    @FXML
    private Tab invoice_tab;
    @FXML
    private Tab quote_tab;
    @FXML
    private Tab invoice_payment_tab;
    @FXML
    private Tab container_tab;
    @FXML
    private Tab process_tab;
    @FXML
    private Tab transaction_history_tab;
    @FXML
    private MenuItem logout;
    
    private List<Module> modules;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modules = msabase.getModuleEmployeeDAO().list(msabase.getEmployeeDAO().find(MainApp.employee_id));
        
        for(Module module : modules){
            switch(module.getName()){
                default:
                    employee_tab.setDisable(true);
                    company_tab.setDisable(true);
                    product_tab.setDisable(true);
                    orderpurchase_tab.setDisable(true);
                    incoming_tab.setDisable(true);
                    depart_tab.setDisable(true);
                    quote_tab.setDisable(true);
                    invoice_payment_tab.setDisable(true);
                    container_tab.setDisable(true);
                    process_tab.setDisable(true);
                    break;
                case "Recursos Humanos":
                    employee_tab.setDisable(false);
                    try {
                        employee_tab.setContent((BorderPane) FXMLLoader.load(getClass().getResource("/fxml/HrFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Compras":
                    company_tab.setDisable(false);
                    product_tab.setDisable(false);
                    container_tab.setDisable(false);
                    orderpurchase_tab.setDisable(false);
                    try {
                        company_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/CompanyFX.fxml")));
                        product_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProductFX.fxml")));
                        container_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ContainerFX.fxml")));
                        orderpurchase_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Reciba":
                    incoming_tab.setDisable(false);
                    depart_tab.setDisable(false);
                    try {
                        incoming_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/IncomingReportFX.fxml")));
                        depart_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/DepartReportFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Facturación":
                    invoice_tab.setDisable(false);
                    invoice_payment_tab.setDisable(false);
                    try {
                        invoice_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoiceFX.fxml")));
                        invoice_payment_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoicePaymentReportFX.fxml")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Cotización":
                    quote_tab.setDisable(false);
                    try{
                        quote_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/QuoteFX.fxml")));
                    } catch(IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Producción":
                    process_tab.setDisable(false);
                    try{
                        process_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/ProcessReportFX.fxml")));
                    } catch(IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Historial Producción":
                    break;
                case "Historial de Transacciones":
                    transaction_history_tab.setDisable(false);
                    try{
                        transaction_history_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/TransactionHistoryFX.fxml")));
                    } catch(IOException ex) {
                        Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
        
        logout.setOnAction((ActionEvent) ->{
            MainApp.employee_id = null;
            showLogin();
        });
    }
    
    public void showLogin(){
        try {
            Stage stage = (Stage) root_pane.getScene().getWindow();
            stage.close();
            stage = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("MSA Manager");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
