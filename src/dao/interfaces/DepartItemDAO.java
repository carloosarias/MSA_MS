/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.DepartItem;
import model.DepartReport;
import model.PartRevision;

/**
 *
 * @author Pavilion Mini
 */
public interface DepartItemDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the DepartItem from the database matching the given ID, otherwise null.
     * @param id The ID of the DepartItem to be returned.
     * @return The DepartItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public DepartItem find(Integer id) throws DAOException;
    
    /**
     * Returns the IncomingReport from the database matching the given DepartItem ID, otherwise null.
     * DepartItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param depart_item The DepartItem ID to get the DepartReport from.
     * @return The IncomingReport from the database matching the given DepartItem ID, otherwise null.
     * @throws IllegalArgumentException If DepartItem ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public DepartReport findDepartReport(DepartItem depart_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given DepartItem ID, otherwise null.
     * DepartItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param depart_item The DepartItem ID to get the PartRevision from.
     * @return The PartRevision from the database matching the given DepartItem ID, otherwise null.
     * @throws IllegalArgumentException If DepartItem ID is null.
     * @throws DAOException If something fails at database level.
     */          
    public PartRevision findPartRevision(DepartItem depart_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all DepartItem from a given DepartReport from the database ordered by DepartItem ID. The DepartReport ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any DepartItem matching DepartReport.
     * @param depart_report The DepartReport to be searched for.
     * @return A list of all DepartItem matching DepartReport from the database ordered by DepartItem ID.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<DepartItem> list(DepartReport depart_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given DepartItem for a given DepartReport in the database. The DepartReport ID must not be null,
     * the PartRevision ID must not be null and the DepartItem ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param depart_report The DepartReport to be assigned to the DepartItem.
     * @param part_revision The PartRevision to be assigned to the DepartItem.
     * @param depart_item The DepartItem to be created.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the DepartItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(DepartReport depart_report, PartRevision part_revision, DepartItem depart_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartItem in the database. The DepartItem ID must not be null
     * and the PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be assigned to the DepartItem.
     * @param depart_item The DepartItem to be updated in the database.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the DepartItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(PartRevision part_revision, DepartItem depart_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartItem from the database. After deleting,
     * the DAO will set the ID of the given DepartItem to null.
     * @param depart_item The DepartItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(DepartItem depart_item) throws DAOException;    
}
