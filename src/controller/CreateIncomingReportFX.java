/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Company;
import model.DepartLot;
import model.DepartReport;
import model.Employee;
import model.IncomingLot;
import model.IncomingReport;
import model.PartRevision;
import model.ProductPart;
import msa_ms.MainApp;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private TextField ponumber_field;
    @FXML
    private TextField packinglist_field;
    @FXML
    private ComboBox<String> status_combo;
    @FXML
    private VBox departreport_vbox;
    @FXML
    private ComboBox<DepartReport> departreport_combo;
    @FXML
    private Label departreport_label;
    @FXML
    private ComboBox<DepartLot> departlot_combo;
    @FXML
    private Label departlot_label;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, String> lotnumber_column;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column;
    @FXML
    private TableColumn<IncomingLot, String> partnumber_column;
    @FXML
    private TableColumn<IncomingLot, String> revision_column;
    @FXML
    private TableColumn<IncomingLot, String> status_column;
    @FXML
    private TableColumn<IncomingLot, String> comments_column;
    @FXML
    private Button lot_add_button;
    @FXML
    private Button lot_delete_button;
    @FXML
    private Button save_button;
    
    private ProductPart partcombo_selection;
    private String partcombo_text;
    
    private PartRevision partrevcombo_selection;
    private String partrevcombo_text;
    
    private DepartReport departreportcombo_selection;
    private String departreportcombo_text;
    
    private DepartLot departlotcombo_selection;
    
    private List<DepartLot> departlot_queue = new ArrayList<DepartLot>();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private List<IncomingLot> incoming_lots = new ArrayList<IncomingLot>();
    
    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        departreport_vbox.getChildren().removeAll(departreport_label, departreport_combo, departlot_label, departlot_combo);
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(msabase.getPartRevisionDAO().find(c.getValue().getPart_revision_id())).toString()));
        revision_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().find(c.getValue().getPart_revision_id()).getRev()));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<IncomingLot, String> t) -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setComments(t.getNewValue());
        });
        
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().listActive(true)));
        status_combo.setItems(FXCollections.observableArrayList(MainApp.status_list));
        status_combo.getSelectionModel().selectFirst();
        reportdate_picker.setValue(LocalDate.now());
        
        status_combo.setOnAction((ActionEvent) -> {
            if(status_combo.getSelectionModel().getSelectedItem().equals("Rechazo")){
                departreport_vbox.getChildren().addAll(departreport_label, departreport_combo, departlot_label, departlot_combo);
                departreport_combo.setItems(FXCollections.observableArrayList(msabase.getDepartLotDAO().listDepartReport(false)));
                departreport_combo.requestFocus();
            }else{
                departreport_vbox.getChildren().removeAll(departreport_label, departreport_combo, departlot_label, departlot_combo);
                departreport_combo.getItems().clear();
                departreport_combo.getEditor().setText(null);
                departlot_combo.getItems().clear();
            }
            departreport_combo.setDisable(departreport_combo.getItems().isEmpty());
        });
        
        departreport_combo.setOnAction((ActionEvent) -> {
            departreportcombo_text = departreport_combo.getEditor().textProperty().getValue();
            try{
                departreportcombo_selection = msabase.getDepartReportDAO().find(Integer.parseInt(departreportcombo_text));
            } catch(Exception e){
                departreport_combo.getEditor().selectAll();
            }
            updateDepartLotCombo();
            departlot_combo.requestFocus();
            ActionEvent.consume();
        });
        
        departlot_combo.setOnAction((ActionEvent) -> {
            departlotcombo_selection = departlot_combo.getSelectionModel().getSelectedItem();
            lotnumber_field.requestFocus();
            ActionEvent.consume();
        });
        
        lotnumber_field.setOnAction((ActionEvent) -> {
            part_combo.requestFocus();
            ActionEvent.consume();
        });
        
        part_combo.setOnAction((ActionEvent) -> {
            partcombo_text = part_combo.getEditor().textProperty().getValue();
            partcombo_selection = msabase.getProductPartDAO().find(partcombo_text);
            if(partcombo_selection == null){
                part_combo.getEditor().selectAll();
            }else{
                updatePartrev_combo();
                partrev_combo.requestFocus();
            }
            ActionEvent.consume();
        });
        
        partrev_combo.setOnAction((ActionEvent) -> {
            if(partcombo_selection == null){
                ActionEvent.consume();
                return;
            }
                partrevcombo_text = partrev_combo.getEditor().textProperty().getValue();
                partrevcombo_selection = msabase.getPartRevisionDAO().find(partcombo_selection, partrevcombo_text);
                if(partrevcombo_selection == null){
                    partrev_combo.getEditor().selectAll();
                }
                else{
                    quantity_field.requestFocus();
                    ActionEvent.consume();
                }
        });
        
        quantity_field.setOnAction((ActionEvent) -> {
            lot_add_button.fireEvent(new ActionEvent());
        });
        
        lot_add_button.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                lot_add_button.fireEvent(new ActionEvent());
            }
        });
        
        lot_add_button.setOnAction((ActionEvent) -> {
            if(!testLotFields()){
                return;
            }
            IncomingLot incoming_lot = new IncomingLot();
            incoming_lot.setLot_number(lotnumber_field.getText());
            incoming_lot.setQuantity(Integer.parseInt(quantity_field.getText()));
            incoming_lot.setBox_quantity(1);
            incoming_lot.setStatus(status_combo.getSelectionModel().getSelectedItem());
            incoming_lot.setComments("");
            incoming_lot.setPart_revision_id(partrevcombo_selection.getId());
            if(incoming_lot.getStatus().equals("Rechazo")){
                incoming_lot.setDepart_lot_id(departlotcombo_selection.getId());
                departlot_queue.add(departlotcombo_selection);
                incoming_lot.setComments(incoming_lot.getComments()+"Folio de Remisión #"+msabase.getDepartLotDAO().findDepartReport(departlotcombo_selection));
            }
            incoming_lots.add(incoming_lot);
            clearFields();
            updateLotListview();
            lotnumber_field.requestFocus();
        });
        
        incominglot_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingLot> observable, IncomingLot oldValue, IncomingLot newValue) -> {
            lot_delete_button.setDisable(incominglot_tableview.getSelectionModel().isEmpty());
        });
        
        lot_delete_button.setOnAction((ActionEvent) -> {
            incoming_lots.remove(incominglot_tableview.getSelectionModel().getSelectedItem());
            updateLotListview();
        });
       
        save_button.setOnAction((ActionEvent) -> {
            if(!testSaveFields()){
                return;
            }
            saveIncomingReport();
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
        
    }   
    
    public boolean testSaveFields(){
        boolean b = true;
        clearStyle();
        if(incoming_lots.isEmpty()){
            incominglot_tableview.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(packinglist_field.getText().replace(" ","").equals("")){
            packinglist_field.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        
        if(ponumber_field.getText().replace(" ", "").equals("")){
            ponumber_field.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(company_combo.getSelectionModel().isEmpty()){
            company_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        if(reportdate_picker.getValue() == null){
            reportdate_picker.setStyle("-fx-background-color: lightpink;");
        }
        return b;
    }
    
    public void updatePartrev_combo(){
        try{
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection)));
        } catch(Exception e){
            partrev_combo.getItems().clear();
        }
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
    public void saveIncomingReport(){
        IncomingReport incoming_report = new IncomingReport();
        incoming_report.setReport_date(java.sql.Date.valueOf(reportdate_picker.getValue()));
        incoming_report.setPo_number(ponumber_field.getText());
        incoming_report.setPacking_list(packinglist_field.getText());
        msabase.getIncomingReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), incoming_report);
        saveIncomingLots(incoming_report);
    }
    
    public void saveIncomingLots(IncomingReport incoming_report){
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getDepart_lot_id() != null){
                msabase.getIncomingLotDAO().create(incoming_report, msabase.getDepartLotDAO().find(incoming_lot.getDepart_lot_id()), msabase.getPartRevisionDAO().find(incoming_lot.getPart_revision_id()), incoming_lot);
                DepartLot depart_lot = msabase.getDepartLotDAO().find(incoming_lot.getDepart_lot_id());
                depart_lot.setRejected(true);
                msabase.getDepartLotDAO().update(depart_lot);
            }else{
                msabase.getIncomingLotDAO().create(incoming_report, msabase.getPartRevisionDAO().find(incoming_lot.getPart_revision_id()), incoming_lot);
            }
        }
    }
    
    public void updateLotListview(){
        incominglot_tableview.setItems(FXCollections.observableArrayList(incoming_lots));
        incominglot_tableview.disableProperty().bind(Bindings.isEmpty(incominglot_tableview.getItems()));
    }
    
    public void clearFields(){
        status_combo.getSelectionModel().selectFirst();
        departreport_combo.getItems().clear();
        departlot_combo.getItems().clear();
        departreportcombo_selection = null;
        departlotcombo_selection = null;
        lotnumber_field.setText(null);
        part_combo.getSelectionModel().select(null);
        part_combo.getEditor().setText(null);
        partrev_combo.getSelectionModel().clearSelection();
        partrev_combo.getEditor().setText(null);
        quantity_field.setText(null);
    }
    
    public boolean testLotFields(){
        boolean b = true;
        clearStyle();
        if(lotnumber_field.getText().replace(" ", "").equals("")){
            lotnumber_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(status_combo.getSelectionModel().isEmpty()){
            status_combo.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(quantity_field.getText());
        }catch(Exception e){
            quantity_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            partcombo_selection.getId();
        }
        catch(Exception e){
            part_combo.setStyle("-fx-background-color: lightpink;");
            part_combo.getSelectionModel().select(null);
            part_combo.getEditor().setText(null);
            b = false;
        }
        try {
            partrevcombo_selection.getId();
        }
        catch(Exception e){
            partrev_combo.setStyle("-fx-background-color: lightpink;");
            partrev_combo.getSelectionModel().select(null);
            partrev_combo.getEditor().setText(null);
            b = false;
        }
        if(status_combo.getSelectionModel().getSelectedItem().equals("Rechazo")){
            try{
                departlotcombo_selection.getId();
            }catch(Exception e){
                departlot_combo.setStyle("-fx-background-color: lightpink;");
                departlot_combo.getSelectionModel().select(null);
                b = false;
            }
        }
        
        return b;
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        status_combo.setStyle(null);
        quantity_field.setStyle(null);
        part_combo.setStyle(null);
        partrev_combo.setStyle(null);
        departreport_combo.setStyle(null);
    }
    
    public void updateDepartLotCombo(){
        List<DepartLot> list = new ArrayList<DepartLot>();
        try{
            list = msabase.getDepartLotDAO().list(departreportcombo_selection, false);
            list.removeAll(departlot_queue);
        } catch(Exception e){
            
        }
        departlot_combo.setItems(FXCollections.observableArrayList(list));
        departlot_combo.setDisable(departlot_combo.getItems().isEmpty());
    }
    
/**
 * Handles tab/shift-tab keystrokes to navigate to other fields,
 * ctrl-tab to insert a tab character in the text area.
 */
public class TabTraversalEventHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Node node = (Node) event.getSource();
            if (node instanceof TextArea) {
                TextAreaSkin skin = (TextAreaSkin) ((TextArea)node).getSkin();
                if (!event.isControlDown()) {
                    // Tab or shift-tab => navigational action
                    if (event.isShiftDown()) {
                        skin.getBehavior().traversePrevious();
                    } else {
                        skin.getBehavior().traverseNext();
                    }
                } else {
                    // Ctrl-Tab => insert a tab character in the text area
                    TextArea textArea = (TextArea) node;
                    textArea.replaceSelection("\t");
                }
                event.consume();
            }
        }
    }
}
}

