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
     * Returns the Item from the database matching the given ID, otherwise null.
     * @param item The Item to get the Item ID from.
     * @return The Item from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public ItemPart find(Item item) throws DAOException;
    
    /**
     * Returns the Item from the database matching the given ID, otherwise null.
     * @param part The ItemPart to get the Item ID from.
     * @return The Item from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Item findItem(ItemPart part) throws DAOException;
    
    /**
     * Returns the Item from the database matching the given ID, otherwise null.
     * @param part The ItemPart to get the Metal ID from.
     * @return The Item from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Metal findMetal(ItemPart part) throws DAOException;
    
    public ItemPart list() throws DAOException;
    
    public ItemPart list(Metal metal) throws IllegalArgumentException, DAOException;
    
    public void create(Item item, Metal metal, ItemPart part) throws IllegalArgumentException, DAOException;
    
    public void update(Item item, Metal metal, ItemPart part) throws IllegalArgumentException, DAOException;
    
    public void delete(ItemPart part) throws DAOException;
}
