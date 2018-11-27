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
public class Equipment implements Serializable{
    
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private String name;
    private String description;
    private Date next_mantainance;
    
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
    
    public Date getNext_mantainance() {
        return next_mantainance;
    }

    public void setNext_mantainance(Date next_mantainance) {
        this.next_mantainance = next_mantainance;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Equipment by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Equipment) && (id != null)
            ? id.equals(((Equipment) other).id)
            : (other == this);
    }

    /**
     * Equipment with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Equipment. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d - %s",
                id, name);
    }
}
