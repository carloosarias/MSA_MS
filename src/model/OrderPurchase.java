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
public class OrderPurchase implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String comments;
    private String status;
    private Double exchange_rate;
    private Double iva_rate;
    
    //INNER JOINS
    private String company_name;
    private String companyaddress_address;
    
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getExchange_rate(){
        return exchange_rate;
    }
    
    public void setExchange_rate(Double exchange_rate){
        this.exchange_rate = exchange_rate;
    }
    
    public Double getIva_rate(){
        return iva_rate;
    }
    
    public void setIva_rate(Double iva_rate){
        this.iva_rate = iva_rate;
    }
    
    //INNER JOINS
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompanyaddress_address() {
        return companyaddress_address;
    }

    public void setCompanyaddress_address(String companyaddress_address) {
        this.companyaddress_address = companyaddress_address;
    }
    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare OrderPurchase by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrderPurchase) && (id != null)
            ? id.equals(((OrderPurchase) other).id)
            : (other == this);
    }

    /**
     * OrderPurchase with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this OrderPurchase. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d", id);
    }    

}
