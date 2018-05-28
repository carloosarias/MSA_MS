/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Coating;

/**
 *
 * @author Pavilion Mini
 */
public interface CoatingDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Coating from the database matching the given ID, otherwise null.
     * @param id The ID of the Coating to be returned.
     * @return The Coating from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Coating find(Integer id) throws DAOException;
    
    /**
     * Returns the Coating from the database matching the given name, otherwise null.
     * @param name The name of the Coating to be returned.
     * @return The Coating from the database matching the given name, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Coating find(String name) throws DAOException;
    
    /**
     * Returns a list of all Coating from the database ordered by Coating ID. The list is never null and
     * is empty when the database does not contain any Coating.
     * @return A list of all Coating from the database ordered by Coating ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Coating> list() throws DAOException;
    
    /**
     * Create the given Coating in the database. The Coating ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Coating.
     * @param coating The Coating to be created in the database.
     * @throws IllegalArgumentException If the Coating ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Coating coating) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Coating in the database. The Coating ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param coating The Coating to be updated in the database.
     * @throws IllegalArgumentException If the Coating ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Coating coating) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Coating from the database. After deleting, the DAO will set the ID of the given
     * Coating to null.
     * @param coating The Coating to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Coating coating) throws DAOException;
    
}
