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
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyContact;
import model.DepartLot;
import model.DepartReport;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {

    @FXML
    private GridPane root_gridpane;
    @FXML
    private TableView<DepartReport> departreport_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> reportid_column;
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
    private TableView<DepartLot> departlot_tableview1;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column1;
    @FXML
    private TableColumn<DepartLot, Integer> quantity_column1;
    @FXML
    private TableColumn<DepartLot, Integer> boxquantity_column1;
    @FXML
    private TableView<DepartLot> departlot_tableview2;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column2;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column1;
    @FXML
    private TableColumn<DepartLot, String> process_column1;
    @FXML
    private TableColumn<DepartLot, String> ponumber_column1;
    @FXML
    private TableColumn<DepartLot, String> linenumber_column1;
    @FXML
    private TableColumn<DepartLot, Integer> quantity_column2;
    @FXML
    private TableColumn<DepartLot, Integer> boxquantity_column2;
    @FXML
    private TableView<DepartLot> departlot_tableview3;
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
    private Button add_button;
    @FXML
    private Button edit_button;
    @FXML
    private Button pdf_button1;
    @FXML
    private Button pdf_button2;
    @FXML
    private Button pdf_button3;
    
    private Stage add_stage = new Stage();
    
    private Stage edit_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDepartReportTable();
        setDepartLotTable();
        updateDepartReportTable();
        
        details_tab.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        pdf_button1.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        departreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            edit_button.setDisable(true);
            if(!departreport_tableview.getSelectionModel().isEmpty()){
                updateDepartLotTable();
                edit_button.setDisable(!newValue.getReport_date().toString().equals(LocalDate.now().toString()));
            }else{
                departlot_tableview3.getItems().clear();
                departlot_tableview3.getItems().clear();
                departlot_tableview3.getItems().clear();
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateDepartReportTable();
        });
        
        edit_button.setOnAction((ActionEvent) -> {
           showEdit_stage();
           updateDepartReportTable();
        });
        
        pdf_button1.setOnAction((ActionEvent) -> {
            buildPDF(departlot_tableview1.getItems());
        });
        pdf_button2.setOnAction((ActionEvent) -> {
            buildPDF(departlot_tableview2.getItems());
        });
        pdf_button3.setOnAction((ActionEvent) -> {
            buildPDF(departlot_tableview3.getItems());
        });
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
    
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/fxml/CreateDepartReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Remisión");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DepartReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showEdit_stage(){
        EditDepartReportFX.departreport_selection = departreport_tableview.getSelectionModel().getSelectedItem();
        try {
            edit_stage = new Stage();
            edit_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            edit_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/EditDepartReportFX.fxml"));
            Scene scene = new Scene(root);
            
            edit_stage.setTitle("Agregar Nuevo Lote a Remisión");
            edit_stage.setResizable(false);
            edit_stage.initStyle(StageStyle.UTILITY);
            edit_stage.setScene(scene);
            edit_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DepartReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateDepartReportTable(){
        departreport_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartReportDAO().list()));
    }
    
    public void updateDepartLotTable(){
        departlot_tableview3.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().list(departreport_tableview.getSelectionModel().getSelectedItem())));
        departlot_tableview2.setItems(FXCollections.observableArrayList(mergeByLot_number(departlot_tableview3.getItems())));
        departlot_tableview1.setItems(FXCollections.observableArrayList(mergeByPart_number(departlot_tableview3.getItems())));
    }
    
    public List<DepartLot> mergeByLot_number(List<DepartLot> unfiltered_list){
        ArrayList<DepartLot> filtered_list = new ArrayList();
        ArrayList<String> string_list = new ArrayList();
        String string_item = "";
        
        for(DepartLot unfiltered_item : unfiltered_list){
            string_item = (unfiltered_item.getPartrevision_id()+unfiltered_item.getProcess()+unfiltered_item.getPo_number()+unfiltered_item.getLine_number()+unfiltered_item.getStatus()+unfiltered_item.getComments()).toUpperCase();
            if(string_list.contains(string_item)){
                filtered_list.get(string_list.indexOf(string_item)).setQuantity(filtered_list.get(string_list.indexOf(string_item)).getQuantity() + unfiltered_item.getQuantity());
                filtered_list.get(string_list.indexOf(string_item)).setBox_quantity(filtered_list.get(string_list.indexOf(string_item)).getBox_quantity() + unfiltered_item.getBox_quantity());
                if(!(filtered_list.get(string_list.indexOf(string_item)).getLot_number().contains(unfiltered_item.getLot_number()))){
                    filtered_list.get(string_list.indexOf(string_item)).setLot_number(filtered_list.get(string_list.indexOf(string_item)).getLot_number()+","+ unfiltered_item.getLot_number());
                }
            }else{
                string_list.add(string_item);
                DepartLot filtered_item = new DepartLot();
                filtered_item.setReport_date(unfiltered_item.getReport_date());
                filtered_item.setDepartreport_id(unfiltered_item.getDepartreport_id());
                filtered_item.setPartrevision_id(unfiltered_item.getPartrevision_id());
                filtered_item.setPart_number(unfiltered_item.getPart_number());
                filtered_item.setPart_revision(unfiltered_item.getPart_revision());
                filtered_item.setLot_number(unfiltered_item.getLot_number());
                filtered_item.setProcess(unfiltered_item.getProcess());
                filtered_item.setPo_number(unfiltered_item.getPo_number());
                filtered_item.setLine_number(unfiltered_item.getLine_number());
                filtered_item.setQuantity(unfiltered_item.getQuantity());
                filtered_item.setBox_quantity(unfiltered_item.getBox_quantity());
                filtered_item.setPending(unfiltered_item.isPending());
                filtered_item.setRejected(unfiltered_item.isPending());
                filtered_item.setComments(unfiltered_item.getComments());
                filtered_list.add(filtered_item);
            }
        }
        
        filtered_list.sort((o1, o2) -> o1.getPart_number().compareTo(o2.getPart_number()));
        return filtered_list;
    }
    
    public List<DepartLot> mergeByPart_number(List<DepartLot> unfilteredList){
        //find all part_number
        ArrayList<String> partnumber = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        for(DepartLot depart_lot : unfilteredList){
            if(partnumber.contains(depart_lot.getPart_number())){
                mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).setQuantity(mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).getQuantity() + depart_lot.getQuantity());
                mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).setBox_quantity(mergedList.get(partnumber.indexOf(depart_lot.getPart_number())).getBox_quantity() + depart_lot.getBox_quantity());
            }else{
                partnumber.add(depart_lot.getPart_number());
                DepartLot item = new DepartLot();
                item.setReport_date(depart_lot.getReport_date());
                item.setPart_revision("N/A");
                item.setDepartreport_id(depart_lot.getDepartreport_id());
                item.setPart_number(depart_lot.getPart_number());
                item.setLot_number("N/A");
                item.setProcess("N/A");
                item.setPo_number("N/A");
                item.setLine_number("N/A");
                item.setQuantity(depart_lot.getQuantity());
                item.setBox_quantity(depart_lot.getBox_quantity());
                item.setComments("N/A");
                mergedList.add(partnumber.indexOf(depart_lot.getPart_number()),item);
            }
        }
        
        return mergedList;
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        client_column.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("company_address"));
    }
    
    public void setDepartLotTable(){
        partnumber_column1.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column2.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column3.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partrevision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column1.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column2.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        lotnumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLot_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
        
        process_column1.setCellValueFactory(new PropertyValueFactory<>("process"));
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        ponumber_column1.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        ponumber_column2.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        ponumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        ponumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setPo_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
        linenumber_column1.setCellValueFactory(new PropertyValueFactory<>("line_number"));
        linenumber_column2.setCellValueFactory(new PropertyValueFactory<>("line_number"));
        linenumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        linenumber_column2.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLine_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
        
        quantity_column1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column3.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
        
        boxquantity_column1.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column2.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getBox_quantity()));
        boxquantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        boxquantity_column3.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setBox_quantity(getBox_quantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
        
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
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
            /*List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(msabase.getDepartReportDAO().findCompany(depart_report), true);
            if(company_contact.isEmpty()){
                fields.get("contact_name").setValue("N/A");
                fields.get("contact_email").setValue("N/A");
                fields.get("contact_number").setValue("N/A");
            }else{
                fields.get("contact_name").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_phone").setValue(company_contact.get(0).getPhone_number());
            }*/
            int current_row = 1;
            for(DepartLot item : departlot_list){
                if(current_row > 41) break;
                int offset = 0;
                fields.get("part_number"+current_row).setValue(item.getPart_number());
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
}
