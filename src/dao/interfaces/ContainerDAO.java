/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Container;
import model.DepartReport;
import model.Employee;

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
     * Returns a list of all DepartReport matching Company ID from the database ordered by DepartReport ID. 
     * The Company ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any DepartReport.
     * @param type The type to be searched for.
     * @return A list of all DepartReport matching Company ID from the database ordered by DepartReport ID.
     * @throws IllegalArgumentException If Company ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<Container> listProcess(String process) throws DAOException;
    
    public List<Container> listTypeProcess(String type, String process) throws DAOException;
    
    /**
     * Create the given DepartReport in the database.
     * The Employee ID must not be null, The Company ID must not be null, and
     * The DepartReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given DepartReport.
     * @param container The Container to be created.
     * @throws IllegalArgumentException If the DepartReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Container container) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartReport in the database. The DepartReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param container The Container to be updated.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Container container) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartReport from the database. After deleting, the DAO will set the ID of the given
     * DepartReport to null.
     * @param container The Container to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Container container) throws DAOException;
}
