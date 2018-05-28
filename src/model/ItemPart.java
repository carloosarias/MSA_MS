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
public class ItemPart implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String part_number;
    private double area;
    private double initial_weight;
    private double after_weight;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getInitial_weight() {
        return initial_weight;
    }

    public void setInitial_weight(double initial_weight) {
        this.initial_weight = initial_weight;
    }

    public double getAfter_weight() {
        return after_weight;
    }

    public void setAfter_weight(double after_weight) {
        this.after_weight = after_weight;
    }
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare ItemPart by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof ItemPart) && (id != null)
            ? id.equals(((ItemPart) other).id)
            : (other == this);
    }

    /**
     * ItemPart with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this ItemPart. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ItemPart[id=%d,part_number=%s,area=%f,initial_weight=%f,after_weight=%f]", id, part_number, area, initial_weight, after_weight);
    }
}
