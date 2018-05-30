/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.ProductType;

/**
 *
 * @author Pavilion Mini
 */
public interface ProductTypeDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ProductType from the database matching the given ID, otherwise null.
     * @param id The ID of the ProductPart to be returned.
     * @return The ProductType from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ProductType find(Integer id) throws DAOException;
    
    /**
     * Returns a list of all ProductTypes from the database ordered by ProductType ID. The list is never null and
     * is empty when the database does not contain any ProductType.
     * @return A list of all ProductTypes from the database ordered by ProductType ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductType> list() throws DAOException;
    
    /**
     * Returns a list of all ProductTypes from the database matching active ordered by ProductType ID. The list is never null and
     * is empty when the database does not contain any ProductType matching active.
     * @param active The active to be searched for.
     * @return A list of all ProductTypes matching active from the database ordered by ProductType ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductType> listActive(boolean active) throws DAOException;
    
    
    /**
     * Create the given ProductType in the database. The ProductType ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ProductType.
     * @param type The ProductType to be created.
     * @throws IllegalArgumentException If the ProductType ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(ProductType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ProductType in the database. The ProductType ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param type The ProductType to be updated.
     * @throws IllegalArgumentException If the ProductType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(ProductType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ProductType from the database. After deleting, the DAO will set the ID of the given
     * ProductType to null.
     * @param type The ProductType to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ProductType type) throws DAOException;
}
