/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Metal;

/**
 *
 * @author Pavilion Mini
 */
public interface MetalDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Metal from the database matching the given ID, otherwise null.
     * @param id The ID of the Metal to be returned.
     * @return The Metal from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Metal find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Metal from the database ordered by Metal ID. The list is never null and
     * is empty when the database does not contain any Metal.
     * @return A list of all Metal from the database ordered by Metal ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Metal> list() throws DAOException;
    
    public List<Metal> list(boolean active) throws DAOException;
    
    /**
     * Create the given Metal in the database.
     * The Metal ID must not be null otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Metal.
     * @param metal The Metal to be created.
     * @throws IllegalArgumentException If the Metal ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Metal metal) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Metal in the database. The Metal ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param metal The Metal to be updated.
     * @throws IllegalArgumentException If the Metal ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Metal metal) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Metal from the database. After deleting, the DAO will set the ID of the given
     * Metal to null.
     * @param metal The Metal to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Metal metal) throws DAOException;
}
