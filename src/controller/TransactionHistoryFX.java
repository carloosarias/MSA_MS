/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import model.ProductPart;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class TransactionHistoryFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private ComboBox<ProductPart> partnumber_combo;
    @FXML
    private DatePicker startdate_picker;
    @FXML
    private DatePicker enddate_picker;

    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partnumber_combo.setItems(FXCollections.observableList(msabase.getProductPartDAO().listActive(true)));
        startdate_picker.setValue(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1));
        enddate_picker.setValue(startdate_picker.getValue().plusMonths(1).minusDays(1));
        
        partnumber_combo.setOnAction((ActionEvent) -> {
            updateList(partnumber_combo.getSelectionModel().getSelectedItem(), java.sql.Date.valueOf(startdate_picker.getValue()), java.sql.Date.valueOf(enddate_picker.getValue()));
        });
        
        startdate_picker.setOnAction((ActionEvent) ->{
            partnumber_combo.fireEvent(new ActionEvent());
        });
        
        enddate_picker.setOnAction((ActionEvent) -> {
            partnumber_combo.fireEvent(new ActionEvent());
        });
    }
    
    public void updateList(ProductPart product_part, Date start_date, Date end_date){
        try{
            msabase.getDepartLotDAO().listDateRange(product_part, start_date, end_date);
            msabase.getProcessReportDAO().listDateRange(product_part, start_date, end_date);
            msabase.getIncomingLotDAO().listDateRange(product_part, start_date, end_date);
        }catch(Exception e){
            System.out.println("test");
        }
    }
    
}
