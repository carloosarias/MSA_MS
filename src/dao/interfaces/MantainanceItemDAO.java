/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.EquipmentTypeCheck;
import model.MantainanceItem;
import model.MantainanceReport;

/**
 *
 * @author Pavilion Mini
 */
public interface MantainanceItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the MantainanceItem from the database matching the given ID, otherwise null.
     * @param id The ID of the MantainanceReport to be returned.
     * @return The MantainanceItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public MantainanceItem find(Integer id) throws DAOException;
    
    /**
     * Returns the MantainanceReport from the database matching the given MantainanceItem ID, otherwise null.
     * MantainanceItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param mantainance_item The MantainanceItem to get the MantainanceReport from.
     * @return The MantainanceReport from the database matching the given MantainanceItem ID, otherwise null.
     * @throws IllegalArgumentException If MantainanceItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public MantainanceReport findMantainanceReport(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the EquipmentTypeCheck from the database matching the given MantainanceItem ID, otherwise null.
     * MantainanceItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param mantainance_item The MantainanceItem to get the EquipmentTypeCheck from.
     * @return The EquipmentTypeCheck from the database matching the given MantainanceItem ID, otherwise null.
     * @throws IllegalArgumentException If MantainanceItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public EquipmentTypeCheck findEquipmentTypeCheck(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all MantainanceItem matching MantainanceReport ID from the database ordered by MantainanceItem ID. 
     * The MantainanceReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any MantainanceItem matching MantainanceReport ID.
     * @param mantainance_report The MantainanceReport ID to be searched for.
     * @return A list of all MantainanceItem matching MantainanceReport ID from the database ordered by MantainanceItem ID.
     * @throws IllegalArgumentException If MantainanceReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<MantainanceItem> list(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given MantainanceItem in the database.
     * The MantainanceReport ID must not be null, The EquipmentTypeCheck ID must not be null, and
     * The MantainanceItem ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given MantainanceItem.
     * @param mantainance_report The MantainanceReport to be assigned to this MantainanceItem.
     * @param equipment_type_check The EquipmentTypeCheck to be assigned to this MantainanceItem.
     * @param mantainance_item The MantainanceItem to be created.
     * @throws IllegalArgumentException If the MantainanceReport ID is null.
     * @throws IllegalArgumentException If the EquipmentTypeCheck ID is null.
     * @throws IllegalArgumentException If the MantainanceItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(MantainanceReport mantainance_report, EquipmentTypeCheck equipment_type_check, MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given MantainanceItem in the database. The MantainanceItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param mantainance_item The MantainanceItem to be updated.
     * @throws IllegalArgumentException If the MantainanceItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given MantainanceItem from the database. After deleting, the DAO will set the ID of the given
     * MantainanceItem to null.
     * @param mantainance_item The MantainanceItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(MantainanceItem mantainance_item) throws DAOException;
}
