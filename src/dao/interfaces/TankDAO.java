/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Tank;

/**
 *
 * @author Pavilion Mini
 */
public interface TankDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Tank from the database matching the given ID, otherwise null.
     * @param id The ID of the Tank to be returned.
     * @return The Tank from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Tank find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Tank from the database ordered by Tank ID. The list is never null and
     * is empty when the database does not contain any Tank.
     * @return A list of all Container from the database ordered by Tank ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Tank> list() throws DAOException;
    
    /**
     * Create the given Tank in the database.
     * The Tank ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Tank.
     * @param tank The Tank to be created.
     * @throws IllegalArgumentException If the Tank ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Tank tank) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Tank in the database. The Tank ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param tank The Tank to be updated.
     * @throws IllegalArgumentException If the Container ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Tank tank) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Tank from the database. After deleting, the DAO will set the ID of the given
     * Tank to null.
     * @param tank The Tank to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Tank tank) throws DAOException;
}
