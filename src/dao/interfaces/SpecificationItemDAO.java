/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Metal;
import model.Quote;
import model.Specification;
import model.SpecificationItem;

/**
 *
 * @author Pavilion Mini
 */
public interface SpecificationItemDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns SpecificationItem from ID.
     * @param id
     * @return SpecificationItem
     * @throws DAOException
     */
    public SpecificationItem find(Integer id) throws DAOException;
    
    /**
     * Returns List from Specification.
     * @param specification
     * @return List(Of SpecificationItem)
     * @throws IllegalArgumentException
     * @throws DAOException
     */        
    public List<SpecificationItem> list(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
    * Returns List from Quote -> PartRevision -> Specification.
    * @param quote
    * @return List(Of SpecificationItem)
    * @throws IllegalArgumentException
    * @throws DAOException
    */
    public List<SpecificationItem> list(Quote quote) throws IllegalArgumentException, DAOException;
    
    /**
     * Create New SpecificationItem.
     * @param specification
     * @param metal
     * @param specification_item
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public void create(Specification specification, Metal metal, SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update SpecificationItem.
     * @param specification_item
     * @throws IllegalArgumentException
     * @throws DAOException
     */    
    public void update(SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Disable SpecificationItem.
     * @param specification_item
     * @throws DAOException 
     */
    public void delete(SpecificationItem specification_item) throws DAOException;
}
