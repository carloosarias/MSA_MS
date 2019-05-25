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
public class Specification implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String specification_number;
    private String specification_name;
    private String process;
    private boolean active;
    
    // Getters/setters ----------------------------------------------------------------------------
    
    public Specification(){
        id = null;
        specification_number = "N/A";
        specification_name = "N/A";
        process = "N/A";
        active = false;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getSpecification_number() {
        return specification_number;
    }
    
    public void setSpecification_number(String specification_number) {
        this.specification_number = specification_number;
    }
    
    public String getSpecification_name() {
        return specification_name;
    }
    
    public void setSpecification_name(String specification_name) {
        this.specification_name = specification_name;
    }
    
    public String getProcess() {
        return process;
    }
    
    public void setProcess(String process) {
        this.process = process;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Specification by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Specification) && (id != null)
            ? id.equals(((Specification) other).id)
            : (other == this);
    }

    /**
     * Specification with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Specification. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s: %s",
                specification_number, process);
    }
}
