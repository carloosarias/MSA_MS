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
    private boolean check_value;
    private String details;
    private EquipmentTypeCheck temp_typecheck;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public boolean isCheck_value(){
        return check_value;
    }
    
    public void setCheck_value(boolean check_value) {
        this.check_value = check_value;
    }
    
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public EquipmentTypeCheck getTemp_typecheck() {
        return temp_typecheck;
    }

    public void setTemp_typecheck(EquipmentTypeCheck temp_typecheck) {
        this.temp_typecheck = temp_typecheck;
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
