/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import model.Item;
import model.ItemPart;
import model.Metal;

/**
 *
 * @author Pavilion Mini
 */
public interface ItemPartDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ItemPart from the database matching the given ID, otherwise null.
     * @param id The ID of the ItemPart to be returned.
     * @return The ItemPart from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ItemPart find(Integer id) throws DAOException;
    
    /**
     * Returns the ItemPart from the database matching the given Item ID, otherwise null.
     * @param item The Item to get the Item ID from
     * @return The ItemPart from the database matching the given Item ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ItemPart find(Item item) throws IllegalArgumentException, DAOException;
    
    public Item findItem(ItemPart part) throws DAOException;
    
    public Metal findMetal(ItemPart part) throws DAOException;
    
    /**
     * Returns a list of all ItemPart from the database ordered by Item ID. The list is never null and
     * is empty when the database does not contain any ItemPart.
     * @return A list of all ItemPart from the database ordered by Item ID.
     * @throws DAOException If something fails at database level.
     */
    public ItemPart list() throws DAOException;
    
    /**
     * Returns a list of all ItemPart from the database matching Metal ID ordered by Item ID. The Metal ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any ItemPart.
     * @param metal The metal to be searched for
     * @return A list of all ItemPart from the database ordered by Item ID.
     * @throws IllegalArgumentException If the Metal ID is null.
     * @throws DAOException If something fails at database level.
     */
    public ItemPart list(Metal metal) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given Item in the database. The Item ID and Metal ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param item The Item to get the Item ID from
     * @param metal The Metal to get the Metal ID from
     * @param part the ItemPart to be created in the database.
     * @throws IllegalArgumentException If the Item ID is null.
     * @throws IllegalArgumentException If the Metal ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Item item, Metal metal, ItemPart part) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Item in the database. The Item ID and Metal ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param metal The Metal to get the Metal ID from
     * @param part the ItemPart to be updated.
     * @throws IllegalArgumentException If the Item ID is null.
     * @throws IllegalArgumentException If the Metal ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Metal metal, ItemPart part) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ItemPart from the database.
     * @param part The ItemPart to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ItemPart part) throws DAOException;
}
