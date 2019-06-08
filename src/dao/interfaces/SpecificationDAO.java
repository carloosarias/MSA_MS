/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public interface SpecificationDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns Specification from ID.
     * @param id
     * @return Specification
     * @throws DAOException
     */
    public Specification find(Integer id) throws DAOException;
    
    /**
     * Returns List.
     * @return List(Of Specification)
     * @throws DAOException
     */         
    public List<Specification> list() throws DAOException;
    
    /**
     * Returns List that match search parameters
     * @param specification_number
     * @param specification_name
     * @param process
     * @return List(Of Specification)
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public List<Specification> search(String specification_number, String specification_name, String process) throws DAOException;
    
    /**
     * Create New Specification.
     * @param specification
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public void create(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
     * Update Specification.
     * @param specification
     * @throws IllegalArgumentException
     * @throws DAOException
     */      
    public void update(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
     * Disable Specification.
     * @param specification
     * @throws DAOException 
     */
    public void delete(Specification specification) throws DAOException;
}
