/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import model.Company;
import model.PurchaseItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseCartFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, String> serialnumber_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> supplier_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasureprice_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitprice_column;
    @FXML
    private TableColumn<PurchaseItem, Integer> unitsordered_column;
    @FXML
    private Button delete_button;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private Button create_button;
    
    public static ObservableList<PurchaseItem> cart_list = FXCollections.observableArrayList();
    
    private static ObservableList<Company> company_list = FXCollections.observableArrayList();
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPurchaseItemTable();
        purchaseitem_tableview.setItems(cart_list);
        company_combo.setItems(company_list);
        
        delete_button.disableProperty().bind(purchaseitem_tableview.getSelectionModel().selectedItemProperty().isNull());
        company_combo.disableProperty().bind(Bindings.size(company_combo.getItems()).isEqualTo(0));
        create_button.disableProperty().bind(company_combo.getSelectionModel().selectedItemProperty().isNull());

        
        create_button.setOnAction((ActionEvent) -> {
            CreateOrderPurchaseReportFX.company_selection = company_combo.getSelectionModel().getSelectedItem();
            showAdd_stage();
            company_combo.getSelectionModel().clearSelection();
            purchaseitem_tableview.refresh();
            OrderPurchaseFX.updateOrderPurchaseList(msabase);
            updateCompanyList();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            cart_list.remove(purchaseitem_tableview.getSelectionModel().getSelectedItem());
            updateCompanyList();
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/CreateOrderPurchaseReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Generar Orden De Compra");
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateCompanyList(){
        ObservableSet<Company> company_set = FXCollections.observableSet();
        
        for(PurchaseItem item : cart_list){
            Company company = new Company();
            company.setId(item.getTemp_productsupplier().getCompany_id());
            company.setName(item.getTemp_productsupplier().getCompany_name());
            company_set.add(company);
        }
        
        company_list.setAll(FXCollections.observableArrayList(company_set));
    }

    public void setPurchaseItemTable(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(4);
        serialnumber_column.setCellValueFactory(new PropertyValueFactory("productsupplier_serialnumber"));
        description_column.setCellValueFactory(new PropertyValueFactory("product_description"));
        supplier_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getCompany_name()));
        quantity_column.setCellValueFactory(new PropertyValueFactory("productsupplier_quantity"));
        unitmeasure_column.setCellValueFactory(new PropertyValueFactory("product_unitmeasure"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_unit()+" USD"));
        unitprice_column.setCellFactory(TextFieldTableCell.forTableColumn());
        unitprice_column.setOnEditCommit((TableColumn.CellEditEvent<PurchaseItem, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice_updated(getPrice_Unitvalue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            purchaseitem_tableview.refresh();
        });
        unitmeasureprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format((c.getValue().getPrice_unit()/c.getValue().getTemp_productsupplier().getQuantity()))+" USD"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        unitsordered_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        unitsordered_column.setOnEditCommit((TableColumn.CellEditEvent<PurchaseItem, Integer> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_ordered(t.getNewValue());
            if(t.getNewValue() < 1){
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_ordered(0);
            }
            purchaseitem_tableview.refresh();
        });
        
    }
    
    public Double getPrice_Unitvalue(PurchaseItem item, String unit_price){
        try{
            return Double.parseDouble(unit_price);
        }catch(Exception e){
            return item.getPrice_unit();
        }
    }
}
