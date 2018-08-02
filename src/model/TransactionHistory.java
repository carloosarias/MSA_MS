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
public class TransactionHistory implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private String type;
    private Integer quantity;
    private Date date;
    
    // Getters/setters ----------------------------------------------------------------------------
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare TransactionHistory by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof TransactionHistory) && (type != null)
            ? type.equals(((TransactionHistory) other).type)
            : (other == this);
    }

    /**
     * ProcessReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (type != null) 
             ? (this.getClass().hashCode() + type.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this ProcessReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                type);
    }
}
