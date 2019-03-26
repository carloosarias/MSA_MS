/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.AnalysisType;
import model.AnalysisTypeVar;

/**
 *
 * @author Pavilion Mini
 */
public interface AnalysisTypeVarDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the AnalysisTypeVar from the database matching the given ID, otherwise null.
     * @param id The ID of the AnalysisTypeVar to be returned.
     * @return The AnalysisType from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisTypeVar find(Integer id) throws DAOException;
    
    /**
     * Returns the AnalysisType from the database matching the given AnalysisTypeVar ID, otherwise null.
     * AnalysisTypeVar ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param analysistype_var The AnalysisTypeVar to get the AnalysisType from.
     * @return The AnalysisType from the database matching the given AnalysisTypeVar ID, otherwise null.
     * @throws IllegalArgumentException If AnalysisTypeVar ID is null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisType findAnalysisType(AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all AnalysisTypeVar from the database matching AnalysisType and active ordered by AnalysisTypeVar ID. The list is never null and
     * is empty when the database does not contain any AnalysisTypeVar matching AnalysisType and active.
     * AnalysisType ID must not be null, otherwise will throw IllegalArgumentException.
     * @param analysis_type AnalysisType to get the AnalysisTypeVar
     * @param active active status
     * @return A list of all AnalysisTypeVar from the database ordered by AnalysisTypeVar ID.
     * @throws IllegalArgumentException If AnalysisType ID is null.
     * @throws DAOException If something fails at database level.
     */        
    public List<AnalysisTypeVar> list(AnalysisType analysis_type, boolean active) throws DAOException;
    
    /**
     * Create the given AnalysisTypeVar in the database.
     * The AnalysisTypeVar ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given AnalysisTypeVar.
     * @param analysis_type The AnalysisType for this AnalysisTypeVar
     * @param analysistype_var The AnalysisTypeVar to be created.
     * @throws IllegalArgumentException If the AnalysisTypeVar ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(AnalysisType analysis_type, AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given AnalysisTypeVar in the database. The AnalysisTypeVar ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param analysistype_var The AnalysisTypeVar to be updated.
     * @throws IllegalArgumentException If the AnalysisTypeVar ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given AnalysisTypeVar from the database. After deleting, the DAO will set the ID of the given
     * AnalysisTypeVar to null.
     * @param analysistype_var The AnalysisTypeVar to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(AnalysisTypeVar analysistype_var) throws DAOException;
}
