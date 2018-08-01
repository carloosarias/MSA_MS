/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class ProcessReport implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String process;
    private Date report_date;
    private String lot_number;
    private Integer quantity;
    private Double amperage;
    private Double voltage;
    private Time start_time;
    private Time end_time;
    private String comments;
    private boolean quality_passed;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getProcess(){
        return process;
    }
    
    public void setProcess(String process){
        this.process = process;
    }

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }
    
    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmperage() {
        return amperage;
    }

    public void setAmperage(Double amperage) {
        this.amperage = amperage;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public boolean isQuality_passed() {
        return quality_passed;
    }

    public void setQuality_passed(boolean quality_passed) {
        this.quality_passed = quality_passed;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare ProcessReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof ProcessReport) && (id != null)
            ? id.equals(((ProcessReport) other).id)
            : (other == this);
    }

    /**
     * ProcessReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this ProcessReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }
}  
