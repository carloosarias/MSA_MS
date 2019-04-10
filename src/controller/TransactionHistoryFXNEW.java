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
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.DepartLot;
import model.IncomingLot;
import model.ProcessReport;
import model.ProductPart;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class TransactionHistoryFXNEW implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;
    @FXML
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, Integer> id_column1;
    @FXML
    private TableColumn<IncomingLot, String> date_column1;
    @FXML
    private TableColumn<IncomingLot, String> part_column1;
    @FXML
    private TableColumn<IncomingLot, String> rev_column1;
    @FXML
    private TableColumn<IncomingLot, String> lot_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> boxquantity_column1;
    @FXML
    private TableColumn<IncomingLot, String> status_column1;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, Integer> id_column2;
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
    private TableView<ProcessReport> processreport_tableview;
    @FXML
    private TableColumn<ProcessReport, Integer> tank_column;
    @FXML
    private TableColumn<ProcessReport, String> process_column3;
    @FXML
    private TableColumn<ProcessReport, String> part_column3;
    @FXML
    private TableColumn<ProcessReport, String> rev_column3;
    @FXML
    private TableColumn<ProcessReport, String> quality_column1;
    @FXML
    private TableColumn<ProcessReport, String> quality_column2;
    @FXML
    private TableColumn<ProcessReport, Integer> quantity_column3;
    @FXML
    private TableView<?> weekly_tableview;
    @FXML
    private TableColumn<?, ?> startdate_column;
    @FXML
    private TableColumn<?, ?> enddate_column;
    @FXML
    private TableColumn<?, ?> virgin_column;
    @FXML
    private TableColumn<?, ?> rework_column;
    @FXML
    private TableColumn<?, ?> incominglotquantity_column;
    @FXML
    private TableColumn<?, ?> departlotquantity_column;
    @FXML
    private TableColumn<?, ?> departlotrejected_column;
    @FXML
    private TableColumn<?, ?> departlotbalance_column;
    @FXML
    private TableColumn<?, ?> quality_column3;
    @FXML
    private TableColumn<?, ?> quality_column4;
    @FXML
    private TableColumn<?, ?> processquantity_column;
    @FXML
    private Label virgin_label;
    @FXML
    private Label rework_label;
    @FXML
    private Label incominglotquantity_label;
    @FXML
    private Label departlotbalance_label;
    @FXML
    private Label departlotrejected_label;
    @FXML
    private Label departlotquantity_label;
    @FXML
    private Label quality_label1;
    @FXML
    private Label quality_label2;
    @FXML
    private Label processquantity_label;
    
    private List<IncomingLot> incominglot_list;
    private List<DepartLot> departlot_list;
    private List<ProcessReport> processreport_list;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partnumber_combo.getItems().setAll(msabase.getProductPartDAO().list());
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        setDatePicker(startdate_picker);
        setDatePicker(enddate_picker);
        
        setIncomingLotTable();
        setDepartLotTable();
        setProcessReportTable();
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            updateIncomingLotTable();
            updateDepartLotTable();
            updateProcessReportTable();
        });
        startdate_picker.setOnAction((ActionEvent) -> {
            updateIncomingLotTable();
            updateDepartLotTable();
            updateProcessReportTable();
        });
        enddate_picker.setOnAction((ActionEvent) -> {
            updateIncomingLotTable();
            updateDepartLotTable();
            updateProcessReportTable();
        });
    }
    
    public void updateProcessReportTable(){
        try{
            processreport_list = msabase.getProcessReportDAO().listProductPartDateRange(partnumber_combo.getValue(), DAOUtil.toUtilDate(startdate_picker.getValue().minusDays(1)), DAOUtil.toUtilDate(enddate_picker.getValue()));
            processreport_tableview.getItems().setAll(mergeByTank_Process(processreport_list));
            quality_label1.setText(""+getProcessReportQualityValue(true, false));
            quality_label2.setText(""+getProcessReportQualityValue(false, false));
            processquantity_label.setText(""+getProcessReportQualityValue(true, true));
        }catch(Exception e){
            processreport_tableview.getItems().clear();
            quality_label1.setText("0");
            quality_label2.setText("0");
            processquantity_label.setText("0");
        }
    }
    
    public void updateDepartLotTable(){
        try{
            departlot_list = msabase.getDepartLotDAO().listDateRange(partnumber_combo.getValue(), DAOUtil.toUtilDate(startdate_picker.getValue().minusDays(1)), DAOUtil.toUtilDate(enddate_picker.getValue()));
            departlot_tableview.getItems().setAll(mergeByDepartReport_Lotnumber(departlot_list));
            departlotbalance_label.setText(""+(getDepartLotStatusValue("Pendiente", false)+getDepartLotStatusValue("Facturado", false)));
            departlotrejected_label.setText(""+getDepartLotStatusValue("Rechazado", false));
            departlotquantity_label.setText(""+getDepartLotStatusValue("", true));
        }catch(Exception e){
            departlot_tableview.getItems().clear();
            departlotbalance_label.setText("0");
            departlotrejected_label.setText("0");
            departlotquantity_label.setText("0");
        }
    }
    
    public void updateIncomingLotTable(){
        try{
            incominglot_list = msabase.getIncomingLotDAO().listDateRange(partnumber_combo.getValue(), false, DAOUtil.toUtilDate(startdate_picker.getValue().minusDays(1)), DAOUtil.toUtilDate(enddate_picker.getValue()));
            incominglot_tableview.getItems().setAll(mergeByIncomingReport_Lotnumber(incominglot_list));
            virgin_label.setText(""+getIncomingLotStatusValue("Virgen", false));
            rework_label.setText(""+getIncomingLotStatusValue("Rechazo", false));
            incominglotquantity_label.setText(""+getIncomingLotStatusValue("", true));
        }catch(Exception e){
            incominglot_tableview.getItems().clear();
            virgin_label.setText("0");
            rework_label.setText("0");
            incominglotquantity_label.setText("0");
        }
    }
    
    public int getProcessReportQualityValue(boolean quality, boolean skip){
        int quantity_total = 0;
        for(ProcessReport item: processreport_list){
            if(item.isQuality_passed() == quality || skip){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }
    
    public int getDepartLotStatusValue(String status, boolean skip) {
        int quantity_total = 0;
        for(DepartLot item: departlot_tableview.getItems()){
            if(item.getStatus().equals(status) || skip){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }
    
    public int getIncomingLotStatusValue(String status, boolean skip){
        int quantity_total = 0;
        for(IncomingLot item: incominglot_tableview.getItems()){
            if(item.getStatus().equals(status) || skip){
                quantity_total += item.getQuantity();
            }
        }
        return quantity_total;
    }

    public void setProcessReportTable(){
        tank_column.setCellValueFactory(new PropertyValueFactory<>("tank_name"));
        process_column3.setCellValueFactory(new PropertyValueFactory<>("process"));
        part_column3.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column3.setCellValueFactory(new PropertyValueFactory<>("rev"));
        quality_column1.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getGood_quantity()));
        quality_column2.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getBad_quantity()));
        quantity_column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }
    
    public void setDepartLotTable(){
        id_column2.setCellValueFactory(new PropertyValueFactory<>("departreport_id"));
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        part_column2.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column2.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lot_column2.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column2.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        status_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
    }
    
    public void setIncomingLotTable(){
        id_column1.setCellValueFactory(new PropertyValueFactory<>("incomingreport_id"));
        date_column1.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        part_column1.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        rev_column1.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lot_column1.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column1.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        status_column1.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    public List<ProcessReport> mergeByTank_Process(List<ProcessReport> unfilteredList){
        ArrayList<Integer> tank_id = new ArrayList();
        ArrayList<String> process = new ArrayList();
        ArrayList<ProcessReport> mergedList = new ArrayList();
        
        for(ProcessReport process_report : unfilteredList){
            if(tank_id.contains(process_report.getTank_id()) && process.contains(process_report.getProcess())){
                for(ProcessReport listitem : mergedList){
                    if(process_report.getTank_id().equals(listitem.getTank_id()) && process_report.getProcess().equals(listitem.getProcess())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + process_report.getQuantity());
                        if(process_report.isQuality_passed()){
                            mergedList.get(mergedList.indexOf(listitem)).setGood_quantity(mergedList.get(mergedList.indexOf(listitem)).getGood_quantity() + process_report.getQuantity());
                        }else{
                            mergedList.get(mergedList.indexOf(listitem)).setBad_quantity(mergedList.get(mergedList.indexOf(listitem)).getBad_quantity() + process_report.getQuantity());
                        }
                    }
                }
            }
            else{
                tank_id.add(process_report.getTank_id());
                process.add(process_report.getProcess());
                
                ProcessReport item = new ProcessReport();
                item.setTank_id(process_report.getTank_id());
                item.setTank_name(process_report.getTank_name());
                item.setProcess(process_report.getProcess());
                item.setQuantity(process_report.getQuantity());
                if(process_report.isQuality_passed()){
                    item.setGood_quantity(process_report.getQuantity());
                    item.setBad_quantity(0);
                }else{
                    item.setGood_quantity(0);
                    item.setBad_quantity(process_report.getQuantity());
                }
                item.setPart_number(process_report.getPart_number());
                item.setRev(process_report.getRev());
                mergedList.add(item);
            }
        }
        
        return mergedList;
    }
    
    public List<DepartLot> mergeByDepartReport_Lotnumber(List<DepartLot> unfilteredList){
        //find all part_number
        ArrayList<Integer> departreport_id = new ArrayList();
        ArrayList<String> lot_number = new ArrayList();
        ArrayList<String> status = new ArrayList();
        ArrayList<String> process = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        
        for(DepartLot depart_lot : unfilteredList){
            if(process.contains(depart_lot.getProcess()) && status.contains(depart_lot.getStatus()) && lot_number.contains(depart_lot.getLot_number()) && departreport_id.contains(depart_lot.getDepartreport_id())){
                for(DepartLot listitem : mergedList){
                    if(depart_lot.getProcess().equals(listitem.getProcess()) && depart_lot.getStatus().equals(listitem.getStatus()) && depart_lot.getLot_number().equals(listitem.getLot_number()) && depart_lot.getDepartreport_id().equals(listitem.getDepartreport_id())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + depart_lot.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + depart_lot.getBox_quantity());
                        break;
                    }
                }
            }
            else{
                departreport_id.add(depart_lot.getDepartreport_id());
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
    
    public List<IncomingLot> mergeByIncomingReport_Lotnumber(List<IncomingLot> unfilteredList){
        ArrayList<Integer> incomingreport_id = new ArrayList();
        ArrayList<String> lot_number = new ArrayList();
        ArrayList<String> status = new ArrayList();
        ArrayList<IncomingLot> mergedList = new ArrayList();
        
        for(IncomingLot incoming_lot : unfilteredList){
            if(status.contains(incoming_lot.getStatus()) && lot_number.contains(incoming_lot.getLot_number()) && incomingreport_id.contains(incoming_lot.getIncomingreport_id())){
                for(IncomingLot listitem : mergedList){
                    if(incoming_lot.getStatus().equals(listitem.getStatus()) && incoming_lot.getLot_number().equals(listitem.getLot_number()) && incoming_lot.getIncomingreport_id().equals(listitem.getIncomingreport_id())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + incoming_lot.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + incoming_lot.getBox_quantity());
                        break;
                    }
                }
            }
            else{
                incomingreport_id.add(incoming_lot.getIncomingreport_id());
                lot_number.add(incoming_lot.getLot_number());
                status.add(incoming_lot.getStatus());
                
                IncomingLot item = new IncomingLot();
                item.setReport_date(incoming_lot.getReport_date());
                item.setPart_revision(incoming_lot.getPart_revision());
                item.setIncomingreport_id(incoming_lot.getIncomingreport_id());
                item.setPart_number(incoming_lot.getPart_number());
                item.setPart_revision(incoming_lot.getPart_revision());
                item.setQuantity(incoming_lot.getQuantity());
                item.setBox_quantity(incoming_lot.getBox_quantity());
                item.setLot_number(incoming_lot.getLot_number());
                item.setStatus(incoming_lot.getStatus());
                mergedList.add(item);
            }
        }
        return mergedList;
    }
    
}
