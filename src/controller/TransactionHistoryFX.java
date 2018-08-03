/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<ProductPart> part_list = msabase.getProductPartDAO().listActive(true);
        
        msabase.getDepartLotDAO().listDateRange(msabase.getProductPartDAO().find(1), java.sql.Date.valueOf(LocalDate.MIN), java.sql.Date.valueOf(LocalDate.now()));
        msabase.getProcessReportDAO().listDateRange(msabase.getProductPartDAO().find(1), java.sql.Date.valueOf(LocalDate.MIN), java.sql.Date.valueOf(LocalDate.now()));
        msabase.getIncomingLotDAO().listDateRange(msabase.getProductPartDAO().find(1), java.sql.Date.valueOf(LocalDate.MIN), java.sql.Date.valueOf(LocalDate.now()));
    }    
    
}
