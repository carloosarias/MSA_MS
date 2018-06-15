/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.OrderPurchase;
import model.Product;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public interface PurchaseItemDAO {
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
     * @param purchase_item The PurchaseItem ID to get the OrderPurchase from.
     * @return The OrderPurchase from the database matching the given PurchaseItem ID, otherwise null.
     * @throws IllegalArgumentException If PurchaseItem ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public OrderPurchase findOrderPurchase(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Product from the database matching the given PurchaseItem ID, otherwise null.
     * PurchaseItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param purchase_item The PurchaseItem ID to get the Product from.
     * @return The Product from the database matching the given PurchaseItem ID, otherwise null.
     * @throws IllegalArgumentException If PurchaseItem ID is null.
     * @throws DAOException If something fails at database level.
     */          
    public Product findProduct(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all PurchaseItem from a given OrderPurchase from the database ordered by PurchaseItem ID. The OrderPurchase ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any PurchaseItem matching OrderPurchase.
     * @param order_purchase The OrderPurchase to be searched for.
     * @return A list of all PurchaseItem matching OrderPurchase from the database ordered by PurchaseItem ID.
     * @throws IllegalArgumentException If the OrderPurchase ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given PurchaseItem for a given OrderPurchase in the database. The OrderPurchase ID must not be null,
     * the Product ID must not be null and the PurchaseItem ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param order_purchase The OrderPurchase to be assigned to the PurchaseItem.
     * @param product The Product to be assigned to the PurchaseItem.
     * @param purchase_item The PurchaseItem to be created.
     * @throws IllegalArgumentException If the OrderPurchase ID is null.
     * @throws IllegalArgumentException If the Product ID is null.
     * @throws IllegalArgumentException If the PurchaseItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(OrderPurchase order_purchase, Product product, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given PurchaseItem in the database. The PurchaseItem ID must not be null,
     * otherwise it will throw IllegalArgumentException.
     * @param purchase_item The PurchaseItem to be updated in the database.
     * @throws IllegalArgumentException If the CompanyAddress ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given PurchaseItem from the database. After deleting,
     * the DAO will set the ID of the given PurchaseItem to null.
     * @param purchase_item The PurchaseItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(PurchaseItem purchase_item) throws DAOException;
}
