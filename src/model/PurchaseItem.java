/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.DAOUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class PurchaseItem implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer units_ordered;
    private double price_timestamp;
    private double price_updated;
    private Date date_modified;
    private boolean modified;
    
    private ProductSupplier temp_productsupplier;
    
    //INNER JOINS
    private String productsupplier_serialnumber;
    private String product_description;
    private double productsupplier_quantity;
    private String product_unitmeasure;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUnits_ordered() {
        return units_ordered;
    }

    public void setUnits_ordered(Integer units_ordered) {
        this.units_ordered = units_ordered;
    }

    public double getPrice_timestamp() {
        return price_timestamp;
    }

    public void setPrice_timestamp(double price_timestamp) {
        this.price_timestamp = price_timestamp;
    }

    public double getPrice_updated() {
        return price_updated;
    }

    public void setPrice_updated(double price_updated) {
        this.price_updated = price_updated;
        this.modified = true;
        this.date_modified = DAOUtil.toUtilDate(LocalDate.now());
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    public ProductSupplier getTemp_productsupplier(){
        return temp_productsupplier;
    }
        
    public void setTemp_productsupplier(ProductSupplier temp_productsupplier){
        this.temp_productsupplier = temp_productsupplier;
    }
    
    
    public Double getPrice_unit(){
        if(modified){
            return price_updated;
        }
        return price_timestamp;
    }
    
    public Double getPrice_total(){
        if(modified){
            return price_updated*units_ordered;
        }
        return price_timestamp*units_ordered;
    }
    
    //INNER JOINS
    public String getProductsupplier_serialnumber() {
        return productsupplier_serialnumber;
    }

    public void setProductsupplier_serialnumber(String productsupplier_serialnumber) {
        this.productsupplier_serialnumber = productsupplier_serialnumber;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getProductsupplier_quantity() {
        return productsupplier_quantity;
    }

    public void setProductsupplier_quantity(double productsupplier_quantity) {
        this.productsupplier_quantity = productsupplier_quantity;
    }

    public String getProduct_unitmeasure() {
        return product_unitmeasure;
    }

    public void setProduct_unitmeasure(String product_unitmeasure) {
        this.product_unitmeasure = product_unitmeasure;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare PurchaseItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof PurchaseItem) && (id != null)
            ? id.equals(((PurchaseItem) other).id)
            : (other == this);
    }

    /**
     * PurchaseItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this PurchaseItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%f", id);
    }

}
