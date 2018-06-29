/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.glass.ui.Robot;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Company;
import model.Employee;
import model.IncomingItem;
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
    private HBox report_hbox;
    @FXML
    private DatePicker reportdate_picker;
    @FXML
    private ComboBox<Employee> employee_combo;
    @FXML
    private ComboBox<Company> company_combo;
    @FXML
    private TextField ponumber_field;
    @FXML
    private TextField packinglist_field;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private VBox lot_vbox;
    @FXML
    private TableView<IncomingLot> incominglot_tableview;
    @FXML
    private TableColumn<IncomingLot, String> lotnumber_column;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column;
    @FXML
    private TableColumn<PartRevision, String> partnumber_column;
    @FXML
    private TableColumn<PartRevision, String> revision_column;
    @FXML
    private TableColumn<IncomingLot, String> status_column;
    @FXML
    private TableColumn<IncomingLot, String> comments_column;
    @FXML
    private TextField lotnumber_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private ComboBox<String> status_combo;
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
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private List<PartRevision> partrev_queue = new ArrayList<PartRevision>();

    private List<IncomingLot> incoming_lots = new ArrayList<IncomingLot>();
    
    private ObservableList<Employee> employee = FXCollections.observableArrayList(
        msabase.getEmployeeDAO().find(MainApp.employee_id)
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        revision_column.setCellValueFactory(new PropertyValueFactory<>("rev"));
        partnumber_column.setCellValueFactory(c -> new SimpleStringProperty(msabase.getPartRevisionDAO().findProductPart(c.getValue()).toString()));
        lotnumber_column.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        
        employee_combo.setItems(employee);
        employee_combo.getSelectionModel().selectFirst();
        company_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        part_combo.setItems(FXCollections.observableArrayList(msabase.getProductPartDAO().list()));
        status_combo.setItems(FXCollections.observableArrayList(MainApp.status_list));
        reportdate_picker.setValue(LocalDate.now());

        lotnumber_field.setOnAction((ActionEvent) -> {
            part_combo.requestFocus();
            ActionEvent.consume();
        });
        
        part_combo.setOnAction((ActionEvent) -> {
            partcombo_text = part_combo.getEditor().textProperty().getValue();
            partcombo_selection = msabase.getProductPartDAO().find(partcombo_text);
            updatePartrev_combo();
            partrev_combo.requestFocus();
            ActionEvent.consume();
        });
        
        partrev_combo.setOnAction((ActionEvent) -> {
            partrevcombo_text = partrev_combo.getEditor().textProperty().getValue();
            partrevcombo_selection = msabase.getPartRevisionDAO().find(partcombo_selection, partrevcombo_text);
            if(!partrev_queue.contains(partrevcombo_selection)){
                partrev_queue.add(partrevcombo_selection);
            }
            updatePartrev_combo();
            quantity_field.requestFocus();
            ActionEvent.consume();
        });
        
        quantity_field.setOnAction((ActionEvent) -> {
            lotnumber_field.requestFocus();
            ActionEvent.consume();
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
            incoming_lot.setComments("n/a");
            incoming_lot.setPartrevision_index(partrevcombo_selection.getId());
            incoming_lots.add(incoming_lot);
            clearLotFields();
            updateLotListview();
            lotnumber_field.requestFocus();
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
        if(partcombo_selection != null){
            partrevcombo_text = null;
            partrevcombo_selection = null;
            partrev_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().list(partcombo_selection)));
        }
        partrev_combo.setDisable(partrev_combo.getItems().isEmpty());
    }
    
    public void saveIncomingReport(){
        IncomingReport incoming_report = new IncomingReport();
        incoming_report.setReport_date(java.sql.Date.valueOf(reportdate_picker.getValue()));
        incoming_report.setPo_number(ponumber_field.getText());
        incoming_report.setPacking_list(packinglist_field.getText());
        msabase.getIncomingReportDAO().create(employee_combo.getSelectionModel().getSelectedItem(), company_combo.getSelectionModel().getSelectedItem(), incoming_report);
        saveIncomingItems(incoming_report);
    }
    
    public void saveIncomingItems(IncomingReport incoming_report){
        for(PartRevision part_revision : partrev_queue){
            IncomingItem incoming_item = new IncomingItem();
            msabase.getIncomingItemDAO().create(incoming_report, part_revision, incoming_item);
            saveIncomingLots(part_revision, incoming_item);
        }
    }
    
    public void saveIncomingLots(PartRevision part_revision, IncomingItem incoming_item){
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getPartRevision_index().equals(part_revision.getId())){
                msabase.getIncomingLotDAO().create(incoming_item, incoming_lot);
            }
        }
    }
    
    public void clearLots(PartRevision part_revision){
        for(IncomingLot incoming_lot : incoming_lots){
            if(incoming_lot.getPartRevision_index().equals(part_revision.getId())){
                incoming_lots.remove(incoming_lot);
            }
        }
    }
    
    public void updateLotListview(){
        incominglot_tableview.setItems(FXCollections.observableArrayList(incoming_lots));
        incominglot_tableview.disableProperty().bind(Bindings.isEmpty(incominglot_tableview.getItems()));
    }
    
    public void clearLotFields(){
        lotnumber_field.setText(null);
        status_combo.getSelectionModel().clearSelection();
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
        return b;
    }
    
    public void clearStyle(){
        lotnumber_field.setStyle(null);
        status_combo.setStyle(null);
        quantity_field.setStyle(null);
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

