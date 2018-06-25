/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.IncomingItem;
import model.IncomingReport;
import model.PartRevision;

/**
 *
 * @author Pavilion Mini
 */
public interface IncomingItemDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the IncomingItem from the database matching the given ID, otherwise null.
     * @param id The ID of the IncomingItem to be returned.
     * @return The IncomingItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public IncomingItem find(Integer id) throws DAOException;
    
    /**
     * Returns the IncomingReport from the database matching the given IncomingItem ID, otherwise null.
     * IncomingItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_item The IncomingItem ID to get the IncomingReport from.
     * @return The IncomingReport from the database matching the given IncomingItem ID, otherwise null.
     * @throws IllegalArgumentException If IncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public IncomingReport findIncomingReport(IncomingItem incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given IncomingItem ID, otherwise null.
     * IncomingItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_item The IncomingItem ID to get the PartRevision from.
     * @return The PartRevision from the database matching the given IncomingItem ID, otherwise null.
     * @throws IllegalArgumentException If IncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */          
    public PartRevision findPartRevision(IncomingItem incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all IncomingItem from a given IncomingReport from the database ordered by IncomingItem ID. The IncomingReport ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any IncomingItem matching IncomingReport.
     * @param incoming_report The IncomingReport to be searched for.
     * @return A list of all IncomingItem matching IncomingReport from the database ordered by IncomingItem ID.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<IncomingItem> list(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given IncomingItem for a given IncomingReport in the database. The IncomingReport ID must not be null,
     * the PartRevision ID must not be null and the IncomingItem ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param incoming_report The IncomingReport to be assigned to the IncomingItem.
     * @param part_revision The PartRevision to be assigned to the IncomingItem.
     * @param incoming_item The IncomingItem to be created.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the IncomingItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(IncomingReport incoming_report, PartRevision part_revision, IncomingItem incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given IncomingItem in the database. The IncomingItem ID must not be null
     * and the PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be assigned to the IncomingItem.
     * @param incoming_item The IncomingItem to be updated in the database.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the IncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(PartRevision part_revision, IncomingItem incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given IncomingItem from the database. After deleting,
     * the DAO will set the ID of the given IncomingItem to null.
     * @param incoming_item The IncomingItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(IncomingItem incoming_item) throws DAOException;
}
