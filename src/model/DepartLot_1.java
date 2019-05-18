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
public class DepartLot_1 implements Serializable{
    
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Employee employee;
    private DepartReport_1 depart_report;
    private IncomingReport_1 incoming_report;
    private Date date;
    private Integer qty_out;
    private Integer qty_ava;
    private String comments;
    private boolean open;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public IncomingReport_1 getIncoming_report() {
        return incoming_report;
    }

    public void setIncoming_report(IncomingReport_1 incoming_report) {
        this.incoming_report = incoming_report;
    }

    public DepartReport_1 getDepart_report() {
        return depart_report;
    }

    public void setDepart_report(DepartReport_1 depart_report) {
        this.depart_report = depart_report;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQty_out() {
        return qty_out;
    }

    public void setQty_out(Integer qty_out) {
        this.qty_out = qty_out;
    }
    
    public Integer getQty_ava() {
        return qty_ava;
    }

    public void setQty_ava(Integer qty_ava) {
        this.qty_ava = qty_ava;
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
     * This should compare DepartLot_1 by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof DepartLot_1) && (id != null)
            ? id.equals(((DepartLot_1) other).id)
            : (other == this);
    }
    
    
    /**
     * DepartLot_1 with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartLot_1. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s %s",
                depart_report.toString(), incoming_report.toString());
    }
}
