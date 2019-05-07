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
public class DepartReport_1 implements Serializable {
    
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer count;
    private Employee employee;
    private CompanyAddress company_address;
    private Date date;
    private Integer qty_total;
    private String comments;
    private boolean open;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public CompanyAddress getCompany_address() {
        return company_address;
    }

    public void setCompany_address(CompanyAddress company_address) {
        this.company_address = company_address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQty_total() {
        return qty_total;
    }

    public void setQty_total(Integer qty_total) {
        this.qty_total = qty_total;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare DepartReport_1 by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof DepartReport_1) && (id != null)
            ? id.equals(((DepartReport_1) other).id)
            : (other == this);
    }
    
    
    /**
     * DepartReport_1 with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartReport_1. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("REMISIÓN: %d",
                id);
    }
}
