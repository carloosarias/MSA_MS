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
public class InvoiceItem implements Serializable{
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String comments;
    
    private DepartLot temp_departlot;
    private Quote temp_quote;
    
    //INNER JOINS
    private Integer departreport_id;
    private String part_number;
    private String part_revision;
    private String lot_number;
    private Integer departlot_quantity;
    private Integer departlot_boxquantity;
    private double quote_estimatedtotal;
    // Getters/setters ----------------------------------------------------------------------------
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }
    
    public void setComments(String comments){
        this.comments = comments;
    }
    
    public String getComments(){
        return comments;
    }
    
    public DepartLot getTemp_departlot() {
        return temp_departlot;
    }

    public void setTemp_departlot(DepartLot temp_departlot) {
        this.temp_departlot = temp_departlot;
    }

    public Quote getTemp_quote() {
        return temp_quote;
    }

    public void setTemp_quote(Quote temp_quote) {
        this.temp_quote = temp_quote;
    }
    
    //INNER JOINS
    public void setDepartreport_id(Integer departreport_id){
        this.departreport_id = departreport_id;
    }
    
    public Integer getDepartreport_id(){
        return departreport_id;
    }
    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getPart_revision() {
        return part_revision;
    }

    public void setPart_revision(String part_revision) {
        this.part_revision = part_revision;
    }

    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public Integer getDepartlot_quantity() {
        return departlot_quantity;
    }

    public void setDepartlot_quantity(Integer departlot_quantity) {
        this.departlot_quantity = departlot_quantity;
    }

    public Integer getDepartlot_boxquantity() {
        return departlot_boxquantity;
    }

    public void setDepartlot_boxquantity(Integer departlot_boxquantity) {
        this.departlot_boxquantity = departlot_boxquantity;
    }

    public double getQuote_estimatedtotal() {
        return quote_estimatedtotal;
    }

    public void setQuote_estimatedtotal(double quote_estimatedtotal) {
        this.quote_estimatedtotal = quote_estimatedtotal;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare InvoiceItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof InvoiceItem) && (id != null)
            ? id.equals(((InvoiceItem) other).id)
            : (other == this);
    }

    /**
     * InvoiceItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Invoice. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d",
                id);
    } 
}
