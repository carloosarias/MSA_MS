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
public class AnalysisType {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    private String description;
    private double min_range;
    private double optimal; //G/L
    private double max_range;
    private String formula;
    private boolean active;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public double getMin_range() {
        return min_range;
    }

    public void setMin_range(double min_range) {
        this.min_range = min_range;
    }

    public double getOptimal() {
        return optimal;
    }

    public void setOptimal(double optimal) {
        this.optimal = optimal;
    }
    
    public double getMax_range() {
        return max_range;
    }

    public void setMax_range(double max_range) {
        this.max_range = max_range;
    }
    
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare AnalysisType by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof AnalysisType) && (id != null)
            ? id.equals(((AnalysisType) other).id)
            : (other == this);
    }

    /**
     * AnalysisType with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this AnalysisType. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                name);
    }

}
