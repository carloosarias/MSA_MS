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
public class OrderPurchaseIncomingItem {
    // Properties ---------------------------------------------------------------------------------
    private Integer id;
    private Integer units_arrived;
    
    private PurchaseItem temp_purchaseitem;

    //INNER JOINS
    private String productsupplier_serialnumber;
    private String product_description;
    private double productsupplier_quantity;
    private String product_unitmeasure;
    private Integer purchaseitem_unitsordered;
    
    // Getters/setters ----------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnits_arrived() {
        return units_arrived;
    }

    public void setUnits_arrived(Integer units_arrived) {
        this.units_arrived = units_arrived;
    }
    
    public PurchaseItem getTemp_purchaseitem() {
        return temp_purchaseitem;
    }

    public void setTemp_purchaseitem(PurchaseItem temp_purchaseitem) {
        this.temp_purchaseitem = temp_purchaseitem;
    }
    
    //INNER JOINS
    public String getProductsupplier_serialnumber() {
        return productsupplier_serialnumber;
    }

    public void setProductsupplier_serialnumber(String productsupplier_serialnumber) {
        this.productsupplier_serialnumber = productsupplier_serialnumber;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getProductsupplier_quantity() {
        return productsupplier_quantity;
    }

    public void setProductsupplier_quantity(double productsupplier_quantity) {
        this.productsupplier_quantity = productsupplier_quantity;
    }

    public String getProduct_unitmeasure() {
        return product_unitmeasure;
    }

    public void setProduct_unitmeasure(String product_unitmeasure) {
        this.product_unitmeasure = product_unitmeasure;
    }

    public Integer getPurchaseitem_unitsordered() {
        return purchaseitem_unitsordered;
    }

    public void setPurchaseitem_unitsordered(Integer purchaseitem_unitsordered) {
        this.purchaseitem_unitsordered = purchaseitem_unitsordered;
    }
    
    // Object overrides ---------------------------------------------------------------------------
    
    /**
     * This should compare OrderPurchaseIncomingItem by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof OrderPurchaseIncomingItem) && (id != null)
            ? id.equals(((OrderPurchaseIncomingItem) other).id)
            : (other == this);
    }

    /**
     * OrderPurchaseIncomingItem with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this OrderPurchaseIncomingItem. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%f", id);
    }

}
