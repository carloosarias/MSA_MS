/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Tank;

/**
 *
 * @author Pavilion Mini
 */
public interface TankDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns Tank from ID.
     * @param id
     * @return Tank
     * @throws DAOException
     */
    public Tank find(Integer id) throws DAOException;
    
    /**
     * Returns List.
     * @return List(Of Tank)
     * @throws DAOException
     */           
    public List<Tank> list() throws DAOException;
    
    /**
     * Returns List that match search parameters
     * @param tank_name
     * @param description
     * @return List(Of Specification)
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public List<Tank> search(String tank_name, String description) throws DAOException;
    
    /**
     * Create New Tank.
     * @param tank
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public void create(Tank tank) throws IllegalArgumentException, DAOException;
    
    /**
     * Update Tank.
     * @param tank
     * @throws IllegalArgumentException
     * @throws DAOException
     */      
    public void update(Tank tank) throws IllegalArgumentException, DAOException;
    
    /**
     * Disable Tank.
     * @param tank
     * @throws DAOException 
     */
    public void delete(Tank tank) throws DAOException;
}
