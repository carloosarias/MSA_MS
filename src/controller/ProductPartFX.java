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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Company;
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
    private ComboBox<Company> company_combo1;
    @FXML
    private TextField part_field;
    @FXML
    private Button reset_button;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private Button add_button;
    @FXML
    private TableView<ProductPart> productpart_tableview;
    @FXML
    private TableColumn<ProductPart, Company> company_column;
    @FXML
    private TableColumn<ProductPart, String> counter_column;
    @FXML
    private TableColumn<ProductPart, String> partnumber_column;
    @FXML
    private TableColumn<ProductPart, String> description_column;
    @FXML
    private Button delete_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMaximumFractionDigits(6);
        setProductPartTable();
        updateProductPartTable();
        
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.setItems(company_combo1.getItems());
        
        delete_button.disableProperty().bind(productpart_tableview.getSelectionModel().selectedItemProperty().isNull());
        add_button.disableProperty().bind(company_combo2.getSelectionModel().selectedItemProperty().isNull());
        
        company_combo1.setOnAction((ActionEvent) -> {updateProductPartTable();});
        part_field.setOnAction(company_combo1.getOnAction());
        reset_button.setOnAction((ActionEvent) -> {
            company_combo1.getSelectionModel().clearSelection();
            part_field.clear();
            updateProductPartTable();
        });
        
        add_button.setOnAction((ActionEvent) -> {
            createProductPart();
        });
    }
    
    public void createProductPart(){
        int current_size = productpart_tableview.getItems().size();
        ProductPart product_part = new ProductPart();
        product_part.setCompany(company_combo2.getSelectionModel().getSelectedItem());
        product_part.setPart_number("N/A");
        product_part.setDescription("N/A");
        product_part.setActive(true);
        msabase.getProductPartDAO().create(product_part);
            updateProductPartTable();
            if(current_size < productpart_tableview.getItems().size()){
                productpart_tableview.scrollTo(product_part);
                productpart_tableview.getSelectionModel().select(product_part);
            }
    }
 
    
    public void setProductPartTable(){
        counter_column.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getTableView().getItems().indexOf(c.getValue())+1)));
        company_column.setCellValueFactory(new PropertyValueFactory<>("company"));
        company_column.setCellFactory(ComboBoxTableCell.forTableColumn(company_combo1.getItems()));
        company_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, Company> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setCompany(t.getNewValue());
            msabase.getProductPartDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            productpart_tableview.refresh();
        });
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
    
    public void updateProductPartTable(){
        productpart_tableview.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list(company_combo1.getSelectionModel().getSelectedItem(), part_field.getText())));
    }

    
    public void disableProductPart(){
        productpart_tableview.getSelectionModel().getSelectedItem().setActive(false);
        msabase.getProductPartDAO().update(productpart_tableview.getSelectionModel().getSelectedItem());
    }
    
    public String getPartNumberValue(ProductPart product_part, String part_number){
        for(ProductPart item : productpart_tableview.getItems()){
            if(item.getPart_number().replace(" ", "").equalsIgnoreCase(part_number.replace(" ", ""))){
                return product_part.getPart_number().replace(" ", "").toUpperCase();
            }
        }
        return part_number.replace(" ", "").toUpperCase();
    } 
}
