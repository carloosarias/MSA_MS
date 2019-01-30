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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Invoice;
import model.Product;
import model.ProductSupplier;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductFX implements Initializable {

    @FXML
    private HBox root_hbox;
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
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductTable();
        updateProductTable();
        
        product_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
            delete_button.setDisable(product_tableview.getSelectionModel().isEmpty());
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateProductTable();
        });
        
        delete_button.setOnAction((ActionEvent) -> {
            disableProduct(product_tableview.getSelectionModel().getSelectedItem());
            updateProductTable();
        });
       
    }    
    
    public void disableProduct(Product product){
        product.setActive(false);
        msabase.getProductDAO().update(product);
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddProductFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Producto");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    }
    
    public void updateProductTable(){
        product_tableview.setItems(FXCollections.observableArrayList(msabase.getProductDAO().list(true)));
    }
}
