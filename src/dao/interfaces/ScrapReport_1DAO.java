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
import model.ScrapReport_1;

/**
 *
 * @author Pavilion Mini
 */
public interface ScrapReport_1DAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Return ScrapReport_1 From ID.
     * @param id
     * @return ScrapReport_1
     * @throws DAOException
     */
    public ScrapReport_1 find(Integer id) throws DAOException;
    
    /**
     * Returns List that match search parameters
     * @param incomingreport_id
     * @param start_date
     * @param end_date
     * @param company
     * @param part_number
     * @param rev
     * @param lot
     * @param packing
     * @param po
     * @param line
     * @return List(Of ScrapReport_1)
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public List<ScrapReport_1> search(Integer incomingreport_id, Date start_date, Date end_date, Company company, String part_number, String rev, 
        String lot, String packing, String po, String line) throws IllegalArgumentException;
    
    /**
     * Create New ScrapReport_1.
     * @param scrap_report
     * @throws IllegalArgumentException
     * @throws DAOException 
     */ 
    public void create(ScrapReport_1 scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update ScrapReport_1.
     * @param scrap_report
     * @throws IllegalArgumentException
     * @throws DAOException
     */       
    public void update(ScrapReport_1 scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete ScrapReport_1.
     * @param scrap_report
     * @throws DAOException 
     */
    public void delete(ScrapReport_1 scrap_report) throws DAOException;
}
