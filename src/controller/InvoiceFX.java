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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class InvoiceFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private TableView<?> invoice_tableview;
    @FXML
    private TableColumn<?, ?> id_column;
    @FXML
    private TableColumn<?, ?> invoicedate_column;
    @FXML
    private TableColumn<?, ?> client_column;
    @FXML
    private TableColumn<?, ?> billingaddress_column;
    @FXML
    private TableColumn<?, ?> shippingaddress_column;
    @FXML
    private TableColumn<?, ?> pending_column;
    @FXML
    private TextField terms_field;
    @FXML
    private TextField shippingmethod_field;
    @FXML
    private TextField fob_field;
    @FXML
    private TableView<?> incominglot_tableview;
    @FXML
    private TableColumn<?, ?> remision_column;
    @FXML
    private TableColumn<?, ?> part_column;
    @FXML
    private TableColumn<?, ?> revision_column;
    @FXML
    private TableColumn<?, ?> lot_column;
    @FXML
    private TableColumn<?, ?> comments_column;
    @FXML
    private TableColumn<?, ?> lot_qty;
    @FXML
    private TableColumn<?, ?> lot_boxqty_column;
    @FXML
    private TableColumn<?, ?> unitprice_column;
    @FXML
    private TableColumn<?, ?> lotprice_column;
    @FXML
    private TextField total_field;
    @FXML
    private Button add_button;
    
    private Stage add_stage = new Stage();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        add_button.setOnAction((ActionEvent) -> {
            add_button.setDisable(true);
            showAdd_stage();
        });
    }
    
    public void showAdd_stage(){
        try {
            add_stage = new Stage();
            add_stage.initOwner((Stage) root_hbox.getScene().getWindow());
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("/fxml/CreateInvoiceFX.fxml"));
            Scene scene = new Scene(root);
            
            add_stage.setTitle("Nueva Factura");
            add_stage.setResizable(false);
            add_stage.initStyle(StageStyle.UTILITY);
            add_stage.setScene(scene);
            add_stage.showAndWait();
            add_button.setDisable(false);
        } catch (IOException ex) {
            Logger.getLogger(ProductPartFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
