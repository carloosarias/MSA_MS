/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PartRevision;
import model.ProductPart;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class ProductPartFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private Tab revision_tab;
    @FXML
    private TextField partnumber_field;
    @FXML
    private CheckBox partnumber_filter;
    @FXML
    private TableView<ProductPart> productpart_tableview;
    @FXML
    private TableColumn<ProductPart, String> counter_column;
    @FXML
    private TableColumn<ProductPart, String> partnumber_column;
    @FXML
    private TableColumn<ProductPart, String> description_column;
    @FXML
    private Button addpart_button;
    @FXML
    private Button disablepart_button;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> partnumber_column1;
    @FXML
    private TableColumn<PartRevision, String> rev_column;
    @FXML
    private TableColumn<PartRevision, String> revdate_column;
    @FXML
    private TableColumn<PartRevision, String> basemetal_column;
    @FXML
    private TableColumn<PartRevision, String> finalprocess_column;
    @FXML
    private TableColumn<PartRevision, String> specnumber_column;
    @FXML
    private TableColumn<PartRevision, String> area_column;
    @FXML
    private TableColumn<PartRevision, String> baseweight_column;
    @FXML
    private TableColumn<PartRevision, String> finalweight_column;
    @FXML
    private Button addrev_button;
    @FXML
    private Button disablerev_button;
    
    private ProductPart product_part;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setProductPartTable();
        setPartRevisionTable();
        updateProductPartTable();
        
        disablepart_button.disableProperty().bind(productpart_tableview.getSelectionModel().selectedItemProperty().isNull());
        disablerev_button.disableProperty().bind(partrevision_tableview.getSelectionModel().selectedItemProperty().isNull());
        partnumber_field.disableProperty().bind(partnumber_filter.selectedProperty().not());
        revision_tab.disableProperty().bind(productpart_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        productpart_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductPart> observable, ProductPart oldValue, ProductPart newValue) -> {
            updatePartRevisionTable();
        });
        
        partnumber_field.setOnAction((ActionEvent) -> {
            updateProductPartTable();
        });
        
        partnumber_filter.setOnAction((ActionEvent) -> {
           updateProductPartTable(); 
        });
        
        addpart_button.setOnAction((ActionEvent) -> {
            int current_size = productpart_tableview.getItems().size();
            createProductPart();
            updateProductPartTable();
            if(current_size < productpart_tableview.getItems().size()){
                productpart_tableview.scrollTo(product_part);
                productpart_tableview.getSelectionModel().select(product_part);
            }
        });
        
        disablepart_button.setOnAction((ActionEvent) -> {
            disableProductPart();
            updateProductPartTable();
        });
        
        addrev_button.setOnAction((ActionEvent) -> {
            int current_size = partrevision_tableview.getItems().size();
            showAdd_stage();
            updatePartRevisionTable();
            if(current_size < partrevision_tableview.getItems().size()){
                partrevision_tableview.scrollTo(AddPartRevisionFX.part_revision);
                partrevision_tableview.getSelectionModel().select(AddPartRevisionFX.part_revision);
            }
        });
        
        disablerev_button.setOnAction((ActionEvent) -> {
            disablePartRevision();
            updatePartRevisionTable();
        });
    }
    
    public void createProductPart(){
        product_part = new ProductPart();
        product_part.setPart_number("N/A");
        product_part.setDescription("N/A");
        product_part.setActive(true);
        msabase.getProductPartDAO().create(product_part);
    }
 
    
    public void setProductPartTable(){
        counter_column.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getTableView().getItems().indexOf(c.getValue())+1)));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        partnumber_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPart_number(getPartNumberValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
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
    
    public void setPartRevisionTable(){
        partnumber_column1.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        revdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getRev_date()))));
        finalprocess_column.setCellValueFactory(new PropertyValueFactory<>("specification_process"));
        specnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification_specificationnumber"));
        basemetal_column.setCellValueFactory(new PropertyValueFactory<>("metal_metalname"));
        
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        rev_column.setCellFactory(TextFieldTableCell.forTableColumn());
        rev_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRev(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getPartRevisionDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            partrevision_tableview.refresh();
        });
        
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
    }
    
    public void updatePartRevisionTable(){
        try{
            partrevision_tableview.getItems().setAll(msabase.getPartRevisionDAO().list(productpart_tableview.getSelectionModel().getSelectedItem(), true));
        }catch(Exception e){
            partrevision_tableview.getItems().clear();
        }
    }
    
    public void updateProductPartTable(){
        productpart_tableview.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list(partnumber_field.getText(), partnumber_filter.isSelected(), true)));
    }
    
    public void disablePartRevision(){
        partrevision_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getPartRevisionDAO().update(partrevision_tableview.getSelectionModel().getSelectedItem());
    }
    
    public void disableProductPart(){
        productpart_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getProductPartDAO().update(productpart_tableview.getSelectionModel().getSelectedItem());
        for(PartRevision part_revision : msabase.getPartRevisionDAO().list(product_part, true)){
            part_revision.setActive(false);
            msabase.getPartRevisionDAO().update(part_revision);
        }
    }
    
    public String getPartNumberValue(ProductPart product_part, String part_number){
        for(ProductPart item : productpart_tableview.getItems()){
            if(item.getPart_number().replace(" ", "").equalsIgnoreCase(part_number.replace(" ", ""))){
                return product_part.getPart_number().replace(" ", "").toUpperCase();
            }
        }
        return part_number.replace(" ", "").toUpperCase();
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

        public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/AddPartRevisionFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Registrar Revisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
