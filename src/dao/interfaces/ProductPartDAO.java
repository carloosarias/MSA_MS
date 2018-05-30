/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Product;
import model.ProductPart;
import model.Specification;

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
     * Returns the ProductPart from the database matching the given Product and rev, otherwise null. 
     * The Product ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product The product of the ProductPart.
     * @param rev The rev of the ProductPart.
     * @return The ProductPart from the database matching the given Product and rev, otherwise null.
     * @throws IllegalArgumentException If the Product ID is null.
     * @throws DAOException If something fails at database level.
     */
    public ProductPart find(Product product, String rev) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Product from the database matching the given ProductPart, otherwise null.
     * The ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part The ProductPart to be searched for.
     * @return The Product from the database matching the given ProductPart, otherwise null
     * @throws IllegalArgumentException If the ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Product findProduct(ProductPart part) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Specification from the database matching the given ProductPart, otherwise null.
     * The ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param part The ProductPart to be searched for.
     * @return The Specification from the database matching the given ProductPart, otherwise null
     * @throws IllegalArgumentException If the ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Specification findSpecification(ProductPart part) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProductParts from the database ordered by ProductPart ID. The list is never null and
     * is empty when the database does not contain any ProductPart.
     * @return A list of all ProductParts from the database ordered by ProductPart ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductPart> list() throws DAOException;
    
    /**
     * Returns a list of all ProductParts matching Product from the database ordered by Product ID. The Product ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any ProductPart matching Product.
     * @param product The ProductType to be searched for.
     * @return A list of all ProductParts matching Product from the database ordered by ProductPart ID.
     * @throws IllegalArgumentException If the Product ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductPart> list(Product product) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProductParts matching Specification from the database ordered by ProductPart ID. The Specification ID must not be null, otherwise it will throw
     * IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any ProductPart matching Specification1.
     * @param specification The Specification to be searched for.
     * @return A list of all ProductParts matching Specification from the database ordered by ProductPart ID.
     * @throws IllegalArgumentException If the Specification ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductPart> list(Specification specification) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given ProductPart in the database. The Product ID must not be null, the Specification ID must not be null and
     * the ProductPart ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ProductPart.
     * @param product The Product to be assigned to this ProductPart.
     * @param specification The Specification to be assigned to this ProductPart
     * @param part The ProductPart to be created.
     * @throws IllegalArgumentException if the Product ID is null.
     * @throws IllegalArgumentException if the Specification ID is null.
     * @throws IllegalArgumentException If the ProductPart ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Product product, Specification specification, ProductPart part) throws IllegalArgumentException, DAOException;

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
