/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.OrderPurchase;
import model.ProductSupplier;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public interface PurchaseItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the PurchaseItem from the database matching the given ID, otherwise null.
     * @param id The ID of the PurchaseItem to be returned.
     * @return The PurchaseItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public PurchaseItem find(Integer id) throws DAOException;
    
    /**
     * Returns the OrderPurchase from the database matching the given PurchaseItem ID, otherwise null.
     * PurchaseItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param purchase_item The PurchaseItem to get the OrderPurchase from.
     * @return The OrderPurchase from the database matching the given PurchaseItem ID, otherwise null.
     * @throws IllegalArgumentException If PurchaseItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchase findOrderPurchase(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the ProductSupplier from the database matching the given PurchaseItem ID, otherwise null.
     * PurchaseItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param purchase_item The PurchaseItem to get the ProductSupplier from.
     * @return The ProductSupplier from the database matching the given PurchaseItem ID, otherwise null.
     * @throws IllegalArgumentException If PurchaseItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public ProductSupplier findProductSupplier(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all PurchaseItem matching OrderPurchase from the database ordered by PurchaseItem ID. The list is never null and
     * is empty when the database does not contain any PurchaseItem matching OrderPurchase.
     * @param order_purchase The OrderPurchase of the PurchaseItem to be returned.
     * @return A list of all PurchaseItem matching OrderPurchase from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */      
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws DAOException;
    
    /**
     * Create the given PurchaseItem in the database.
     * The PurchaseItem ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given PurchaseItem.
     * @param order_purchase The Company to be assigned to this PurchaseItem.
     * @param product_supplier The CompanyAddress to be assigned to this PurchaseItem.
     * @param purchase_item The PurchaseItem to be created.
     * @throws IllegalArgumentException If the PurchaseItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(OrderPurchase order_purchase, ProductSupplier product_supplier, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given PurchaseItem in the database. The PurchaseItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param purchase_item The PurchaseItem to be updated.
     * @throws IllegalArgumentException If the PurchaseItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given PurchaseItem from the database. After deleting, the DAO will set the ID of the given
     * PurchaseItem to null.
     * @param purchase_item The PurchaseItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(PurchaseItem purchase_item) throws DAOException;
}
