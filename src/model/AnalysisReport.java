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
public class AnalysisReport {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date report_date;
    private String report_time;
    private double result;
    private double applied_adjust; //KG
    private double ph;
    private String formula_timestamp;
    private boolean active;
    private Tank tank;
    private AnalysisType analysis_type;
    private Employee employee;
    
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
    
    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
    
    public double getApplied_adjust() {
        return applied_adjust;
    }

    public void setApplied_adjust(double applied_adjust) {
        this.applied_adjust = applied_adjust;
    }
    
    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }
    
    public String getFormula_timestamp() {
        return formula_timestamp;
    }

    public void setFormula_timestamp(String formula_timestamp) {
        this.formula_timestamp = formula_timestamp;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public AnalysisType getAnalysis_type() {
        return analysis_type;
    }

    public void setAnalysis_type(AnalysisType analysis_type) {
        this.analysis_type = analysis_type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    //Estimated adjust = ((optimal - result) * volume) / 1000
    //KG
    public double getEstimated_adjust() {
        return ((analysis_type.getOptimal() - result) * tank.getVolume())/1000;
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
