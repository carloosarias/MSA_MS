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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CompanyContact;
import model.DepartLot;
import model.DepartReport;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartReport> departreport_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> reportid_column;
    @FXML
    private TableColumn<DepartReport, String> employee_column;
    @FXML
    private TableColumn<DepartReport, Date> reportdate_column;
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
    private TableColumn<DepartLot, Integer> quantity_column2;
    @FXML
    private TableColumn<DepartLot, Integer> boxquantity_column2;
    @FXML
    private TableColumn<DepartLot, String> process_column1;
    @FXML
    private TableView<DepartLot> departlot_tableview3;
    @FXML
    private TableColumn<DepartLot, String> partnumber_column3;
    @FXML
    private TableColumn<DepartLot, String> partrevision_column;
    @FXML
    private TableColumn<DepartLot, String> lotnumber_column2;
    @FXML
    private TableColumn<DepartLot, Integer> quantity_column3;
    @FXML
    private TableColumn<DepartLot, Integer> boxquantity_column3;
    @FXML
    private TableColumn<DepartLot, String> process_column2;
    @FXML
    private TableColumn<DepartLot, String> comments_column;
    @FXML
    private Button add_button;
    @FXML
    private Button edit_button;
    @FXML
    private Button pdf_button;
    
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
        pdf_button.disableProperty().bind(departreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
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
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(departreport_tableview.getSelectionModel().getSelectedItem(), departlot_tableview2.getItems()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateDepartReportFX.fxml"));
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
            edit_stage.initOwner((Stage) root_hbox.getScene().getWindow());
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
        departlot_tableview2.setItems(FXCollections.observableArrayList(mergeByProcess(departlot_tableview3.getItems())));
        departlot_tableview1.setItems(FXCollections.observableArrayList(mergeByPart_number(departlot_tableview3.getItems())));
    }
    
    public List<DepartLot> mergeByProcess(List<DepartLot> unfilteredList){
        //find all part_number + process
        ArrayList<String> partnumber_process = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        for(DepartLot depart_lot : unfilteredList){
            if(partnumber_process.contains(depart_lot.getPart_number()+" "+depart_lot.getProcess())){
                mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).setQuantity(mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).getQuantity() + depart_lot.getQuantity());
                mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).setBox_quantity(mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).getBox_quantity() + depart_lot.getBox_quantity());
                if(!depart_lot.getComments().equalsIgnoreCase("N/A") && !depart_lot.getComments().replace(" ", "").equals("")){
                    mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).setComments(mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).getComments()+","+depart_lot.getComments());
                }
                if(!mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).getLot_number().contains(depart_lot.getLot_number())){
                    mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).setLot_number(mergedList.get(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess())).getLot_number()+","+depart_lot.getLot_number());
                }
            }else{
                partnumber_process.add(depart_lot.getPart_number()+" "+depart_lot.getProcess());
                DepartLot item = new DepartLot();
                item.setLot_number(depart_lot.getLot_number());
                item.setPart_number(depart_lot.getPart_number());
                item.setProcess(depart_lot.getProcess());
                item.setQuantity(depart_lot.getQuantity());
                item.setBox_quantity(depart_lot.getBox_quantity());
                item.setComments("");
                if(!depart_lot.getComments().equalsIgnoreCase("N/A") && !depart_lot.getComments().replace(" ", "").equals("")){
                    item.setComments(depart_lot.getComments());
                }
                mergedList.add(partnumber_process.indexOf(depart_lot.getPart_number()+" "+depart_lot.getProcess()),item);
            }
        }
        
        return mergedList;
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
                item.setPart_number(depart_lot.getPart_number());
                item.setQuantity(depart_lot.getQuantity());
                item.setBox_quantity(depart_lot.getBox_quantity());
                mergedList.add(partnumber.indexOf(depart_lot.getPart_number()),item);
            }
        }
        
        return mergedList;
    }
    
    public void setDepartReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        reportdate_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
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
        quantity_column1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boxquantity_column1.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column2.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column3.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        process_column1.setCellValueFactory(new PropertyValueFactory<>("process"));
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }
    
    private File buildPDF(DepartReport depart_report, List<DepartLot> departlot_list) throws Exception{
        
            Path template = Files.createTempFile("DepartReportTemplate", ".pdf");
            template.toFile().deleteOnExit();
            
            try (InputStream is = MainApp.class.getClassLoader().getResourceAsStream("template/DepartReportTemplate.pdf")) {
                Files.copy(is, template, StandardCopyOption.REPLACE_EXISTING);
            }
            
            Path output = Files.createTempFile("RemisionPDF", ".pdf");
            template.toFile().deleteOnExit();
            
            PdfDocument pdf = new PdfDocument(
                new PdfReader(template.toFile()),
                new PdfWriter(output.toFile())
            );
            
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            fields.get("depart_report_id").setValue(""+depart_report.getId());
            fields.get("date").setValue(depart_report.getReport_date().toString());
            fields.get("employee_email").setValue("alberto.nunez@maquilasales.com");
            fields.get("client").setValue(depart_report.getCompany_name());
            fields.get("client_address").setValue(depart_report.getCompany_address());
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(msabase.getDepartReportDAO().findCompany(depart_report), true);
            if(company_contact.isEmpty()){
                fields.get("contact").setValue("n/a");
                fields.get("contact_email").setValue("n/a");
                fields.get("contact_number").setValue("n/a");
            }else{
                fields.get("contact").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_number").setValue(company_contact.get(0).getPhone_number());
            }
            int current_row = 1;
            for(DepartLot item : departlot_list){
                if(current_row > 26) break;
                int extra_rows = 0;
                int offset = 0;
                fields.get("part_number"+current_row).setValue(item.getPart_number());
                fields.get("process"+current_row).setValue(item.getProcess());
                fields.get("quantity"+current_row).setValue(""+item.getQuantity());
                fields.get("quantity_box"+current_row).setValue(""+item.getBox_quantity());
                for(String lot_number : item.getLot_number().split(",")){
                    fields.get("lotnumber"+(current_row+offset)).setValue(lot_number);
                    offset++;
                }
                extra_rows = offset;
                
                offset = 0;
                for(String comment : item.getComments().split(",")){
                    fields.get("description"+(current_row+offset)).setValue(comment);
                    offset++;
                }
                
                if(offset > extra_rows) extra_rows = offset;

                current_row = (current_row + extra_rows) + 1;
            }
            
            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
}
