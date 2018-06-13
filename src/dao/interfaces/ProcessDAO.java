/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Process;

/**
 *
 * @author Pavilion Mini
 */
public interface ProcessDAO {
    // Actions ------------------------------------------------------------------------------------
   
    /**
     * Returns the Process from the database matching the given ID, otherwise null.
     * @param id The ID of the Process to be returned.
     * @return The Process from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Process find(Integer id) throws DAOException;
    /**
     * Returns the Process from the database matching the given name, otherwise null.
     * @param name The name of the Process to be returned.
     * @return The Process from the database matching the given name, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Process find(String name) throws DAOException;
    
    /**
     * Returns a list of all Processes from the database ordered by Process ID. The list is never null and
     * is empty when the database does not contain any Process.
     * @return A list of all Processes from the database ordered by Process ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Process> list() throws DAOException;
    
    /**
     * Returns a list of all active Processes from the database ordered by Process ID. The list is never null and
     * is empty when the database does not contain any active Process.
     * @param active the active to be searched for
     * @return A list of all Processes from the database ordered by Process ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Process> listActive(boolean active) throws DAOException;
    
    /**
     * Create the given Process in the database. The Process ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Process.
     * @param process The Process to be created in the database.
     * @throws IllegalArgumentException If the Process ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Process process) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Process in the database. The Process ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param process The Process to be updated in the database.
     * @throws IllegalArgumentException If the Process ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Process process) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Process from the database. After deleting, the DAO will set the ID of the given
     * Process to null.
     * @param process The Process to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Process process) throws DAOException;
        
}
