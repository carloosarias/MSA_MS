/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
import model.DepartLot;
import model.Module;
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
    private MenuItem logout;
    
    private List<Tab> tabs = new ArrayList<>();
    
    private Map<String,String> resources = new HashMap<String, String>(){{
            put("Empleados","/fxml/EmployeeFX.fxml");
            put("Compañías","/fxml/CompanyFX.fxml");
            put("Productos","/fxml/ProductFX.fxml");
            put("Lista de Precios","/fxml/ProductSupplierFX.fxml");
            put("Carrito de Compras","/fxml/OrderPurchaseCartFX.fxml");
            put("Historial de Compras","/fxml/OrderPurchaseFX.fxml");
            put("Registro de Revisiones","/fxml/PartRevisionFX.fxml");
            put("Registro de Partes","/fxml/ProductPartFX.fxml");
            put("Registro de Specificaciones","/fxml/SpecificationFX.fxml");
            put("Densidad de Metales","/fxml/MetalFX.fxml");
            put("Reporte de Reciba","/fxml/IncomingReportFX_1.fxml");
            put("Reciba de Compras","/fxml/OrderPurchaseIncomingReportFX.fxml");
            put("Reporte de Remisión","/fxml/DepartReportFX_1.fxml");
            put("Reporte de Scrap","/fxml/ScrapReportFX_1.fxml");
            put("Facturas","/fxml/InvoiceFX.fxml");
            put("Cotizaciones","/fxml/QuoteFX.fxml");
            put("Reporte de Ventas","/fxml/InvoiceQuoteFX.fxml");
            put("Reporte de Entrada a Proceso","/fxml/ProcessReportFX.fxml");
            put("Reporte de Transacciones","/fxml/TransactionHistoryFX.fxml");
            put("Reporte de Análisis","/fxml/AnalysisReportFX.fxml");
            put("Control de Análisis","/fxml/AnalysisTypeFX.fxml");
            put("Control de Tanques","/fxml/TankFX.fxml");
            put("Reporte de Mantemiento Programado","/fxml/MantainanceReportFX.fxml");
            put("Reporte de Actividades No Programado","/fxml/ActivityReportFX.fxml");
            put("Control de Equipo","/fxml/EquipmentFX.fxml");
            put("Control de Categorías","/fxml/EquipmentTypeFX.fxml");
            put("Remisiónes (Legacy)", "/fxml/DepartReportFX.fxml");
            put("Reporte de Rechazo", "/fxml/RejectReportFX.fxml");
        }};
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<Module> modules = msabase.getModuleDAO().list(MainApp.current_employee);
        for(Module module : modules){
                menu_listview.getItems().add(module);
        }
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
        tabs.clear();
        try {
            switch(module.getName()){
                case "Recursos Humanos":
                    tabs.add(new Tab("Empleados"));
                    tabs.add(new Tab("Compañías"));
                    break;
                case "Compras":
                    tabs.add(new Tab("Productos"));
                    tabs.add(new Tab("Lista de Precios"));
                    tabs.add(new Tab("Carrito de Compras"));
                    tabs.add(new Tab("Historial de Compras"));
                    break;
                case "Números de Parte":
                    tabs.add(new Tab("Registro de Revisiones"));
                    tabs.add(new Tab("Registro de Partes"));
                    tabs.add(new Tab("Registro de Specificaciones"));
                    tabs.add(new Tab("Densidad de Metales"));
                    break;
                case "Reciba":
                    tabs.add(new Tab("Reporte de Reciba"));
                    tabs.add(new Tab("Reciba de Compras"));
                    tabs.add(new Tab("Reporte de Remisión"));
                    tabs.add(new Tab("Remisiónes (Legacy)"));
                    break;
                case "Scrap":
                    tabs.add(new Tab("Reporte de Scrap"));
                    tabs.add(new Tab("Reporte de Rechazo"));
                    break;
                case "Facturación":
                    tabs.add(new Tab("Facturas"));
                    break;
                case "Cotización":
                    tabs.add(new Tab("Cotizaciones"));
                    tabs.add(new Tab("Reporte de Ventas"));
                    break;
                case "Producción":
                    tabs.add(new Tab("Reporte de Entrada a Proceso"));
                    break;
                case "Historial de Transacciones":
                    tabs.add(new Tab("Reporte de Transacciones"));
                    break;
                case "Análisis":
                    tabs.add(new Tab("Reporte de Análisis"));
                    tabs.add(new Tab("Control de Análisis"));
                    tabs.add(new Tab("Control de Tanques"));
                    break;
                case "Mantenimiento":
                    tabs.add(new Tab("Reporte de Mantemiento Programado"));
                    tabs.add(new Tab("Reporte de Actividades No Programado"));
                    tabs.add(new Tab("Control de Equipo"));
                    tabs.add(new Tab("Control de Categorías"));
                    break;
            }
            for(Tab tab : tabs){
                tab.setContent((GridPane) FXMLLoader.load(getClass().getResource(resources.get(tab.getText()))));
            }
            root_tabpane.getTabs().setAll(tabs);
        }
        catch(Exception e){
            e.printStackTrace();
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