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
 * @author Carlos
 */
public class IncomingReport_1 implements Serializable {
    
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer count;
    private Employee employee;
    private PartRevision part_revision;
    private Date date;
    private String packing;
    private String po;
    private String line;
    private String lot;
    private Integer qty_in;
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
    
    public Integer getCount(){
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

    public PartRevision getPart_revision() {
        return part_revision;
    }

    public void setPart_revision(PartRevision part_revision) {
        this.part_revision = part_revision;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Integer getQty_in() {
        return qty_in;
    }

    public void setQty_in(Integer qty_in) {
        this.qty_in = qty_in;
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
     * This should compare IncomingReport_1 by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof IncomingReport_1) && (id != null)
            ? id.equals(((IncomingReport_1) other).id)
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
        return String.format("FOLIO: %d Packing: %s PO: %s Line/Rel: %s\n%s Lote: %s",
                id, packing, po, line, part_revision, lot);
    }
    
}
