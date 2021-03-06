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
public class InvoicePaymentItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer invoice_id;
    private Invoice temp_invoice;
    //INNER JOINS
    private Date invoice_date;
    private String invoice_terms;
    
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
    
    public Invoice getTemp_invoice(){
        return temp_invoice;
    }
    
    public void setTemp_invoice(Invoice temp_invoice){
        this.temp_invoice = temp_invoice;
    }
    
    public Date getInvoice_date(){
        return invoice_date;
    }
    
    public void setInvoice_date(Date invoice_date){
        this.invoice_date = invoice_date;
    }
    
    public String getInvoice_terms(){
        return invoice_terms;
    }
    
    public void setInvoice_terms(String invoice_terms){
        this.invoice_terms = invoice_terms;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoicePaymentItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof InvoicePaymentItem) && (id != null)
            ? id.equals(((InvoicePaymentItem) other).id)
            : (other == this);
    }

    /**
     * InvoicePaymentItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this InvoicePaymentItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                id);
    }    

}
