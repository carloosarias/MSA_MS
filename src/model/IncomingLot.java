/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingLot implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String po_number;
    private String line_number;
    private String lot_number;
    private Integer quantity;
    private Integer box_quantity;
    private String status;
    private String comments;
    
    private PartRevision temp_partrevision;
    private DepartLot temp_departlot;
    private ProductPart temp_productpart;
    
    //INNER JOINS
    private String part_number;
    private String part_revision;
    private Date report_date;
    private Integer incomingreport_id;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getBox_quantity() {
        return box_quantity;
    }

    public void setBox_quantity(Integer box_quantity) {
        this.box_quantity = box_quantity;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setComments(String comments){
        this.comments = comments;
    }
    
    public String getComments(){
        return comments;
    }
    
    public void setTemp_partrevision(PartRevision temp_partrevision){
        this.temp_partrevision = temp_partrevision;
    }
    
    public PartRevision getTemp_partrevision(){
        return temp_partrevision;
    }
    
    public void setTemp_departlot(DepartLot temp_departlot){
        this.temp_departlot = temp_departlot;
    }
    
    public DepartLot getTemp_departlot(){
        return temp_departlot;
    }
    
    public void setTemp_productpart(ProductPart temp_productpart){
        this.temp_productpart = temp_productpart;
    }
    
    public ProductPart getTemp_productpart(){
        return temp_productpart;
    }
    
    //INNER JOINS
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
    
    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }
    
    public Integer getIncomingreport_id() {
        return incomingreport_id;
    }

    public void setIncomingreport_id(Integer incomingreport_id) {
        this.incomingreport_id = incomingreport_id;
    }
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare IncomingLot by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof IncomingLot) && (id != null)
            ? id.equals(((IncomingLot) other).id)
            : (other == this);
    }
    
    
    /**
     * IncomingLot with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this IncomingLot. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s",
                lot_number);
    }    

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getLine_number() {
        return line_number;
    }

    public void setLine_number(String line_number) {
        this.line_number = line_number;
    }
    
}
