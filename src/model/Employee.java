/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pavilion Mini
 */
public class Employee implements Serializable {
    
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String user;
    private String password;
    private String first_name;
    private String last_name;
    private Date hire_date;
    private String entry_time;
    private String end_time;
    private Date birth_date;
    private String curp;
    private String address;
    private boolean active;
    private boolean admin;
    private String email;
    private String phone;
    
    //schedule
    private boolean mon,tue,wed,thu,fri,sat,sun;
    
    
    
    // Getters/setters ----------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }
    
    public void setScheduleFromString(String schedule){
        List<Boolean> boolean_list = new ArrayList();
        for(String item : schedule.split(",")){
            if(item.equals("1")){
                boolean_list.add(true);
            }else{
                boolean_list.add(false);
            }
        }
        
        mon = boolean_list.get(0);
        tue = boolean_list.get(1);
        wed = boolean_list.get(2);
        thu = boolean_list.get(3);
        fri = boolean_list.get(4);
        sat = boolean_list.get(5);
        sun = boolean_list.get(6);
    }
    
    public String getScheduleAsString(){
        boolean[] schedule_list = {mon,tue,wed,thu,fri,sat,sun};
        List<String> string_list = new ArrayList();
        for(boolean item : schedule_list){
            if(item){
                string_list.add("1");
            }else{
                string_list.add("0");
            }
        }
        String string = string_list.get(0);
        for(int i = 1 ; i < string_list.size() ; i++){
            string += ","+string_list.get(i);
        }
        
        return string;
    }
    
    // Object overrides ---------------------------------------------------------------------------

    /**
     * This should compare Employees by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Employee) && (id != null)
            ? id.equals(((Employee) other).id)
            : (other == this);
    }

    /**
     * Employee with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Employee. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s %s",
                first_name, last_name);
    }
    
}
