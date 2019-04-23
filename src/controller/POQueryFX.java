/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.POQuery;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class POQueryFX implements Initializable {

    @FXML
    private TableView<POQuery> poquery_tableview;
    @FXML
    private TableColumn<POQuery, String> company_column;
    @FXML
    private TableColumn<POQuery, String> ponumber_column;
    @FXML
    private TableColumn<POQuery, String> partnumber_column;
    @FXML
    private TableColumn<POQuery, String> rev_column;
    @FXML
    private TableColumn<POQuery, Integer> incoming_column;
    @FXML
    private TableColumn<POQuery, Integer> depart_column;
    @FXML
    private TableColumn<POQuery, Integer> scrap_column;
    @FXML
    private TableColumn<POQuery, Integer> balance_column;
    @FXML
    private TextField ponumber_field;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private TextField partnumber_field;
    @FXML
    private Button reset_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPOQueryTable();
        updatePOQueryTable();
        resetFields();
        
        ponumber_field.setOnAction((ActionEvent) -> updatePOQueryTable());
        company_combo.setOnAction(ponumber_field.getOnAction());
        partnumber_field.setOnAction(ponumber_field.getOnAction());
        reset_button.setOnAction((ActionEvent) -> {
            resetFields();
        });
        
    }
    
    public void resetFields(){
        ponumber_field.clear();
        company_combo.getSelectionModel().clearSelection();
        company_combo.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        partnumber_field.clear();
        updatePOQueryTable();
    }
    
    public void updatePOQueryTable(){
        poquery_tableview.getItems().setAll(msabase.getPOQueryDAO().list(company_combo.getSelectionModel().getSelectedItem(), ponumber_field.getText(), partnumber_field.getText()));
    }
    
    public void setPOQueryTable(){
        company_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getProduct_part().getCompany().getName()));
        ponumber_column.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getProduct_part().getPart_number()));
        rev_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getRev()));
        incoming_column.setCellValueFactory(new PropertyValueFactory<>("incoming_qty"));
        depart_column.setCellValueFactory(new PropertyValueFactory<>("depart_qty"));
        scrap_column.setCellValueFactory(new PropertyValueFactory<>("scrap_qty"));
        balance_column.setCellValueFactory(new PropertyValueFactory<>("balance_qty"));
    }
    
}
