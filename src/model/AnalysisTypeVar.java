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
public class AnalysisTypeVar {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    private String description;
    private Double default_value;
    private boolean active;
    
    //INNER JOINS
    private Integer analysistype_id;
    
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

    public Double getDefault_value() {
        return default_value;
    }

    public void setDefault_value(Double default_value) {
        this.default_value = default_value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    //INNER JOINS
    public Integer getAnalysistype_id() {
        return analysistype_id;
    }

    public void setAnalysistype_id(Integer analysistype_id) {
        this.analysistype_id = analysistype_id;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare AnalysisTypeVar by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof AnalysisTypeVar) && (id != null)
            ? id.equals(((AnalysisTypeVar) other).id)
            : (other == this);
    }

    /**
     * AnalysisTypeVar with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this AnalysisTypeVar. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                name);
    }
    
}
