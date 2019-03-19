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
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, String> id_column1;
    @FXML
    private TableColumn<IncomingLot, String> date_column1;
    @FXML
    private TableColumn<IncomingLot, String> lot_column1;
    @FXML
    private TableColumn<IncomingLot, String> rev_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> boxquantity_column1;
    @FXML
    private TableColumn<IncomingLot, String> status_column1;
    
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
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> id_column2;
    @FXML
    private TableColumn<DepartLot, String> date_column2;
    @FXML
    private TableColumn<DepartLot, String> part_column2;
    @FXML
    private TableColumn<DepartLot, String> rev_column2;
    @FXML
    private TableColumn<DepartLot, String> lot_column2;
    @FXML
    private TableColumn<DepartLot, Integer> quantity_column2;
    @FXML
    private TableColumn<DepartLot, Integer> boxquantity_column2;
    @FXML
    private TableColumn<DepartLot, String> process_column2;
    @FXML
    private TableColumn<DepartLot, String> status_column2;
    
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
    
    private List<DepartLot> departlot_list;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartLotTable();
        setProcessReportTable();
        setIncomingReportTable();
        //setWeeklyTableViewItems();
        partnumber_combo.getItems().setAll(msabase.getProductPartDAO().listActive(true));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            updateDepartLotTable();
            //weekly_tableview.setItems(FXCollections.observableArrayList(getWeeklySummaryList(partnumber_combo.getSelectionModel().getSelectedItem(), startdate_picker.getValue(), enddate_picker.getValue())));
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
    
    public void updateDepartLotTable(){
        departlot_list = msabase.getDepartLotDAO().listDateRange(partnumber_combo.getValue(), DAOUtil.toUtilDate(startdate_picker.getValue().minusDays(1)), DAOUtil.toUtilDate(enddate_picker.getValue()));
        departlot_tableview.getItems().setAll(mergeByDepartReport_Partnumber(departlot_list));
    }
    
    public void updateList(ProductPart product_part, LocalDate start_date, LocalDate end_date){
        try{
            process_tableview.setItems(FXCollections.observableArrayList(msabase.getProcessReportDAO().listProductPartDateRange(product_part, DAOUtil.toUtilDate(start_date), DAOUtil.toUtilDate(end_date))));
            incominglot_tableview.setItems(FXCollections.observableArrayList(msabase.getIncomingLotDAO().listDateRange(product_part, false, DAOUtil.toUtilDate(start_date), DAOUtil.toUtilDate(end_date))));
        }catch(Exception e){
            System.out.println("Failed to update transaction history list");
        }
    }
    
    public void setIncomingReportTable(){
        id_column1.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getIncomingreport_id()));
        date_column1.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        lot_column1.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        rev_column1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision()));
        quantity_column1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column1.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        status_column1.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public void setProcessReportTable(){
        processid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        processdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        processlotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        processrevision_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRev()));
        processquantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        processstatus_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().quality_passedToString()));
        processprocess_column.setCellValueFactory(new PropertyValueFactory<>("process"));
    }
    
    public void setDepartLotTable(){
        id_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartreport_id()+""));
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        part_column2.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision()));
        lot_column2.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column2.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        status_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
    }
    /*
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
    */
    public void setFieldValues(){
        incomingqty_field.setText(""+getIncomingQuantity(incominglot_tableview.getItems()));
        incomingnew_field.setText(""+getIncomingStatus(incominglot_tableview.getItems(), "Virgen"));
        incomingrework_field.setText(""+getIncomingStatus(incominglot_tableview.getItems(), "Rechazo"));
        //scrapqty_field.setText(""+getScrapReportQuantity(msabase.getScrapReportDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))));
        processqty_field.setText(""+getProcessQuantity(process_tableview.getItems()));
        processgood_field.setText(""+getProcessStatus(process_tableview.getItems(), "Bueno"));
        processbad_field.setText(""+getProcessStatus(process_tableview.getItems(), "Malo"));
        departqty_field.setText(""+getDepartQuantity(departlot_tableview.getItems()));
        departrejected_field.setText(""+getDepartStatus(departlot_tableview.getItems(), "Rechazado"));
        departaccepted_field.setText(""+(getDepartQuantity(departlot_tableview.getItems()) - getDepartStatus(departlot_tableview.getItems(), "Rechazado")));
        //onhand_field.setText(""+(getIncomingQuantity(msabase.getIncomingLotDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), false, DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))) - getDepartQuantity(msabase.getDepartLotDAO().listDateRange(partnumber_combo.getSelectionModel().getSelectedItem(), DAOUtil.toUtilDate(LocalDate.MIN), DAOUtil.toUtilDate(LocalDate.now()))) - getScrapReportQuantity(msabase.getScrapReportDAO().listProductPart(partnumber_combo.getSelectionModel().getSelectedItem()))));
        //balance_field.setText(""+((getIncomingQuantity(incominglot_tableview.getItems()))-(getDepartQuantity(departlot_tableview.getItems())) - Integer.parseInt(scrapqty_field.getText())));
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
    public List<DepartLot> mergeByDepartReport_Partnumber(List<DepartLot> unfilteredList){
        //find all part_number
        ArrayList<Integer> departreport_id = new ArrayList();
        ArrayList<String> partnumber = new ArrayList();
        ArrayList<String> part_revision = new ArrayList();
        ArrayList<String> lot_number = new ArrayList();
        ArrayList<String> status = new ArrayList();
        ArrayList<String> process = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        
        for(DepartLot depart_lot : unfilteredList){
            if(process.contains(depart_lot.getProcess()) && status.contains(depart_lot.getStatus()) && lot_number.contains(depart_lot.getLot_number()) && departreport_id.contains(depart_lot.getDepartreport_id()) && partnumber.contains(depart_lot.getPart_number()) && part_revision.contains(depart_lot.getPart_revision())){
                for(DepartLot listitem : mergedList){
                    if(depart_lot.getProcess().equals(listitem.getProcess()) && depart_lot.getStatus().equals(listitem.getStatus()) && depart_lot.getLot_number().equals(listitem.getLot_number()) && depart_lot.getDepartreport_id().equals(listitem.getDepartreport_id()) && depart_lot.getPart_number().equals(listitem.getPart_number()) && depart_lot.getPart_revision().equals(listitem.getPart_revision())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + depart_lot.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + depart_lot.getBox_quantity());
                        break;
                    }
                }
            }
            else{
                departreport_id.add(depart_lot.getDepartreport_id());
                partnumber.add(depart_lot.getPart_number());
                part_revision.add(depart_lot.getPart_revision());
                lot_number.add(depart_lot.getLot_number());
                status.add(depart_lot.getStatus());
                process.add(depart_lot.getProcess());
                
                DepartLot item = new DepartLot();
                item.setReport_date(depart_lot.getReport_date());
                item.setPart_revision(depart_lot.getPart_revision());
                item.setDepartreport_id(depart_lot.getDepartreport_id());
                item.setPart_number(depart_lot.getPart_number());
                item.setPart_revision(depart_lot.getPart_revision());
                item.setQuantity(depart_lot.getQuantity());
                item.setBox_quantity(depart_lot.getBox_quantity());
                item.setLot_number(depart_lot.getLot_number());
                item.setProcess(depart_lot.getProcess());
                item.setPending(depart_lot.isPending());
                item.setRejected(depart_lot.isPending());
                mergedList.add(item);
            }
        }
        
        return mergedList;
    }
}
