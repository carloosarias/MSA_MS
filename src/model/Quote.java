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
public class Quote implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date quote_date;
    private int estimated_annual_usage;
    private String comments;
    private double margin;
    private double estimated_total;
    private String approved;
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getQuote_date() {
        return quote_date;
    }

    public void setQuote_date(Date quote_date) {
        this.quote_date = quote_date;
    }
    
    public int getEstimated_annual_usage() {
        return estimated_annual_usage;
    }
    
    public void setEstimated_annual_usage(int estimated_annual_usage) {
        this.estimated_annual_usage = estimated_annual_usage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Double getMargin(){
        return margin;
    }
    
    public void setMargin(Double margin){
        this.margin = margin;
    }
    
    public Double getEstimated_total(){
        return estimated_total;
    }
    
    public void setEstimated_total(Double estimated_total){
        this.estimated_total = estimated_total;
    }
    
    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoiceItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Quote) && (id != null)
            ? id.equals(((Quote) other).id)
            : (other == this);
    }

    /**
     * Quote with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Quote. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    
    }
}
