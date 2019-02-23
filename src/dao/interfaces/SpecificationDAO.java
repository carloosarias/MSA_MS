/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public interface SpecificationDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Specification from the database matching the given ID, otherwise null.
     * @param id The ID of the Specification to be returned.
     * @return The Specification from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Specification find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Specification from the database ordered by Specification ID. The list is never null and
     * is empty when the database does not contain any Specification.
     * @return A list of all Specification from the database ordered by Specification ID.
     * @throws DAOException If something fails at database level.
     */        
    //public List<Specification> list() throws DAOException;
    public List<Specification> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all Specification matching process from the database ordered by Specification ID. 
     * The list is never null and is empty when the database does not contain any Specification matching process.
     * @param process the process to be searched for.
     * @return A list of all Specification matching process from the database ordered by Specification ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<Specification> listByProcess(String process) throws DAOException;
    
    /**
     * Create the given Specification in the database.
     * The Specification ID must not be null otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Specification.
     * @param specification The Specification to be created.
     * @throws IllegalArgumentException If the Specification ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Specification in the database. The Specification ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param specification The Specification to be updated.
     * @throws IllegalArgumentException If the Specification ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Specification from the database. After deleting, the DAO will set the ID of the given
     * Specification to null.
     * @param specification The Specification to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Specification specification) throws DAOException;
}
