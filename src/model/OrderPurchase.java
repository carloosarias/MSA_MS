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
public class OrderPurchase implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date order_date;
    private String description;
    private boolean active;
    private Double exchange_rate;
    private Double iva_rate;
    private Double sub_total;
    private Double iva;
    private Double total;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Double getExchange_rate(){
        return exchange_rate;
    }
    
    public void setExchange_rate(Double exchange_rate){
        this.exchange_rate = exchange_rate;
    }
    
    public Double getIva_rate(){
        return iva_rate;
    }
    
    public void setIva_rate(Double iva_rate){
        this.iva_rate = iva_rate;
    }
    
    public Double getSub_total() {
        return sub_total;
    }

    public void setSub_total(Double sub_total) {
        this.sub_total = sub_total;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare OrderPurchase by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrderPurchase) && (id != null)
            ? id.equals(((OrderPurchase) other).id)
            : (other == this);
    }

    /**
     * OrderPurchase with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this OrderPurchase. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ID: %f, Description: %s", id, description);
    }    

}
