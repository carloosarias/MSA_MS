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
public class Coating implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    private String desc;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDesc(String desc){
        this.desc = desc;
    }
    
    public String getDesc(){
        return desc;
    }
    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare Coating by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Coating) && (id != null)
            ? id.equals(((Coating) other).id)
            : (other == this);
    }

    /**
     * Coating with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Coating. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Coating[id=%d,name=%s,desc=%s]", id, name, desc);
    }
}
