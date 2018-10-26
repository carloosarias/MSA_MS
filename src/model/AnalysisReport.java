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
public class AnalysisReport {
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private double quantity_used;
    private double result;
    private double estimated_adjust;
    private double applied_adjust;
   
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getQuantity_used() {
        return quantity_used;
    }

    public void setQuantity_used(double quantity_used) {
        this.quantity_used = quantity_used;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getEstimated_adjust() {
        return estimated_adjust;
    }

    public void setEstimated_adjust(double estimated_adjust) {
        this.estimated_adjust = estimated_adjust;
    }
    
    public double getApplied_adjust(){
        return applied_adjust;
    }
    
    public void setApplied_adjust(double applied_adjust){
        this.applied_adjust = applied_adjust;
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
