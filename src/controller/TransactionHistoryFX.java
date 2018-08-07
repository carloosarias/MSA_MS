/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.DepartLot;
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
    private TableColumn<IncomingLot, String> incomingid_column;
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
    private TableView<DepartLot> depart_tableview;
    @FXML
    private TableColumn<DepartLot, String> departid_column;
    @FXML
    private TableColumn<DepartLot, String> departdate_column;
    @FXML
    private TableColumn<DepartLot, String> departlotnumber_column;
    @FXML
    private TableColumn<DepartLot, String> departrevision_column;
    @FXML
    private TableColumn<DepartLot, Integer> departquantity_column;
    @FXML
    private TableColumn<DepartLot, Integer> departquantitybox_column;
    @FXML
    private TableColumn<DepartLot, String> departstatus_column;
    @FXML
    private TableColumn<DepartLot, String> departprocess_column;
    
    @FXML
    private TextField incomingqty_field;
    @FXML
    private TextField incomingnew_field;
    @FXML
    private TextField incomingrework_field;
    @FXML
    private TextField processqty_field;
    @FXML
    private TextField processgood_field;
    @FXML
    private TextField processbad_field;
    @FXML
    private TextField departqty_field;
    @FXML
    private TextField departaccepted_field;
    @FXML
    private TextField departrejected_field;
    @FXML
    private TextField qtypending_field;
    @FXML
    private TableView<weekly_summary> weekly_tableview;
    @FXML
    private TableColumn<weekly_summary, Date> weeklystartdate_column;
    @FXML
    private TableColumn<weekly_summary, Date> weeklyenddate_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyincomingtotal_column;
    @FXML
    private TableColumn<?, ?> weeklyincomingnew_column;
    @FXML
    private TableColumn<?, ?> weeklyincomingrejected_column;
    @FXML
    private TableColumn<?, ?> weeklyprocesstotal_column;
    @FXML
    private TableColumn<?, ?> weeklyprocessgood_column;
    @FXML
    private TableColumn<?, ?> weeklyprocessbad_column;
    @FXML
    private TableColumn<?, ?> weeklydeparttotal_column;
    @FXML
    private TableColumn<?, ?> weeklydepartaccepted_column;
    @FXML
    private TableColumn<?, ?> weeklydepartrejected_column;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartTableViewItems();
        setProcessReportTableViewItems();
        setIncomingTableViewItems();
        setWeeklyTableViewItems();
        partnumber_combo.setItems(FXCollections.observableList(msabase.getProductPartDAO().listActive(true)));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            updateList(partnumber_combo.getSelectionModel().getSelectedItem(), startdate_picker.getValue(), enddate_picker.getValue());
            weekly_tableview.setItems(FXCollections.observableArrayList(getWeeklySummaryList(partnumber_combo.getSelectionModel().getSelectedItem(), startdate_picker.getValue(), enddate_picker.getValue())));
            setFieldValues();
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            partnumber_combo.fireEvent(new ActionEvent());
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            partnumber_combo.fireEvent(new ActionEvent());
        });
    }
    
    public void updateList(ProductPart product_part, LocalDate start_date, LocalDate end_date){
        try{
            depart_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().listDateRange(product_part, java.sql.Date.valueOf(start_date), java.sql.Date.valueOf(end_date))));
            process_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listDateRange(product_part, java.sql.Date.valueOf(start_date), java.sql.Date.valueOf(end_date))));
            incoming_tableview.setItems(FXCollections.observableArrayList(msabase.getIncomingLotDAO().listDateRange(product_part, java.sql.Date.valueOf(start_date), java.sql.Date.valueOf(end_date))));
        }catch(Exception e){
            System.out.println("test");
        }
    }
    
    public void setIncomingTableViewItems(){
        incomingid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findIncomingReport(c.getValue()).getId() + ""));
        incomingdate_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findIncomingReport(c.getValue()).getReport_date().toString()));
        incominglotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        incomingrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findPartRevision(c.getValue()).getRev()));
        incomingquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        incomingboxquantity_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        incomingstatus_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setProcessReportTableViewItems(){
        processid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        processdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        processlotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        processrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findPartRevision(c.getValue()).getRev()));
        processquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        processstatus_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        processprocess_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setDepartTableViewItems(){
        departid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findDepartReport(c.getValue()).getId() + ""));
        departdate_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findDepartReport(c.getValue()).getReport_date().toString()));
        departlotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        departrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findPartRevision(c.getValue()).getRev()));
        departquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        departquantitybox_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        departstatus_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        departprocess_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setWeeklyTableViewItems(){
        weeklystartdate_column.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        weeklyenddate_column.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        weeklyincomingtotal_column.setCellValueFactory(new PropertyValueFactory<>("incoming_total"));
    }
    
    public void setFieldValues(){
        incomingqty_field.setText(""+getIncomingQuantity(incoming_tableview.getItems()));
        incomingnew_field.setText(""+getIncomingStatus(incoming_tableview.getItems(), "Virgen"));
        incomingrework_field.setText(""+getIncomingStatus(incoming_tableview.getItems(), "Rechazo"));
        processqty_field.setText(""+getProcessQuantity());
        processgood_field.setText(""+getProcessStatus("Bueno"));
        processbad_field.setText(""+getProcessStatus("Malo"));
        departqty_field.setText(""+getDepartQuantity());
        departrejected_field.setText(""+getDepartStatus("Rechazado"));
        departaccepted_field.setText(""+(getDepartQuantity() - getDepartStatus("Rechazado")));
        int balance = getIncomingQuantity(incoming_tableview.getItems())-getProcessQuantity();
        if(getIncomingQuantity(incoming_tableview.getItems())-getProcessQuantity() < 0){
            balance = 0;
        }
        qtypending_field.setText(""+balance);
    }
    
    public Integer getIncomingQuantity(List<IncomingLot> incominglot_list){
        int quantity_total = 0;
        
        for(IncomingLot item : incominglot_list){
            quantity_total += item.getQuantity();
        }
        return quantity_total;
    }
    
    public Integer getIncomingStatus(List<IncomingLot> incominglot_list, String status){
        int quantity_total = 0;
        
        for(IncomingLot item: incominglot_list){
            if(item.getStatus().equals(status)){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }
    
    public Integer getProcessQuantity(){
        List<ProcessReport> processreport_list = process_tableview.getItems(); 
        int quantity_total = 0;
        
        for(ProcessReport item : processreport_list){
            quantity_total += item.getQuantity();
        }
        
        return quantity_total;
    }
    
    public Integer getProcessStatus(String status){
        List<ProcessReport> processreport_list = process_tableview.getItems();
        int quantity_total = 0;
        
        for(ProcessReport item: processreport_list){
            if(item.getStatus().equals(status)){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }
    
    public Integer getDepartQuantity(){
        List<DepartLot> departlot_list = depart_tableview.getItems(); 
        int quantity_total = 0;
        
        for(DepartLot item : departlot_list){
            quantity_total += item.getQuantity();
        }
        
        return quantity_total;
    }
    
    public Integer getDepartStatus(String status){
        List<DepartLot> departlot_list = depart_tableview.getItems();
        int quantity_total = 0;
        
        for(DepartLot item: departlot_list){
            if(item.getStatus().equals(status)){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
        
    }
    
    public List<weekly_summary> getWeeklySummaryList(ProductPart product_part, LocalDate start_date, LocalDate end_date){
        List<weekly_summary> weekly_summary_list = new ArrayList<>();
        for(LocalDate current_date = start_date; current_date.isBefore(end_date); current_date = current_date.plusWeeks(1)){
            weekly_summary_list.add(new weekly_summary(product_part, java.sql.Date.valueOf(current_date), java.sql.Date.valueOf(current_date.plusWeeks(1))));
        }
        return weekly_summary_list;
    }
    
    public class weekly_summary{
        private Date start_date;
        private Date end_date;
        private Integer incoming_total;
        
        public weekly_summary(ProductPart product_part, Date start_date, Date end_date){
            this.start_date = start_date;
            this.end_date = end_date;
            incoming_total = getIncomingQuantity(msabase.getIncomingLotDAO().listDateRange(product_part, start_date, end_date));
        }

        public Date getStart_date() {
            return start_date;
        }

        public void setStart_date(Date start_date) {
            this.start_date = start_date;
        }

        public Date getEnd_date() {
            return end_date;
        }

        public void setEnd_date(Date end_date) {
            this.end_date = end_date;
        }

        public Integer getIncoming_total() {
            return incoming_total;
        }

        public void setIncoming_total(Integer incoming_total) {
            this.incoming_total = incoming_total;
        }
    }
}
