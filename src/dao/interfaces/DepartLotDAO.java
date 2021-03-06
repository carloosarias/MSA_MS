/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.Company;
import model.DepartLot;
import model.DepartReport;
import model.PartRevision;

/**
 *
 * @author Pavilion Mini
 */
public interface DepartLotDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the DepartLot from the database matching the given ID, otherwise null.
     * @param id The ID of the DepartLot to be returned.
     * @return The DepartLot from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public DepartLot find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all DepartLot from a given DepartReport from the database ordered by DepartLot ID. The DepartReport ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any DepartLot matching DepartReport.
     * @param depart_report The DepartReport to be searched for.
     * @return A list of all DepartLot matching DepartReport from the database ordered by DepartLot ID.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<DepartLot> list(DepartReport depart_report) throws IllegalArgumentException, DAOException;
    
    public List<DepartLot> listGroup(DepartReport depart_report) throws IllegalArgumentException, DAOException;
    
    public List<DepartLot> listPending(Company company) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all DepartLot matching a given lot_number from the database ordered by IncomingLot ID.
     * The list is never null and is empty when the database does not contain any DepartLot matching lot_number.
     * @param lot_number The lot_number to be searched for.
     * @return A list of all DepartLot matching lot_number from the database ordered by DepartLot ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<DepartLot> list(DepartReport depart_report, Boolean rejected, Company company, Date start_date, Date end_date, String partnumber_pattern, String rev_pattern, String lotnumber_pattern) throws IllegalArgumentException;
    
    /**
     * Create the given DepartLot for a given DepartReport in the database. 
     * The DepartReport ID must not be null, the PartRevision ID must not be null
     * and the DepartReport ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given DepartLot.
     * @param depart_report The DepartReport to be assigned to the DepartLot.
     * @param part_revision The PartRevision to be assigned to the DepartLot.
     * @param depart_lot The DepartLot to be created.
     * @throws IllegalArgumentException If the DepartReport ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the DepartLot ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(DepartReport depart_report, PartRevision part_revision, DepartLot depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartLot in the database.
     * The DepartLot ID must not be null otherwise it will throw IllegalArgumentException.
     * @param depart_lot The DepartLot to be updated in the database.
     * @throws IllegalArgumentException If the DepartLot ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(DepartLot depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartLot from the database. After deleting,
     * the DAO will set the ID of the given DepartLot to null.
     * @param depart_lot The DepartLot to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(DepartLot depart_lot) throws DAOException;
    
}
