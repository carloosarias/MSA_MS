/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Metal;
import model.PartRevision;
import model.ProductPart;
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
     * Returns the SpecificationItem from the database matching the given ID, otherwise null.
     * @param id The ID of the SpecificationItem to be returned.
     * @return The SpecificationItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public SpecificationItem find(Integer id) throws DAOException;
    
    /**
     * Returns the Specification from the database matching the given SpecificationItem, otherwise null.
     * The SpecificationItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param specification_item The SpecificationItem to be searched for.
     * @return The Specification from the database matching the given SpecificationItem, otherwise null
     * @throws IllegalArgumentException If the SpecificationItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public Specification findSpecification(SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Metal from the database matching the given SpecificationItem, otherwise null.
     * The SpecificationItem ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param specification_item The SpecificationItem to be searched for.
     * @return The Metal from the database matching the given SpecificationItem, otherwise null
     * @throws IllegalArgumentException If the SpecificationItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public Metal findMetal(SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all SpecificationItem matching Specification ID from the database ordered by SpecificationItem ID. 
     * The list is never null and is empty when the database does not contain any SpecificationItem matching Specification ID.
     * @param specification the Specification to be searched for.
     * @param active
     * @return A list of all Specification matching Specification ID from the database ordered by SpecificationItem ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<SpecificationItem> list(Specification specification, boolean active) throws IllegalArgumentException, DAOException;
    public List<SpecificationItem> list(Quote quote, boolean active) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given Specification in the database. The Specification ID must not be null, 
     * the Metal ID must not be null and The SpecificationItem ID must be null 
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given SpecificationItem.
     * @param specification The Specification ID to be assigned to this SpecificationItem.
     * @param metal The Metal ID to be assigned to this SpecificationItem.
     * @param specification_item The Specification to be created.
     * @throws IllegalArgumentException If the Specification ID is null.
     * @throws IllegalArgumentException If the Metal ID is null.
     * @throws IllegalArgumentException If the SpecificationItem ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Specification specification, Metal metal, SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given SpecificationItem in the database. The SpecificationItem ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param specification_item The SpecificationItem to be updated.
     * @throws IllegalArgumentException If the SpecificationItem ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(SpecificationItem specification_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given SpecificationItem from the database. After deleting, the DAO will set the ID of the given
     * SpecificationItem to null.
     * @param specification_item The SpecificationItem to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(SpecificationItem specification_item) throws DAOException;
}
