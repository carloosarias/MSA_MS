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
public class IncomingReport implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String po_number;
    private String packing_list;
    private boolean discrepancy;
    private String[] discrepancy_string = new String[]{"Recibido","Discrepancia"};
    
    //INNER JOINS
    private String employee_name;
    private String client_name;
    
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

    public String getPacking_list() {
        return packing_list;
    }

    public void setPacking_list(String packing_list) {
        this.packing_list = packing_list;
    }
    
    public boolean getDiscrepancy(){
        return discrepancy;
    }
    
    public void setDiscrepancy(boolean discrepancy){
        this.discrepancy = discrepancy;
    }
    
    public String getDiscrepancyString(){
        if(discrepancy){
            return discrepancy_string[1];
        }
        
        return discrepancy_string[0];
    }
    
    //INNER JOINS
    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare IncomingReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof IncomingReport) && (id != null)
            ? id.equals(((IncomingReport) other).id)
            : (other == this);
    }

    /**
     * IncomingReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this IncomingReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }

}
