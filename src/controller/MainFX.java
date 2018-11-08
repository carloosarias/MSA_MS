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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    private Tab productpart_tab;
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
    private Tab tank_tab;
    @FXML
    private Tab process_tab;
    @FXML
    private Tab transaction_history_tab;
    @FXML
    private Tab scrap_tab;
    @FXML
    private Tab analysis_tab;
    @FXML
    private MenuItem logout;
    
    private List<Module> modules;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    @FXML
    private ListView<Module> menu_listview;
    @FXML
    private TabPane root_tabpane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setModules();
        modules = msabase.getModuleEmployeeDAO().list(msabase.getEmployeeDAO().find(MainApp.employee_id));
        menu_listview.setItems(FXCollections.observableArrayList(modules));
        Module delete = new Module();
        for(Module module : menu_listview.getItems()){
            if(module.getName().equals("Historial Producción")){
                delete = module;
            }
        }
        menu_listview.getItems().remove(delete);
        menu_listview.getSelectionModel().clearAndSelect(0);
        setTabs(menu_listview.getSelectionModel().getSelectedItem());
        
        menu_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Module> observable, Module oldValue, Module newValue) -> {
            root_tabpane = new TabPane();
            setTabs(newValue);
            root_pane.setCenter(root_tabpane);
            root_pane.getScene().getWindow().sizeToScene();
        });
        
        logout.setOnAction((ActionEvent) ->{
            MainApp.employee_id = null;
            showLogin();
        });
    }
    public void setModules(){
        try {
            employee_tab.setContent((BorderPane) FXMLLoader.load(getClass().getResource("/fxml/HrFX.fxml")));
            company_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/CompanyFX.fxml")));
            productpart_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProductPartFX.fxml")));
            tank_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/TankFX.fxml")));
            orderpurchase_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseFX.fxml")));
            incoming_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/IncomingReportFX.fxml")));
            depart_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/DepartReportFX.fxml")));
            invoice_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoiceFX.fxml")));
            invoice_payment_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoicePaymentReportFX.fxml")));
            quote_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/QuoteFX.fxml")));
            process_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/ProcessReportFX.fxml")));
            transaction_history_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/TransactionHistoryFX.fxml")));
            scrap_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/ScrapReportFX.fxml")));
            analysis_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/AnalysisReportFX.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setTabs(Module module){
            switch(module.getName()){
                case "Recursos Humanos":
                    root_tabpane.getTabs().setAll(employee_tab);
                    break;
                case "Compras":
                    root_tabpane.getTabs().setAll(company_tab, productpart_tab, tank_tab, orderpurchase_tab);
                    break;
                case "Reciba":
                    root_tabpane.getTabs().setAll(incoming_tab, depart_tab);
                    break;
                case "Facturación":
                    root_tabpane.getTabs().setAll(invoice_tab, invoice_payment_tab);
                    break;
                case "Cotización":
                    root_tabpane.getTabs().setAll(quote_tab);
                    break;
                case "Historial Producción":
                case "Producción":
                    root_tabpane.getTabs().setAll(process_tab);
                    break;
                case "Historial de Transacciones":
                    root_tabpane.getTabs().setAll(transaction_history_tab);
                    break;
                case "Scrap":
                    root_tabpane.getTabs().setAll(scrap_tab);
                    break;
                case "Análisis":
                    root_tabpane.getTabs().setAll(analysis_tab);
            }
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
