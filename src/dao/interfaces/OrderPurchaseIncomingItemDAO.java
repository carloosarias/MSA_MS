/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.OrderPurchaseIncomingItem;
import model.OrderPurchaseIncomingReport;
import model.PurchaseItem;


/**
 *
 * @author Pavilion Mini
 */
public interface OrderPurchaseIncomingItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the PurchaseItem from the database matching the given ID, otherwise null.
     * @param id The ID of the PurchaseItem to be returned.
     * @return The PurchaseItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchaseIncomingItem find(Integer id) throws DAOException;
    
    /**
     * Returns the OrderPurchaseIncomingReport from the database matching the given OrderPurchaseIncomingItem ID, otherwise null.
     * OrderPurchaseIncomingItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_item The OrderPurchaseIncomingItem to get the OrderPurchaseIncomingReport from.
     * @return The OrderPurchaseIncomingReport from the database matching the given OrderPurchaseIncomingItem ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchaseIncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchaseIncomingReport findOrderPurchaseIncomingReport(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PurchaseItem from the database matching the given OrderPurchaseIncomingItem ID, otherwise null.
     * OrderPurchaseIncomingItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_item The OrderPurchaseIncomingItem to get the PurchaseItem from.
     * @return The PurchaseItem from the database matching the given OrderPurchaseIncomingItem ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchaseIncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public PurchaseItem findPurchaseItem(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all OrderPurchaseIncomingItem matching OrderPurchaseIncomingReport from the database ordered by OrderPurchaseIncomingItem ID. The list is never null and
     * is empty when the database does not contain any OrderPurchaseIncomingItem matching OrderPurchaseIncomingReport.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport of the OrderPurchaseIncomingItem to be returned.
     * @return A list of all OrderPurchaseIncomingItem matching OrderPurchaseIncomingReport from the database ordered by OrderPurchaseIncomingItem ID.
     * @throws DAOException If something fails at database level.
     */      
    public List<OrderPurchaseIncomingItem> list(OrderPurchaseIncomingReport order_purchase_incoming_report) throws DAOException;
    
    /**
     * Create the given OrderPurchaseIncomingItem in the database.
     * The OrderPurchaseIncomingItem ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given OrderPurchaseIncomingItem.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to be assigned to this OrderPurchaseIncomingItem.
     * @param purchase_item The PurchaseItem to be assigned to this OrderPurchaseIncomingItem.
     * @param order_purchase_incoming_item The OrderPurchaseIncomingItem to be created.
     * @throws IllegalArgumentException If the OrderPurchaseIncomingItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(OrderPurchaseIncomingReport order_purchase_incoming_report, PurchaseItem purchase_item, OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given OrderPurchaseIncomingItem in the database. The OrderPurchaseIncomingItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_item The OrderPurchaseIncomingItem to be updated.
     * @throws IllegalArgumentException If the OrderPurchaseIncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given OrderPurchaseIncomingItem from the database. After deleting, the DAO will set the ID of the given
     * OrderPurchaseIncomingItem to null.
     * @param order_purchase_incoming_item The OrderPurchaseIncomingItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(OrderPurchaseIncomingItem order_purchase_incoming_item) throws DAOException;
}
