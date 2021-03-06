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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.CompanyContact;
import model.DepartLot;
import model.DepartReport;
import model.DepartReport_1;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartReport> departreport_tableview;
    @FXML
    private TableColumn<DepartReport, String> reportid_column;
    @FXML
    private TableColumn<DepartReport, String> employee_column;
    @FXML
    private TableColumn<DepartReport, String> reportdate_column;
    @FXML
    private TableColumn<DepartReport, String> client_column;
    @FXML
    private TableColumn<DepartReport, String> address_column;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column3;
    @FXML
    private TableColumn<DepartLot, String> partrevision_column;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column2;
    @FXML
    private TableColumn<DepartLot, String> process_column2;
    @FXML
    private TableColumn<DepartLot, String> ponumber_column2;
    @FXML
    private TableColumn<DepartLot, String> linenumber_column2;
    @FXML
    private TableColumn<DepartLot, String> quantity_column3;
    @FXML
    private TableColumn<DepartLot, String> boxquantity_column3;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button pdf_button3;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            updateDepartLotTable();
        });
        details_tab.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button3.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        pdf_button3.setOnAction((ActionEvent) -> {
            buildPDF(departlot_tableview.getItems());
        });
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()+""));
        employee_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmployee()+""));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        client_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany().getName()+""));
        address_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompany_address().getAddress()+""));
    }

    public void updateDepartLotTable(){
        try{
            departlot_tableview.getItems().setAll(msabase.getDepartLotDAO().list(departreport_tableview.getSelectionModel().getSelectedItem()));
        }catch(Exception e){
            departlot_tableview.getItems().clear();
        }
            
    }
    
    public void setDepartLotTable(){
       partnumber_column3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getProduct_part().getPart_number()+""));
       partrevision_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPart_revision().getRev()+""));
       lotnumber_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLot_number()+""));
       process_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProcess()+""));
       ponumber_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPo_number()+""));
       linenumber_column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLine_number()+""));
       quantity_column3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuantity()+""));
       boxquantity_column3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBox_quantity()+""));
       comments_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getComments()+""));
    }
    
    public void updateDepartReportTable(){
        departreport_tableview.getItems().setAll(msabase.getDepartReportDAO().list());
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
            fields.get("employee_name").setValue(depart_report.getEmployee().toString());
            fields.get("client_name").setValue(depart_report.getCompany().getName());
            fields.get("client_address").setValue(depart_report.getCompany_address().getAddress());
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
                fields.get("part_number"+current_row).setValue(item.getPart_revision().getProduct_part().getPart_number());
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
            
            fields.get("total_quantity").setValue(""+depart_report.getTotal_qty());
            fields.get("total_boxquantity").setValue(""+depart_report.getTotal_box());
            
            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
}
