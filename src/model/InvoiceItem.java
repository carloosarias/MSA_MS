/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Pavilion Mini
 */
public class InvoiceItem implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private double unit_price;
    private String comments;
    private Integer depart_lot_id;
    // Getters/setters ----------------------------------------------------------------------------
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }
    
    public void setUnit_price(double unit_price){
        this.unit_price = unit_price;
    }
    
    public double getUnit_price(){
        return unit_price;
    }
    
    public void setComments(String comments){
        this.comments = comments;
    }
    
    public String getComments(){
        return comments;
    }
    
    public void setDepart_lot_id(Integer depart_lot_id){
        this.depart_lot_id = depart_lot_id;
    }
    
    public Integer getDepart_lot_id(){
        return depart_lot_id;
    }

    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoiceItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof InvoiceItem) && (id != null)
            ? id.equals(((InvoiceItem) other).id)
            : (other == this);
    }

    /**
     * InvoiceItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Invoice. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    } 
}
