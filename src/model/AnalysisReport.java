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
public class AnalysisReport implements Serializable{
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private Date report_date;
    private String report_time;
    private double quantity_used; //ML
    private double applied_adjust; //KG
    private boolean active;
    
    //INNER JOINS
    private String employee_name;
    private String tank;
    private double volume;
    private String analysis_type;
    private double factor;
    private double optimal;
    
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
    
    public String getReport_time(){
        return report_time;
    }
    
    public void setReport_time(String report_time){
        this.report_time = report_time;
    }

    public double getQuantity_used() {
        return quantity_used;
    }

    public void setQuantity_used(double quantity_used) {
        this.quantity_used = quantity_used;
    }
    
    //Result = Quantity used * factor
    public double getResult() {
        return quantity_used*factor;
    }
    
    //Estimated adjust = ((result - optimal) * volume) / 1000
    //KG
    public double getEstimated_adjust() {
        return ((getResult() - optimal) * volume)/1000;
    }
    
    public double getApplied_adjust(){
        return applied_adjust;
    }
    
    public void setApplied_adjust(double applied_adjust){
        this.applied_adjust = applied_adjust;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    //INNER JOINS
    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getTank() {
        return tank;
    }

    public void setTank(String tank) {
        this.tank = tank;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
    
    public String getAnalysis_type() {
        return analysis_type;
    }

    public void setAnalysis_type(String analysis_type) {
        this.analysis_type = analysis_type;
    }
    
    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getOptimal() {
        return optimal;
    }

    public void setOptimal(double optimal) {
        this.optimal = optimal;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare AnalysisReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof AnalysisReport) && (id != null)
            ? id.equals(((AnalysisReport) other).id)
            : (other == this);
    }

    /**
     * AnalysisReport with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this AnalysisReport. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    }

}
