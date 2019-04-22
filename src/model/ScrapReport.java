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
public class ScrapReport implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String po_number;
    private Integer quantity;
    private String comments;
    private boolean active;
    private Employee employee;
    private PartRevision part_revision;
    
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
    
    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PartRevision getPart_revision() {
        return part_revision;
    }

    public void setPart_revision(PartRevision part_revision) {
        this.part_revision = part_revision;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare ScrapReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof ScrapReport) && (id != null)
            ? id.equals(((ScrapReport) other).id)
            : (other == this);
    }

    /**
     * ScrapReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this ScrapReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("PO#: %s Parte: %s Cantidad: %d",
                po_number, part_revision, quantity);
    }
}
