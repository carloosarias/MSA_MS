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
public class Part {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String name;
    private String desc;
    private String base_metal;
    private double area;
    private double base_weight;
    private double final_weight;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBase_metal() {
        return base_metal;
    }

    public void setBase_metal(String base_metal) {
        this.base_metal = base_metal;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getBase_weight() {
        return base_weight;
    }

    public void setBase_weight(double base_weight) {
        this.base_weight = base_weight;
    }

    public double getFinal_weight() {
        return final_weight;
    }

    public void setFinal_weight(double final_weight) {
        this.final_weight = final_weight;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Parts by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Part) && (id != null)
            ? id.equals(((Part) other).id)
            : (other == this);
    }

    /**
     * Part with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Part. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Part[id=%d,name=%s,desc=%s,base_metal=%s,area=%f,base_weight=%f,final_weight=%f]",
                id, name, desc, base_metal, area, base_weight, final_weight);
    }
}
