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
    private Integer departreport_id;
    private Integer employee_id;
    private Integer incomingreport_id;
    private Date date;
    private Integer qty_out;
    private String comments;
    
    //CALCULATED VALUES
    private Integer qty_ava;    //qty_out - sum(qty_scrap)
    private boolean open;       //departlot_id not in scrapreport
    
    //INNER JOINS
    private String employee_name;
    private String incomingreport_partnumber;
    private String incomingreport_lot;
    private String incomingreport_po;
    private String incomingreport_line;
    private String incomingreport_packing;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartreport_id() {
        return departreport_id;
    }

    public void setDepartreport_id(Integer departreport_id) {
        this.departreport_id = departreport_id;
    }
    
    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }
    
    public Integer getIncomingreport_id() {
        return incomingreport_id;
    }

    public void setIncomingreport_id(Integer incomingreport_id) {
        this.incomingreport_id = incomingreport_id;
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
    
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    //CALCULATED VALUES
    public Integer getQty_ava() {
        return qty_ava;
    }

    public void setQty_ava(Integer qty_ava) {
        this.qty_ava = qty_ava;
    }
    
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    //INNER JOINS
    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getIncomingreport_partnumber() {
        return incomingreport_partnumber;
    }

    public void setIncomingreport_partnumber(String incomingreport_partnumber) {
        this.incomingreport_partnumber = incomingreport_partnumber;
    }
    
    public String getIncomingreport_lot() {
        return incomingreport_lot;
    }

    public void setIncomingreport_lot(String incomingreport_lot) {
        this.incomingreport_lot = incomingreport_lot;
    }

    public String getIncomingreport_po() {
        return incomingreport_po;
    }

    public void setIncomingreport_po(String incomingreport_po) {
        this.incomingreport_po = incomingreport_po;
    }

    public String getIncomingreport_line() {
        return incomingreport_line;
    }

    public void setIncomingreport_line(String incomingreport_line) {
        this.incomingreport_line = incomingreport_line;
    }

    public String getIncomingreport_packing() {
        return incomingreport_packing;
    }

    public void setIncomingreport_packing(String incomingreport_packing) {
        this.incomingreport_packing = incomingreport_packing;
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
        return String.format("Reciba#%d Parte#%s || Remisión#%d Qty.%d",
                incomingreport_id, incomingreport_partnumber, departreport_id, qty_out);
    }
}
