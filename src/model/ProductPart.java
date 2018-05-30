/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Pavilion Mini
 */
public class ProductPart implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String part_number;
    private String rev;
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

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
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
     * This should compare ProductPart by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof ProductPart) && (id != null)
            ? id.equals(((ProductPart) other).id)
            : (other == this);
    }

    /**
     * ProductPart with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this ProductPart. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ProductPart[id=%d,part_number=%s,rev=%s,base_metal=%s,area=%f,base_weight=%f,final_weight=%f]",
                id, part_number,rev,base_metal,area,base_weight,final_weight);
    }
}
