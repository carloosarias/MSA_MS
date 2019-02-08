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
public class OrderPurchaseIncomingReport implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String comments;
    
    //INNER JOINS
    private Integer orderpurchase_id;
    private String orderpurchase_companyname;
    private Integer employee_id;
    private String employee_employeename;
    
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    //INNER JOINS
    public Integer getOrderpurchase_id() {
        return orderpurchase_id;
    }

    public void setOrderpurchase_id(Integer orderpurchase_id) {
        this.orderpurchase_id = orderpurchase_id;
    }

    public String getOrderpurchase_companyname() {
        return orderpurchase_companyname;
    }

    public void setOrderpurchase_companyname(String orderpurchase_companyname) {
        this.orderpurchase_companyname = orderpurchase_companyname;
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_employeename() {
        return employee_employeename;
    }

    public void setEmployee_employeename(String employee_employeename) {
        this.employee_employeename = employee_employeename;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare OrderPurchaseIncomingReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrderPurchaseIncomingReport) && (id != null)
            ? id.equals(((OrderPurchaseIncomingReport) other).id)
            : (other == this);
    }

    /**
     * OrderPurchaseIncomingReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this OrderPurchaseIncomingReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%f", id);
    }
}
