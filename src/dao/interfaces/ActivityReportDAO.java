/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.ActivityReport;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public interface ActivityReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ActivityReport from the database matching the given ID, otherwise null.
     * @param id The ID of the ActivityReport to be returned.
     * @return The ActivityReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ActivityReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Employee from the database matching the given ActivityReport ID, otherwise null.
     * ActivityReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param activity_report The ActivityReport to get the Employee from.
     * @return The Employee from the database matching the given ActivityReport ID, otherwise null.
     * @throws IllegalArgumentException If ActivityReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(ActivityReport activity_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ActivityReport from the database ordered by ActivityReport ID. The list is never null and
     * is empty when the database does not contain any ActivityReport.
     * @return A list of all ActivityReport from the database ordered by ActivityReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<ActivityReport> list() throws DAOException;
    
    /**
     * Returns a list of all ActivityReport matching Employee ID from the database ordered by ActivityReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any ActivityReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @return A list of all ActivityReport matching Employee ID from the database ordered by ActivityReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ActivityReport> listEmployee(Employee employee) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ActivityReport matching a range of dates from the database ordered by ActivityReport ID.
     * The list is never null and is empty when the database does not contain any ActivityReport matching the range of dates.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all ActivityReport matching Employee ID from the database ordered by ActivityReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ActivityReport> listDateRange(Date start, Date end) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ActivityReport matching Employee ID from the database ordered by ActivityReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any ActivityReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all ActivityReport matching Employee ID from the database ordered by ActivityReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<ActivityReport> listEmployeeDateRange(Employee employee, Date start, Date end) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given ActivityReport in the database.
     * The Employee ID must not be null and the ActivityReport ID must be null, 
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ActivityReport.
     * @param employee The Employee to be assigned to this ActivityReport.
     * @param activity_report The ActivityReport to be created.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws IllegalArgumentException If the ActivityReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Employee employee, ActivityReport activity_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ActivityReport in the database. The ActivityReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param activity_report The ActivityReport to be updated.
     * @throws IllegalArgumentException If the ActivityReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(ActivityReport activity_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ActivityReport from the database. After deleting, the DAO will set the ID of the given
     * ActivityReport to null.
     * @param activity_report The ActivityReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ActivityReport activity_report) throws DAOException;
}
