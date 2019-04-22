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
import model.Employee;
import model.PartRevision;
import model.ProductPart;
import model.ScrapReport;

/**
 *
 * @author Pavilion Mini
 */
public interface ScrapReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ScrapReport from the database matching the given ID, otherwise null.
     * @param id The ID of the ScrapReport to be returned.
     * @return The ScrapReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ScrapReport find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all ScrapReport from the database ordered by ScrapReport ID. The list is never null and
     * is empty when the database does not contain any ScrapReport.
     * @return A list of all ScrapReport from the database ordered by ScrapReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<ScrapReport> list(Company company, String partnumber_pattern, String ponumber_pattern, Date start_date, Date end_date) throws DAOException;
    
    /**
     * Create the given ScrapReport in the database.
     * The Employee ID must not be null, The PartRevision ID must not be null,
     * and the ScrapReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ScrapReport.
     * @param scrap_report The ScrapReport to be created.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws IllegalArgumentException If the ScrapReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(ScrapReport scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ScrapReport in the database. The ScrapReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param scrap_report The ScrapReport to be updated.
     * @throws IllegalArgumentException If the ScrapReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(ScrapReport scrap_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ScrapReport from the database. After deleting, the DAO will set the ID of the given
     * ScrapReport to null.
     * @param scrap_report The ScrapReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ScrapReport scrap_report) throws DAOException;
}
