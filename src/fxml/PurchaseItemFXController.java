/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import controller.OrderPurchaseDetailsFX;
import dao.JDBC.DAOFactory;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Product;
import model.PurchaseItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class PurchaseItemFXController implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker deliverydate_picker;
    @FXML
    private ComboBox<Product> product_combo;
    @FXML
    private TextArea description_area;
    @FXML
    private TextField unitprice_field;
    @FXML
    private TextField quantity_field;
    @FXML
    private Button save_button;
    @FXML
    private Button cancel_button;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
    private ObservableList<Product> product_list = FXCollections.observableArrayList(
        msabase.getProductDAO().listActive(true)
    );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        product_combo.setItems(product_list);
        save_button.setOnAction((ActionEvent) -> {
            OrderPurchaseDetailsFX.getPurchaseItems().add(mapPurchaseItem(new PurchaseItem()));
            Stage stage = (Stage) root_hbox.getScene().getWindow();
            stage.close();
        });
    }
    
    public PurchaseItem mapPurchaseItem(PurchaseItem purchase_item){
        purchase_item.setDelivery_date(Date.valueOf(deliverydate_picker.getValue()));
        purchase_item.setProduct_id(product_combo.getSelectionModel().getSelectedItem().getId());
        purchase_item.setDescription(description_area.getText());
        purchase_item.setUnit_price(Double.parseDouble(unitprice_field.getText()));
        purchase_item.setQuantity(Integer.parseInt(quantity_field.getText()));
        return purchase_item;
    }
    
}
