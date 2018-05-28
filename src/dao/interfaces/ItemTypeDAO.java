/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.ItemType;

/**
 *
 * @author Pavilion Mini
 */
public interface ItemTypeDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ItemType from the database matching the given ID, otherwise null.
     * @param id The ID of the ItemType to be returned.
     * @return The ItemType from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ItemType find(Integer id) throws DAOException;
    
    /**
     * Returns the ItemType from the database matching the given name, otherwise null.
     * @param name The name of the ItemType to be returned.
     * @return The ItemType from the database matching the given name, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ItemType find(String name) throws DAOException;
    
    /**
     * Returns a list of all ItemType from the database ordered by ItemType ID. The list is never null and
     * is empty when the database does not contain any ItemType.
     * @return A list of all ItemType from the database ordered by ItemType ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<ItemType> list() throws DAOException;
    
    /**
     * Create the given ItemType in the database. The ItemType ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given ItemType.
     * @param type The ItemType to be created in the database.
     * @throws IllegalArgumentException If the ItemType ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(ItemType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ItemType in the database. The ItemType ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param type The ItemType to be updated in the database.
     * @throws IllegalArgumentException If the ItemType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(ItemType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Metal from the database. After deleting, the DAO will set the ID of the given
     * ItemType to null.
     * @param type The ItemType to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ItemType type) throws DAOException;
    
}
