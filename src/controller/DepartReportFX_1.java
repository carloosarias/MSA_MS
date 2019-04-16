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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import model.Company;
import model.CompanyAddress;
import model.CompanyContact;
import model.DepartLot;
import model.DepartReport;
import model.Employee;
import model.PartRevision;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import static msa_ms.MainApp.setDatePicker;
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
    private TableView<DepartReport> departreport_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> reportid_column;
    @FXML
    private TableColumn<DepartReport, Employee> employee_column;
    @FXML
    private TableColumn<DepartReport, String> reportdate_column;
    @FXML
    private TableColumn<DepartReport, Company> client_column;
    @FXML
    private TableColumn<DepartReport, CompanyAddress> address_column;
    @FXML
    private TableColumn<DepartReport, Integer> totalqty_column;
    @FXML
    private TableColumn<DepartReport, Integer> totalbox_column;
    @FXML
    private ComboBox<Company> company_combo1;
    @FXML
    private DatePicker start_datepicker;
    @FXML
    private DatePicker end_datepicker;
    @FXML
    private TextField partnumber_field1;
    @FXML
    private TextField lotnumber_field1;
    @FXML
    private TextField ponumber_field;
    @FXML
    private Button reset_button;
    @FXML
    private Button save_button;
    @FXML
    private ComboBox<Company> company_combo2;
    @FXML
    private Button delete_button1;
    @FXML
    private Tab details_tab;
    @FXML
    private Button pdf_button;
    @FXML
    private ComboBox<String> pdf_combo;
    @FXML
    private TextField lotnumber_field2;
    @FXML
    private TextField partnumber_field2;
    @FXML
    private TextField rev_field;
    @FXML
    private ComboBox<String> po_combo;
    @FXML
    private TextField quantity_field;
    @FXML
    private Button save_button2;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column;
    @FXML
    private TableColumn<DepartLot, PartRevision> partrevision_column;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column;
    @FXML
    private TableColumn<DepartLot, String> process_column;
    @FXML
    private TableColumn<DepartLot, String> ponumber_column;
    @FXML
    private TableColumn<DepartLot, String> linenumber_column;
    @FXML
    private TableColumn<DepartLot, String> quantity_column;
    @FXML
    private TableColumn<DepartLot, String> boxquantity_column;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button delete_button2;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        updateComboItems();
        
        details_tab.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button.disableProperty().bind(pdf_combo.getSelectionModel().selectedItemProperty().isNull());
        
        reset_button.setOnAction((ActionEvent) -> {
            updateDepartReportTable();
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            switch(pdf_combo.getSelectionModel().getSelectedIndex()){
                case 0:
                    buildPDF(departlot_tableview.getItems());
                case 1:
                    //buildPDF(msabase.getDepartLotDAO().listGroup(departreport_tableview.getSelectionModel().getSelectedItem()));
            }
        });
        
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            updateDepartLotTable();
        });
    }
    
    public void updatePOCombo(){
        //po_combo.getItems().setAll(msabase.getDepartLotDAO().listPO(departreport_tableview.getSelectionModel().getSelectedItem(), lotnumber_field2.getText(), partnumber_field2.getText(), rev_field.getText(), quantity_field.getText()));
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        client_column.setCellValueFactory(new PropertyValueFactory<>("company"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("company"));
        totalqty_column.setCellValueFactory(new PropertyValueFactory<>("total_qty"));
        totalbox_column.setCellValueFactory(new PropertyValueFactory<>("total_box"));
    }
    
    public void setDepartLotTable(){
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getProduct_part().toString()));
        partrevision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        lotnumber_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLot_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        ponumber_column.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        ponumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        ponumber_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPo_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        linenumber_column.setCellValueFactory(new PropertyValueFactory<>("line_number"));
        linenumber_column.setCellFactory(TextFieldTableCell.forTableColumn());
        linenumber_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLine_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        quantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        boxquantity_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getBox_quantity()));
        boxquantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        boxquantity_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setBox_quantity(getBox_quantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            departlot_tableview.refresh();
        });
    }
    
    public void updateDepartReportTable(){
        departreport_tableview.getItems().setAll(msabase.getDepartReportDAO().list(null, company_combo1.getValue(), DAOUtil.toUtilDate(start_datepicker.getValue()), DAOUtil.toUtilDate(end_datepicker.getValue()), partnumber_field1.getText(), lotnumber_field1.getText(), ponumber_field.getText()));
    }
    
    public void updateDepartLotTable(){
        try{
            departlot_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().list(departreport_tableview.getSelectionModel().getSelectedItem())));
        }catch(Exception e){
            departlot_tableview.getItems().clear();
        }
    }
    
    public void updateComboItems(){
        setDatePicker(start_datepicker);
        setDatePicker(end_datepicker);
        company_combo1.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        company_combo2.itemsProperty().bind(company_combo1.itemsProperty());
        pdf_combo.getItems().setAll("No Agrupar", "Agrupar Números de Parte");
    }
    
    public Integer getQuantityValue(DepartLot depart_lot, String quantity){
        try{
            return Integer.parseInt(quantity);
        }catch(Exception e){
            return depart_lot.getQuantity();
        }
    }
    
    public Integer getBox_quantityValue(DepartLot depart_lot, String box_quantity){
        try{
            return Integer.parseInt(box_quantity);
        }catch(Exception e){
            return depart_lot.getBox_quantity();
        }
    }
    
    public void buildPDF(List<DepartLot> departlot_list){
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
                for(List<DepartLot> divided_list : divideList(departlot_list, size)){
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
    
    public List<List<DepartLot>> divideList(List<DepartLot> arrayList, int size){
        List<List<DepartLot>> dividedList = new ArrayList();
        for (int start = 0; start < arrayList.size(); start += size) {
            int end = Math.min(start + size, arrayList.size());
            List<DepartLot> sublist = arrayList.subList(start, end);
            dividedList.add(sublist);
        }
        return dividedList;
    }
    
    private File buildPDF(int page_offset, DepartReport depart_report, List<DepartLot> departlot_list, int total_pages) throws Exception{

            Path template = Files.createTempFile("Depart Report Template", ".pdf");
            template.toFile().deleteOnExit();
            
            try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/Depart Report Template.pdf")) {
                Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
            }
            
            Path output = Files.createTempFile("RemisionPDF"+page_offset, ".pdf");
            template.toFile().deleteOnExit();
            
            PdfDocument pdf = new PdfDocument(
                new PdfReader(template.toFile()),
                new PdfWriter(output.toFile())
            );
            
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            fields.get("page_number").setValue(page_offset+" / "+total_pages);
            fields.get("report_id").setValue(""+depart_report.getId());
            fields.get("report_date").setValue(depart_report.getReport_date().toString());
            //fields.get("employee_name").setValue(depart_report.getEmployee_name());
            //fields.get("client_name").setValue(depart_report.getCompany_name());
            //fields.get("client_address").setValue(depart_report.getCompany_address());
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(depart_report.getCompany(), true);
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
            for(DepartLot item : departlot_list){
                if(current_row > 41) break;
                int offset = 0;
               // fields.get("part_number"+current_row).setValue(item.getPart_number());
                fields.get("process"+current_row).setValue(item.getProcess());
                fields.get("po_number"+current_row).setValue(item.getPo_number());
                fields.get("line_number"+current_row).setValue(item.getLine_number());
                fields.get("comments"+current_row).setValue(item.getComments());
                fields.get("quantity"+current_row).setValue(""+item.getQuantity());
                fields.get("box_quantity"+current_row).setValue(""+item.getBox_quantity());
                for(String lot_number : item.getLot_number().split(",")){
                    fields.get("lot_number"+(current_row+offset)).setValue(lot_number);
                    offset++;
                }
                current_row += offset;
            }
            
            //fields.get("total_quantity").setValue(""+getQuantity_total());
            //fields.get("total_boxquantity").setValue(""+getBoxquantity_total());
            
            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
    
}
