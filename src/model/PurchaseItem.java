/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class PurchaseItem implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer product_id;
    private Date delivery_date;
    private Double unit_price;
    private String description;
    private Integer quantity;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getProduct_id(){
        return product_id;
    }
    
    public void setProduct_id(Integer product_id){
        this.product_id = product_id;
    }
    
    public Date getDelivery_date(){
        return delivery_date;
    }
    
    public void setDelivery_date(Date delivery_date){
        this.delivery_date = delivery_date;
    }
    
    public Double getUnit_price(){
        return unit_price;
    }
    
    public void setUnit_price(Double unit_price){
        this.unit_price = unit_price;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        return String.format("%s",
                description);
    }    
}
