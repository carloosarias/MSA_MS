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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import model.ScrapReport;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;

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
    private TableColumn<ProcessReport, String> processdate_column;
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
    private TextField scrapqty_field;
    @FXML
    private TextField onhand_field;
    @FXML
    private TextField balance_field;
    @FXML
    private TableView<weekly_summary> weekly_tableview;
    @FXML
    private TableColumn<weekly_summary, String> weeklystartdate_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyenddate_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyincomingtotal_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyincomingnew_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyincomingrejected_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyprocesstotal_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyprocessgood_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklyprocessbad_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklydeparttotal_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklydepartaccepted_column;
    @FXML
    private TableColumn<weekly_summary, String> weeklydepartrejected_column;
    @FXML
    private Button update_button;
    
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
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
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
        
        update_button.setOnAction((ActionEvent) -> {
           partnumber_combo.fireEvent(new ActionEvent()); 
        });
    }
    
    public void updateList(ProductPart product_part, LocalDate start_date, LocalDate end_date){
        try{
            depart_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().listDateRange(product_part, DAOUtil.toUtilDate(start_date), DAOUtil.toUtilDate(end_date))));
            process_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listProductPartDateRange(product_part, DAOUtil.toUtilDate(start_date), DAOUtil.toUtilDate(end_date))));
            incoming_tableview.setItems(FXCollections.observableArrayList(msabase.getIncomingLotDAO().listDateRange(product_part, false, DAOUtil.toUtilDate(start_date), DAOUtil.toUtilDate(end_date))));
        }catch(Exception e){
            System.out.println("Failed to update transaction history list");
        }
    }
    
    public void setIncomingTableViewItems(){
        incomingid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findIncomingReport(c.getValue()).getId() + ""));
        incomingdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(msabase.getIncomingLotDAO().findIncomingReport(c.getValue()).getReport_date()))));
        incominglotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        incomingrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getIncomingLotDAO().findPartRevision(c.getValue()).getRev()));
        incomingquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        incomingboxquantity_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        incomingstatus_column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setProcessReportTableViewItems(){
        processid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        processdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        processlotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        processrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getProcessReportDAO().findPartRevision(c.getValue()).getRev()));
        processquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        processstatus_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().quality_passedToString()));
        processprocess_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setDepartTableViewItems(){
        departid_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findDepartReport(c.getValue()).getId() + ""));
        departdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(msabase.getDepartLotDAO().findDepartReport(c.getValue()).getReport_date()))));
        departlotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        departrevision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartLotDAO().findPartRevision(c.getValue()).getRev()));
        departquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        departquantitybox_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        departstatus_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        departprocess_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setWeeklyTableViewItems(){
        weeklystartdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getStart_date()))));
        weeklyenddate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getEnd_date()))));
        weeklyincomingtotal_column.setCellValueFactory(new PropertyValueFactory<>("incoming_total"));
        weeklyincomingnew_column.setCellValueFactory(new PropertyValueFactory<>("incoming_new"));
        weeklyincomingrejected_column.setCellValueFactory(new PropertyValueFactory<>("incoming_rejected"));
        weeklyprocesstotal_column.setCellValueFactory(new PropertyValueFactory<>("process_total"));
        weeklyprocessgood_column.setCellValueFactory(new PropertyValueFactory<>("process_good"));
        weeklyprocessbad_column.setCellValueFactory(new PropertyValueFactory<>("process_bad"));
        weeklydeparttotal_column.setCellValueFactory(new PropertyValueFactory<>("depart_total"));
        weeklydepartaccepted_column.setCellValueFactory(new PropertyValueFactory<>("depart_accepted"));
        weeklydepartrejected_column.setCellValueFactory(new PropertyValueFactory<>("depart_rejected"));
    }
    
    public void setFieldValues(){
        incomingqty_field.setText(""+getIncomingQuantity(incoming_tableview.getItems()));
        incomingnew_field.setText(""+getIncomingStatus(incoming_tableview.getItems(), "Virgen"));
        incomingrework_field.setText(""+getIncomingStatus(incoming_tableview.getItems(), "Rechazo"));
        scrapqty_field.setText(""+getScrapReportQuantity(msabase.getScrapReportDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))));
        processqty_field.setText(""+getProcessQuantity(process_tableview.getItems()));
        processgood_field.setText(""+getProcessStatus(process_tableview.getItems(), "Bueno"));
        processbad_field.setText(""+getProcessStatus(process_tableview.getItems(), "Malo"));
        departqty_field.setText(""+getDepartQuantity(depart_tableview.getItems()));
        departrejected_field.setText(""+getDepartStatus(depart_tableview.getItems(), "Rechazado"));
        departaccepted_field.setText(""+(getDepartQuantity(depart_tableview.getItems()) - getDepartStatus(depart_tableview.getItems(), "Rechazado")));
        onhand_field.setText(""+(getIncomingQuantity(msabase.getIncomingLotDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), false, DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))) - getDepartQuantity(msabase.getDepartLotDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))) - getScrapReportQuantity(msabase.getScrapReportDAO().listProductPart(partnumber_combo.getSelectionModel().getSelectedItem()))));
        balance_field.setText(""+((getIncomingQuantity(incoming_tableview.getItems()))-(getDepartQuantity(depart_tableview.getItems())) - Integer.parseInt(scrapqty_field.getText())));
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
    
    public Integer getScrapReportQuantity(List<ScrapReport> scrapreport_list){
        int quantity_total = 0;
        
        for(ScrapReport item : scrapreport_list){
            quantity_total += item.getQuantity();
        }
        
        return quantity_total;
    }
    
    public Integer getProcessQuantity(List<ProcessReport> processreport_list){ 
        int quantity_total = 0;
        
        for(ProcessReport item : processreport_list){
            quantity_total += item.getQuantity();
        }
        
        return quantity_total;
    }
    
    public Integer getProcessStatus(List<ProcessReport> processreport_list, String status){
        int quantity_total = 0;
        
        for(ProcessReport item: processreport_list){
            if(item.quality_passedToString().equals(status)){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }
    
    public Integer getDepartQuantity(List<DepartLot> departlot_list){
        int quantity_total = 0;
        
        for(DepartLot item : departlot_list){
            quantity_total += item.getQuantity();
        }
        
        return quantity_total;
    }
    
    
    public Integer getDepartStatus(List<DepartLot> departlot_list, String status){
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
        for(LocalDate current_date = start_date; current_date.isBefore(end_date); current_date = current_date.plusWeeks(1).plusDays(1)){
            weekly_summary_list.add(new weekly_summary(product_part, DAOUtil.toUtilDate(current_date), DAOUtil.toUtilDate(current_date.plusWeeks(1))));
        }
        return weekly_summary_list;
    }
    
    public class weekly_summary{
        private Date start_date;
        private Date end_date;
        private Integer incoming_total;
        private Integer incoming_new;
        private Integer incoming_rejected;
        private Integer process_total;
        private Integer process_good;
        private Integer process_bad;
        private Integer depart_total;
        private Integer depart_accepted;
        private Integer depart_rejected;
        
        public weekly_summary(ProductPart product_part, Date start_date, Date end_date){
            List<IncomingLot> incoming_list = msabase.getIncomingLotDAO().listDateRange(product_part, false, start_date, end_date);
            List<DepartLot> depart_list = msabase.getDepartLotDAO().listDateRange(product_part, start_date, end_date);
            List<ProcessReport> process_list = msabase.getProcessReportDAO().listProductPartDateRange(product_part, start_date, end_date);
            this.start_date = start_date;
            this.end_date = end_date;
            incoming_total = getIncomingQuantity(incoming_list);
            incoming_new = getIncomingStatus(incoming_list, "Virgen");
            incoming_rejected = getIncomingStatus(incoming_list, "Rechazo");
            process_total = getProcessQuantity(process_list);
            process_good = getProcessStatus(process_list, "Bueno");
            process_bad = getProcessStatus(process_list, "Malo");
            depart_total = getDepartQuantity(depart_list);
            depart_rejected = getDepartStatus(depart_list, "Rechazado");
            depart_accepted = depart_total - depart_rejected;
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

        public Integer getIncoming_new() {
            return incoming_new;
        }

        public void setIncoming_new(Integer incoming_new) {
            this.incoming_new = incoming_new;
        }

        public Integer getIncoming_rejected() {
            return incoming_rejected;
        }

        public void setIncoming_rejected(Integer incoming_rejected) {
            this.incoming_rejected = incoming_rejected;
        }

        public Integer getProcess_total() {
            return process_total;
        }

        public void setProcess_total(Integer process_total) {
            this.process_total = process_total;
        }

        public Integer getProcess_good() {
            return process_good;
        }

        public void setProcess_good(Integer process_good) {
            this.process_good = process_good;
        }

        public Integer getProcess_bad() {
            return process_bad;
        }

        public void setProcess_bad(Integer process_bad) {
            this.process_bad = process_bad;
        }

        public Integer getDepart_total() {
            return depart_total;
        }

        public void setDepart_total(Integer depart_total) {
            this.depart_total = depart_total;
        }

        public Integer getDepart_accepted() {
            return depart_accepted;
        }

        public void setDepart_accepted(Integer depart_accepted) {
            this.depart_accepted = depart_accepted;
        }

        public Integer getDepart_rejected() {
            return depart_rejected;
        }

        public void setDepart_rejected(Integer depart_rejected) {
            this.depart_rejected = depart_rejected;
        }
    }
}
