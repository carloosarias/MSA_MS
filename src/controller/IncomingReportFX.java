/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.IncomingItem;
import model.IncomingLot;
import model.IncomingReport;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class IncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<IncomingReport> incoming_report_tableview;
    @FXML
    private TableColumn<IncomingReport, Integer> report_id_column;
    @FXML
    private TableColumn<IncomingReport, String> report_employee_column;
    @FXML
    private TableColumn<IncomingReport, Date> report_date_column;
    @FXML
    private TableColumn<IncomingReport, String> report_client_column;
    @FXML
    private TableColumn<IncomingReport, String> report_ponumber_column;
    @FXML
    private TableColumn<IncomingReport, String> report_packinglist_column;
    @FXML
    private TableView<IncomingItem> incomingitem_tableview;
    @FXML
    private TableColumn<IncomingItem, String> part_column;
    @FXML
    private TableColumn<IncomingItem, String> revision_column;
    @FXML
    private TableColumn<IncomingItem, String> item_qty_column;
    @FXML
    private TableColumn<IncomingItem, String> item_boxqty_column;
    @FXML
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, String> lot_column;
    @FXML
    private TableColumn<IncomingLot, Integer> lot_qty;
    @FXML
    private TableColumn<IncomingLot, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<IncomingLot, String> lot_status_column;
    @FXML
    private TableColumn<IncomingLot, String> lot_comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setReportTable();
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
        });
    }
    
    public void showAdd_stage(){
        
    }
    
    public void setReportTable(){
        report_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        report_employee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingReportDAO().findEmployee(c.getValue()).toString()));
        report_date_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        report_client_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingReportDAO().findCompany(c.getValue()).toString()));
        report_ponumber_column.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        report_packinglist_column.setCellValueFactory(new PropertyValueFactory<>("packing_list"));
    }
    
    public void setItemTable(){
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
                msabase.getPartRevisionDAO().findProductPart(
                       msabase .getIncomingItemDAO().findPartRevision(c.getValue())
                ).toString()
        ));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingItemDAO().findPartRevision(c.getValue()).getRev()));
        item_qty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getIncomingLotDAO().getTotalQuantity(c.getValue())));
        item_boxqty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getIncomingLotDAO().getTotalBoxQuantity(c.getValue())));
    }
    
    public void setLotTable(){
        lot_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        lot_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        lot_comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
}
