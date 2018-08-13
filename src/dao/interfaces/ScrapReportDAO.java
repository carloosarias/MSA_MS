/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.Container;
import model.Employee;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
import model.ScrapReport;

/**
 *
 * @author Pavilion Mini
 */
public interface ScrapReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ScrapReport from the database matching the given ID, otherwise null.
     * @param id The ID of the ScrapReport to be returned.
     * @return The ScrapReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ScrapReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Employee from the database matching the given ScrapReport ID, otherwise null.
     * ScrapReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param scrap_report The ScrapReport to get the Employee from.
     * @return The Employee from the database matching the given ScrapReport ID, otherwise null.
     * @throws IllegalArgumentException If ScrapReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(ScrapReport scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given ScrapReport ID, otherwise null.
     * ScrapReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param scrap_report The ScrapReport to get the PartRevision from.
     * @return The PartRevision from the database matching the given ScrapReport ID, otherwise null.
     * @throws IllegalArgumentException If ScrapReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public PartRevision findPartRevision(ScrapReport scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ScrapReport from the database ordered by ScrapReport ID. The list is never null and
     * is empty when the database does not contain any ScrapReport.
     * @return A list of all ScrapReport from the database ordered by ScrapReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<ScrapReport> list() throws DAOException;
    
    /**
     * Returns a list of all ScrapReport from the database matching ProductPart ordered by ScrapReport ID.
     * The list is never null and is empty when the database does not contain any ScrapReport matching ProductPart.
     * @param product_part the ProductPart to be searched for.
     * @return A list of all ScrapReport matching ProductPart from the database ordered by ScrapReport ID.
     * @throws IllegalArgumentException If ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ScrapReport> listProductPart(ProductPart product_part) throws IllegalArgumentException, DAOException;
    
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
    
    public List<ProcessReport> listDateRange(ProductPart product_part, Date start, Date end);

}
