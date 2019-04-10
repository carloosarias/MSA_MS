/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Module;
import model.ProductPart;
import model.Quote;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class MainFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TabPane root_tabpane;
    @FXML
    private ListView<Module> menu_listview;
    @FXML
    private Tab employee_tab;
    @FXML
    private Tab company_tab;
    @FXML
    private Tab partrevision_tab;
    @FXML
    private Tab productpart_tab;
    @FXML
    private Tab metal_tab;
    @FXML
    private Tab specification_tab;
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
    private Tab invoicequote_tab;
    @FXML
    private Tab tank_tab;
    @FXML
    private Tab process_tab;
    @FXML
    private Tab transactionhistory_tab;
    @FXML
    private Tab scrap_tab;
    @FXML
    private Tab analysistype_tab;
    @FXML
    private Tab analysisreport_tab;
    @FXML
    private Tab mantainance_tab;
    @FXML
    private Tab equipment_tab;
    @FXML
    private Tab equipmenttype_tab;
    @FXML
    private Tab product_tab;
    @FXML
    private Tab productsupplier_tab;
    @FXML
    private Tab orderpurchaseincomingreport_tab;
    @FXML
    private Tab orderpurchasecart_tab;
    @FXML
    private Tab activityreport_tab;
    @FXML
    private MenuItem logout;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menu_listview.getItems().setAll(msabase.getModuleDAO().list(MainApp.current_employee));
        menu_listview.getSelectionModel().selectFirst();
        
        setTabs(menu_listview.getSelectionModel().getSelectedItem());
        
        menu_listview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Module> observable, Module oldValue, Module newValue) -> {
            setTabs(newValue);
        });
        
        logout.setOnAction((ActionEvent) ->{
            MainApp.current_employee = null;
            showLogin();
        });
        
    }
        
    public void setTabs(Module module){
        
        try {
                switch(module.getName()){
                    case "Recursos Humanos":
                        employee_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/EmployeeFX.fxml")));
                        company_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/CompanyFX.fxml")));
                        root_tabpane.getTabs().setAll(employee_tab, company_tab);
                        break;
                    case "Compras":
                        product_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ProductFX.fxml")));
                        productsupplier_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ProductSupplierFX.fxml")));
                        orderpurchasecart_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseCartFX.fxml")));
                        orderpurchase_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseFX.fxml")));
                        root_tabpane.getTabs().setAll(product_tab, productsupplier_tab, orderpurchasecart_tab, orderpurchase_tab);
                        break;
                    case "Números de Parte":
                        partrevision_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/PartRevisionFX.fxml")));
                        productpart_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ProductPartFX.fxml")));
                        specification_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/SpecificationFX.fxml")));
                        metal_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/MetalFX.fxml")));
                        root_tabpane.getTabs().setAll(productpart_tab, specification_tab, metal_tab);
                        break;
                    case "Reciba":
                        incoming_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/IncomingReportFX.fxml")));
                        orderpurchaseincomingreport_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/OrderPurchaseIncomingReportFX.fxml")));
                        depart_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/DepartReportFX.fxml")));
                        root_tabpane.getTabs().setAll(incoming_tab, orderpurchaseincomingreport_tab, depart_tab);
                        break;
                    case "Facturación":
                        invoice_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/InvoiceFX.fxml")));
                        root_tabpane.getTabs().setAll(invoice_tab);
                        break;
                    case "Cotización":
                        quote_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/QuoteFX.fxml")));
                        invoicequote_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/InvoiceQuoteFX.fxml")));
                        root_tabpane.getTabs().setAll(quote_tab, invoicequote_tab);
                        break;
                    case "Producción":
                        process_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ProcessReportFX.fxml")));
                        root_tabpane.getTabs().setAll(process_tab);
                        break;
                    case "Historial de Transacciones":
                        transactionhistory_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/TransactionHistoryFX.fxml")));
                        root_tabpane.getTabs().setAll(transactionhistory_tab);
                        break;
                    case "Scrap":
                        scrap_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ScrapReportFX.fxml")));
                        root_tabpane.getTabs().setAll(scrap_tab);
                        break;
                    case "Análisis":
                        analysisreport_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/AnalysisReportFX.fxml")));
                        analysistype_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/AnalysisTypeFX.fxml")));
                        tank_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/TankFX.fxml")));
                        root_tabpane.getTabs().setAll(analysisreport_tab, analysistype_tab, tank_tab);
                        break;
                    case "Mantenimiento":
                        mantainance_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/MantainanceReportFX.fxml")));
                        activityreport_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/ActivityReportFX.fxml")));
                        equipment_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/EquipmentFX.fxml")));
                        equipmenttype_tab.setContent((GridPane) FXMLLoader.load(getClass().getResource("/fxml/EquipmentTypeFX.fxml")));
                        root_tabpane.getTabs().setAll(mantainance_tab, activityreport_tab, equipment_tab, equipmenttype_tab);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void showLogin(){
        try {
            Stage stage = (Stage) root_gridpane.getScene().getWindow();
            stage.close();
            stage = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));

            Scene scene = new Scene(root);
            stage.getIcons().add(new Image("/MSA-icon.png"));
            stage.setTitle("MSA Manager");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}