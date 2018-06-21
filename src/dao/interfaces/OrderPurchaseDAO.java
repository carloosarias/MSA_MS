/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Employee;
import model.OrderPurchase;

/**
 *
 * @author Pavilion Mini
 */
public interface OrderPurchaseDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the OrderPurchase from the database matching the given ID, otherwise null.
     * @param id The ID of the OrderPurchase to be returned.
     * @return The OrderPurchase from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public OrderPurchase find(Integer id) throws DAOException;
    
    /**
     * Returns the Company from the database matching the given OrderPurchase ID, otherwise null.
     * OrderPurchase ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase The OrderPurchase to get the Company from.
     * @return The Company from the database matching the given OrderPurchase ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchase ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findCompany(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the CompanyAddress from the database matching the given OrderPurchase ID, otherwise null.
     * OrderPurchase ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase The OrderPurchase to get the CompanyAddress from.
     * @return The CompanyAddress from the database matching the given OrderPurchase ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchase ID is null.
     * @throws DAOException If something fails at database level.
     */
    public CompanyAddress findAddress(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Employee from the database matching the given OrderPurchase ID, otherwise null.
     * OrderPurchase ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param order_purchase The OrderPurchase to get the Employee from.
     * @return The Employee from the database matching the given OrderPurchase ID, otherwise null.
     * @throws IllegalArgumentException If OrderPurchase ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all OrderPurchase from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase.
     * @return A list of all OrderPurchase from the database ordered by OrderPurchase ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<OrderPurchase> list() throws DAOException;
    
    /**
     * Returns a list of all active OrderPurchase from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase.
     * @param active the active to be searched for
     * @return A list of all active OrderPurchase from the database ordered by OrderPurchase ID.
     * @throws DAOException If something fails at database level.
     */
    public List<OrderPurchase> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all OrderPurchase matching Company ID from the database ordered by OrderPurchase ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any OrderPurchase.
     * @param company The Company ID to be searched for.
     * @return A list of all OrderPurchase matching Company ID from the database ordered by OrderPurchase ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<OrderPurchase> listCompany(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all active OrderPurchase matching Company ID from the database ordered by OrderPurchase ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any active OrderPurchase.
     * @param company The Company ID to be searched for.
     * @param active the active to be searched for
     * @return A list of all OrderPurchase matching Company ID from the database ordered by OrderPurchase ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<OrderPurchase> listCompany(Company company, boolean active) throws IllegalArgumentException, DAOException;    
    
    /**
     * Create the given OrderPurchase in the database.
     * The Employee ID must not be null, The Company ID must not be null,
     * the CompanyAddress must not be null and The Specification ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Specification.
     * @param employee The Employee to be assigned to this OrderPurchase.
     * @param company The Company to be assigned to this OrderPurchase.
     * @param address The CompanyAddress to be assigned to this OrderPurchase.
     * @param order_purchase The Specification to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the CompanyAddress ID is null.
     * @throws IllegalArgumentException If the Specification ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Employee employee, Company company, CompanyAddress address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given OrderPurchase in the database. The OrderPurchase ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param order_purchase The OrderPurchase to be updated.
     * @throws IllegalArgumentException If the OrderPurchase ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given OrderPurchase from the database. After deleting, the DAO will set the ID of the given
     * OrderPurchase to null.
     * @param order_purchase The OrderPurchase to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(OrderPurchase order_purchase) throws DAOException;
}
