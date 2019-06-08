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
     * Create New DepartLot_1.
     * @param depart_lot
     * @throws IllegalArgumentException
     * @throws DAOException 
     */ 
    public void create(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update DepartLot_1.
     * @param depart_lot
     * @throws IllegalArgumentException
     * @throws DAOException
     */       
    public void update(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete DepartLot_1 if not locked.
     * @param depart_lot
     * @throws DAOException 
     */
    public void delete(DepartLot_1 depart_lot) throws DAOException;
}
