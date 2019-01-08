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
    private Tab invoicepayment_tab;
    @FXML
    private Tab tank_tab;
    @FXML
    private Tab process_tab;
    @FXML
    private Tab transactionhistory_tab;
    @FXML
    private Tab scrap_tab;
    @FXML
    private Tab analysis_tab;
    @FXML
    private Tab equipment_tab;
    @FXML
    private Tab mantainance_tab;
    @FXML
    private Tab product_tab;
    @FXML
    private Tab productsupplier_tab;
    @FXML
    private Tab orderpurchaseincomingreport_tab;
    @FXML
    private Tab orderpurchasecart_tab;
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
            incoming_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/IncomingReportFX.fxml")));
            orderpurchaseincomingreport_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseIncomingReportFX.fxml")));
            depart_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/DepartReportFX.fxml")));
            invoice_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoiceFX.fxml")));
            invoicepayment_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoicePaymentReportFX.fxml")));
            quote_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/QuoteFX.fxml")));
            process_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProcessReportFX.fxml")));
            transactionhistory_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/TransactionHistoryFX.fxml")));
            scrap_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ScrapReportFX.fxml")));
            analysis_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/AnalysisReportFX.fxml")));
            equipment_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/EquipmentFX.fxml")));
            mantainance_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/MantainanceReportFX.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setTabs(Module module){
            try {
                switch(module.getName()){
                    case "Recursos Humanos":
                        employee_tab.setContent((BorderPane) FXMLLoader.load(getClass().getResource("/fxml/HrFX.fxml")));
                        root_tabpane.getTabs().setAll(employee_tab);
                        break;
                    case "Compras":
                        company_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/CompanyFX.fxml")));
                        product_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProductFX.fxml")));
                        productsupplier_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProductSupplierFX.fxml")));
                        orderpurchase_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseFX.fxml")));
                        orderpurchasecart_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseCartFX.fxml")));
                        root_tabpane.getTabs().setAll(company_tab, product_tab, productsupplier_tab, orderpurchase_tab, orderpurchasecart_tab);
                        break;
                    case "Partes y Revisiones":
                        productpart_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProductPartFX.fxml")));
                        root_tabpane.getTabs().setAll(productpart_tab);
                        break;
                    case "Reciba":
                        incoming_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/IncomingReportFX.fxml")));
                        orderpurchaseincomingreport_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseIncomingReportFX.fxml")));
                        depart_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/DepartReportFX.fxml")));
                        root_tabpane.getTabs().setAll(incoming_tab, orderpurchaseincomingreport_tab, depart_tab);
                        break;
                    case "Facturación":
                        invoice_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoiceFX.fxml")));
                        invoicepayment_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/InvoicePaymentReportFX.fxml")));
                        root_tabpane.getTabs().setAll(invoice_tab, invoicepayment_tab);
                        break;
                    case "Cotización":
                        quote_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/QuoteFX.fxml")));
                        root_tabpane.getTabs().setAll(quote_tab);
                        break;
                    case "Historial Producción":
                    case "Producción":
                        process_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ProcessReportFX.fxml")));
                        root_tabpane.getTabs().setAll(process_tab);
                        break;
                    case "Historial de Transacciones":
                        transactionhistory_tab.setContent( (HBox) FXMLLoader.load(getClass().getResource("/fxml/TransactionHistoryFX.fxml")));
                        root_tabpane.getTabs().setAll(transactionhistory_tab);
                        break;
                    case "Scrap":
                        scrap_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/ScrapReportFX.fxml")));
                        root_tabpane.getTabs().setAll(scrap_tab);
                        break;
                    case "Análisis":
                        analysis_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/AnalysisReportFX.fxml")));
                        root_tabpane.getTabs().setAll(analysis_tab);
                        break;
                    case "Equipo":
                        equipment_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/EquipmentFX.fxml")));
                        tank_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/TankFX.fxml")));
                        root_tabpane.getTabs().setAll(equipment_tab, tank_tab);
                        break;
                    case "Mantenimiento":
                        mantainance_tab.setContent((HBox) FXMLLoader.load(getClass().getResource("/fxml/MantainanceReportFX.fxml")));
                        root_tabpane.getTabs().setAll(mantainance_tab);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
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
