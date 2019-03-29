/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisReportVar;
import model.AnalysisType;
import model.AnalysisTypeVar;

/**
 *
 * @author Pavilion Mini
 */
public interface AnalysisReportVarDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the AnalysisReportVar from the database matching the given ID, otherwise null.
     * @param id The ID of the AnalysisReportVar to be returned.
     * @return The AnalysisReportVar from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisReportVar find(Integer id) throws DAOException;
    
    /**
     * Returns the AnalysisTypeVar from the database matching the given AnalysisReportVar ID, otherwise null.
     * AnalysisReportVar ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysisreport_var The AnalysisReportVar to get the AnalysisTypeVar from.
     * @return The AnalysisTypeVar from the database matching the given AnalysisReportVar ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisReportVar ID is null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisTypeVar findAnalysisTypeVar(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the AnalysisReport from the database matching the given AnalysisReportVar ID, otherwise null.
     * AnalysisReportVar ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysisreport_var The AnalysisReportVar to get the AnalysisReport from.
     * @return The AnalysisReport from the database matching the given AnalysisReportVar ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisReportVar ID is null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisReport findAnalysisReport(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all AnalysisReportVar from the database matching AnalysisReport ordered by AnalysisReportVar ID. The list is never null and
     * is empty when the database does not contain any AnalysisReportVar matching AnalysisReport.
     * AnalysisReport ID must not be null, otherwise will throw IllegalArgumentException.
     * @param analysis_report AnalysisReport to get the AnalysisReportVar from
     * @return A list of all AnalysisReportVar from the database ordered by AnalysisReport ID.
     * @throws IllegalArgumentException If AnalysisReport ID is null.
     * @throws DAOException If something fails at database level.
     */        
    public List<AnalysisReportVar> list(AnalysisReport analysis_report) throws DAOException;
    
    /**
     * Create the given AnalysisReportVar in the database.
     * The AnalysisReportVar ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given AnalysisTypeVar.
     * @param analysistype_var The AnalysisTypeVar for this AnalysisTypeVar
     * @param analysis_report The AnalysisReport for this AnalysisReportVar
     * @param analysisreport_var The AnalysisReportVar to be created.
     * @throws IllegalArgumentException If the AnalysisReportVar ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(AnalysisTypeVar analysistype_var, AnalysisReport analysis_report, AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given AnalysisReportVar in the database. The AnalysisReportVar ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param analysisreport_var The AnalysisReportVar to be updated.
     * @throws IllegalArgumentException If the AnalysisReportVar ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given AnalysisReportVar from the database. After deleting, the DAO will set the ID of the given
     * AnalysisReportVar to null.
     * @param analysisreport_var The AnalysisReportVar to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(AnalysisReportVar analysisreport_var) throws DAOException;
}
