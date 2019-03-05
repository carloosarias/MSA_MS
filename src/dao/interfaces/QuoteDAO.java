/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.CompanyContact;
import model.DepartLot;
import model.PartRevision;
import model.Quote;

/**
 *
 * @author Pavilion Mini
 */
public interface QuoteDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Quote from the database matching the given ID, otherwise null.
     * @param id The ID of the Quote to be returned.
     * @return The Quote from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Quote find(Integer id) throws DAOException;
    /**
     * @param depart_report_id
     * @param part_revision_id
     * @param active
     */
    public Quote findLatest(Integer depart_report_id, Integer part_revision_id, boolean active) throws DAOException;
    /**
     * Returns the PartRevision from the database matching the given Quote ID, otherwise null.
     * Invoice ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param quote The Quote to get the PartRevision from.
     * @return The PartRevision from the database matching the given Quote ID, otherwise null.
     * @throws IllegalArgumentException If Quote ID is null.
     * @throws DAOException If something fails at database level.
     */
    public PartRevision findPartRevision(Quote quote) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the CompanyContact from the database matching the given Quote ID, otherwise null.
     * Quote ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param quote The Quote to get the CompanyContact from.
     * @return The CompanyContact from the database matching the given Quote ID, otherwise null.
     * @throws IllegalArgumentException If Quote ID is null.
     * @throws DAOException If something fails at database level.
     */
    public CompanyContact findCompanyContact(Quote quote) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all Quote from the database ordered by Quote ID. The list is never null and
     * is empty when the database does not contain any Quote.
     * @param active
     * @return A list of all Quote from the database ordered by Quote ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<Quote> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all Quote matching PartRevision ID and String approved from the database ordered by Quote ID. 
     * PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not 
     * contain any Quote matching PartRevision ID and String approved.
     * @param part_revision The PartRevision ID to be searched for.
     * @param active The status of the Quote to be searched for
     * @return A list of all Quote matching PartRevision ID and String approved from the database ordered by Quote ID.
     * @throws IllegalArgumentException If PartRevision ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<Quote> list(PartRevision part_revision, boolean active) throws IllegalArgumentException, DAOException;
    public List<Quote> list(DepartLot depart_lot, boolean active) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given Quote in the database. The PartRevision ID must not be null,
     * The CompanyContact must not be null, and The Quote ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Quote.
     * @param part_revision The PartRevision to be assigned to this Quote.
     * @param company_contact The CompanyContact to be assigned to this Quote.
     * @param quote The Quote to be created.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the CompanyContact ID is null.
     * @throws IllegalArgumentException If the Quote ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(PartRevision part_revision, CompanyContact company_contact, Quote quote) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given Quote in the database. The Quote ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param quote The Quote to be updated.
     * @throws IllegalArgumentException If the Quote ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(Quote quote) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Quote from the database. After deleting, the DAO will set the ID of the given
     * Quote to null.
     * @param quote The Quote to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Quote quote) throws DAOException;
}
