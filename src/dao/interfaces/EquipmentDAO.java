/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.Equipment;
import model.EquipmentType;

/**
 *
 * @author Pavilion Mini
 */
public interface EquipmentDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Equipment from the database matching the given ID, otherwise null.
     * @param id The ID of the Equipment to be returned.
     * @return The Equipment from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Equipment find(Integer id) throws DAOException;
    
    /**
     * Returns the EquipmentType from the database matching the given Equipment ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param equipment The Equipment to get the EquipmentType from.
     * @return The EquipmentType from the database matching the given Equipment ID, otherwise null.
     * @throws IllegalArgumentException If Equipment ID is null.
     * @throws DAOException If something fails at database level.
     */
    public EquipmentType findEquipmentType(Equipment equipment) throws IllegalArgumentException, DAOException;

    /**
     * Returns a list of all Equipment from the database ordered by Equipment ID. The list is never null and
     * is empty when the database does not contain any Equipment.
     * @return A list of all Equipment from the database ordered by Equipment ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Equipment> list() throws DAOException;
    
    /**
     * Returns a list of all Equipment matching Invoice ID from the database ordered by EquipmentType ID. 
     * The EquipmentType ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not 
     * contain any Equipment matching EquipmentType ID.
     * @param equipment_type The EquipmentType ID to be searched for.
     * @return A list of all Equipment matching EquipmentType ID from the database ordered by Equipment ID.
     * @throws IllegalArgumentException If EquipmentType ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<Equipment> list(EquipmentType equipment_type) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all Equipment that are pending mantainance from the database ordered by EquipmentType ID. 
     * The list is never null and is empty when the database does not 
     * contain any Equipment pending mantainance.
     * @param date The Date to be searched for.
     * @return A list of all Equipment pending mantainance from the database ordered by Equipment ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Equipment> listPending(Date date) throws IllegalArgumentException;
    
    /**
     * Create the given Equipment in the database.
     * The EquipmentType ID must not be null and the Equipment ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Equipment.
     * @param equipment_type The EquipmentType to be assigned to this Equipment.
     * @param equipment The Equipment to be created.
     * @throws IllegalArgumentException If the EquipmentType ID is null.
     * @throws IllegalArgumentException If the Equipment ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(EquipmentType equipment_type, Equipment equipment) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Equipment in the database. The Equipment ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param equipment The Equipment to be updated.
     * @throws IllegalArgumentException If the Equipment ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Equipment equipment) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Equipment from the database. After deleting, the DAO will set the ID of the given
     * Equipment to null.
     * @param equipment The Equipment to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Equipment equipment) throws DAOException;
}
