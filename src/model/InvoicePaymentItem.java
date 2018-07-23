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
public class InvoicePaymentItem {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer invoice_id;
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(Integer invoice_id) {
        this.invoice_id = invoice_id;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoicePaymentReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof InvoicePaymentReport) && (id != null)
            ? id.equals(((InvoicePaymentReport) other).id)
            : (other == this);
    }

    /**
     * InvoicePaymentReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this InvoicePaymentReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                id);
    }    

}
