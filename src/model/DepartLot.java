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
public class DepartLot implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String lot_number;
    private Integer quantity;
    private Integer box_quantity;
    private String process;
    private String comments;
    private String po_number;
    private String line_number;
    private boolean rejected;
    private boolean pending;
    private PartRevision part_revision;
    private DepartReport depart_report;

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
    
    public PartRevision getPart_revision() {
        return part_revision;
    }

    public void setPart_revision(PartRevision part_revision) {
        this.part_revision = part_revision;
    }

    public DepartReport getDepart_report() {
        return depart_report;
    }

    public void setDepart_report(DepartReport depart_report) {
        this.depart_report = depart_report;
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
            return String.format("Remisión#%d, Número de Parte:%s Lote:%s, Cantidad:%d Cajas:%d",
                part_revision.toString(), lot_number, quantity, box_quantity);
    }

}
