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
import model.IncomingReport_1;

/**
 *
 * @author Pavilion Mini
 */
public interface IncomingReport_1DAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the DepartLot from the database matching the given ID, otherwise null.
     * @param id The ID of the DepartLot to be returned.
     * @return The DepartLot from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public IncomingReport_1 find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all IncomingReport_1 matching a given lot_number from the database ordered by IncomingReport_1 ID.
     * The list is never null and is empty when the database does not contain any IncomingReport_1 matching.
     * @param start_date
     * @param end_date
     * @param company
     * @param part_number
     * @param rev
     * @param packing
     * @param po
     * @param line
     * @param lot
     * @return A list of all DepartLot matching lot_number from the database ordered by DepartLot ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<IncomingReport_1> list(Date start_date, Date end_date, Company company, String part_number, String rev, 
        String lot, String packing, String po, String line) throws IllegalArgumentException;
    
    /**
     * Create the given IncomingReport_1 in the database. 
     * and the IncomingReport_1 ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given IncomingReport_1.
     * @param incoming_report The IncomingReport_1 to be created.
     * @throws IllegalArgumentException If the IncomingReport_1 ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(IncomingReport_1 incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given IncomingReport_1 in the database.
     * The IncomingReport_1 ID must not be null otherwise it will throw IllegalArgumentException.
     * @param incoming_report The IncomingReport_1 to be updated in the database.
     * @throws IllegalArgumentException If the IncomingReport_1 ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(IncomingReport_1 incoming_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given IncomingReport_1 from the database. After deleting,
     * the DAO will set the ID of the given IncomingReport_1 to null.
     * @param incoming_report The IncomingReport_1 to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(IncomingReport_1 incoming_report) throws DAOException;
    
}
