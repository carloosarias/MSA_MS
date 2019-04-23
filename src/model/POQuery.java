/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Pavilion Mini
 */
public class POQuery implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private String po_number;
    private Integer incoming_qty;
    private Integer depart_qty;
    private Integer scrap_qty;
    private Integer balance_qty;
    private PartRevision part_revision;
    //metal, specification, productpart, company
    
    // Getters/setters ----------------------------------------------------------------------------
    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public Integer getIncoming_qty() {
        return incoming_qty;
    }

    public void setIncoming_qty(Integer incoming_qty) {
        this.incoming_qty = incoming_qty;
    }

    public Integer getDepart_qty() {
        return depart_qty;
    }

    public void setDepart_qty(Integer depart_qty) {
        this.depart_qty = depart_qty;
    }

    public Integer getScrap_qty() {
        return scrap_qty;
    }

    public void setScrap_qty(Integer scrap_qty) {
        this.scrap_qty = scrap_qty;
    }

    public Integer getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(Integer balance_qty) {
        this.balance_qty = balance_qty;
    }

    public PartRevision getPart_revision() {
        return part_revision;
    }

    public void setPart_revision(PartRevision part_revision) {
        this.part_revision = part_revision;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare POQuery by po_number and part_revision only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof POQuery) && (po_number != null) && (part_revision != null)
            ? (po_number.equals(((POQuery) other).po_number) && part_revision.equals(((POQuery) other).part_revision))
            : (other == this);
    }

    /**
     * DepartReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (po_number != null) && (part_revision != null) 
             ? (this.getClass().hashCode() + po_number.hashCode() + part_revision.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s - %s %s",
                po_number, part_revision.getProduct_part().getPart_number(), part_revision.getRev());
    }
}
