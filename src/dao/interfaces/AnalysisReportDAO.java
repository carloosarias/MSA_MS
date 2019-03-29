/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisType;
import model.Employee;
import model.Tank;

/**
 *
 * @author Pavilion Mini
 */
public interface AnalysisReportDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the AnalysisReport from the database matching the given ID, otherwise null.
     * @param id The ID of the AnalysisReport to be returned.
     * @return The AnalysisReport from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisReport find(Integer id) throws DAOException;
    
     /**
     * Returns the Tank from the database matching the given AnalysisReport ID, otherwise null.
     * AnalysisReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysis_report The AnalysisReport to get the Tank from.
     * @return The Tank from the database matching the given AnalysisReport ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Tank findTank(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException;   
    
    /**
     * Returns the AnalysisType from the database matching the given AnalysisReport ID, otherwise null.
     * AnalysisReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysis_report The AnalysisReport to get the AnalysisType from.
     * @return The AnalysisType from the database matching the given AnalysisReport ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisType findAnalysisType(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Employee from the database matching the given AnalysisReport ID, otherwise null.
     * AnalysisReport ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysis_report The AnalysisReport to get the Employee from.
     * @return The Employee from the database matching the given AnalysisReport ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisReport ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findEmployee(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all AnalysisReport from the database ordered by AnalysisReport ID. The list is never null and
     * is empty when the database does not contain any AnalysisReport.
     * @return A list of all AnalysisReport from the database ordered by AnalysisReport ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<AnalysisReport> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all AnalysisReport matching Tank ID from the database ordered by AnalysisReport ID. 
     * The Tank ID must not be null, otherwise it will throw IllegalArgumentException.
     * The list is never null and is empty when the database does not contain any AnalysisReport matching Tank ID.
     * @param tank The Tank ID to be searched for.
     * @param type The AnalysisType ID to be searched for.
     * @param start The start date of the range.
     * @param end the end date of the range.
     * @param tank_filter
     * @param analysistype_filter
     * @param date_filter
     * @param active
     * @return A list of all AnalysisReport matching Tank ID from the database ordered by AnalysisReport ID.
     * @throws IllegalArgumentException If Tank ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public List<AnalysisReport> list(Tank tank, AnalysisType type, Date start, Date end, boolean tank_filter, boolean analysistype_filter, boolean date_filter, boolean active) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given AnalysisReport in the database.
     * The AnalysisReport ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given AnalysisReport.
     * @param tank The Tank to be attached to this AnalysisReport.
     * @param analysis_type The AnalysisType to be attached to this AnalysisReport.
     * @param employee The Employee to be attached to this AnalysisReport.
     * @param analysis_report The AnalysisReport to be created.
     * @throws IllegalArgumentException If the AnalysisReport ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Tank tank, AnalysisType analysis_type, Employee employee, AnalysisReport analysis_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given AnalysisReport in the database. The AnalysisReport ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param analysis_report The AnalysisReport to be updated.
     * @throws IllegalArgumentException If the AnalysisReport ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given AnalysisReport from the database. After deleting, the DAO will set the ID of the given
     * AnalysisReport to null.
     * @param analysis_report The AnalysisReport to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(AnalysisReport analysis_report) throws DAOException;
}
