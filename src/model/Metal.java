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
public class Metal implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String metal_name;
    private double density;
    private boolean active;
    
    // Getters/setters ----------------------------------------------------------------------------
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getMetal_name() {
        return metal_name;
    }
    
    public void setMetal_name(String metal_name) {
        this.metal_name = metal_name;
    }
    
    public double getDensity() {
        return density;
    }
    
    public void setDensity(double density) {
        this.density = density;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void setActive(boolean active){
        this.active = active;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Metal by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Metal) && (id != null)
            ? id.equals(((Metal) other).id)
            : (other == this);
    }

    /**
     * Metal with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Metal. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                metal_name);
    }
}
