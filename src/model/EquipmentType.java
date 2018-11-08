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
public class EquipmentType implements Serializable{
    
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private String name;
    private String description;
    private Integer frequency;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getFrequency(){
        return frequency;
    }
    
    public void setFrequency(Integer frequency){
        this.frequency = frequency;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare EquipmentType by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof EquipmentType) && (id != null)
            ? id.equals(((EquipmentType) other).id)
            : (other == this);
    }

    /**
     * EquipmentType with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this EquipmentType. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d - %s",
                id, name);
    }
}
