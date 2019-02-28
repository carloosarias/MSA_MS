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
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public interface PartRevisionDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the PartRevision from the database matching the given ID, otherwise null.
     * @param id The ID of the PartRevision to be returned.
     * @return The PartRevision from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public PartRevision find(Integer id) throws DAOException;
    
    /**
     * Returns the PartRevision from the database matching the given ProductPart ID and rev, otherwise null. ProductPart ID must not be
     * null otherwise it will throw IllegalArgumentException..
     * @param part The ProductPart of the PartRevision to be returned.
     * @param rev The rev of the PartRevision to be returned.
     * @return The PartRevision from the database matching the given ProductPart ID and rev, otherwise null.
     * @throws IllegalArgumentException If ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */
    public PartRevision find(ProductPart part, String rev) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the ProductPart from the database matching the given PartRevision, otherwise null.
     * The PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be searched for.
     * @return The ProductPart from the database matching the given PartRevision, otherwise null
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public ProductPart findProductPart(PartRevision part_revision) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Specification from the database matching the given PartRevision, otherwise null.
     * The PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be searched for.
     * @return The Specification from the database matching the given PartRevision, otherwise null
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public Specification findSpecification(PartRevision part_revision) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Metal from the database matching the given PartRevision, otherwise null.
     * The PartRevision ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be searched for.
     * @return The Metal from the database matching the given PartRevision, otherwise null
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public Metal findMetal(PartRevision part_revision) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all PartRevisions from the database ordered by PartRevision ID. The list is never null and
     * is empty when the database does not contain any PartRevision.
     * @return A list of all PartRevisions from the database ordered by PartRevision ID.
     * @throws DAOException If something fails at database level.
     */    
    //public List<PartRevision> list() throws DAOException;
    
    /**
     * Returns a list of all active PartRevisions from the database ordered by PartRevision ID. The list is never null and
     * is empty when the database does not contain any PartRevision.
     * @param active The active to be searched for
     * @return A list of all active PartRevisions from the database ordered by PartRevision ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<PartRevision> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all PartRevisions matching ProductPart ID from the database ordered by PartRevision ID. The ProductPart ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any PartRevision matching ProductPart.
     * @param product_part The ProductPart to be searched for.
     * @param active
     * @return A list of all PartRevisions matching ProductPart from the database ordered by PartRevision ID.
     * @throws IllegalArgumentException If the ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<PartRevision> list(ProductPart product_part, boolean active) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all PartRevisions matching Specification ID from the database ordered by PartRevision ID. The Specification ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any PartRevision matching Specification.
     * @param specification The specification to be searched for.
     * @return A list of all PartRevisions matching Specification from the database ordered by PartRevision ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<PartRevision> listOfSpecification(Specification specification, boolean active) throws DAOException;
    
    /**
     * Create the given PartRevision in the database. The ProductPart ID must 
     * not be null and the PartRevision ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given PartRevision.
     * @param part The ProductPart to be assigned to this PartRevision.
     * @param specification The Specification to be assigned to this PartRevision.
     * @param metal The Metal to be assigned to this PartRevision.
     * @param part_revision The PartRevision to be created.
     * @throws IllegalArgumentException if the ProductPart ID is null.
     * @throws IllegalArgumentException If the PartRevision ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(ProductPart part, Specification specification, Metal metal, PartRevision part_revision) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given PartRevision in the database. The PartRevision ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param part_revision The PartRevision to be updated.
     * @throws IllegalArgumentException If the PartRevision ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(PartRevision part_revision) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given PartRevision from the database. After deleting, the DAO will set the ID of the given
     * PartRevision to null.
     * @param part_revision The PartRevision to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(PartRevision part_revision) throws DAOException;
    
}