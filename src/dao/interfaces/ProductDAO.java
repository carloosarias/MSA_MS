/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Product;
import model.ProductType;

/**
 *
 * @author Pavilion Mini
 */
public interface ProductDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Product from the database matching the given ID, otherwise null.
     * @param id The ID of the Product to be returned.
     * @return The Product from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Product find(Integer id) throws DAOException;
    
    public ProductType findType(Product product) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all Products from the database ordered by Product ID. The list is never null and
     * is empty when the database does not contain any Product.
     * @return A list of all Products from the database ordered by Product ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Product> list() throws DAOException;
    
    /**
     * Returns a list of all Products matching ProductType from the database ordered by Product ID. The ProductType ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any Product matching ProductType.
     * @param type The ProductType to be searched for.
     * @return A list of all Products from the database ordered by Product ID.
     * @throws IllegalArgumentException If the ProductType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<Product> list(ProductType type) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all Products that are active from the database ordered by Product ID. The list is never null and
     * is empty when the database does not contain any Product matching active.
     * @param active The boolean value to be searched for.
     * @return A list of all Products from the database ordered by Product ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Product> listActive(boolean active) throws DAOException;
    
    /**
     * Returns a list of all Products matching ProductType and active from the database ordered by Product ID. The ProductType ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any Product matching ProductType active.
     * @param type The ProductType to be searched for.
     * @param active The boolean value to be searched for.
     * @return A list of all Products matching active and type from the database ordered by Product ID.
     * @throws IllegalArgumentException If the ProductType ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<Product> listActive(ProductType type, boolean active) throws DAOException;
    
    /**
     * Create the given Product in the database. The ProductType ID must not be null and
     * the Product ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given Product.
     * @param type The type of product to be created.
     * @param product The Product to be created in the database.
     * @throws IllegalArgumentException if the ProductType ID is null.
     * @throws IllegalArgumentException If the Product ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(ProductType type, Product product) throws IllegalArgumentException, DAOException;

    /**
     * Update the given Product in the database. The Product ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param product The Product to be updated.
     * @throws IllegalArgumentException If the Product ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Product product) throws IllegalArgumentException, DAOException;

    /**
     * Delete the given Product from the database. After deleting, the DAO will set the ID of the given
     * Product to null.
     * @param product The Product to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Product product) throws DAOException;
}
