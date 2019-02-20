/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Product;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<Product> product_tableview;
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
    @FXML
    private TableColumn<Product, Integer> id_column;
    @FXML
    private TableColumn<Product, String> description_column;
    @FXML
    private TableColumn<Product, String> unitmeasure_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductTable();
        updateProductTable();
        
        delete_button.disableProperty().bind(product_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        add_button.setOnAction((ActionEvent) -> {
            createProduct();
            updateProductTable();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            disableProduct();
            updateProductTable();
            product_tableview.getSelectionModel().selectLast();
        });
       
    }    
    
    public void createProduct(){
        Product product = new Product();
        product.setDescription("N/A");
        product.setUnit_measure("N/A");
        product.setActive(true);
        msabase.getProductDAO().create(product);
    }
    
    public void disableProduct(){
        product_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getProductDAO().update(product_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void setProductTable(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getProductDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            product_tableview.refresh();
        });
        
        unitmeasure_column.setCellValueFactory(new PropertyValueFactory<>("unit_measure"));
        unitmeasure_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(MainApp.unit_measure)));
        unitmeasure_column.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnit_measure(t.getNewValue());
            msabase.getProductDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            product_tableview.refresh();
        });
    }
    
    public void updateProductTable(){
        product_tableview.setItems(FXCollections.observableArrayList(msabase.getProductDAO().list(true)));
    }
}
