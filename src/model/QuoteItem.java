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
public class QuoteItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Double unit_price;
    
    //INNER JOINS
    private Double specitem_maximumthickness;
    private Double partrev_area;
    private String metal_name;
    private Double metal_density;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }
    
    //Calculates the volume using the area(mm2) and the thickness(mm)
    public Double getVolume(){
        return getPartrev_areaMM() * getSpecitem_maximumthicknessMM();
    }
    
    //Calculates the weight using the Volume and the Density    
    public Double getWeight(){
        return getVolume() * getMetal_densityGMM();
    }
    
    //Calculates the estimated price using the Weight, Unit Price and Margin
    public Double getEstimatedPrice(){
        return (getWeight() * unit_price);
    }
    
    //Converts specification maximum_thickness from in to mm
    public Double getSpecitem_maximumthicknessMM(){
        return specitem_maximumthickness * 25.4;
    }
    
    //1 in² == 645.16 mm²
    public double getPartrev_areaMM(){
        return partrev_area * 645.16;
    }
    
    //Converts the metal density from (g/cm³) to (g/mm³)    
    public Double getMetal_densityGMM(){
        return metal_density / 1000;
    }
    
    //INNER JOINS
    public Double getSpecitem_maximumthickness(){
        return specitem_maximumthickness;
    }
    
    public void setSpecitem_maximumthickness(Double specitem_maximumthickness){
        this.specitem_maximumthickness = specitem_maximumthickness;
    }
    
    public Double getPartrev_area(){
        return partrev_area;
    }
    
    public void setPartrev_area(Double partrev_area){
        this.partrev_area = partrev_area;
    }
    
    public String getMetal_name() {
        return metal_name;
    }

    public void setMetal_name(String metal_name) {
        this.metal_name = metal_name;
    }
    
    public Double getMetal_density(){
        return metal_density;
    }
    
    public void setMetal_density(Double metal_density){
        this.metal_density = metal_density;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoiceItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof QuoteItem) && (id != null)
            ? id.equals(((QuoteItem) other).id)
            : (other == this);
    }

    /**
     * Quote with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Quote. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    
    }
}
