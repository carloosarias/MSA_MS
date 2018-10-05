/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dao.JDBC.DAOFactory;
import java.io.Serializable;

/**
 *
 * @author Pavilion Mini
 */
public class QuoteItem implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
    private Integer id;
    private Double unit_price;
    private SpecificationItem temp_specificationitem;
    
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
    
    public SpecificationItem getTemp_specificationitem(){
        return temp_specificationitem;
    }
    
    public void setTemp_specificationitem(SpecificationItem temp_specificationitem){
        this.temp_specificationitem = temp_specificationitem;
    }
    //Converts specification maximum_thickness from in to mm
    public Double getThickness(){
        return getThickness(msabase.getQuoteItemDAO().findSpecificationItem(this));
    }
    
    public Double getThickness(SpecificationItem temp_specificationitem){
        return temp_specificationitem.getMaximum_thickness()/24.5;
    }
    
    //calculates the volume using the area(mm2) and the thickness(mm)
    public Double getVolume(){
        return getVolume(msabase.getQuoteDAO().findPartRevision(msabase.getQuoteItemDAO().findQuote(this)), msabase.getQuoteItemDAO().findSpecificationItem(this));
    }
    
    public Double getVolume(PartRevision temp_partrevision, SpecificationItem temp_specificationitem){
        return temp_partrevision.getArea()*getThickness(temp_specificationitem);
    }
    
    //Converts the metal density from (g/cm3) to (g/mm3)
    public Double getDensity(){
        return getDensity(msabase.getQuoteItemDAO().findSpecificationItem(this));
    }
    
    public Double getDensity(SpecificationItem temp_specificationitem){
        return msabase.getSpecificationItemDAO().findMetal(temp_specificationitem).getDensity() / 1000;
    }
    
    //Calculates the weight using the Volume and the Density
    public Double getWeight(){
        return getWeight(msabase.getQuoteDAO().findPartRevision(msabase.getQuoteItemDAO().findQuote(this)), msabase.getQuoteItemDAO().findSpecificationItem(this));
    }
    
    public Double getWeight(PartRevision temp_partrevision, SpecificationItem temp_specificationitem){
        return getVolume(temp_partrevision, temp_specificationitem) * getDensity(temp_specificationitem);
    }
    
    //Calculates the estimated price using the Weight, Unit Price and Margin
    public Double getEstimatedPrice(){
        return getEstimatedPrice(msabase.getQuoteDAO().findPartRevision(msabase.getQuoteItemDAO().findQuote(this)), msabase.getQuoteItemDAO().findSpecificationItem(this), msabase.getQuoteItemDAO().findQuote(this));
    }
    
    public Double getEstimatedPrice(PartRevision temp_partrevision, SpecificationItem temp_specificationitem, Quote temp_quote){
        return getWeight(temp_partrevision, temp_specificationitem) * unit_price * temp_quote.getMargin();
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
