/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Company;
import model.DepartReport;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
import model.Specification;
import static msa_ms.MainApp.df;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PartRevisionFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> counter_column;
    @FXML
    private TableColumn<PartRevision, String> company_column;
    @FXML
    private TableColumn<PartRevision, ProductPart> partnumber_column1;
    @FXML
    private TableColumn<PartRevision, String> rev_column;
    @FXML
    private TableColumn<PartRevision, String> revdate_column;
    @FXML
    private TableColumn<PartRevision, Metal> basemetal_column;
    @FXML
    private TableColumn<PartRevision, Specification> specnumber_column;
    @FXML
    private TableColumn<PartRevision, String> area_column;
    @FXML
    private TableColumn<PartRevision, String> baseweight_column;
    @FXML
    private TableColumn<PartRevision, String> finalweight_column;
    @FXML
    private Button delete_button;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private ComboBox<Metal> metal_combo1;
    @FXML
    private ComboBox<Specification> spec_combo1;
    @FXML
    private Button reset_button;
    @FXML
    private TextField part_field1;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private ComboBox<ProductPart> part_combo2;
    @FXML
    private ComboBox<Metal> metal_combo2;
    @FXML
    private ComboBox<Specification> spec_combo2;
    @FXML
    private Button add_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setPartRevisionTable();
        updatePartRevisionTable();
        updateComboItems();
        
        delete_button.disableProperty().bind(partrevision_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        company_combo1.setOnAction((ActionEvent) -> {updatePartRevisionTable();});
        metal_combo1.setOnAction(company_combo1.getOnAction());
        spec_combo1.setOnAction(company_combo1.getOnAction());
        part_field1.setOnAction(company_combo1.getOnAction());
        reset_button.setOnAction((ActionEvent) -> {
            company_combo1.getSelectionModel().clearSelection(); 
            metal_combo1.getSelectionModel().clearSelection();
            spec_combo1.getSelectionModel().clearSelection();
            part_field1.clear();
            updatePartRevisionTable();
        });
        
        part_combo2.disableProperty().bind(company_combo2.valueProperty().isNull());
        add_button.disableProperty().bind(company_combo2.valueProperty().isNull().or(part_combo2.valueProperty().isNull()).or(metal_combo2.valueProperty().isNull().or(spec_combo2.valueProperty().isNull())));
        
        company_combo2.setOnAction((ActionEvent) -> {
            part_combo2.getSelectionModel().clearSelection();
            part_combo2.getItems().setAll(msabase.getProductPartDAO().list(company_combo2.getSelectionModel().getSelectedItem(), ""));
        });
        
        add_button.setOnAction((ActionEvent) -> {
            createPartRevision();
            company_combo2.getSelectionModel().clearSelection(); 
            metal_combo2.getSelectionModel().clearSelection();
            spec_combo2.getSelectionModel().clearSelection();
            part_combo2.getSelectionModel().clearSelection();
        });
        
        delete_button.setOnAction((ActionEvent) -> {disablePartRevision();});
        
    }
    
    public void disablePartRevision(){
        partrevision_tableview.getSelectionModel().getSelectedItem().setActive(false);
        System.out.println(partrevision_tableview.getSelectionModel().getSelectedItem().isActive());
        msabase.getPartRevisionDAO().update(partrevision_tableview.getSelectionModel().getSelectedItem());
        System.out.println(partrevision_tableview.getSelectionModel().getSelectedItem().isActive());
        updatePartRevisionTable();
    }
    
    public void createPartRevision(){
        reset_button.getOnAction();
        int current_size = partrevision_tableview.getItems().size();

        PartRevision part_revision = new PartRevision();
        part_revision.setRev_date(DAOUtil.toUtilDate(LocalDate.now()));
        part_revision.setProduct_part(part_combo2.getValue());
        part_revision.setMetal(metal_combo2.getValue());
        part_revision.setSpecification(spec_combo2.getValue());
        part_revision.setRev("N/A");
        part_revision.setArea(0.0);
        part_revision.setBase_weight(0.0);
        part_revision.setFinal_weight(0.0);
        part_revision.setActive(true);
        msabase.getPartRevisionDAO().create(part_revision);

        updatePartRevisionTable();

        if(current_size < partrevision_tableview.getItems().size()){
            partrevision_tableview.scrollTo(part_revision);
            partrevision_tableview.getSelectionModel().select(part_revision);
        }
    }
    
    public void updateComboItems(){
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        metal_combo1.getItems().setAll(msabase.getMetalDAO().list(true));
        spec_combo1.getItems().setAll(msabase.getSpecificationDAO().list(true));
        part_combo2.getItems().setAll(msabase.getProductPartDAO().list());

        company_combo2.itemsProperty().bind(company_combo1.itemsProperty());
        metal_combo2.itemsProperty().bind(metal_combo1.itemsProperty());
        spec_combo2.itemsProperty().bind(spec_combo1.itemsProperty());
    }
    
    public void setPartRevisionTable(){
        counter_column.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getTableView().getItems().indexOf(c.getValue())+1)));
        company_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct_part().getCompany().toString()));
        partnumber_column1.setCellValueFactory(new PropertyValueFactory<>("product_part"));
        revdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getRev_date()))));
        specnumber_column.setCellValueFactory(new PropertyValueFactory<>("specification"));
        basemetal_column.setCellValueFactory(new PropertyValueFactory<>("metal"));
        
        rev_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        rev_column.setCellFactory(TextFieldTableCell.forTableColumn());
        rev_column.setOnEditCommit((TableColumn.CellEditEvent<PartRevision, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRev(getRevValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
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
            System.out.println(part_field1.getText());
            partrevision_tableview.getItems().setAll(msabase.getPartRevisionDAO().list(company_combo1.getSelectionModel().getSelectedItem(), metal_combo1.getSelectionModel().getSelectedItem(), spec_combo1.getSelectionModel().getSelectedItem(), part_field1.getText()));
        }catch(Exception e){
            System.out.println("fail");
            partrevision_tableview.getItems().clear();
        }
    }
    
    public String getRevValue(PartRevision part_revision, String rev){
        for(PartRevision item : partrevision_tableview.getItems()){
            if(item.getRev().replace(" ", "").equalsIgnoreCase(rev.replace(" ", "")) && item.getProduct_part().getPart_number().equalsIgnoreCase(part_revision.getProduct_part().getPart_number())){
                return part_revision.getRev().replace(" ", "").toUpperCase();
            }
        }
        return rev.replace(" ", "").toUpperCase();
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
