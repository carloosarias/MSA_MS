/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.DepartLot;
import model.IncomingLot;
import model.IncomingReport;
import model.PartRevision;
import model.ProductPart;


/**
 *
 * @author Pavilion Mini
 */
public interface IncomingLotDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the IncomingLot from the database matching the given ID, otherwise null.
     * @param id The ID of the IncomingLot to be returned.
     * @return The IncomingLot from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public IncomingLot find(Integer id) throws DAOException;
    
    /**
     * Returns the IncomingReport from the database matching the given IncomingLot ID, otherwise null.
     * IncomingLot ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot ID to get the IncomingReport from.
     * @return The IncomingReport from the database matching the given IncomingLot ID, otherwise null.
     * @throws IllegalArgumentException If IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public IncomingReport findIncomingReport(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given IncomingLot ID, otherwise null.
     * IncomingLot ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot ID to get the PartRevision from.
     * @return The PartRevision from the database matching the given IncomingLot ID, otherwise null.
     * @throws IllegalArgumentException If IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */          
    public PartRevision findPartRevision(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the DepartLot from the database matching the given IncomingLot ID, otherwise null.
     * IncomingLot ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot ID to get the DepartLot from.
     * @return The DepartLot from the database matching the given IncomingLot ID, otherwise null.
     * @throws IllegalArgumentException If IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */          
    public DepartLot findDepartLot(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;  
    
    /**
     * Returns a list of all IncomingLot from a given IncomingReport from the database ordered by IncomingLot ID. The IncomingReport ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any IncomingLot matching IncomingReport.
     * @param incoming_report The IncomingReport to be searched for.
     * @return A list of all IncomingLot matching IncomingReport from the database ordered by IncomingLot ID.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<IncomingLot> list(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all IncomingLot matching a given lot_number from the database ordered by IncomingLot ID.
     * The list is never null and is empty when the database does not contain any IncomingLot matching lot_number.
     * @param lot_number The lot_number to be searched for.
     * @return A list of all IncomingLot matching lot_number from the database ordered by IncomingLot ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<IncomingLot> list(String lot_number) throws IllegalArgumentException;
    
    /**
     * Returns a list of distinct PartRevision matching a given IncomingReport from the database ordered by PartRevision ID.
     * The IncomingReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any PartRevisions matching IncomingReport.
     * @param incoming_report The incoming_report to be searched for.
     * @return A list of distinct PartRevision matching IncomingReport from the database ordered by PartRevision ID.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws DAOException If something fails at database level.
     */       
    public List<PartRevision> listPartRevision(IncomingReport incoming_report) throws IllegalArgumentException, DAOException;    
    /**
     * Create the given IncomingLot for a given IncomingReport in the database. 
     * The IncomingReport ID must not be null, the PartRevision ID must not be null
     * and the IncomingLot ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param incoming_report The IncomingReport to be assigned to the IncomingLot.
     * @param part_revision The PartRevision to be assigned to the IncomingLot.
     * @param incoming_lot The IncomingLot to be created.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the IncomingLot ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(IncomingReport incoming_report, PartRevision part_revision, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given IncomingLot for a given IncomingReport in the database. 
     * The IncomingReport ID must not be null, the DepartLot ID must not be null
     * the PartRevision ID must not be null and the IncomingLot ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given IncomingLot.
     * @param incoming_report The IncomingReport to be assigned to the IncomingLot.
     * @param depart_lot The DepartLot to be assigned to the IncomingLot.
     * @param part_revision The PartRevision to be assigned to the IncomingLot.
     * @param incoming_lot The IncomingLot to be created.
     * @throws IllegalArgumentException If the IncomingReport ID is null.
     * @throws IllegalArgumentException If the DepartLot ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the IncomingLot ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(IncomingReport incoming_report, DepartLot depart_lot, PartRevision part_revision, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given IncomingLot in the database.
     * The IncomingLot ID must not be null otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot to be updated in the database.
     * @throws IllegalArgumentException If the IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given IncomingLot from the database. After deleting,
     * the DAO will set the ID of the given IncomingLot to null.
     * @param incoming_lot The IncomingLot to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(IncomingLot incoming_lot) throws DAOException;
    
    public Integer findTotalQuantity(IncomingReport incoming_report, PartRevision part_revision);
    public Integer findTotalBoxQuantity(IncomingReport incoming_report, PartRevision part_revision);
    
    public List<IncomingLot> listDateRange(ProductPart product_part, boolean discrepancy, Date start, Date end);
}
