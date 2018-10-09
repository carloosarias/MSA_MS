/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JDBC.DAOFactory;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Company;
import model.CompanyContact;
import model.PartRevision;
import model.ProductPart;
import model.QuoteItem;
import model.Specification;
import model.SpecificationItem;

/**
 * FXML Controller class
 *
 * @author Pavilion Mini
 */
public class CreateQuoteFX implements Initializable {

    @FXML
    private HBox root_hbox;
    @FXML
    private DatePicker quotedate_picker;
    @FXML
    private ComboBox<Company> client_combo;
    @FXML
    private ComboBox<CompanyContact> contact_combo;
    @FXML
    private ComboBox<ProductPart> part_combo;
    @FXML
    private ComboBox<PartRevision> partrev_combo;
    @FXML
    private TextField area_field;
    @FXML
    private TextField eau_field;
    @FXML
    private ComboBox<Specification> specificationnumber_combo;
    @FXML
    private TextField process_field;
    @FXML
    private TextArea comments_area;
    @FXML
    private TableView<QuoteItem> quoteitem_tableview;
    @FXML
    private TableColumn<QuoteItem, String> listnumber_column;
    @FXML
    private TableColumn<QuoteItem, String> metalname_column;
    @FXML
    private TableColumn<QuoteItem, String> density_column;
    @FXML
    private TableColumn<QuoteItem, Double> unitprice_column;
    @FXML
    private TableColumn<QuoteItem, String> maximumthickness_column;
    @FXML
    private TableColumn<QuoteItem, String> volume_column;
    @FXML
    private TableColumn<QuoteItem, String> weight_column;
    @FXML
    private TableColumn<QuoteItem, String> estimatedprice_column;
    @FXML
    private Slider margin_slider;
    @FXML
    private TextField estimatedtotal_field;
    @FXML
    private Button save_button;
    
    private ArrayList<QuoteItem> quoteitem_list = new ArrayList<QuoteItem>();
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quotedate_picker.setValue(LocalDate.now());
        client_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyDAO().listClient(true)));
        part_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().findProductPart(QuoteFX.getPartrevision_selection())));
        part_combo.getSelectionModel().selectFirst();
        partrev_combo.setItems(FXCollections.observableArrayList(QuoteFX.getPartrevision_selection()));
        partrev_combo.getSelectionModel().selectFirst();
        specificationnumber_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().findSpecification(QuoteFX.getPartrevision_selection())));
        specificationnumber_combo.getSelectionModel().selectFirst();
        area_field.setText(""+QuoteFX.getPartrevision_selection().getAreaSquareMillimiters());
        process_field.setText(specificationnumber_combo.getSelectionModel().getSelectedItem().getProcess());
        margin_slider.setValue(120);
        setQuoteItemTable();
        setQuoteItemItems();
    }
    
    public void setQuoteItemItems(){
        for(SpecificationItem specification_item : msabase.getSpecificationItemDAO().list(specificationnumber_combo.getSelectionModel().getSelectedItem())){
            QuoteItem item = new QuoteItem();
            item.setTemp_specificationitem(specification_item);
            item.setUnit_price(0.0);
            quoteitem_list.add(item);
        }
        
        quoteitem_tableview.setItems(FXCollections.observableArrayList(quoteitem_list));
    }
    
    public void setQuoteItemTable(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+(quoteitem_tableview.getItems().indexOf(c.getValue())+1)));
        metalname_column.setCellValueFactory(c -> new SimpleStringProperty(""+msabase.getSpecificationItemDAO().findMetal(c.getValue().getTemp_specificationitem())));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getDensity(c.getValue().getTemp_specificationitem())));
        unitprice_column.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        unitprice_column.setOnEditCommit(
                (TableColumn.CellEditEvent<QuoteItem, Double> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setUnit_price(t.getNewValue())
                );
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getThickness(c.getValue().getTemp_specificationitem()))));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getVolume(partrev_combo.getSelectionModel().getSelectedItem(), c.getValue().getTemp_specificationitem()))));
        weight_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getWeight(partrev_combo.getSelectionModel().getSelectedItem(), c.getValue().getTemp_specificationitem()))));
        estimatedprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+df.format(c.getValue().getEstimatedPrice(partrev_combo.getSelectionModel().getSelectedItem(), c.getValue().getTemp_specificationitem(), margin_slider.getValue()))));
    }
    
}
