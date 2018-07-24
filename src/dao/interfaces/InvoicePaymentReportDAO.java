/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.InvoicePaymentReport;

/**
 *
 * @author Pavilion Mini
 */
public interface InvoicePaymentReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the InvoicePaymentReport from the database matching the given ID, otherwise null.
     * @param id The ID of the InvoicePaymentReport to be returned.
     * @return The InvoicePaymentReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public InvoicePaymentReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Company from the database matching the given InvoicePaymentReport ID, otherwise null.
     * InvoicePaymentReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param invoice_payment_report The InvoicePaymentReport to get the Company from.
     * @return The Company from the database matching the given InvoicePaymentReport ID, otherwise null.
     * @throws IllegalArgumentException If InvoicePaymentReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findCompany(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all InvoicePaymentReport from the database ordered by InvoicePaymentReport ID. The list is never null and
     * is empty when the database does not contain any InvoicePaymentReport.
     * @return A list of all InvoicePaymentReport from the database ordered by InvoicePaymentReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<InvoicePaymentReport> list() throws DAOException;
    
    /**
     * Returns a list of all InvoicePaymentReport matching Company ID from the database ordered by InvoicePaymentReport ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any InvoicePaymentReport matching Company.
     * @param company The Company ID to be searched for.
     * @return A list of all InvoicePaymentReport matching Company ID from the database ordered by InvoicePaymentReport ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<InvoicePaymentReport> list(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given InvoicePaymentReport in the database.
     * The Company ID must not be null and the InvoicePaymentReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given InvoicePaymentReport.
     * @param company The Company to be assigned to this IncomingReport.
     * @param invoice_payment_report The InvoicePaymentReport to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the InvoicePaymentReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Company company, InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given InvoicePaymentReport in the database. The InvoicePaymentReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param invoice_payment_report The InvoicePaymentReport to be updated.
     * @throws IllegalArgumentException If the InvoicePaymentReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given InvoicePaymentReport from the database. After deleting, the DAO will set the ID of the given
     * InvoicePaymentReport to null.
     * @param invoice_payment_report The InvoicePaymentReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(InvoicePaymentReport invoice_payment_report) throws DAOException;
}
