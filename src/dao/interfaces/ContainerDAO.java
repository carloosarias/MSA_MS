/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Container;

/**
 *
 * @author Pavilion Mini
 */
public interface ContainerDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Container from the database matching the given ID, otherwise null.
     * @param id The ID of the Container to be returned.
     * @return The Container from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Container find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Container from the database ordered by Container ID. The list is never null and
     * is empty when the database does not contain any Container.
     * @return A list of all Container from the database ordered by Container ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Container> list() throws DAOException;
    
    /**
     * Returns a list of all Container matching type from the database ordered by Container ID. 
     * The list is never null and is empty when the database does not contain any Container matching type.
     * @param type The type to be searched for.
     * @return A list of all Container matching type from the database ordered by Container ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Container> listType(String type) throws DAOException;
    /**
     * Returns a list of all Container matching process from the database ordered by Container ID. 
     * The list is never null and is empty when the database does not contain any Container matching process.
     * @param process The process to be searched for.
     * @return A list of all Container matching process from the database ordered by Container ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Container> listProcess(String process) throws DAOException;
    
    public List<Container> listTypeProcess(String type, String process) throws DAOException;
    
    /**
     * Create the given Container in the database.
     * The Container ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Container.
     * @param container The Container to be created.
     * @throws IllegalArgumentException If the Container ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Container container) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Container in the database. The Container ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param container The Container to be updated.
     * @throws IllegalArgumentException If the Container ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Container container) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Container from the database. After deleting, the DAO will set the ID of the given
     * Container to null.
     * @param container The Container to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Container container) throws DAOException;
}
