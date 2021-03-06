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
public class PartRevision implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String rev;
    private Date rev_date;
    private double area;
    private double base_weight;
    private double final_weight;
    private boolean active;
    private ProductPart product_part;
    private Metal metal;
    private Specification specification;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public Date getRev_date(){
        return rev_date;
    }
    
    public void setRev_date(Date rev_date){
        this.rev_date = rev_date;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public ProductPart getProduct_part() {
        return product_part;
    }

    public void setProduct_part(ProductPart product_part) {
        this.product_part = product_part;
    }

    public Metal getMetal() {
        return metal;
    }

    public void setMetal(Metal metal) {
        this.metal = metal;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare PartRevision by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof PartRevision) && (id != null)
            ? id.equals(((PartRevision) other).id)
            : (other == this);
    }

    /**
     * PartRevision with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this PartRevision. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Parte: %s Rev: %s",
                product_part ,rev);
    }
    
}
