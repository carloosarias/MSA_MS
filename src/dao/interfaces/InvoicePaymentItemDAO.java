/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.Invoice;
import model.InvoicePaymentItem;
import model.InvoicePaymentReport;

/**
 *
 * @author Pavilion Mini
 */
public interface InvoicePaymentItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the InvoicePaymentItem from the database matching the given ID, otherwise null.
     * @param id The ID of the InvoicePaymentItem to be returned.
     * @return The InvoicePaymentItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public InvoicePaymentItem find(Integer id) throws DAOException;
    
    /**
     * Returns the InvoicePaymentReport from the database matching the given InvoicePaymentItem ID, otherwise null.
     * InvoicePaymentReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_payment_item The InvoicePaymentItem to get the InvoicePaymentReport from.
     * @return The InvoicePaymentReport from the database matching the given InvoicePaymentItem ID, otherwise null.
     * @throws IllegalArgumentException If InvoicePaymentReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findInvoicePaymentReport(InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Invoice from the database matching the given InvoicePaymentItem ID, otherwise null.
     * InvoicePaymentReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_payment_item The InvoicePaymentItem to get the Invoice from.
     * @return The Invoice from the database matching the given InvoicePaymentReport ID, otherwise null.
     * @throws IllegalArgumentException If InvoicePaymentReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findInvoice(InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all InvoicePaymentItem from the database ordered by InvoicePaymentItem ID. The list is never null and
     * is empty when the database does not contain any InvoicePaymentItem.
     * @return A list of all InvoicePaymentItem from the database ordered by InvoicePaymentItem ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<InvoicePaymentItem> list() throws DAOException;
    
    /**
     * Returns a list of all InvoicePaymentItem matching InvoicePaymentReport ID from the database ordered by InvoicePaymentItem ID. 
     * The InvoicePaymentReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any InvoicePaymentItem matching InvoicePaymentReport.
     * @param invoice_payment_report The InvoicePaymentReport ID to be searched for.
     * @return A list of all InvoicePaymentItem matching InvoicePaymentReport ID from the database ordered by InvoicePaymentItem ID.
     * @throws IllegalArgumentException If InvoicePaymentReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<InvoicePaymentItem> list(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given InvoicePaymentItem in the database.
     * The Invoice ID must not be null, the InvoicePaymentReport ID must not be null 
     * and the InvoicePaymentItem ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given InvoicePaymentItem.
     * @param invoice The Invoice to be assigned to this InvoicePaymentItem.
     * @param invoice_payment_report The Company to be assigned to this InvoicePaymentItem.
     * @param invoice_payment_item The InvoicePaymentItem to be created.
     * @throws IllegalArgumentException If the Invoice ID is null.
     * @throws IllegalArgumentException If the InvoicePaymentReport ID is null.
     * @throws IllegalArgumentException If the InvoicePaymentItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Invoice invoice, InvoicePaymentReport invoice_payment_report, InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given InvoicePaymentItem in the database. The InvoicePaymentItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param invoice_payment_item The InvoicePaymentItem to be updated.
     * @throws IllegalArgumentException If the InvoicePaymentItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given InvoicePaymentItem from the database. After deleting, the DAO will set the ID of the given
     * InvoicePaymentItem to null.
     * @param invoice_payment_item The InvoicePaymentItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(InvoicePaymentItem invoice_payment_item) throws DAOException;
}
