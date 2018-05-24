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
public class CompanyAddress {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String address;
    private boolean active;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Companies by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof CompanyAddress) && (id != null)
            ? id.equals(((CompanyAddress) other).id)
            : (other == this);
    }

    /**
     * Employee with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this User. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("CompanyAddress[id=%d,address=%s,active=%b]",
                id, address, active);
    }
}
