/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.EquipmentType;
import model.EquipmentTypeCheck;

/**
 *
 * @author Pavilion Mini
 */
public interface EquipmentTypeCheckDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the EquipmentTypeCheck from the database matching the given ID, otherwise null.
     * @param id The ID of the EquipmentTypeCheck to be returned.
     * @return The EquipmentTypeCheck from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public EquipmentTypeCheck find(Integer id) throws DAOException;
    
    /**
     * Returns the EquipmentType from the database matching the given EquipmentTypeCheck ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param equipment_type_check The EquipmentTypeCheck to get the EquipmentType from.
     * @return The EquipmentType from the database matching the given EquipmentTypeCheck ID, otherwise null.
     * @throws IllegalArgumentException If EquipmentTypeCheck ID is null.
     * @throws DAOException If something fails at database level.
     */
    public EquipmentType findEquipmentType(EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all EquipmentTypeCheck matching Invoice ID from the database ordered by EquipmentType ID. 
     * The EquipmentType ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not 
     * contain any EquipmentTypeCheck matching EquipmentType ID.
     * @param equipment_type The EquipmentType ID to be searched for.
     * @param active
     * @return A list of all EquipmentTypeCheck matching EquipmentType ID from the database ordered by EquipmentTypeCheck ID.
     * @throws IllegalArgumentException If EquipmentType ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<EquipmentTypeCheck> list(EquipmentType equipment_type, boolean active) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given EquipmentTypeCheck in the database.
     * The EquipmentType ID must not be null and the EquipmentTypeCheck ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given EquipmentTypeCheck.
     * @param equipment_type The EquipmentType to be assigned to this EquipmentTypeCheck.
     * @param equipment_type_check The EquipmentTypeCheck to be created.
     * @throws IllegalArgumentException If the EquipmentType ID is null.
     * @throws IllegalArgumentException If the EquipmentTypeCheck ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(EquipmentType equipment_type, EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given EquipmentTypeCheck in the database. The EquipmentTypeCheck ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param equipment_type_check The EquipmentTypeCheck to be updated.
     * @throws IllegalArgumentException If the EquipmentTypeCheck ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given EquipmentTypeCheck from the database. After deleting, the DAO will set the ID of the given
     * EquipmentTypeCheck to null.
     * @param equipment_type_check The EquipmentTypeCheck to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(EquipmentTypeCheck equipment_type_check) throws DAOException;
}
