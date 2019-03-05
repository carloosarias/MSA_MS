/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.JDBC.DAOFactory;
import java.io.Serializable;
import java.util.Date;
import static msa_ms.MainApp.df;

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
    private double estimated_total;
    private boolean active;
    
    //INNER JOINS
    private String spec_number;
    private String spec_process;
    private double partrev_area;
    private Integer company_id;
    private String company_name;
    private String contact_name;
    private String contact_email;
    private String contact_number;
    private String part_number;
    private String part_description;
    private String part_rev;
    
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    
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

    public double getEstimated_total(){
        return estimated_total;
    }
    
    public void setEstimated_total(double estimated_total) {
        this.estimated_total = estimated_total;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public double getCalculatedPrice(){
        double calcultated_price = 0;
        for(QuoteItem quote_item : msabase.getQuoteItemDAO().list(this)){
            calcultated_price += quote_item.getEstimatedPrice();
        }
        return calcultated_price;
    }
    
    //INNER JOINS
    public String getSpec_number() {
        return spec_number;
    }

    public void setSpec_number(String spec_number) {
        this.spec_number = spec_number;
    }

    public String getSpec_process() {
        return spec_process;
    }

    public void setSpec_process(String spec_process) {
        this.spec_process = spec_process;
    }

    public double getPartrev_area() {
        return partrev_area;
    }

    public void setPartrev_area(double partrev_area) {
        this.partrev_area = partrev_area;
    }
    
    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getPart_description() {
        return part_description;
    }

    public void setPart_description(String part_description) {
        this.part_description = part_description;
    }

    public String getPart_rev() {
        return part_rev;
    }

    public void setPart_rev(String part_rev) {
        this.part_rev = part_rev;
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
        try{
            return "$ "+df.format(getEstimated_total())+" USD";
        }catch(Exception e){
            return "N/A";
        }
    
    }
}
