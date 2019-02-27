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
public class Equipment implements Serializable{
    
    // Properties ---------------------------------------------------------------------------------

    private Integer id;
    private String name;
    private String description;
    private String physical_location;
    private String serial_number;
    private Date next_mantainance;
    private boolean active;
    
    //INNER JOIN
    private String equipmenttype_name;
    private Integer frequency;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhysical_location() {
        return physical_location;
    }

    public void setPhysical_location(String physical_location) {
        this.physical_location = physical_location;
    }
    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }
    
    public Date getNext_mantainance() {
        return next_mantainance;
    }

    public void setNext_mantainance(Date next_mantainance) {
        this.next_mantainance = next_mantainance;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void setActive(boolean active){
        this.active = active;
    }
    
    //INNER JOINS
    
    public String getEquipmenttype_name(){
        return equipmenttype_name;
    }
    
    public void setEquipmenttype_name(String equipmenttype_name){
        this.equipmenttype_name = equipmenttype_name;
    }
    public Integer getFrequecy(){
        return frequency;
    }
    
    public void setFrequency(Integer frequency){
        this.frequency = frequency;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Equipment by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Equipment) && (id != null)
            ? id.equals(((Equipment) other).id)
            : (other == this);
    }

    /**
     * Equipment with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Equipment. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d - %s",
                id, name);
    }
}
