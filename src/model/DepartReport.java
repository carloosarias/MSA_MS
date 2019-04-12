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
public class DepartReport implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private boolean active;
    private Integer total_qty;
    private Integer total_box;
    private Company company;
    private CompanyAddress company_address;
    private Employee employee;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(Integer total_qty) {
        this.total_qty = total_qty;
    }

    public Integer getTotal_box() {
        return total_box;
    }

    public void setTotal_box(Integer total_box) {
        this.total_box = total_box;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyAddress getCompany_address() {
        return company_address;
    }

    public void setCompany_address(CompanyAddress company_address) {
        this.company_address = company_address;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare DepartReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof DepartReport) && (id != null)
            ? id.equals(((DepartReport) other).id)
            : (other == this);
    }

    /**
     * DepartReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }
}
