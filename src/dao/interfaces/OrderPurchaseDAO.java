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
    public CompanyAddress findCompanyAddress(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all OrderPurchase from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase.
     * @return A list of all OrderPurchase from the database ordered by OrderPurchase ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<OrderPurchase> list() throws DAOException;
    
    /**
     * Returns a list of all OrderPurchase matching Company from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase matching Company.
     * @param company The Company of the OrderPurchase to be returned.
     * @return A list of all OrderPurchase matching Company from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */
    public List<OrderPurchase> listOfCompany(Company company) throws DAOException;
    
    /**
     * Returns a list of all OrderPurchase matching status from the database ordered by OrderPurchase ID. The list is never null and
     * is empty when the database does not contain any OrderPurchase matching status.
     * @param status The status of the OrderPurchase to be returned.
     * @return A list of all OrderPurchase matching status from the database ordered by OrderPurchase ID.
     * @throws DAOException If something fails at database level.
     */
    public List<OrderPurchase> listOfStatus(String status) throws DAOException;
    
    /**
     * Create the given OrderPurchase in the database.
     * The OrderPurchase ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given OrderPurchase.
     * @param company The Company to be assigned to this OrderPurchase.
     * @param company_address The CompanyAddress to be assigned to this OrderPurchase.
     * @param order_purchase The OrderPurchase to be created.
     * @throws IllegalArgumentException If the OrderPurchase ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Company company, CompanyAddress company_address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
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
