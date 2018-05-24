/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public interface ModuleDAO {
    // Actions ------------------------------------------------------------------------------------
   
    /**
     * Returns the Module from the database matching the given ID, otherwise null.
     * @param id The ID of the Module to be returned.
     * @return The Module from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Module find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Modules from the database ordered by Module ID. The list is never null and
     * is empty when the database does not contain any Module.
     * @return A list of all Modules from the database ordered by Module ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Module> list() throws DAOException;
    
    /**
     * Create the given Module in the database. The Module ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Module.
     * @param module The Module to be created in the database.
     * @throws IllegalArgumentException If the Module ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Module module) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Module in the database. The Module ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param module The module to be updated in the database.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Module module) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Module from the database. After deleting, the DAO will set the ID of the given
     * Module to null.
     * @param module The Module to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Module module) throws DAOException;
    
}
