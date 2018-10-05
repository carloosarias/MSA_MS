/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Quote;
import model.QuoteItem;
import model.SpecificationItem;

/**
 *
 * @author Pavilion Mini
 */
public interface QuoteItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the QuoteItem from the database matching the given ID, otherwise null.
     * @param id The ID of the QuoteItem to be returned.
     * @return The QuoteItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public QuoteItem find(Integer id) throws DAOException;
    
    /**
     * Returns the Quote from the database matching the given QuoteItem ID, otherwise null.
     * QuoteItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param quote_item The QuoteItem to get the Quote from.
     * @return The Quote from the database matching the given QuoteItem ID, otherwise null.
     * @throws IllegalArgumentException If QuoteItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Quote findQuote(QuoteItem quote_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the SpecificationItem from the database matching the given QuoteItem ID, otherwise null.
     * QuoteItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param quote_item The QuoteItem to get the SpecificationItem from.
     * @return The SpecificationItem from the database matching the given QuoteItem ID, otherwise null.
     * @throws IllegalArgumentException If QuoteItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public SpecificationItem findSpecificationItem(QuoteItem quote_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all QuoteItem matching Quote ID from the database ordered by QuoteItem ID. 
     * The Quote ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not 
     * contain any QuoteItem matching Quote ID.
     * @param quote The Quote ID to be searched for.
     * @return A list of all QuoteItem matching Quote ID from the database ordered by QuoteItem ID.
     * @throws IllegalArgumentException If Quote ID is null.
     * @throws DAOException If something fails at database level.
     */         
    public List<QuoteItem> list(Quote quote) throws DAOException;
    
    /**
     * Create the given QuoteItem in the database. The SpecificationItem ID must not be null, 
     * The Quote must not be null, and The QuoteItem ID must be null,
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given QuoteItem.
     * @param specification_item The SpecificationItem to be assigned to this QuoteItem.
     * @param quote The Quote to be assigned to this QuoteItem.
     * @param quote_item The QuoteItem to be created.
     * @throws IllegalArgumentException If the SpecificationItem ID is null.
     * @throws IllegalArgumentException If the Quote ID is null.
     * @throws IllegalArgumentException If the QuoteItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(SpecificationItem specification_item, Quote quote, QuoteItem quote_item) throws IllegalArgumentException, DAOException;
     
    /**
     * Update the given QuoteItem in the database. The QuoteItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param quote_item The QuoteItem to be updated.
     * @throws IllegalArgumentException If the Quote ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(QuoteItem quote_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given QuoteItem from the database. After deleting, the DAO will set the ID of the given
     * QuoteItem to null.
     * @param quote_item The QuoteItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(QuoteItem quote_item) throws DAOException;
}
