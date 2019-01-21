/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> orderpurchaseincomingreport_tableview;
    @FXML
    private TableColumn<?, ?> orderpurchaseincomingreportid_column;
    @FXML
    private TableColumn<?, ?> orderpurchaseid_column;
    @FXML
    private TableColumn<?, ?> company_column;
    @FXML
    private TableColumn<?, ?> reportdate_column;
    @FXML
    private TableColumn<?, ?> employeeid_column;
    @FXML
    private TableColumn<?, ?> employeename_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private TableView<?> orderpurchaseincomingitem_tableview;
    @FXML
    private TableColumn<?, ?> productid_column;
    @FXML
    private TableColumn<?, ?> description_column;
    @FXML
    private TableColumn<?, ?> quantity_column;
    @FXML
    private TableColumn<?, ?> unitmeasure_column;
    @FXML
    private TableColumn<?, ?> unitsordered_column;
    @FXML
    private TableColumn<?, ?> unitsarrived_column;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        add_button.setOnAction((ActionEvent) -> {
            showAdd_stage();
        });
    }
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            add_stage.initModality(Modality.APPLICATION_MODAL);
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateOrderPurchaseIncomingReportFX.fxml"));
            Scene scene = new Scene(root);

            add_stage.setTitle("Nuevo Reporte Reciba de Orden de Compra");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
