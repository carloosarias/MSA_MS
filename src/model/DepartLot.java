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
public class DepartLot implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String lot_number;
    private Integer quantity;
    private Integer box_quantity;
    private String process;
    private String comments;
    private boolean rejected;
    private boolean pending;
    
    private PartRevision temp_partrevision;
    private ProductPart temp_productpart;
    
    //INNER JOINS
    private String part_number;
    private String part_revision;
    private Date report_date;
    private Integer departreport_id;
    private Integer partrevision_id;

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
    
    public void setProcess(String process){
        this.process = process;
    }
    
    public String getProcess(){
        return process;
    }
    
    public void setComments(String comments){
        this.comments = comments;
    }
    
    public String getComments(){
        return comments;
    }
    
    public void setRejected(boolean rejected){
        this.rejected = rejected;
    }
    
    public boolean isRejected(){
        return rejected;
    }
    
    public void setPending(boolean pending){
        this.pending = pending;
    }
    
    public boolean isPending(){
        return pending;
    }
    
    public void setTemp_partrevision(PartRevision temp_partrevision){
        this.temp_partrevision = temp_partrevision;
    }
    
    public PartRevision getTemp_partrevision(){
        return temp_partrevision;
    }
    
    public void setTemp_productpart(ProductPart temp_productpart){
        this.temp_productpart = temp_productpart;
    }
    
    public ProductPart getTemp_productpart(){
        return temp_productpart;
    }
    
    public String getStatus(){
        if(pending){
            return "Pendiente";
        }
        if(rejected){
            return "Rechazado";
        }else{
            return "Facturado";
        }
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
    
    public Integer getDepartreport_id() {
        return departreport_id;
    }

    public void setDepartreport_id(Integer departreport_id) {
        this.departreport_id = departreport_id;
    }
    
    public Integer getPartrevision_id() {
        return partrevision_id;
    }

    public void setPartrevision_id(Integer partrevision_id) {
        this.partrevision_id = partrevision_id;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare DepartLot by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof DepartLot) && (id != null)
            ? id.equals(((DepartLot) other).id)
            : (other == this);
    }

    /**
     * DepartLot with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this DepartLot. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
            return String.format("%s %s %s - %d",
                part_number, part_revision, lot_number, quantity);
    }

}
