/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DAOUtil;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Company;
import model.CompanyContact;
import model.PartRevision;
import model.ProductPart;
import model.Quote;
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
    private TableColumn<QuoteItem, String> unitprice_column;
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
    private TextField actualprice_field;
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
        client_combo.getItems().setAll(msabase.getCompanyDAO().listClient(true));
        part_combo.getItems().setAll(msabase.getPartRevisionDAO().findProductPart(QuoteFX.getPartrevision_selection()));
        part_combo.getSelectionModel().selectFirst();
        partrev_combo.getItems().setAll(QuoteFX.getPartrevision_selection());
        partrev_combo.getSelectionModel().selectFirst();
        specificationnumber_combo.setItems(FXCollections.observableArrayList(msabase.getPartRevisionDAO().findSpecification(QuoteFX.getPartrevision_selection())));
        specificationnumber_combo.getSelectionModel().selectFirst();
        area_field.setText(""+partrev_combo.getSelectionModel().getSelectedItem().getArea());
        process_field.setText(specificationnumber_combo.getSelectionModel().getSelectedItem().getProcess());
        margin_slider.setValue(120);
        setQuoteItemList();
        setQuoteItemTable();
        setQuoteItemItems();
        
        margin_slider.setOnMouseReleased((ActionEvent) -> {
            setQuoteItemItems();
            quoteitem_tableview.refresh();
        });
        
        client_combo.setOnAction((ActionEvent) -> {
            contact_combo.setItems(FXCollections.observableArrayList(msabase.getCompanyContactDAO().list(client_combo.getSelectionModel().getSelectedItem(), true)));
        });
        
        save_button.setOnAction((ActionEvent) -> {
            if(!testFields()){
                return;
            }else{
                saveQuote();
                Stage stage = (Stage) root_hbox.getScene().getWindow();
                stage.close();
            }
        });
    }
    
    public void saveQuote(){
        Quote quote = new Quote();
        quote.setQuote_date(DAOUtil.toUtilDate(quotedate_picker.getValue()));
        quote.setEstimated_annual_usage(Integer.parseInt(eau_field.getText()));
        quote.setComments(comments_area.getText());
        quote.setMargin(margin_slider.getValue());
        quote.setEstimated_total(Double.parseDouble(estimatedtotal_field.getText()));
        quote.setApproved("Pendiente");
        quote.setSpec_number(partrev_combo.getSelectionModel().getSelectedItem().getSpecification_specificationnumber());
        quote.setSpec_process(partrev_combo.getSelectionModel().getSelectedItem().getSpecification_process());
        quote.setPartrev_area(partrev_combo.getSelectionModel().getSelectedItem().getArea());
        quote.setCompany_id(client_combo.getSelectionModel().getSelectedItem().getId());
        quote.setCompany_name(client_combo.getSelectionModel().getSelectedItem().getName());
        quote.setContact_name(contact_combo.getSelectionModel().getSelectedItem().getName());
        quote.setContact_email(contact_combo.getSelectionModel().getSelectedItem().getEmail());
        quote.setContact_number(contact_combo.getSelectionModel().getSelectedItem().getPhone_number());
        quote.setPart_number(part_combo.getSelectionModel().getSelectedItem().getPart_number());
        quote.setPart_description(part_combo.getSelectionModel().getSelectedItem().getDescription());
        quote.setPart_rev(partrev_combo.getSelectionModel().getSelectedItem().getRev());
        
        
        msabase.getQuoteDAO().create(partrev_combo.getSelectionModel().getSelectedItem(), contact_combo.getSelectionModel().getSelectedItem(), quote);
        
        saveQuoteItems(quote);
    }
    
    public void saveQuoteItems(Quote quote){
        for(QuoteItem item : quoteitem_list){
            msabase.getQuoteItemDAO().create(item.getTemp_specificationitem(), quote, item);
        }
    }
    
    public boolean testFields(){
        boolean b = true;
        clearStyle();
        if(quotedate_picker.getValue() == null){
            quotedate_picker.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(comments_area.getText().replace(" ", "").equals("")){
            comments_area.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        if(contact_combo.getSelectionModel().isEmpty()){
            contact_combo.setStyle("-fx-background-color: lightpink ;");
            b = false;
        }
        try{
            Integer.parseInt(eau_field.getText());
        } catch(Exception e){
            eau_field.setText("0.0");
            eau_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        try{
            Double.parseDouble(area_field.getText());
        } catch(Exception e){
            area_field.setText("0.0");
            area_field.setStyle("-fx-background-color: lightpink;");
            b = false;
        }
        return b;
    }
    
    public void clearStyle(){
        quotedate_picker.setStyle(null);
        eau_field.setStyle(null);
        comments_area.setStyle(null);
        contact_combo.setStyle(null);
        area_field.setStyle(null);
    }
    
    public void setQuoteItemList(){
        quoteitem_list.clear();
        for(SpecificationItem specification_item : msabase.getSpecificationItemDAO().list(specificationnumber_combo.getSelectionModel().getSelectedItem())){
            QuoteItem item = new QuoteItem();
            item.setTemp_specificationitem(specification_item);
            item.setUnit_price(0.0);
            item.setMetal_name(item.getTemp_specificationitem().getMetal_name());
            item.setMetal_density(item.getTemp_specificationitem().getMetal_density());
            item.setQuote_margin(margin_slider.getValue());
            item.setPartrev_area(partrev_combo.getSelectionModel().getSelectedItem().getArea());
            item.setSpecitem_maximumthickness(specification_item.getMaximum_thickness());
            quoteitem_list.add(item);
        }
    }
    public void setQuoteItemItems(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        quoteitem_tableview.setItems(FXCollections.observableArrayList(quoteitem_list));
        actualprice_field.setText(df.format((getTotal() * 100) / margin_slider.getValue()));
        estimatedtotal_field.setText(df.format(getTotal()));
    }
    
    public void setQuoteItemTable(){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        listnumber_column.setCellValueFactory(c -> new SimpleStringProperty(""+(quoteitem_tableview.getItems().indexOf(c.getValue())+1)));
        metalname_column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMetal_name()));
        density_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getMetal_densityGMM())+" G/MM³"));
        unitprice_column.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getUnit_price()));
        unitprice_column.setCellFactory(TextFieldTableCell.forTableColumn());
        unitprice_column.setOnEditCommit((TableColumn.CellEditEvent<QuoteItem, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnit_price(getUnit_priceValue(t.getNewValue()));
            quoteitem_tableview.refresh();
            setQuoteItemItems();
        });
        maximumthickness_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getSpecitem_maximumthicknessMM())+" MM"));
        volume_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getVolume())+" MM³"));
        weight_column.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getWeight())+" G"));
        estimatedprice_column.setCellValueFactory(c -> new SimpleStringProperty("$ "+df.format(c.getValue().getEstimatedPrice())+" USD"));
    }
    
    public Double getUnit_priceValue(String unit_price){
        try{
            return Double.parseDouble(unit_price);
        }catch(Exception e){
            return 0.0;
        }
    }
    
    public Double getTotal(){
        Double total = 0.0;
        for(QuoteItem item : quoteitem_list){
            item.setQuote_margin(margin_slider.getValue());
            total += item.getEstimatedPrice();
        }
        return total;
    }
}
