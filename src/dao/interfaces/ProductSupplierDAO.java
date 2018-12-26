/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.Employee;
import model.Product;
import model.ProductSupplier;

/**
 *
 * @author Pavilion Mini
 */
public interface ProductSupplierDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the ProductSupplier from the database matching the given ID, otherwise null.
     * @param id The ID of the ProductSupplier to be returned.
     * @return The ProductSupplier from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public ProductSupplier find(Integer id) throws DAOException;
    
    /**
     * Returns the Product from the database matching the given ProductSupplier ID, otherwise null.
     * ProductSupplier ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_supplier The ProductSupplier to get the Product from.
     * @return The Product from the database matching the given ProductSupplier ID, otherwise null.
     * @throws IllegalArgumentException If ProductSupplier ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findProduct(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the Company from the database matching the given ProductSupplier ID, otherwise null.
     * ProductSupplier ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_supplier The ProductSupplier to get the Company from.
     * @return The Company from the database matching the given ProductSupplier ID, otherwise null.
     * @throws IllegalArgumentException If ProductSupplier ID is null.
     * @throws DAOException If something fails at database level.
     */
    public Employee findCompany(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all ProductSupplier from the database ordered by ProductSupplier ID. The list is never null and
     * is empty when the database does not contain any ProductSupplier.
     * @return A list of all ProductSupplier from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */        
    public List<ProductSupplier> list() throws DAOException;
    
    /**
     * Returns a list of all ProductSupplier matching active from the database ordered by ProductSupplier ID. The list is never null and
     * is empty when the database does not contain any ProductSupplier matching active.
     * @param active The active of the ProductSupplier to be returned.
     * @return A list of all ProductSupplier matching active from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductSupplier> list(boolean active) throws DAOException;
    
    /**
     * Returns a list of all ProductSupplier matching Product from the database ordered by ProductSupplier ID. The list is never null and
     * is empty when the database does not contain any ProductSupplier matching Product.
     * @param product The Product of the ProductSupplier to be returned.
     * @return A list of all ProductSupplier matching Product from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductSupplier> listOfProduct(Product product) throws DAOException;
    
    /**
     * Returns a list of all ProductSupplier matching Company from the database ordered by ProductSupplier ID. The list is never null and
     * is empty when the database does not contain any ProductSupplier matching Company.
     * @param company The Company of the ProductSupplier to be returned.
     * @return A list of all ProductSupplier matching Company from the database ordered by ProductSupplier ID.
     * @throws DAOException If something fails at database level.
     */
    public List<ProductSupplier> listOfCompany(Company company) throws DAOException;    
    
    /**
     * Create the given ProductSupplier in the database.
     * The ProductSupplier ID must be null, otherwise it will throw IllegalArgumentException.
     * After creating, the DAO will set the obtained ID in the given ProductSupplier.
     * @param product The Product to be assigned to this ProductSupplier.
     * @param company The Company to be assigned to this ProductSupplier.
     * @param product_supplier The ProductSupplier to be created.
     * @throws IllegalArgumentException If the ProductSupplier ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(Product product, Company company, ProductSupplier product_supplier) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given ProductSupplier in the database. The ProductSupplier ID must not be null, 
     * otherwise it will throw IllegalArgumentException.
     * @param product_supplier The ProductSupplier to be updated.
     * @throws IllegalArgumentException If the ProductSupplier ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given ProductSupplier from the database. After deleting, the DAO will set the ID of the given
     * ProductSupplier to null.
     * @param product_supplier The ProductSupplier to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(ProductSupplier product_supplier) throws DAOException;
}
