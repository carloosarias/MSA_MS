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
public class Item implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    private String desc;
    private double unit_price;
    
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
    
    public void setUnit_price(double unit_price){
        this.unit_price = unit_price;
    }
    
    public double getUnit_price(){
        return unit_price;
    }
    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare Item by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Item) && (id != null)
            ? id.equals(((Item) other).id)
            : (other == this);
    }

    /**
     * Item with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Item. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Item[id=%d,name=%s,desc=%s,unit_price=%f]", id, name, desc, unit_price);
    }
}
