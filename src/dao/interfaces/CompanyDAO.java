/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;

/**
 *
 * @author Pavilion Mini
 */
public interface CompanyDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Company from the database matching the given ID, otherwise null.
     * @param id The ID of the Company to be returned.
     * @return The Company from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public Company find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all Companies from the database ordered by Company ID. The list is never null and
     * is empty when the database does not contain any Company.
     * @return A list of all Companies from the database ordered by Company ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Company> list() throws DAOException;
    
    /**
     * Returns a list of all Companies that are suppliers from the database ordered by Company ID. The list is never null and
     * is empty when the database does not contain any Company supplier.
     * @param supplier The state of the company if false then return companies that are NOT suppliers
     * @return A list of all Companies that are suppliers from the database ordered by Company ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Company> listSupplier(boolean supplier) throws DAOException;
    
    /**
     * Returns a list of all Companies that are clients from the database ordered by Company ID. The list is never null and
     * is empty when the database does not contain any Company client.
     * @param client The state of the company if false then return companies that are NOT clients
     * @return A list of all Companies that are clients from the database ordered by Company ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Company> listClient(boolean supplier) throws DAOException;
    
    /**
     * Returns a list of all Companies that are active from the database ordered by Company ID. The list is never null and
     * is empty when the database does not contain any Company active.
     * @param active The state of the company if false then return companies that are NOT active
     * @return A list of all Companies that are active from the database ordered by Company ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Company> listActive(boolean active) throws DAOException;
    
    /**
     * Create the given Company in the database. The Company ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Company.
     * @param company The Company to be created in the database.
     * @throws IllegalArgumentException If the Company ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Company in the database. The Company ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param company The company to be updated in the database.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Company from the database. After deleting, the DAO will set the ID of the given
     * Company to null.
     * @param company The Company to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Company company) throws DAOException;
}
