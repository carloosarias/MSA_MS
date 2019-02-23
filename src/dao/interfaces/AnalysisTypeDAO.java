/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.AnalysisType;

/**
 *
 * @author Pavilion Mini
 */
public interface AnalysisTypeDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the AnalysisType from the database matching the given ID, otherwise null.
     * @param id The ID of the AnalysisType to be returned.
     * @return The AnalysisType from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public AnalysisType find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all AnalysisType from the database ordered by AnalysisType ID. The list is never null and
     * is empty when the database does not contain any AnalysisType.
     * @return A list of all AnalysisType from the database ordered by AnalysisType ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<AnalysisType> list(boolean active) throws DAOException;
    
    /**
     * Create the given AnalysisType in the database.
     * The AnalysisType ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given AnalysisType.
     * @param analysis_type The AnalysisType to be created.
     * @throws IllegalArgumentException If the AnalysisType ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(AnalysisType analysis_type) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given AnalysisType in the database. The AnalysisType ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param analysis_type The AnalysisType to be updated.
     * @throws IllegalArgumentException If the AnalysisType ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(AnalysisType analysis_type) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given AnalysisType from the database. After deleting, the DAO will set the ID of the given
     * AnalysisType to null.
     * @param analysis_type The AnalysisType to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(AnalysisType analysis_type) throws DAOException;
}
