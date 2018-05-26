/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author Pavilion Mini
 */
public class EmployeeAttendance {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Date date;
    private LocalTime entry_time;
    private LocalTime end_time;
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(LocalTime entry_time) {
        this.entry_time = entry_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare EmployeeAttendances by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof EmployeeAttendance) && (id != null)
            ? id.equals(((EmployeeAttendance) other).id)
            : (other == this);
    }

    /**
     * EmployeeAttendance with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this EmployeeAttendance. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("EmployeeAttendance[id=%d,entry_time=%s,end_time=%s]",
                id, entry_time, end_time);
    }
    
}
