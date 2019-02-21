/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
    
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
    
    //INNER JOIN
    private String part_number;
    private String metal_metalname;
    private String specification_process;
    private String specification_specificationnumber;
    
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
    
    //INNER JOINS
    
    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }
    
    public String getMetal_metalname() {
        return metal_metalname;
    }

    public void setMetal_metalname(String metal_metalname) {
        this.metal_metalname = metal_metalname;
    }

    public String getSpecification_process() {
        return specification_process;
    }

    public void setSpecification_process(String specification_process) {
        this.specification_process = specification_process;
    }

    public String getSpecification_specificationnumber() {
        return specification_specificationnumber;
    }

    public void setSpecification_specificationnumber(String specification_specificationnumber) {
        this.specification_specificationnumber = specification_specificationnumber;
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
        return String.format("%s",
                rev);
    }
    
}
