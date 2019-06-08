/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Metal;

/**
 *
 * @author Pavilion Mini
 */
public interface MetalDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Return Metal From ID.
     * @param id
     * @return Metal
     * @throws DAOException
     */
    public Metal find(Integer id) throws DAOException;
    
    /**
     * Returns List.
     * @return List(Of Metal)
     * @throws DAOException
     */        
    public List<Metal> list() throws DAOException;
    
    /**
     * Create New Metal.
     * @param metal
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public void create(Metal metal) throws IllegalArgumentException, DAOException;
    
    /**
     * Update Metal.
     * @param metal
     * @throws IllegalArgumentException
     * @throws DAOException
     */      
    public void update(Metal metal) throws IllegalArgumentException, DAOException;
    
    /**
     * Disable Metal.
     * @param metal
     * @throws DAOException 
     */
    public void delete(Metal metal) throws DAOException;
}
