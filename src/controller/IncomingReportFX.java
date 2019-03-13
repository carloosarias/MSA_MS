/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
import dao.JDBC.DAOFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import model.DepartLot;
import model.IncomingLot;
import model.IncomingReport;
import model.Tank;
import static msa_ms.MainApp.getFormattedDate;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class IncomingReportFX implements Initializable {
    
    @FXML
    private GridPane root_gridpane;
    @FXML
    private Tab details_tab;
    @FXML
    private TableView<IncomingReport> incomingreport_tableview;
    @FXML
    private TableColumn<IncomingReport, Integer> reportid_column;
    @FXML
    private TableColumn<IncomingReport, String> employee_column;
    @FXML
    private TableColumn<IncomingReport, String> reportdate_column;
    @FXML
    private TableColumn<IncomingReport, String> client_column;
    @FXML
    private TableColumn<IncomingReport, String> ponumber_column;
    @FXML
    private TableColumn<IncomingReport, String> packinglist_column;
    @FXML
    private TableColumn<IncomingReport, String> discrepancy_column;
    @FXML
    private TableView<IncomingLot> incominglot_tableview1;
    @FXML
    private TableColumn<IncomingLot, String> partnumber_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> boxquantity_column1;
    @FXML
    private TableView<IncomingLot> incominglot_tableview2;
    @FXML
    private TableColumn<IncomingLot, String> partnumber_column2;
    @FXML
    private TableColumn<IncomingLot, String> lotnumber_column1;
    @FXML
    private TableColumn<IncomingLot, Integer> quantity_column2;
    @FXML
    private TableColumn<IncomingLot, Integer> boxquantity_column2;
    @FXML
    private TableView<IncomingLot> incominglot_tableview3;
    @FXML
    private TableColumn<IncomingLot, String> partnumber_column3;
    @FXML
    private TableColumn<IncomingLot, String> partrevision_column;
    @FXML
    private TableColumn<IncomingLot, String> lotnumber_column2;
    @FXML
    private TableColumn<IncomingLot, String> quantity_column3;
    @FXML
    private TableColumn<IncomingLot, String> boxquantity_column3;
    @FXML
    private TableColumn<IncomingLot, String> status_column;
    @FXML
    private TableColumn<IncomingLot, String> comments_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIncomingReportTable();
        setIncomingLotTable();
        updateIncomingReportTable();
        
        details_tab.disableProperty().bind(incomingreport_tableview.getSelectionModel().selectedItemProperty().isNull());
        
        incomingreport_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends IncomingReport> observable, IncomingReport oldValue, IncomingReport newValue) -> {
            if(!incomingreport_tableview.getSelectionModel().isEmpty()){
                updateIncomingLotTable();
            }else{
                incominglot_tableview3.getItems().clear();
                incominglot_tableview2.getItems().clear();
                incominglot_tableview1.getItems().clear();
            }
        });
        
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
            updateIncomingReportTable();
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_gridpane.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateIncomingReportFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nuevo Reporte de Reciba");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DepartReportFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateIncomingReportTable(){
        incomingreport_tableview.setItems(FXCollections.observableArrayList(msabase.getIncomingReportDAO().list()));
    }
    
    public void updateIncomingLotTable(){
        incominglot_tableview3.setItems(FXCollections.observableArrayList(msabase.getIncomingLotDAO().list(incomingreport_tableview.getSelectionModel().getSelectedItem())));
        incominglot_tableview2.setItems(FXCollections.observableArrayList(mergeByLot_number(incominglot_tableview3.getItems())));
        incominglot_tableview1.setItems(FXCollections.observableArrayList(mergeByPart_number(incominglot_tableview3.getItems())));
    }
    
    public List<IncomingLot> mergeByLot_number(List<IncomingLot> unfilteredList){
        //find all lot_number + part_number
        ArrayList<String> lotnumber_partnumber = new ArrayList();
        ArrayList<IncomingLot> mergedList = new ArrayList();
        for(IncomingLot incoming_lot : unfilteredList){
            if(lotnumber_partnumber.contains(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number())){
                mergedList.get(lotnumber_partnumber.indexOf(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number())).setQuantity(mergedList.get(lotnumber_partnumber.indexOf(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number())).getQuantity() + incoming_lot.getQuantity());
                mergedList.get(lotnumber_partnumber.indexOf(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number())).setBox_quantity(mergedList.get(lotnumber_partnumber.indexOf(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number())).getBox_quantity() + incoming_lot.getBox_quantity());
            }else{
                lotnumber_partnumber.add(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number());
                IncomingLot item = new IncomingLot();
                item.setLot_number(incoming_lot.getLot_number());
                item.setPart_number(incoming_lot.getPart_number());
                item.setQuantity(incoming_lot.getQuantity());
                item.setBox_quantity(incoming_lot.getBox_quantity());
                mergedList.add(lotnumber_partnumber.indexOf(incoming_lot.getLot_number()+" "+incoming_lot.getPart_number()),item);
            }
        }
        
        return mergedList;
    }
    
    public List<IncomingLot> mergeByPart_number(List<IncomingLot> unfilteredList){
        //find all part_number
        ArrayList<String> partnumber = new ArrayList();
        ArrayList<IncomingLot> mergedList = new ArrayList();
        for(IncomingLot incoming_lot : unfilteredList){
            if(partnumber.contains(incoming_lot.getPart_number())){
                mergedList.get(partnumber.indexOf(incoming_lot.getPart_number())).setQuantity(mergedList.get(partnumber.indexOf(incoming_lot.getPart_number())).getQuantity() + incoming_lot.getQuantity());
                mergedList.get(partnumber.indexOf(incoming_lot.getPart_number())).setBox_quantity(mergedList.get(partnumber.indexOf(incoming_lot.getPart_number())).getBox_quantity() + incoming_lot.getBox_quantity());
            }else{
                partnumber.add(incoming_lot.getPart_number());
                IncomingLot item = new IncomingLot();
                item.setPart_number(incoming_lot.getPart_number());
                item.setQuantity(incoming_lot.getQuantity());
                item.setBox_quantity(incoming_lot.getBox_quantity());
                mergedList.add(partnumber.indexOf(incoming_lot.getPart_number()),item);
            }
        }
        
        return mergedList;
    }
    
    public void setIncomingReportTable(){
        reportid_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        employee_column.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        reportdate_column.setCellValueFactory(c -> new SimpleStringProperty(getFormattedDate(DAOUtil.toLocalDate(c.getValue().getReport_date()))));
        client_column.setCellValueFactory(new PropertyValueFactory<>("client_name"));
        ponumber_column.setCellValueFactory(new PropertyValueFactory<>("po_number"));
        packinglist_column.setCellValueFactory(new PropertyValueFactory<>("packing_list"));
        discrepancy_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDiscrepancyString()));
    }
    
    public void setIncomingLotTable(){
        partnumber_column1.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column2.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partnumber_column3.setCellValueFactory(new PropertyValueFactory<>("part_number"));
        partrevision_column.setCellValueFactory(new PropertyValueFactory<>("part_revision"));
        lotnumber_column1.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column2.setCellValueFactory(new PropertyValueFactory<>("lot_number"));
        lotnumber_column2.setCellFactory(TextFieldTableCell.forTableColumn());
        lotnumber_column2.setOnEditCommit((TableColumn.CellEditEvent<IncomingLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setLot_number(t.getNewValue().replace(" ", "").toUpperCase());
            msabase.getIncomingLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateIncomingLotTable();
        });
        quantity_column1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getQuantity()));
        quantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column3.setOnEditCommit((TableColumn.CellEditEvent<IncomingLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(getQuantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getIncomingLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateIncomingLotTable();
        });
        boxquantity_column1.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column2.setCellValueFactory(new PropertyValueFactory<>("box_quantity"));
        boxquantity_column3.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getBox_quantity()));
        boxquantity_column3.setCellFactory(TextFieldTableCell.forTableColumn());
        boxquantity_column3.setOnEditCommit((TableColumn.CellEditEvent<IncomingLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(getBox_quantityValue(t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue()));
            msabase.getIncomingLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateIncomingLotTable();
        });
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        comments_column.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments_column.setCellFactory(TextFieldTableCell.forTableColumn());
        comments_column.setOnEditCommit((TableColumn.CellEditEvent<IncomingLot, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setComments(t.getNewValue());
            msabase.getIncomingLotDAO().update(t.getTableView().getItems().get(t.getTablePosition().getRow()));
            updateIncomingLotTable();
        });
    }
    
    public Integer getQuantityValue(IncomingLot incoming_lot, String quantity){
        try{
            return Integer.parseInt(quantity);
        }catch(Exception e){
            return incoming_lot.getQuantity();
        }
    }
    
    public Integer getBox_quantityValue(IncomingLot incoming_lot, String box_quantity){
        try{
            return Integer.parseInt(box_quantity);
        }catch(Exception e){
            return incoming_lot.getBox_quantity();
        }
    }
}
