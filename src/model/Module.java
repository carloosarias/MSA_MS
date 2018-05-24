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
public class Module implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getName() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare Modules by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Module) && (id != null)
            ? id.equals(((Module) other).id)
            : (other == this);
    }

    /**
     * Modules with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Module. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Module[id=%d,name=%s]", id, name);
    }
}
