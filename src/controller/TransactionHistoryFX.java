/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.IncomingLot;
import model.ProcessReport;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class TransactionHistoryFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    
    //IncomingLot TableView ----------------------------------------------------
    @FXML
    private TableView<IncomingLot> incoming_tableview;
    @FXML
    private TableColumn<IncomingLot, Integer> incomingid_column;
    @FXML
    private TableColumn<IncomingLot, String> incomingdate_column;
    @FXML
    private TableColumn<IncomingLot, String> incominglotnumber_column;
    @FXML
    private TableColumn<IncomingLot, String> incomingrevision_column;
    @FXML
    private TableColumn<IncomingLot, Integer> incomingquantity_column;
    @FXML
    private TableColumn<IncomingLot, Integer> incomingboxquantity_column;
    @FXML
    private TableColumn<IncomingLot, String> incomingstatus_column;
    
    //ProcessReport TableView --------------------------------------------------
    @FXML
    private TableView<ProcessReport> process_tableview;
    @FXML
    private TableColumn<ProcessReport, Integer> processid_column;
    @FXML
    private TableColumn<ProcessReport, Date> processdate_column;
    @FXML
    private TableColumn<ProcessReport, String> processlotnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> processrevision_column;
    @FXML
    private TableColumn<ProcessReport, Integer> processquantity_column;
    @FXML
    private TableColumn<ProcessReport, String> processstatus_column;
    @FXML
    private TableColumn<ProcessReport, String> processprocess_column;
    
    //DepartLot TableView ------------------------------------------------------
    @FXML
    private TableView<?> depart_tableview;
    @FXML
    private TableColumn<?, ?> departid_column;
    @FXML
    private TableColumn<?, ?> departdate_column;
    @FXML
    private TableColumn<?, ?> departlotnumber_column;
    @FXML
    private TableColumn<?, ?> departrevision_column;
    @FXML
    private TableColumn<?, ?> departquantity_column;
    @FXML
    private TableColumn<?, ?> departquantitybox_column;
    @FXML
    private TableColumn<?, ?> departstatus_column;
    @FXML
    private TableColumn<?, ?> departprocess_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIncomingTableViewItems();
        partnumber_combo.setItems(FXCollections.observableList(msabase.getProductPartDAO().listActive(true)));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            updateList(partnumber_combo.getSelectionModel().getSelectedItem(), java.sql.Date.valueOf(startdate_picker.getValue()), java.sql.Date.valueOf(enddate_picker.getValue()));
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            partnumber_combo.fireEvent(new ActionEvent());
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            partnumber_combo.fireEvent(new ActionEvent());
        });
    }
    
    public void updateList(ProductPart product_part, Date start_date, Date end_date){
        try{
            msabase.getDepartLotDAO().listDateRange(product_part, start_date, end_date);
            msabase.getProcessReportDAO().listDateRange(product_part, start_date, end_date);
            incoming_tableview.setItems(FXCollections.observableArrayList(msabase.getIncomingLotDAO().listDateRange(product_part, start_date, end_date)));
        }catch(Exception e){
            System.out.println("test");
        }
    }
    
    public void setIncomingTableViewItems(){
        incomingid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        incomingdate_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findIncomingReport(c.getValue()).getReport_date().toString()));
        incominglotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        incomingrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findPartRevision(c.getValue()).getRev()));
        incomingquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        incomingboxquantity_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        incomingstatus_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setProcessReportTableViewItems(){
            private TableColumn<ProcessReport, Integer> processid_column;
    @FXML
    private TableColumn<ProcessReport, Date> processdate_column;
    @FXML
    private TableColumn<ProcessReport, String> processlotnumber_column;
    @FXML
    private TableColumn<ProcessReport, String> processrevision_column;
    @FXML
    private TableColumn<ProcessReport, Integer> processquantity_column;
    @FXML
    private TableColumn<ProcessReport, String> processstatus_column;
    @FXML
    private TableColumn<ProcessReport, String> processprocess_column;
    }
    
}
