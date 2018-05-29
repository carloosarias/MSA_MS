/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pavilion Mini
 */
public class Spec {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private String spec_code;
    private String name;
    private String desc;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpec_code() {
        return spec_code;
    }

    public void setSpec_code(String spec_code) {
        this.spec_code = spec_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare Specs by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Spec) && (id != null)
            ? id.equals(((Spec) other).id)
            : (other == this);
    }

    /**
     * Spec with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this Spec. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Spec[id=%d,spec_code=%s,name=%s,desc=%s]",
                id, spec_code, name, desc);
    }    
}
