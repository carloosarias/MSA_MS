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
public class DepartItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer part_revision_id;
    
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
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare DepartItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof DepartItem) && (id != null)
            ? id.equals(((DepartItem) other).id)
            : (other == this);
    }

    /**
     * DepartItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }    
}
