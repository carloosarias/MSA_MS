/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportNEW {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String report_time;
    private double applied_adjust; //KG
    private boolean active;
    
    //INNER JOINS
    private String employee_name;
    private String tank_name;
    private double tank_volume;
    private String analysistype_name;
    private double analysistype_optimal;
    private String analysistype_formula;
    
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

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public double getApplied_adjust() {
        return applied_adjust;
    }

    public void setApplied_adjust(double applied_adjust) {
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

    public String getTank_name() {
        return tank_name;
    }

    public void setTank_name(String tank_name) {
        this.tank_name = tank_name;
    }

    public double getTank_volume() {
        return tank_volume;
    }

    public void setTank_volume(double tank_volume) {
        this.tank_volume = tank_volume;
    }

    public String getAnalysistype_name() {
        return analysistype_name;
    }

    public void setAnalysistype_name(String analysistype_name) {
        this.analysistype_name = analysistype_name;
    }

    public double getAnalysistype_optimal() {
        return analysistype_optimal;
    }

    public void setAnalysistype_optimal(double analysistype_optimal) {
        this.analysistype_optimal = analysistype_optimal;
    }
    
    public String getAnalysistype_formula() {
        return analysistype_formula;
    }

    public void setAnalysistype_formula(String analysistype_formula) {
        this.analysistype_formula = analysistype_formula;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare AnalysisReport by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof AnalysisReportNEW) && (id != null)
            ? id.equals(((AnalysisReportNEW) other).id)
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
