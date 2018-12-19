/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.Employee;
import model.IncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public interface IncomingReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the IncomingReport from the database matching the given ID, otherwise null.
     * @param id The ID of the IncomingReport to be returned.
     * @return The IncomingReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public IncomingReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Company from the database matching the given IncomingReport ID, otherwise null.
     * IncomingReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_report The IncomingReport to get the Company from.
     * @return The Company from the database matching the given IncomingReport ID, otherwise null.
     * @throws IllegalArgumentException If IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Company findCompany(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Employee from the database matching the given IncomingReport ID, otherwise null.
     * IncomingReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_report The IncomingReport to get the Employee from.
     * @return The Employee from the database matching the given IncomingReport ID, otherwise null.
     * @throws IllegalArgumentException If IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all IncomingReport from the database ordered by IncomingReport ID. The list is never null and
     * is empty when the database does not contain any IncomingReport.
     * @return A list of all IncomingReport from the database ordered by IncomingReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<IncomingReport> list() throws DAOException;
    
    /**
     * Returns a list of all IncomingReport matching discrepancy from the database ordered by IncomingReport ID. The list is never null and
     * is empty when the database does not contain any IncomingReport matching discrepancy.
     * @param discrepancy The discrepancy to be searched for.
     * @return A list of all IncomingReport matching discrepancy from the database ordered by IncomingReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<IncomingReport> list(boolean discrepancy) throws DAOException;
    
    /**
     * Returns a list of all IncomingReport matching Company ID from the database ordered by IncomingReport ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any IncomingReport.
     * @param company The Company ID to be searched for.
     * @return A list of all IncomingReport matching Company ID from the database ordered by IncomingReport ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<IncomingReport> listCompany(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given IncomingReport in the database.
     * The Employee ID must not be null, The Company ID must not be null, and
     * The IncomingReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given IncomingReport.
     * @param employee The Employee to be assigned to this IncomingReport.
     * @param company The Company to be assigned to this IncomingReport.
     * @param incoming_report The IncomingReport to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the IncomingReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Employee employee, Company company, IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given IncomingReport in the database. The IncomingReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param incoming_report The IncomingReport to be updated.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given IncomingReport from the database. After deleting, the DAO will set the ID of the given
     * IncomingReport to null.
     * @param incoming_report The IncomingReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(IncomingReport incoming_report) throws DAOException;
}
