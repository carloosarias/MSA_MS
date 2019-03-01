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
import model.EquipmentType;
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
    public List<MantainanceReport> list(boolean active) throws DAOException;
    
    /**
     * 
     * @param equipment_type
     * @param equipment
     * @param type_filter
     * @param equipment_filter
     * @param active
     * @return
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public List<MantainanceReport> list(EquipmentType equipment_type, Equipment equipment, boolean type_filter, boolean equipment_filter, boolean active) throws IllegalArgumentException, DAOException;
    
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
