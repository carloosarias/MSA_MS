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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
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
    private GridPane root_gridpane;
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
    private TableColumn<PartRevision, String> area_column;
    @FXML
    private TableColumn<PartRevision, String> baseweight_column;
    @FXML
    private TableColumn<PartRevision, String> finalweight_column;
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
    private TableColumn<SpecificationItem, String> specificationitemid_column;
    @FXML
    private TableColumn<SpecificationItem, String> metal_column;
    @FXML
    private TableColumn<SpecificationItem, String> minimumthickness_column;
    @FXML
    private TableColumn<SpecificationItem, String> maximumthickness_column;
    @FXML
    private TableView<Metal> metal_tableview;
    @FXML
    private TableColumn<Metal, Integer> metalid_column;
    @FXML
    private TableColumn<Metal, String> metalname_column;
    @FXML
    private TableColumn<Metal, String> density_column;
    @FXML
    private Button addproductpart_button;
    @FXML
    private Button addpartrevision_button;
    @FXML
    private Button addspecification_button;
    @FXML
    private Button addmetal_button;
    
    private static ProductPart productpart_selection;
    
    private Stage add_stage = new Stage();
    
    private List<String> status_items = Arrays.asList("Activo", "Inactivo");
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    DecimalFormat df = new DecimalFormat("#");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setProductPartTable();
        setPartRevisionTable();
        setSpecificationTable();
        setSpecificationItemTable();
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
            showAddProductPartStage();
            setProductPartItems();
        });
        
        addpartrevision_button.setOnAction((ActionEvent) -> {
            productpart_selection = productpart_tableview.getSelectionModel().getSelectedItem();
            showAddPartRevisionStage();
            setPartRevisionItems(productpart_tableview.getSelectionModel().getSelectedItem());
        });
        
        addspecification_button.setOnAction((ActionEvent) -> {
            showAddSpecificationStage();
            setSpecificationItems();
        });
        
        addmetal_button.setOnAction((ActionEvent) -> {
            showAddMetalStage();
            setMetalItems();
        });
    }
    
    public void showAddProductPartStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddProductPartFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Número de Parte");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddPartRevisionStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddPartRevisionFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Revisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddSpecificationStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateSpecificationFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Especificación");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showAddMetalStage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddMetalFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Especificación");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
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
        rev_column.setCellFactory(TextFieldTableCell.forTableColumn());
        rev_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRev(t.getNewValue());
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        revdate_column.setCellValueFactory(new PropertyValueFactory<>("rev_date"));
        basemetal_column.setCellValueFactory(new PropertyValueFactory<>("metal_metalname"));
        finalprocess_column.setCellValueFactory(new PropertyValueFactory<>("specification_process"));
        revspecnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_specificationnumber"));
        area_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getArea())+" IN²"));
        area_column.setCellFactory(TextFieldTableCell.forTableColumn());
        area_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setArea(getAreaValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        baseweight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getBase_weight())+" KG"));
        baseweight_column.setCellFactory(TextFieldTableCell.forTableColumn());
        baseweight_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setBase_weight(getBase_weightValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        finalweight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getFinal_weight())+" KG"));
        finalweight_column.setCellFactory(TextFieldTableCell.forTableColumn());
        finalweight_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFinal_weight(getFinal_weightValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        partrevisionstatus_column.setCellValueFactory(c -> new SimpleStringProperty(getStatus(c.getValue().isActive())));
        partrevisionstatus_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(status_items)));
        partrevisionstatus_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setActive(getActive(t.getNewValue()));
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
    }
    
    public void setSpecificationTable(){
        specificationid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        specificationnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_number"));
        specificationname_column.setCellValueFactory(new PropertyValueFactory<>("specification_name"));
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setSpecificationItemTable(){
        specificationitemid_column.setCellValueFactory(c -> new SimpleStringProperty(""+(specificationitem_tableview.getItems().indexOf(c.getValue())+1)));
        metal_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMetal_name()));
        minimumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMinimum_thickness())+" IN"));
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMaximum_thickness())+" IN"));
    }
    
    public void setMetalTable(){
        metalid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        metalname_column.setCellValueFactory(new PropertyValueFactory<>("metal_name"));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getDensity())+" G/CM³"));
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
    
    public Double getAreaValue(PartRevision revision, String area){
        try{
            return Double.parseDouble(area);
        }catch(Exception e){
            return revision.getArea();
        }
    }
    
    public Double getBase_weightValue(PartRevision revision, String base_weight){
        try{
            return Double.parseDouble(base_weight);
        }catch(Exception e){
            return revision.getBase_weight();
        }
    }
    
    public Double getFinal_weightValue(PartRevision revision, String final_weight){
        try{
            return Double.parseDouble(final_weight);
        }catch(Exception e){
            return revision.getFinal_weight();
        }
    }
}
