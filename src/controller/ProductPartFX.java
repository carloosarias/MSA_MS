/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.ProductPart;

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
    private TableView<?> partrevision_tableview;
    @FXML
    private TableColumn<?, ?> partrevisionid_column;
    @FXML
    private TableColumn<?, ?> rev_column;
    @FXML
    private TableColumn<?, ?> revdate_column;
    @FXML
    private TableColumn<?, ?> basemetal_column;
    @FXML
    private TableColumn<?, ?> finalprocess_column;
    @FXML
    private TableColumn<?, ?> specificationnumber_column;
    @FXML
    private TableColumn<?, ?> area_column;
    @FXML
    private TableColumn<?, ?> baseweight_column;
    @FXML
    private TableColumn<?, ?> finalweight_column;
    @FXML
    private TableColumn<?, ?> partrevisionstatus_column;
    @FXML
    private Button addproductpart_button;
    @FXML
    private Button addpartrevision_button;
    
    private Stage add_stage = new Stage();
    
    private List<String> productpartstatus_items = Arrays.asList("Activo", "Inactivo");
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setProductPartTable();
    }    
    
    public void setProductPartTable(){
        productpartid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("quote_date"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        productpartstatus_column.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(productpartstatus_items)));
        productpartstatus_column.setOnEditCommit((TableColumn.CellEditEvent<ProductPart, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setActive(getActive(t.getNewValue()));
            msabase.getProductPartDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            setProductPartItems();
        });
    }
    
    public void setProductPartItems(){
        productpart_tableview.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
    }
    public boolean getActive(String productpart_status){
        if(productpart_status.equals("Activo")){
            return true;
        }else{
            return false;
        }
    }
    
}
