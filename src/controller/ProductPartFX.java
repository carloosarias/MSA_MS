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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductPartFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<ProductPart> productpart_tableview;
    @FXML
    private TableColumn<ProductPart, String> partnumber_column;
    @FXML
    private TableColumn<ProductPart, String> description_column;
    @FXML
    private Button add_button;
    @FXML
    private Button disable_button;
    
    private ProductPart product_part;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductPartTable();
        updateProductPartTable();
        
        disable_button.disableProperty().bind(productpart_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        add_button.setOnAction((ActionEvent) -> {
            int current_size = productpart_tableview.getItems().size();
            createProductPart();
            updateProductPartTable();
            if(current_size < productpart_tableview.getItems().size()){
                productpart_tableview.scrollTo(product_part);
                productpart_tableview.getSelectionModel().select(product_part);
            }
        });
        
        disable_button.setOnAction((ActionEvent) -> {
            disableProductPart();
            updateProductPartTable();
        });
        
    }
    
    public void createProductPart(){
        product_part = new ProductPart();
        product_part.setPart_number("N/A");
        product_part.setDescription("N/A");
        product_part.setActive(true);
        msabase.getProductPartDAO().create(product_part);
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddProductPartFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Registrar Número de Parte");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setProductPartTable(){
        
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        partnumber_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPart_number(t.getNewValue());
            msabase.getProductPartDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productpart_tableview.refresh();
        });
        
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        description_column.setCellFactory(TextFieldTableCell.forTableColumn());
        description_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
            msabase.getProductPartDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productpart_tableview.refresh();
        });
    }
    
    public void updateProductPartTable(){
        productpart_tableview.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
    }
    
    public void disableProductPart(){
        productpart_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getProductPartDAO().update(productpart_tableview.getSelectionModel().getSelectedItem());
    }
}
