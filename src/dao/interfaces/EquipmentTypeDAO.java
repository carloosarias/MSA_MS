/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.EquipmentType;

/**
 *
 * @author Pavilion Mini
 */
public interface EquipmentTypeDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the EquipmentType from the database matching the given ID, otherwise null.
     * @param id The ID of the EquipmentType to be returned.
     * @return The EquipmentType from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public EquipmentType find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all EquipmentType from the database ordered by EquipmentType ID. The list is never null and
     * is empty when the database does not contain any EquipmentType.
     * @return A list of all EquipmentType from the database ordered by EquipmentType ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<EquipmentType> list(boolean active) throws DAOException;
    
    /**
     * Create the given EquipmentType in the database.
     * The EquipmentType ID must not be null otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Metal.
     * @param equipment_type The EquipmentType to be created.
     * @throws IllegalArgumentException If the Metal ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(EquipmentType equipment_type) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given EquipmentType in the database. The EquipmentType ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param equipment_type The EquipmentType to be updated.
     * @throws IllegalArgumentException If the EquipmentType ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(EquipmentType equipment_type) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given EquipmentType from the database. After deleting, the DAO will set the ID of the given
     * EquipmentType to null.
     * @param equipment_type The EquipmentType to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(EquipmentType equipment_type) throws DAOException;
}
