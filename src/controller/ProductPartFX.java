/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
import model.Specification;
import model.SpecificationItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductPartFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<ProductPart> productpart_tableview;
    @FXML
    private TableColumn<ProductPart, Integer> productpartid_column;
    @FXML
    private TableColumn<ProductPart, String> partnumber_column;
    @FXML
    private TableColumn<ProductPart, String> description_column;
    @FXML
    private TableColumn<ProductPart, String> productpartstatus_column;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, Integer> partrevisionid_column;
    @FXML
    private TableColumn<PartRevision, String> rev_column;
    @FXML
    private TableColumn<PartRevision, Date> revdate_column;
    @FXML
    private TableColumn<PartRevision, String> basemetal_column;
    @FXML
    private TableColumn<PartRevision, String> finalprocess_column;
    @FXML
    private TableColumn<PartRevision, String> revspecnumber_column;
    @FXML
    private TableColumn<PartRevision, Double> area_column;
    @FXML
    private TableColumn<PartRevision, Double> baseweight_column;
    @FXML
    private TableColumn<PartRevision, Double> finalweight_column;
    @FXML
    private TableColumn<PartRevision, String> partrevisionstatus_column;
    @FXML
    private TableView<Specification> specification_tableview;
    @FXML
    private TableColumn<Specification, Integer> specificationid_column;
    @FXML
    private TableColumn<Specification, String> specificationnumber_column;
    @FXML
    private TableColumn<Specification, String> specificationname_column;
    @FXML
    private TableColumn<Specification, String> process_column;
    @FXML
    private TableView<SpecificationItem> specificationitem_tableview;
    @FXML
    private TableColumn<SpecificationItem, Integer> specificationitemid_column;
    @FXML
    private TableColumn<SpecificationItem, String> metal_column;
    @FXML
    private TableColumn<SpecificationItem, Double> minimumthickness_column;
    @FXML
    private TableColumn<SpecificationItem, Double> maximumthickness_column;
    @FXML
    private TableView<Metal> metal_tableview;
    @FXML
    private TableColumn<Metal, Integer> metalid_column;
    @FXML
    private TableColumn<Metal, String> metalname_column;
    @FXML
    private TableColumn<Metal, Double> density_column;
    @FXML
    private Button addproductpart_button;
    @FXML
    private Button addpartrevision_button;
    @FXML
    private Button addspecification_button;
    @FXML
    private Button addmetal_button;
    
    private static ProductPart productpart_selection;
    
    private Stage addproductpart_stage = new Stage();
    
    private Stage addpartrevision_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Activo", "Inactivo");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProductPartTable();
        setPartRevisionTable();
        setSpecificationTable();
        setMetalTable();
        setSpecificationItems();
        setMetalItems();
        setProductPartItems();
        
        productpart_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            addpartrevision_button.setDisable(productpart_tableview.getSelectionModel().isEmpty());
            if(!productpart_tableview.getSelectionModel().isEmpty()){
                setPartRevisionItems(productpart_tableview.getSelectionModel().getSelectedItem());
            }
        });
        
        specification_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Specification> observable, Specification oldValue, Specification newValue) -> {
            if(!specification_tableview.getSelectionModel().isEmpty()){
                setSpecificationItemItems(specification_tableview.getSelectionModel().getSelectedItem());
            }
        });
        
        addproductpart_button.setOnAction((ActionEvent) -> {
            addproductpart_button.setDisable(true);
            showAddProductPartStage();
            setProductPartItems();
        });
        
        addpartrevision_button.setOnAction((ActionEvent) -> {
            productpart_selection = productpart_tableview.getSelectionModel().getSelectedItem();
            productpart_tableview.setDisable(true);
            addpartrevision_button.setDisable(true);
            showAddPartRevisionStage();
            productpart_tableview.setDisable(false);
        });
    }
    
    public void showAddProductPartStage(){
        try {
            addproductpart_stage = new Stage();
            addproductpart_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddProductPartFX.fxml"));
            Scene scene = new Scene(root);
            
            addproductpart_stage.setTitle("Nuevo Número de Parte");
            addproductpart_stage.setResizable(false);
            addproductpart_stage.initStyle(StageStyle.UTILITY);
            addproductpart_stage.setScene(scene);
            addproductpart_stage.showAndWait();
            addproductpart_button.setDisable(false);
            setProductPartItems();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddPartRevisionStage(){
        try {
            addpartrevision_stage = new Stage();
            addpartrevision_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddPartRevisionFX.fxml"));
            Scene scene = new Scene(root);
            
            addpartrevision_stage.setTitle("Nueva Revisión");
            addpartrevision_stage.setResizable(false);
            addpartrevision_stage.initStyle(StageStyle.UTILITY);
            addpartrevision_stage.setScene(scene);
            addpartrevision_stage.showAndWait();
            addpartrevision_button.setDisable(false);
            setPartRevisionItems(productpart_tableview.getSelectionModel().getSelectedItem());
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setProductPartTable(){
        productpartid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        productpartstatus_column.setCellValueFactory(c -> new SimpleStringProperty(getStatus(c.getValue().isActive())));
        productpartstatus_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        productpartstatus_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setActive(getActive(t.getNewValue()));
            msabase.getProductPartDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            setProductPartItems();
        });
    }
    
    public void setPartRevisionTable(){
        partrevisionid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        revdate_column.setCellValueFactory(new PropertyValueFactory<>("rev_date"));
        basemetal_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findMetal(c.getValue()).getMetal_name()));
        finalprocess_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findSpecification(c.getValue()).getProcess()));
        revspecnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findSpecification(c.getValue()).getSpecification_number()));
        area_column.setCellValueFactory(new PropertyValueFactory<>("area"));
        baseweight_column.setCellValueFactory(new PropertyValueFactory<>("base_weight"));
        finalweight_column.setCellValueFactory(new PropertyValueFactory<>("final_weight"));
        partrevisionstatus_column.setCellValueFactory(c -> new SimpleStringProperty(getStatus(c.getValue().isActive())));
        partrevisionstatus_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        partrevisionstatus_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setActive(getActive(t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            setPartRevisionItems(productpart_selection);
        });
    }
    
    public void setSpecificationTable(){
        specificationid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        specificationnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_number"));
        specificationname_column.setCellValueFactory(new PropertyValueFactory<>("specification_name"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setSpecificationItemTable(){
        specificationitemid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        metal_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getSpecificationItemDAO().findMetal(c.getValue()).getMetal_name()));
        minimumthickness_column.setCellValueFactory(new PropertyValueFactory<>("minimum_thickness"));
        maximumthickness_column.setCellValueFactory(new PropertyValueFactory<>("maximum_thickness"));
        
    }
    
    public void setMetalTable(){
        metalid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        metalname_column.setCellValueFactory(new PropertyValueFactory<>("metal_name"));
        density_column.setCellValueFactory(new PropertyValueFactory<>("density"));
    }
    
    
    public void setProductPartItems(){
        productpart_tableview.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
    }
    
    public void setPartRevisionItems(ProductPart product_part){
        partrevision_tableview.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(product_part)));
    }
    
    public void setSpecificationItems(){
        specification_tableview.setItems(FXCollections.observableArrayList(msabase.getSpecificationDAO().list()));
    }
    
    public void setSpecificationItemItems(Specification specification){
        specificationitem_tableview.setItems(FXCollections.observableArrayList(msabase.getSpecificationItemDAO().list(specification)));
    }
    
    public void setMetalItems(){
        metal_tableview.setItems(FXCollections.observableArrayList(msabase.getMetalDAO().list()));
    }
    
    public boolean getActive(String productpart_status){
        if(productpart_status.equals("Activo")){
            return true;
        }else{
            return false;
        }
    }
    
    public String getStatus(Boolean active){
        if(active){
            return "Activo";
        }else{
            return "Inactivo";
        }
    }
    
    public static ProductPart getProductpart_selection(){
        return productpart_selection;
    }
    
}
