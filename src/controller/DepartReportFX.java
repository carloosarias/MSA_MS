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
import dao.JDBC.DAOFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
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
import model.PartRevision;
import msa_ms.MainApp;
import static msa_ms.MainApp.openPDF;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class DepartReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<DepartReport> depart_report_tableview;
    @FXML
    private TableColumn<DepartReport, Integer> report_id_column;
    @FXML
    private TableColumn<DepartReport, String> report_employee_column;
    @FXML
    private TableColumn<DepartReport, Date> report_date_column;
    @FXML
    private TableColumn<DepartReport, String> report_client_column;
    @FXML
    private TableColumn<DepartReport, String> address_column;
    @FXML
    private TableView<PartRevision> partrevision_tableview;
    @FXML
    private TableColumn<PartRevision, String> part_column;
    @FXML
    private TableColumn<PartRevision, String> revision_column;
    @FXML
    private TableColumn<PartRevision, String> item_qty_column;
    @FXML
    private TableColumn<PartRevision, String> item_boxqty_column;
    @FXML
    private TableView<DepartLot> departlot_tableview;
    @FXML
    private TableColumn<DepartLot, String> lot_column;
    @FXML
    private TableColumn<DepartLot, Integer> lot_qty;
    @FXML
    private TableColumn<DepartLot, Integer> lot_boxqty_column;
    @FXML
    private TableColumn<DepartLot, String> lot_process_column;
    @FXML
    private TableColumn<DepartLot, String> lot_comments_column;
    @FXML
    private Button add_button;
    @FXML
    private Button pdf_button;
    
    private Stage add_stage = new Stage();

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setReportTable();
        setItemTable();
        setLotTable();
        depart_report_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartReportDAO().list()));
        
        depart_report_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DepartReport> observable, DepartReport oldValue, DepartReport newValue) -> {
            partrevision_tableview.getItems().clear();
            departlot_tableview.getItems().clear();
            if(newValue != null){
                departlot_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().list(newValue)));
                partrevision_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().listPartRevision(newValue)));
            }
            pdf_button.setDisable(newValue == null);
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            depart_report_tableview.setItems(FXCollections.observableArrayList(msabase.getDepartReportDAO().list()));
        });
        
        pdf_button.setOnAction((ActionEvent) -> {
            try{
                MainApp.openPDF(buildPDF(depart_report_tableview.getSelectionModel().getSelectedItem()));
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
    
    public void setReportTable(){
        report_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        report_employee_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findEmployee(c.getValue()).toString()));
        report_date_column.setCellValueFactory(new PropertyValueFactory<>("report_date"));
        report_client_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findCompany(c.getValue()).toString()));
        address_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getDepartReportDAO().findCompanyAddress(c.getValue()).toString()));
    }
    
    public void setItemTable(){
        part_column.setCellValueFactory(c -> new SimpleStringProperty(
            msabase.getPartRevisionDAO().findProductPart(c.getValue()).toString())
        );
        revision_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        item_qty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().findTotalQuantity(depart_report_tableview.getSelectionModel().getSelectedItem(), c.getValue())));
        item_boxqty_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getDepartLotDAO().findTotalBoxQuantity(depart_report_tableview.getSelectionModel().getSelectedItem(), c.getValue())));
    }
    
    public void setLotTable(){
        lot_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lot_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lot_boxqty_column.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        lot_process_column.setCellValueFactory(new PropertyValueFactory<>("process"));
        lot_comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
    }

    private File buildPDF(DepartReport depart_report) throws Exception{
        
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
            fields.get("employee_email").setValue("cmartinez@maquilasales.com");
            fields.get("client").setValue(report_client_column.getCellData(depart_report));
            fields.get("client_address").setValue(address_column.getCellData(depart_report));
            List<CompanyContact> company_contact = msabase.getCompanyContactDAO().list(msabase.getDepartReportDAO().findCompany(depart_report));
            if(company_contact.isEmpty()){
                fields.get("contact").setValue("n/a");
                fields.get("contact_email").setValue("n/a");
                fields.get("contact_number").setValue("n/a");
            }else{
                fields.get("contact").setValue(company_contact.get(0).getName());
                fields.get("contact_email").setValue(company_contact.get(0).getEmail());
                fields.get("contact_number").setValue(company_contact.get(0).getPhone_number());
            }
            
            
            
            List<PartRevision> part_revision_list = partrevision_tableview.getItems();
            int i = 0;
            for(PartRevision part_revision : part_revision_list){
                List<String> process_list = msabase.getDepartLotDAO().listProcess(part_revision, depart_report);
                for(String process : process_list){
                    List<DepartLot> departlot_list = msabase.getDepartLotDAO().list(part_revision, process, depart_report);
                    List<String> lotnumber_list = new ArrayList();
                    
                    for(DepartLot depart_lot : departlot_list){
                        if(!lotnumber_list.contains(depart_lot.getLot_number())){
                            lotnumber_list.add(depart_lot.getLot_number());
                        }
                    }
                    
                    for(String lot_number : lotnumber_list){
                        int current_row = i+1;
                        if(current_row > 26) break;
                        int quantity = 0;
                        int quantity_box = 0;
                        fields.get("part_number"+current_row).setValue(part_column.getCellData(part_revision));
                        fields.get("lotnumber"+current_row).setValue(lot_number);
                        fields.get("process"+current_row).setValue(process);
                        List<String> departlot_listcomments = new ArrayList();
                        
                        for(DepartLot depart_lot : departlot_list){
                            if(depart_lot.getLot_number().equals(lot_number)){
                                if(!depart_lot.getComments().equalsIgnoreCase("N/A") && !depart_lot.getComments().replace(" ", "").equals("")){
                                    departlot_listcomments.add(depart_lot.getComments());
                                }
                                quantity += depart_lot.getQuantity();
                                quantity_box += depart_lot.getBox_quantity();
                            }
                        }
                        fields.get("quantity"+current_row).setValue(""+quantity);
                        fields.get("quantity_box"+current_row).setValue(""+quantity_box);
                        
                        if(!departlot_listcomments.isEmpty()){
                            for(String comment : departlot_listcomments){
                                current_row = i+1;
                                if(current_row > 26) break;
                                fields.get("description"+current_row).setValue(comment);
                                i++;
                            }
                        }
                        i++;
                    }
                    
                }
            }
            
            form.flattenFields();
            pdf.close();
            
            return output.toFile();
    }
    
}
