/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.CompanyContact;

/**
 *
 * @author Pavilion Mini
 */
public interface CompanyContactDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the CompanyContact from the database matching the given ID, otherwise null.
     * @param id The ID of the CompanyContact to be returned.
     * @return The CompanyContact from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public CompanyContact find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all CompanyContacts from a given Company from the database ordered by CompanyContact ID. The Company ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any CompanyContacts.
     * @param company The Company to grab the CompanyContacts from. 
     * @return A list of all CompanyContacts from the database ordered by CompanyContact ID.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<CompanyContact> list(Company company) throws DAOException;
    
    /**
     * Create the given CompanyAddress for a given Company in the database. The Company ID must not be null and
     * the CompanyContact ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyContact.
     * @param company The Company to be assigned the address.
     * @param contact The CompanyContact to be created.
     * @throws IllegalArgumentException If the Company ID is null.
     * @throws IllegalArgumentException If the CompanyContact ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Company company, CompanyContact contact) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given CompanyContact in the database. The CompanyContact ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param contact The CompanyContact to be updated in the database.
     * @throws IllegalArgumentException If the CompanyContact ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(CompanyContact contact) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given CompanyContact from the database. After deleting, the DAO will set the ID of the given
     * CompanyContact to null.
     * @param contact The CompanyContact to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(CompanyContact contact) throws DAOException;
}
