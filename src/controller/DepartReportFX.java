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
import model.ProductPart;
import msa_ms.MainApp;
import static msa_ms.MainApp.getFormattedDate;

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
    private TableColumn<DepartLot, String> quantity_column3;
    @FXML
    private TableColumn<DepartLot, String> boxquantity_column3;
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
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
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
        departlot_tableview2.setItems(FXCollections.observableArrayList(mergeByDepartReport_Lotnumber(departlot_tableview3.getItems())));
        departlot_tableview1.setItems(FXCollections.observableArrayList(mergeByPart_number(departlot_tableview3.getItems())));
    }
    
    public List<DepartLot> mergeByDepartReport_Lotnumber(List<DepartLot> unfilteredList){
        //find all part_number
        ArrayList<Integer> departreport_id = new ArrayList();
        ArrayList<Integer> partrevision_id = new ArrayList();
        ArrayList<String> lot_number = new ArrayList();
        ArrayList<String> status = new ArrayList();
        ArrayList<String> process = new ArrayList();
        ArrayList<DepartLot> mergedList = new ArrayList();
        
        for(DepartLot depart_lot : unfilteredList){
            if(partrevision_id.contains(depart_lot.getPartrevision_id()) && process.contains(depart_lot.getProcess()) && status.contains(depart_lot.getStatus()) && lot_number.contains(depart_lot.getLot_number()) && departreport_id.contains(depart_lot.getDepartreport_id())){
                for(DepartLot listitem : mergedList){
                    if(depart_lot.getPartrevision_id().equals(listitem.getPartrevision_id()) && depart_lot.getProcess().equals(listitem.getProcess()) && depart_lot.getStatus().equals(listitem.getStatus()) && depart_lot.getLot_number().equals(listitem.getLot_number()) && depart_lot.getDepartreport_id().equals(listitem.getDepartreport_id())){
                        mergedList.get(mergedList.indexOf(listitem)).setQuantity(mergedList.get(mergedList.indexOf(listitem)).getQuantity() + depart_lot.getQuantity());
                        mergedList.get(mergedList.indexOf(listitem)).setBox_quantity(mergedList.get(mergedList.indexOf(listitem)).getBox_quantity() + depart_lot.getBox_quantity());
                        break;
                    }
                }
            }
            else{
                departreport_id.add(depart_lot.getDepartreport_id());
                partrevision_id.add(depart_lot.getPartrevision_id());
                lot_number.add(depart_lot.getLot_number());
                status.add(depart_lot.getStatus());
                process.add(depart_lot.getProcess());
                
                DepartLot item = new DepartLot();
                item.setReport_date(depart_lot.getReport_date());
                item.setPart_revision(depart_lot.getPart_revision());
                item.setDepartreport_id(depart_lot.getDepartreport_id());
                item.setPartrevision_id(depart_lot.getPartrevision_id());
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
        
        process_column1.setCellValueFactory(new PropertyValueFactory<>("process"));
        process_column2.setCellValueFactory(new PropertyValueFactory<>("process"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<DepartLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getDepartLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateDepartLotTable();
        });
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
                System.out.println(current_row);
                System.out.println(item.getPart_number());
                fields.get("part_number"+current_row).setValue(item.getPart_number());
                fields.get("process"+current_row).setValue(item.getProcess());
                fields.get("quantity"+current_row).setValue(""+item.getQuantity());
                fields.get("quantity_box"+current_row).setValue(""+item.getBox_quantity());
                for(String lot_number : item.getLot_number().split(",")){
                    fields.get("lotnumber"+(current_row+offset)).setValue(lot_number);
                    offset++;
                }
                extra_rows = offset;
                
                offset = 0;                for(String comment : item.getComments().split(",")){
                    System.out.println("comment: "+comment);
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
