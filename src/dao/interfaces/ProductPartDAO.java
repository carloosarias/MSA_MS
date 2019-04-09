/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.ProductPart;

/**
 *
 * @author Pavilion Mini
 */
public interface ProductPartDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the ProductPart from the database matching the given ID, otherwise null.
     * @param id The ID of the ProductPart to be returned.
     * @return The ProductPart from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ProductPart find(Integer id) throws DAOException;
    
    /**
     * Returns the ProductPart from the database matching the given part_number, otherwise null. 
     * The Product ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part_number The part_number of the ProductPart.
     * @return The ProductPart from the database matching the given part_number, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ProductPart find(String part_number) throws DAOException;
    
    /**
     * Returns a list of all ProductParts from the database ordered by ProductPart ID. The list is never null and
     * is empty when the database does not contain any ProductPart.
     * @param pattern
     * @param partnumber_filter
     * @param active
     * @return A list of all ProductParts from the database ordered by ProductPart ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductPart> list(String pattern, boolean partnumber_filter, boolean active) throws DAOException;
    
    /**
     * Returns a list of all active ProductParts from the database ordered by ProductPart ID. The list is never null and
     * is empty when the database does not contain any ProductPart.
     * @return A list of all ProductParts from the database ordered by ProductPart ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductPart> listActive (boolean active) throws DAOException;
    
    /**
     * Create the given ProductPart in the database. The ProductPart ID must be null, 
     * otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ProductPart.
     * @param part The ProductPart to be created.
     * @throws IllegalArgumentException If the ProductPart ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(ProductPart part) throws IllegalArgumentException, DAOException;

    /**
     * Update the given ProductPart in the database. The ProductPart ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param part The ProductPart to be updated.
     * @throws IllegalArgumentException If the ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(ProductPart part) throws IllegalArgumentException, DAOException;

    /**
     * Delete the given ProductPart from the database. After deleting, the DAO will set the ID of the given
     * ProductPart to null.
     * @param part The ProductPart to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ProductPart part) throws DAOException;
}
