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
public class MantainanceItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private String details;
    private boolean active;
    
    //INNER JOIN
    private String typecheck_name;
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    //INNER JOIN
    public String getTypecheck_name() {
        return typecheck_name;
    }

    public void setTypecheck_name(String typecheck_name) {
        this.typecheck_name = typecheck_name;
    }

    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare MantainanceItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof MantainanceItem) && (id != null)
            ? id.equals(((MantainanceItem) other).id)
            : (other == this);
    }

    /**
     * MantainanceItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this MantainanceItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }
}
