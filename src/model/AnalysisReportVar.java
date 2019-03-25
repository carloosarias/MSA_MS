/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportVar {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private double value;
    
    //INNER JOINS
    private Integer analysisreport_id;
    private Integer analysistypevar_id;
    private String analysistypevar_name;
    private String analysistypevar_description;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    //INNER JOINS
    public Integer getAnalysisreport_id() {
        return analysisreport_id;
    }

    public void setAnalysisreport_id(Integer analysisreport_id) {
        this.analysisreport_id = analysisreport_id;
    }
    
    public Integer getAnalysistypevar_id() {
        return analysistypevar_id;
    }

    public void setAnalysistypevar_id(Integer analysistypevar_id) {
        this.analysistypevar_id = analysistypevar_id;
    }

    public String getAnalysistypevar_name() {
        return analysistypevar_name;
    }

    public void setAnalysistypevar_name(String analysistypevar_name) {
        this.analysistypevar_name = analysistypevar_name;
    }

    public String getAnalysistypevar_description() {
        return analysistypevar_description;
    }

    public void setAnalysistypevar_description(String analysistypevar_description) {
        this.analysistypevar_description = analysistypevar_description;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare AnalysisReportVar by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof AnalysisReportVar) && (id != null)
            ? id.equals(((AnalysisReportVar) other).id)
            : (other == this);
    }

    /**
     * AnalysisReportVar with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this AnalysisReportVar. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                analysistypevar_name);
    }

}
