/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Employee;
import model.OrderPurchase;
import model.OrderPurchaseIncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public interface OrderPurchaseIncomingReportDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the OrderPurchaseIncomingReport from the database matching the given ID, otherwise null.
     * @param id The ID of the OrderPurchaseIncomingReport to be returned.
     * @return The OrderPurchaseIncomingReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchaseIncomingReport find(Integer id) throws DAOException;
    
    /**
     * Returns the OrderPurchase from the database matching the given OrderPurchaseIncomingReport ID, otherwise null.
     * OrderPurchaseIncomingReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to get the OrderPurchase from.
     * @return The OrderPurchase from the database matching the given OrderPurchaseIncomingReport ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchaseIncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchase findOrderPurchase(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Employee from the database matching the given OrderPurchaseIncomingReport ID, otherwise null.
     * OrderPurchaseIncomingReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to get the Employee from.
     * @return The Employee from the database matching the given OrderPurchaseIncomingReport ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchaseIncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all OrderPurchase from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase.
     * @return A list of all OrderPurchase from the database ordered by OrderPurchase ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<OrderPurchaseIncomingReport> list() throws DAOException;
    
    /**
     * Returns a list of all OrderPurchaseIncomingReport matching OrderPurchase from the database ordered by OrderPurchaseIncomingReport ID. The list is never null and
     * is empty when the database does not contain any OrderPurchaseIncomingReport matching OrderPurchase.
     * @param order_purchase The OrderPurchase of the OrderPurchaseIncomingReport to be returned.
     * @return A list of all OrderPurchaseIncomingReport matching OrderPurchase from the database ordered by OrderPurchaseIncomingReport ID.
     * @throws DAOException If something fails at database level.
     */
    public List<OrderPurchaseIncomingReport> listOfOrderPurchase(OrderPurchase order_purchase) throws DAOException;
    
    /**
     * Returns a list of all OrderPurchaseIncomingReport matching Employee from the database ordered by OrderPurchaseIncomingReport ID. The list is never null and
     * is empty when the database does not contain any OrderPurchaseIncomingReport matching Employee.
     * @param employee The Employee of the OrderPurchaseIncomingReport to be returned.
     * @return A list of all OrderPurchaseIncomingReport matching Employee from the database ordered by OrderPurchaseIncomingReport ID.
     * @throws DAOException If something fails at database level.
     */
    public List<OrderPurchaseIncomingReport> listOfEmployee(Employee employee) throws DAOException;
    
    /**
     * Create the given OrderPurchaseIncomingReport in the database.
     * The OrderPurchase ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given OrderPurchaseIncomingReport.
     * @param order_purchase The OrderPurchase to be assigned to this OrderPurchaseIncomingReport.
     * @param employee The Employee to be assigned to this OrderPurchaseIncomingReport.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to be created.
     * @throws IllegalArgumentException If the OrderPurchaseIncomingReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(OrderPurchase order_purchase, Employee employee, OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given OrderPurchaseIncomingReport in the database. The OrderPurchaseIncomingReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to be updated.
     * @throws IllegalArgumentException If the OrderPurchaseIncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given OrderPurchaseIncomingReport from the database. After deleting, the DAO will set the ID of the given
     * OrderPurchaseIncomingReport to null.
     * @param order_purchase_incoming_report The OrderPurchaseIncomingReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(OrderPurchaseIncomingReport order_purchase_incoming_report) throws DAOException;
    
}
