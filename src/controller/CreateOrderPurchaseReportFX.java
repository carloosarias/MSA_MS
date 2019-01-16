/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;
import model.Company;
import model.CompanyAddress;
import model.PurchaseItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateOrderPurchaseReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private ComboBox<CompanyAddress> companyaddress_combo;
    @FXML
    private TextField exchangerate_field;
    @FXML
    private TextField ivarate_field;
    @FXML
    private TableView<PurchaseItem> purchaseitem_tableview;
    @FXML
    private TableColumn<PurchaseItem, String> productid_column;
    @FXML
    private TableColumn<PurchaseItem, String> description_column;
    @FXML
    private TableColumn<PurchaseItem, String> quantity_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitmeasure_column;
    @FXML
    private TableColumn<PurchaseItem, String> unitprice_column;
    @FXML
    private TableColumn<PurchaseItem, Integer> unitsordered_column;
    @FXML
    private TableColumn<PurchaseItem, String> price_column;
    @FXML
    private TextField subtotal_field;
    @FXML
    private TextField iva_field;
    @FXML
    private TextField total_field;
    @FXML
    private TextArea comments_area;
    @FXML
    private Button cancel_button;
    @FXML
    private Button save_button;

    public static Company company_selection;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportdate_picker.setValue(LocalDate.now());
        
        company_combo.getItems().setAll(company_selection);
        company_combo.getSelectionModel().selectFirst();
        
        companyaddress_combo.getItems().setAll(msabase.getCompanyAddressDAO().listActive(company_selection, true));
        companyaddress_combo.disableProperty().bind(companyaddress_combo.itemsProperty().isNull());
        
        setPurchaseItemTable();
        
        purchaseitem_tableview.setItems(filterCart_list(OrderPurchaseCartFX.cart_list, company_selection));
        
    }
    
    public void setSubtotal(){
        Double subtotal = 0.0;
        
        for(PurchaseItem item : purchaseitem_tableview.getItems()){
            subtotal += item.getPrice_total();
        }
        
        subtotal_field.setText(""+subtotal);
    }
    
    public void setIva(){
        
    }
    
    public ObservableList<PurchaseItem> filterCart_list(ObservableList<PurchaseItem> cart_list, Company company_selection){
        ObservableList<PurchaseItem> filtered_list = FXCollections.observableArrayList(); 
        for(PurchaseItem item : cart_list){
            if(item.getTemp_productsupplier().getCompany().equals(company_selection)){
                filtered_list.add(item);
            }
        }
        return filtered_list;
    }
    
    
    public void setPurchaseItemTable(){
        productid_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_productsupplier().getProduct().getId()));
        description_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getProduct().getDescription()));
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getTemp_productsupplier().getQuantity()));
        unitmeasure_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTemp_productsupplier().getProduct().getUnit_measure()));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_unit()+" USD"));
        unitsordered_column.setCellValueFactory(new PropertyValueFactory("units_ordered"));
        price_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+c.getValue().getPrice_total()+" USD"));
        
        unitsordered_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        unitsordered_column.setOnEditCommit((TableColumn.CellEditEvent<PurchaseItem, Integer> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setUnits_ordered(t.getNewValue());
            purchaseitem_tableview.refresh();
            setSubtotal();
        });
    }
}
