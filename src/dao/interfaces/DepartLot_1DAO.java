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
import model.DepartLot_1;
import model.DepartReport_1;

/**
 *
 * @author Pavilion Mini
 */
public interface DepartLot_1DAO  {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Return DepartLot_1 From ID.
     * @param id
     * @return DepartLot_1
     * @throws DAOException
     */
    public DepartLot_1 find(Integer id) throws DAOException;
    
    /**
     * Returns List From DepartReport_1
     * @param depart_report
     * @return List(Of DepartLot_1)
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public List<DepartLot_1> list(DepartReport_1 depart_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns List that have quantity available > 0 and match search parameters
     * @param departreport_id
     * @param start_date
     * @param end_date
     * @param company
     * @param part_number
     * @param rev
     * @param lot
     * @param packing
     * @param po
     * @param line
     * @return List(Of DepartLot_1)
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public List<DepartLot_1> searchAva(Integer departreport_id, Date start_date, Date end_date, Company company, String part_number, String rev, 
            String lot, String packing, String po, String line) throws IllegalArgumentException;
    
    /**
     * Create the given DepartLot_1 in the database. 
     * and the DepartLot_1 ID must be null, otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given DepartLot_1.
     * @param depart_lot The DepartLot_1 to be created.
     * @throws IllegalArgumentException If the DepartLot_1 ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartLot_1 in the database.
     * The DepartLot_1 ID must not be null otherwise it will throw IllegalArgumentException.
     * @param depart_lot The DepartLot_1 to be updated in the database.
     * @throws IllegalArgumentException If the DepartLot_1 ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartLot_1 from the database. After deleting,
     * the DAO will set the ID of the given DepartLot_1 to null.
     * @param depart_lot The DepartLot_1 to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(DepartLot_1 depart_lot) throws DAOException;
}
