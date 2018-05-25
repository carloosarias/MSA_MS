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

/**
 *
 * @author Pavilion Mini
 */
public interface CompanyAddressDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the CompanyAddress from the database matching the given ID, otherwise null.
     * @param id The ID of the CompanyAddress to be returned.
     * @return The CompanyAddress from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public CompanyAddress find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all CompanyAddresses from the database ordered by CompanyAddress ID. The list is never null and
     * is empty when the database does not contain any CompanyAddress.
     * @return A list of all Companies from the database ordered by CompanyAddress ID.
     * @throws DAOException If something fails at database level.
     */
    public List<CompanyAddress> list(Company company) throws DAOException;
    
    /**
     * Create the given CompanyAddress in the database. The Company ID must not be null and
     * the CompanyAddress ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given Company.
     * @param company The Company to be assigned the address.
     * @param address The CompanyAddress to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the CompanyAddress ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Company company, CompanyAddress address) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given CompanyAddress in the database. The CompanyAddress ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param address The CompanyAddress to be updated in the database.
     * @throws IllegalArgumentException If the CompanyAddress ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(CompanyAddress address) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given CompanyAddress from the database. After deleting, the DAO will set the ID of the given
     * CompanyAddress to null.
     * @param address The CompanyAddress to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(CompanyAddress address) throws DAOException;
}
