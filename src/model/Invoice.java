/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.DAOUtil;
import java.io.Serializable;
import java.util.Date;
import static msa_ms.MainApp.getFormattedDate;

/**
 *
 * @author Pavilion Mini
 */
public class Invoice implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date invoice_date;
    private String terms;
    private Date payment_date;
    private String check_number;
    private Double quantity_paid;
    private String comments;
    private boolean pending;
    
    //INNER JOINS
    private String company_name;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(Date invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public String getCheck_number() {
        return check_number;
    }

    public void setCheck_number(String check_number) {
        this.check_number = check_number;
    }

    public Double getQuantity_paid() {
        return quantity_paid;
    }

    public void setQuantity_paid(Double quantity_paid) {
        this.quantity_paid = quantity_paid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public void setPending(boolean pending){
        this.pending = pending;
    }
    
    public boolean isPending(){
        return pending;
    }
    //INNER JOINS
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    
    public String pendingToString(){
        if(!pending){
            return "Pagada";
        }else{
            return "Pendiente";
        }
    }
    
    public String payment_dateToString(){
        try{
            return getFormattedDate(DAOUtil.toLocalDate(payment_date));
        }catch(Exception e){
            return "N/A";
        }
    }
    
    public void setPending(String string){
        if(string.equals("Pagada")){
            pending = false;
        }else{
            pending = true;
        }
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Invoice by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Invoice) && (id != null)
            ? id.equals(((Invoice) other).id)
            : (other == this);
    }

    /**
     * Invoice with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Invoice. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }
    
}
