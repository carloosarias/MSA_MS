/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.Employee;
import model.Equipment;
import model.MantainanceReport;

/**
 *
 * @author Pavilion Mini
 */
public interface MantainanceReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the MantainanceReport from the database matching the given ID, otherwise null.
     * @param id The ID of the MantainanceReport to be returned.
     * @return The MantainanceReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public MantainanceReport find(Integer id) throws DAOException;
    
    /**
     * Returns the Employee from the database matching the given MantainanceReport ID, otherwise null.
     * MantainanceReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param mantainance_report The MantainanceReport to get the Employee from.
     * @return The Employee from the database matching the given MantainanceReport ID, otherwise null.
     * @throws IllegalArgumentException If MantainanceReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Equipment from the database matching the given MantainanceReport ID, otherwise null.
     * MantainanceReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param mantainance_report The MantainanceReport to get the Equipment from.
     * @return The Equipment from the database matching the given MantainanceReport ID, otherwise null.
     * @throws IllegalArgumentException If MantainanceReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Equipment findEquipment(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all MantainanceReport from the database ordered by MantainanceReport ID. The list is never null and
     * is empty when the database does not contain any MantainanceReport.
     * @return A list of all MantainanceReport from the database ordered by MantainanceReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<MantainanceReport> list() throws DAOException;
    
    /**
     * Returns a list of all MantainanceReport matching Employee ID from the database ordered by MantainanceReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any MantainanceReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @return A list of all MantainanceReport matching Employee ID from the database ordered by MantainanceReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<MantainanceReport> listEmployee(Employee employee) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProcessReport matching a range of dates from the database ordered by ProcessReport ID.
     * The list is never null and is empty when the database does not contain any ProcessReport matching the range of dates.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all ProcessReport matching Employee ID from the database ordered by ProcessReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<MantainanceReport> listDateRange(Date start, Date end) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all MantainanceReport matching Employee ID from the database ordered by MantainanceReport ID. 
     * The Employee ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any MantainanceReport matching Employee ID.
     * @param employee The Employee ID to be searched for.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @return A list of all MantainanceReport matching Employee ID from the database ordered by MantainanceReport ID.
     * @throws IllegalArgumentException If Employee ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<MantainanceReport> listEmployeeDateRange(Employee employee, Date start, Date end) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all MantainanceReport matching Equipment ID from the database ordered by MantainanceReport ID. 
     * The Equipment ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any MantainanceReport matching Equipment ID.
     * @param equipment The Equipment ID to be searched for.
     * @return A list of all MantainanceReport matching Equipment ID from the database ordered by MantainanceReport ID.
     * @throws IllegalArgumentException If Equipment ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<MantainanceReport> listEquipment(Equipment equipment) throws IllegalArgumentException, DAOException;
    /**
     * Create the given MantainanceReport in the database.
     * The Employee ID must not be null, The Equipment ID must not be null, and
     * The MantainanceReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given MantainanceReport.
     * @param employee The Employee to be assigned to this MantainanceReport.
     * @param equipment The Equipment to be assigned to this MantainanceReport.
     * @param mantainance_report The MantainanceReport to be created.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws IllegalArgumentException If the Equipment ID is null.
     * @throws IllegalArgumentException If the MantainanceReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Employee employee, Equipment equipment, MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given MantainanceReport in the database. The MantainanceReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param mantainance_report The MantainanceReport to be updated.
     * @throws IllegalArgumentException If the MantainanceReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given MantainanceReport from the database. After deleting, the DAO will set the ID of the given
     * MantainanceReport to null.
     * @param mantainanance_report The MantainanceReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(MantainanceReport mantainanance_report) throws DAOException;
}
