/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Employee;
import model.DepartReport;

/**
 *
 * @author Pavilion Mini
 */
public interface DepartReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the DepartReport from the database matching the given ID, otherwise null.
     * @param id The ID of the DepartReport to be returned.
     * @return The DepartReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public DepartReport find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all DepartReport from the database ordered by DepartReport ID. The list is never null and
     * is empty when the database does not contain any DepartReport.
     * @return A list of all DepartReport from the database ordered by DepartReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<DepartReport> list() throws DAOException;
    
    /**
     * Returns a list of all DepartReport matching Company ID from the database ordered by DepartReport ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any DepartReport.
     * @param company The Company ID to be searched for.
     * @return A list of all DepartReport matching Company ID from the database ordered by DepartReport ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<DepartReport> list(Company company, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given DepartReport in the database.
     * The Employee ID must not be null, The Company ID must not be null, and
     * The DepartReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given DepartReport.
     * @param depart_report The DepartReport to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the DepartReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(DepartReport depart_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartReport in the database. The DepartReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param depart_report The DepartReport to be updated.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(DepartReport depart_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartReport from the database. After deleting, the DAO will set the ID of the given
     * DepartReport to null.
     * @param depart_report The DepartReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(DepartReport depart_report) throws DAOException;

}
