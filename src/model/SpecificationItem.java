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
public class SpecificationItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private double minimum_thickness;
    private double maximum_thickness;
    
    // Getters/setters ----------------------------------------------------------------------------
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public double getMinimum_thickness() {
        return minimum_thickness;
    }
    
    public void setMinimum_thickness(double minimum_thickness) {
        this.minimum_thickness = minimum_thickness;
    }
    
    public double getMaximum_thickness() {
        return maximum_thickness;
    }
    
    public void setMaximum_thickness(double maximum_thickness) {
        this.maximum_thickness = maximum_thickness;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare SpecificationItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof SpecificationItem) && (id != null)
            ? id.equals(((SpecificationItem) other).id)
            : (other == this);
    }

    /**
     * SpecificationItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this SpecificationItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                id);
    }
}
