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
import model.Invoice;

/**
 *
 * @author Pavilion Mini
 */
public interface InvoiceDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Invoice from the database matching the given ID, otherwise null.
     * @param id The ID of the Invoice to be returned.
     * @return The Invoice from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Invoice find(Integer id) throws DAOException;
    
    /**
     * Returns the Company from the database matching the given Invoice ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice The Invoice to get the Company from.
     * @return The Company from the database matching the given Invoice ID, otherwise null.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findCompany(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the BillingAddress from the database matching the given Invoice ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice The Invoice to get the BillingAddress from.
     * @return The BillingAddress from the database matching the given Invoice ID, otherwise null.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */
    public CompanyAddress findBillingAddress(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the ShippingAddress from the database matching the given Invoice ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice The Invoice to get the ShippingAddress from.
     * @return The ShippingAddress from the database matching the given Invoice ID, otherwise null.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */
    public CompanyAddress findShippingAddress(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all Invoice from the database ordered by Invoice ID. The list is never null and
     * is empty when the database does not contain any Invoice.
     * @return A list of all Invoice from the database ordered by Invoice ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Invoice> list() throws DAOException;
    
    /**
     * Returns a list of all Invoice matching Company ID from the database ordered by Invoice ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any Invoice.
     * @param company The Company ID to be searched for.
     * @return A list of all Invoice matching Company ID from the database ordered by Invoice ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<Invoice> listCompany(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given Invoice in the database.
     * The Company ID must not be null, The BillingAddress must not be null,
     * The ShippingAddress must not be null and The Invoice ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Invoice.
     * @param company The Company to be assigned to this Invoice.
     * @param billing_address The BillingAddress to be assigned to this Invoice.
     * @param shipping_address The ShippingAddress to be assigned to this Invoice.
     * @param invoice The Invoice to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the Invoice ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Company company, CompanyAddress billing_address, CompanyAddress shipping_address, Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Invoice in the database. The Invoice ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param invoice The Invoice to be updated.
     * @throws IllegalArgumentException If the Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Invoice from the database. After deleting, the DAO will set the ID of the given
     * Invoice to null.
     * @param invoice The Invoice to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Invoice invoice) throws DAOException;
}
