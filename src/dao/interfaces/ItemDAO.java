/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Item;
import model.ItemType;

/**
 *
 * @author Pavilion Mini
 */
public interface ItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Item from the database matching the given ID, otherwise null.
     * @param id The ID of the Item to be returned.
     * @return The Item from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Item find(Integer id) throws DAOException;
    
    /**
     * Returns the Item from the database matching the given name, otherwise null.
     * @param name The name of the Item to be returned.
     * @return The Item from the database matching the given name, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Item find(String name) throws DAOException;
    
    /**
     * Returns the ItemType from the database matching the given Item ID, otherwise null.
     * @param item The Item to be searched for.
     * @return The ItemType from the database matching the given Item ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ItemType findType(Item item) throws DAOException;
    
    /**
     * Returns a list of all Item from the database ordered by Item ID. The list is never null and
     * is empty when the database does not contain any Item.
     * @return A list of all Item from the database ordered by Item ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Item> list() throws DAOException;
    
    /**
     * Returns a list of all Item from the database matching ItemType ordered by Item ID. The list is never null and
     * is empty when the database does not contain any Item.
     * @param type The ItemType to be searched for
     * @return A list of all Item from the database matching ItemType ordered by Item ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Item> list(ItemType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given Item in the database. The Item ID must be null and the ItemType must not be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Item.
     * @param type The ItemType to be assigned to the Item.
     * @param item The Item to be created in the database.
     * @throws IllegalArgumentException If the Item ID is not null.
     * @throws IllegalArgumentException If the ItemType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void create(ItemType type, Item item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Item in the database. The Item ID and ItemType ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param type The ItemType to be assigned to the Item.
     * @param item The Item to be updated in the database.
     * @throws IllegalArgumentException If the Item ID is null.
     * @throws IllegalArgumentException If the ItemType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(ItemType type, Item item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Item from the database. After deleting, the DAO will set the ID of the given
     * Item to null.
     * @param item The Item to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Item item) throws DAOException;
    
}
