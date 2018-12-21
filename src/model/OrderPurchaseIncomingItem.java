/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingItem {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer units_arrived;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnits_arrived() {
        return units_arrived;
    }

    public void setUnits_arrived(Integer units_arrived) {
        this.units_arrived = units_arrived;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare OrderPurchaseIncomingItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrderPurchaseIncomingItem) && (id != null)
            ? id.equals(((OrderPurchaseIncomingItem) other).id)
            : (other == this);
    }

    /**
     * OrderPurchaseIncomingItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this OrderPurchaseIncomingItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%f", id);
    }
}
