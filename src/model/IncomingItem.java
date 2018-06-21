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
public class IncomingItem implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer part_revision_id;
    private String lot_number;
    private Integer quantity;
    private Integer box_quantity;
    private boolean quality_pass;
    private String details;
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPart_revision_id() {
        return part_revision_id;
    }

    public void setPart_revision_id(Integer part_revision_id) {
        this.part_revision_id = part_revision_id;
    }

    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getBox_quantity() {
        return box_quantity;
    }

    public void setBox_quantity(Integer box_quantity) {
        this.box_quantity = box_quantity;
    }
    
    public void setQuality_pass(boolean quality_pass){
        this.quality_pass = quality_pass;
    }
    
    public boolean isQuality_pass(){
        return quality_pass;
    }
    
    public void setDetails(String details){
        this.details = details;
    }
    
    public String getDetails(){
        return details;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare IncomingItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof IncomingItem) && (id != null)
            ? id.equals(((IncomingItem) other).id)
            : (other == this);
    }

    /**
     * IncomingItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this IncomingItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }    
}
