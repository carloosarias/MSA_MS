/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.DepartLot;
import model.Invoice;
import model.InvoiceItem;
import model.PartRevision;
import model.Quote;

/**
 *
 * @author Pavilion Mini
 */
public interface InvoiceItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the InvoiceItem from the database matching the given ID, otherwise null.
     * @param id The ID of the InvoiceItem to be returned.
     * @return The InvoiceItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public InvoiceItem find(Integer id) throws DAOException;
    
    /**
     * Returns the Invoice from the database matching the given InvoiceItem ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_item The InvoiceItem to get the Invoice from.
     * @return The Invoice from the database matching the given InvoiceItem ID, otherwise null.
     * @throws IllegalArgumentException If InvoiceItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Invoice findInvoice(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the DepartLot from the database matching the given InvoiceItem ID, otherwise null.
     * InvoiceItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_item The InvoiceItem to get the DepartLot from.
     * @return The DepartLot from the database matching the given InvoiceItem ID, otherwise null.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */
    public DepartLot findDepartLot(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Quote from the database matching the given InvoiceItem ID, otherwise null.
     * InvoiceItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_item The InvoiceItem to get the Quote from.
     * @return The Quote from the database matching the given InvoiceItem ID, otherwise null.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public Quote findQuote(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all InvoiceItem from the database ordered by InvoiceItem ID. The list is never null and
     * is empty when the database does not contain any InvoiceItem.
     * @return A list of all InvoiceItem from the database ordered by InvoiceItem ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<InvoiceItem> list() throws DAOException;
    
    /**
     * Returns a list of all InvoiceItem matching Invoice ID from the database ordered by InvoiceItem ID. 
     * The Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not 
     * contain any InvoiceItem matching Invoice ID.
     * @param invoice The Invoice ID to be searched for.
     * @return A list of all InvoiceItem matching Invoice ID from the database ordered by InvoiceItem ID.
     * @throws IllegalArgumentException If Invoice ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<InvoiceItem> list(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    public List<PartRevision> listPartRevision(Invoice invoice) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given InvoiceItem in the database.
     * The Company ID must not be null, The BillingAddress must not be null,
     * The ShippingAddress must not be null and The InvoiceItem ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given InvoiceItem.
     * @param invoice The Invoice to be assigned to this InvoiceItem.
     * @param depart_lot The DepartLot to be assigned to this InvoiceItem.
     * @param quote The Quote to be assigned to this InvoiceItem.
     * @param invoice_item The InvoiceItem to be created.
     * @throws IllegalArgumentException If the Invoice ID is null.
     * @throws IllegalArgumentException If the DepartLot ID is null.
     * @throws IllegalArgumentException If the InvoiceItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Invoice invoice, DepartLot depart_lot, Quote quote, InvoiceItem invoice_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given InvoiceItem in the database. The InvoiceItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param invoice_item The InvoiceItem to be updated.
     * @throws IllegalArgumentException If the InvoiceItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given InvoiceItem from the database. After deleting, the DAO will set the ID of the given
     * InvoiceItem to null.
     * @param invoice_item The InvoiceItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(InvoiceItem invoice_item) throws DAOException;
}
