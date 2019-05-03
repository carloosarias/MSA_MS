/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class ScrapReport_1 {
    
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Employee employee;
    private IncomingReport_1 incoming_report;
    private Date date;
    private Integer qty_scrap;
    private String comments;
    
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQty_scrap() {
        return qty_scrap;
    }

    public void setQty_scrap(Integer qty_scrap) {
        this.qty_scrap = qty_scrap;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare IncomingReport_1 by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof ScrapReport_1) && (id != null)
            ? id.equals(((ScrapReport_1) other).id)
            : (other == this);
    }
    
    
    /**
     * IncomingReport_1 with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this IncomingLot. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("FOLIO: %d",
                id);
    }
}
