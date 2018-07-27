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
import model.Container;
import model.DepartReport;
import model.Employee;
import model.PartRevision;
import model.ProcessReport;

/**
 *
 * @author Pavilion Mini
 */
public interface ProcessReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ProcessReport from the database matching the given ID, otherwise null.
     * @param id The ID of the ProcessReport to be returned.
     * @return The ProcessReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ProcessReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Employee from the database matching the given ProcessReport ID, otherwise null.
     * ProcessReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param process_report The ProcessReport to get the Employee from.
     * @return The Employee from the database matching the given ProcessReport ID, otherwise null.
     * @throws IllegalArgumentException If ProcessReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given ProcessReport ID, otherwise null.
     * ProcessReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param process_report The ProcessReport to get the PartRevision from.
     * @return The PartRevision from the database matching the given ProcessReport ID, otherwise null.
     * @throws IllegalArgumentException If ProcessReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public PartRevision findPartRevision(ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Container from the database matching the given ProcessReport ID, otherwise null.
     * ProcessReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param process_report The ProcessReport to get the Container from.
     * @return The Container from the database matching the given ProcessReport ID, otherwise null.
     * @throws IllegalArgumentException If ProcessReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Container findTank(ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Container from the database matching the given ProcessReport ID, otherwise null.
     * ProcessReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param process_report The ProcessReport to get the Container from.
     * @return The Container from the database matching the given ProcessReport ID, otherwise null.
     * @throws IllegalArgumentException If ProcessReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Container findContainer(ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProcessReport from the database ordered by ProcessReport ID. The list is never null and
     * is empty when the database does not contain any ProcessReport.
     * @return A list of all ProcessReport from the database ordered by ProcessReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<ProcessReport> list() throws DAOException;
    
    /**
     * Returns a list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any ProcessReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @return A list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ProcessReport> listEmployee(Employee employee) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProcessReport matching a range of dates from the database ordered by ProcessReport ID.
     * The list is never null and is empty when the database does not contain any ProcessReport matching the range of dates.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ProcessReport> listDateRange(Date start, Date end) throws IllegalArgumentException, DAOException;
    /**
     * Returns a list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any ProcessReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ProcessReport> listEmployeeDateRange(Employee employee, Date start, Date end) throws IllegalArgumentException, DAOException;
    /**
     * Create the given ProcessReport in the database.
     * The Employee ID must not be null, The PartRevision ID must not be null,
     * The Container ID must not be null, The Container ID must not be null, and
     * The ProcessReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ProcessReport.
     * @param employee The Employee to be assigned to this ProcessReport.
     * @param part_revision The PartRevision to be assigned to this ProcessReport.
     * @param tank The Container to be assigned to this ProcessReport.
     * @param container The Container to be assigned to this ProcessReport.
     * @param process_report The ProcessReport to be created.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the Container ID is null.
     * @throws IllegalArgumentException If the Container ID is null.
     * @throws IllegalArgumentException If the ProcessReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Employee employee, PartRevision part_revision, Container tank, Container container, ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ProcessReport in the database. The ProcessReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param process_report The ProcessReport to be updated.
     * @throws IllegalArgumentException If the ProcessReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(ProcessReport process_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ProcessReport from the database. After deleting, the DAO will set the ID of the given
     * ProcessReport to null.
     * @param process_report The ProcessReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ProcessReport process_report) throws DAOException;
}
