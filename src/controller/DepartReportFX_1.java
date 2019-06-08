/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Company;
import model.CompanyAddress;
import model.CompanyContact;
import model.DepartLot_1;
import model.DepartReport_1;
import model.Employee;
import model.IncomingReport_1;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX_1 implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartReport_1> departreport_tableview;
    @FXML
    private TableColumn<DepartReport_1, Integer> reportid_column;
    @FXML
    private TableColumn<DepartReport_1, String> date_column;
    @FXML
    private TableColumn<DepartReport_1, Employee> employee_column;
    @FXML
    private TableColumn<DepartReport_1, String> client_column;
    @FXML
    private TableColumn<DepartReport_1, CompanyAddress> companyaddress_column;
    @FXML
    private TableColumn<DepartReport_1, Integer> qtytotal_column;
    @FXML
    private TableColumn<DepartReport_1, Integer> count_column;
    @FXML
    private DatePicker start_datepicker1;
    @FXML
    private DatePicker end_datepicker1;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private TextField id_field1;
    @FXML
    private TextField packing_field1;
    @FXML
    private TextField po_field1;
    @FXML
    private TextField line_field1;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField lot_field1;
    @FXML
    private TextField rev_field1;
    @FXML
    private Button reset_button1;
    @FXML
    private Button save_button2;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private ComboBox<CompanyAddress> companyaddress_combo2;
    @FXML
    private Button delete_button1;
    @FXML
    private Button pdf_button1;
    @FXML
    private Tab details_tab;
    @FXML
    private TextField packing_field3;
    @FXML
    private TextField po_field3;
    @FXML
    private TextField line_field3;
    @FXML
    private TextField partnumber_field3;
    @FXML
    private TextField lot_field3;
    @FXML
    private TextField rev_field3;
    @FXML
    private ComboBox<IncomingReport_1> incomingreport_combo3;
    @FXML
    private Button save_button3;
    @FXML
    private TextField qtyout_field3;
    @FXML
    private DatePicker start_datepicker3;
    @FXML
    private DatePicker end_datepicker3;
    @FXML
    private TextField id_field3;
    @FXML
    private TableView<DepartLot_1> departlot_tableview;
    @FXML
    private TableColumn<DepartLot_1, String> date_column2;
    @FXML
    private TableColumn<DepartLot_1, String> employee_column2;
    @FXML
    private TableColumn<DepartLot_1, Integer> incomingreportid_column;
    @FXML
    private TableColumn<DepartLot_1, String> partnumber_column;
    @FXML
    private TableColumn<DepartLot_1, String> lot_column;
    @FXML
    private TableColumn<DepartLot_1, String> po_column;
    @FXML
    private TableColumn<DepartLot_1, String> line_column;
    @FXML
    private TableColumn<DepartLot_1, String> packing_column;
    @FXML
    private TableColumn<DepartLot_1, Integer> quantity_column;
    @FXML
    private TableColumn<DepartLot_1, String> comments_column;
    @FXML
    private Button delete_button2;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    private ObjectProperty<Integer> qty_out = new SimpleObjectProperty();
    private BooleanProperty departreport_open = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //SETUP
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        
        //UPDATE TABLE ON FILTER CHANGE
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.getItems().setAll(company_combo1.getItems());
        company_combo1.setOnAction((ActionEvent) -> {
            updateDepartReportTable();
        });
        start_datepicker1.setOnAction(company_combo1.getOnAction());
        end_datepicker1.setOnAction(company_combo1.getOnAction());
        id_field1.setOnAction(company_combo1.getOnAction());
        packing_field1.setOnAction(company_combo1.getOnAction());
        po_field1.setOnAction(company_combo1.getOnAction());
        line_field1.setOnAction(company_combo1.getOnAction());
        partnumber_field1.setOnAction(company_combo1.getOnAction());
        lot_field1.setOnAction(company_combo1.getOnAction());
        rev_field1.setOnAction(company_combo1.getOnAction());
        reset_button1.setOnAction((ActionEvent) -> {
            clearSearchFields();
        });
        
        start_datepicker3.setOnAction((ActionEvent) -> {
            updateIncomingReportCombo();
        });
        end_datepicker3.setOnAction(start_datepicker3.getOnAction());
        id_field3.setOnAction(start_datepicker3.getOnAction());
        packing_field3.setOnAction(start_datepicker3.getOnAction());
        po_field3.setOnAction(start_datepicker3.getOnAction());
        line_field3.setOnAction(start_datepicker3.getOnAction());
        partnumber_field3.setOnAction(start_datepicker3.getOnAction());
        lot_field3.setOnAction(start_datepicker3.getOnAction());
        rev_field3.setOnAction(start_datepicker3.getOnAction());
        
        //CHECK IF EDITING ALLOWED ON TABLE ITEM SELECTION
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport_1> observable, DepartReport_1 oldValue, DepartReport_1 newValue) -> {
            updateDepartLotTable();
            updateIncomingReportCombo();
            try{
                departreport_open.setValue(newValue.isOpen());
            }catch(Exception e){
                departreport_open.setValue(false);
            }
        });
        
        //CREATE NEW REPORT
        company_combo2.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Company> observable, Company oldValue, Company newValue) -> {
            try{
                companyaddress_combo2.getItems().clear();
                companyaddress_combo2.getItems().setAll(msabase.getCompanyAddressDAO().list(newValue));
            }catch(Exception e){
                companyaddress_combo2.getItems().clear();
            }
        });
        save_button2.setOnAction((ActionEvent) -> {
            createDepartReport();
            clearSearchFields();
            updateDepartReportTable();
            company_combo2.getSelectionModel().clearSelection();
        });
        
        //LIMIT MAX-QTY ON COMBO ITEM SELECTION
        incomingreport_combo3.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingReport_1> observable, IncomingReport_1 oldValue, IncomingReport_1 newValue) -> {
            try{
                qty_out.setValue(newValue.getQty_ava());
                qtyout_field3.setText(newValue.getQty_ava()+"");
            }catch(Exception e){
                qty_out.setValue(null);
                incomingreport_combo3.getSelectionModel().clearSelection();
                qtyout_field3.clear();
            }
        });
        
        save_button3.setOnAction((ActionEvent) -> {
            try{
                qty_out.setValue(null);
                qtyout_field3.setStyle(null);
                qty_out.setValue(Integer.parseInt(qtyout_field3.getText().trim()));
                if(qty_out.getValue() < 1 || qty_out.getValue() > incomingreport_combo3.getValue().getQty_ava()) throw new Exception();
            }catch(Exception e){
                qty_out.setValue(null);
                qtyout_field3.setStyle("-fx-background-color: lightpink;");
                qtyout_field3.requestFocus();
                qtyout_field3.selectAll();
                return;
            }
            createDepartLot();
            updateIncomingReportCombo();
            updateDepartLotTable();
            qty_out.setValue(null);
        });
                
        qtyout_field3.disableProperty().bind(incomingreport_combo3.getSelectionModel().selectedItemProperty().isNull());
        save_button3.disableProperty().bind(incomingreport_combo3.getSelectionModel().selectedItemProperty().isNull());
        incomingreport_combo3.disableProperty().bind(Bindings.size(incomingreport_combo3.getItems()).isEqualTo(0));
        details_tab.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        departreport_tableview.editableProperty().bind(departreport_open);
        companyaddress_combo2.disableProperty().bind(Bindings.size(companyaddress_combo2.getItems()).isEqualTo(0));
        save_button2.disableProperty().bind(companyaddress_combo2.valueProperty().isNull());
        delete_button1.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull().or(departreport_open.not()));
        pdf_button1.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button1.setOnAction((ActionEvent) -> {
            buildPDF(departlot_tableview.getItems());
        });
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany_address().getCompany().toString()));
        companyaddress_column.setCellValueFactory(new PropertyValueFactory<>("company_address"));
        qtytotal_column.setCellValueFactory(new PropertyValueFactory<>("qty_total"));
        count_column.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
    
    public void setDepartLotTable(){
        date_column2.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getDate()))));
        employee_column2.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        incomingreportid_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_id"));
        partnumber_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_partnumber"));
        lot_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_lot"));
        po_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_po"));
        line_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_line"));
        packing_column.setCellValueFactory(new PropertyValueFactory<>("incomingreport_packing"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("qty_out"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    public void updateDepartReportTable(){
        try{
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(Integer.parseInt(id_field1.getText().trim()), DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                    partnumber_field1.getText(), rev_field1.getText(), lot_field1.getText(), packing_field1.getText(), po_field1.getText(), line_field1.getText()));
        }catch(Exception e){
            departreport_tableview.getItems().setAll(msabase.getDepartReport_1DAO().list(null, DAOUtil.toUtilDate(start_datepicker1.getValue()), DAOUtil.toUtilDate(end_datepicker1.getValue()), company_combo1.getValue(), 
                partnumber_field1.textProperty().getValue(), rev_field1.textProperty().getValue(), lot_field1.textProperty().getValue(), packing_field1.textProperty().getValue(), po_field1.textProperty().getValue(), line_field1.textProperty().getValue()));
        }
    }
    
    public void updateDepartLotTable(){
        departlot_tableview.getItems().setAll(msabase.getDepartLot_1DAO().list(departreport_tableview.getSelectionModel().getSelectedItem()));
    }
    
    public void clearSearchFields(){
        start_datepicker1.setValue(null);
        end_datepicker1.setValue(null);
        company_combo1.setValue(null);
        id_field1.clear();
        partnumber_field1.clear();
        rev_field1.clear();
        lot_field1.clear();
        packing_field1.clear();
        po_field1.clear();
        line_field1.clear();
    }
    public void createDepartLot() {
        DepartLot_1 depart_lot = new DepartLot_1();
        depart_lot.setDate(DAOUtil.toUtilDate(LocalDate.now()));
        depart_lot.setEmployee_id(MainApp.current_employee.getId());
        depart_lot.setIncomingreport_id(incomingreport_combo3.getValue().getId());
        depart_lot.setDepartreport_id(departreport_tableview.getSelectionModel().getSelectedItem().getId());
        depart_lot.setComments("");
        depart_lot.setQty_out(Integer.parseInt(qtyout_field3.getText()));
        
        msabase.getDepartLot_1DAO().create(depart_lot);
    }
    
    public void updateIncomingReportCombo() {
        Company company = departreport_tableview.getSelectionModel().getSelectedItem().getCompany_address().getCompany();
        if (company.getId() == null){
            incomingreport_combo3.getItems().clear();
            return;
        }
        try{
            incomingreport_combo3.getItems().setAll(msabase.getIncomingReport_1DAO().listAva(Integer.parseInt(id_field3.getText().trim()), DAOUtil.toUtilDate(start_datepicker3.getValue()), DAOUtil.toUtilDate(end_datepicker3.getValue()), company, 
                    partnumber_field3.getText(), rev_field3.getText(), lot_field3.getText(), packing_field3.getText(), po_field3.getText(), line_field3.getText()));
        }catch(Exception e){
            incomingreport_combo3.getItems().setAll(msabase.getIncomingReport_1DAO().listAva(null, DAOUtil.toUtilDate(start_datepicker3.getValue()), DAOUtil.toUtilDate(end_datepicker3.getValue()), company, 
                partnumber_field3.textProperty().getValue(), rev_field3.textProperty().getValue(), lot_field3.textProperty().getValue(), packing_field3.textProperty().getValue(), po_field3.textProperty().getValue(), line_field3.textProperty().getValue()));
        }
    }
    
    public void createDepartReport(){
        DepartReport_1 depart_report = new DepartReport_1();
        depart_report.setDate(DAOUtil.toUtilDate(LocalDate.now()));
        depart_report.setEmployee(MainApp.current_employee);
        depart_report.setCompany_address(companyaddress_combo2.getValue());
        depart_report.setComments("");
        
        msabase.getDepartReport_1DAO().create(depart_report);
    }
    
    public void buildPDF(List<DepartLot_1> departlot_list){
            try{
                Path output = Files.createTempFile("RemisionPDF", ".pdf");
                output.toFile().deleteOnExit();
                //Instantiating PDFMergerUtility class
                PDFMergerUtility PDFmerger = new PDFMergerUtility();

                //Setting the destination file
                PDFmerger.setDestinationFileName(output.toString());
                int size = 35;
                int page_offset = 1;
                int total_pages = divideList(departlot_list, size).size();
                for(List<DepartLot_1> divided_list : divideList(departlot_list, size)){
                    PDFmerger.addSource(buildPDF(page_offset, departreport_tableview.getSelectionModel().getSelectedItem(), divided_list, total_pages));
                    page_offset++;
                }
                //Merging the two documents
                PDFmerger.mergeDocuments();
                MainApp.openPDF(new File(PDFmerger.getDestinationFileName()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public List<List<DepartLot_1>> divideList(List<DepartLot_1> arrayList, int size){
        List<List<DepartLot_1>> dividedList = new ArrayList();
        for (int start = 0; start < arrayList.size(); start += size) {
            int end = Math.min(start + size, arrayList.size());
            List<DepartLot_1> sublist = arrayList.subList(start, end);
            dividedList.add(sublist);
        }
        return dividedList;
    }
    
    private File buildPDF(int page_offset, DepartReport_1 depart_report, List<DepartLot_1> departlot_list, int total_pages) throws Exception{

            Path template = Files.createTempFile("Depart Report Template", ".pdf");
            template.toFile().deleteOnExit();
            
            try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/Depart Report Template.pdf")) {
                Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
            }
            
            Path output = Files.createTempFile("Remision_"+depart_report.getId(), ".pdf");
            template.toFile().deleteOnExit();
            
            PdfDocument pdf = new PdfDocument(
                new PdfReader(template.toFile()),
                new PdfWriter(output.toFile())
            );
            
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            fields.get("page_number").setValue(page_offset+" / "+total_pages);
            fields.get("report_id").setValue(""+depart_report.getId());
            fields.get("report_date").setValue(depart_report.getDate().toString());
            fields.get("employee_name").setValue(depart_report.getEmployee().toString());
            fields.get("client_name").setValue(depart_report.getCompany_address().getCompany().getName());
            fields.get("client_address").setValue(depart_report.getCompany_address().getAddress());
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(depart_report.getCompany_address().getCompany(), true);
            if(company_contact.isEmpty()){
                fields.get("contact_name").setValue("N/A");
                fields.get("contact_email").setValue("N/A");
                fields.get("contact_number").setValue("N/A");
            }else{
                fields.get("contact_name").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_phone").setValue(company_contact.get(0).getPhone_number());
            }
            int current_row = 1;
            for(DepartLot_1 item : departlot_list){
                if(current_row > 41) break;
                int offset = 0;
                fields.get("part_number"+current_row).setValue(item.getIncomingreport_partnumber());
                fields.get("process"+current_row).setValue("n/a");
                fields.get("po_number"+current_row).setValue(item.getIncomingreport_po());
                fields.get("line_number"+current_row).setValue(item.getIncomingreport_line());
                fields.get("comments"+current_row).setValue(item.getComments());
                fields.get("quantity"+current_row).setValue(""+item.getQty_out());
                fields.get("box_quantity"+current_row).setValue("1");
                for(String lot_number : item.getIncomingreport_lot().split(",")){
                    fields.get("lot_number"+(current_row+offset)).setValue(lot_number);
                    offset++;
                }
                current_row += offset;
            }
            
            fields.get("total_quantity").setValue(""+depart_report.getQty_total());
            fields.get("total_boxquantity").setValue(""+depart_report.getCount());
            
            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
}
