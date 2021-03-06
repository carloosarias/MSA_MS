/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ProductSupplierDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.Product;
import model.ProductSupplier;

/**
 *
 * @author Pavilion Mini
 */
public class ProductSupplierDAOJDBC implements ProductSupplierDAO{
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT PRODUCT_SUPPLIER.id, PRODUCT_SUPPLIER.serial_number, PRODUCT_SUPPLIER.unit_price, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.active, PRODUCT_SUPPLIER.PRODUCT_ID, PRODUCT_SUPPLIER.COMPANY_ID, "
            + "PRODUCT.description, PRODUCT.unit_measure, COMPANY.name"
            + "FROM PRODUCT_SUPPLIER "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "INNER JOIN COMPANY ON PRODUCT_SUPPLIER.COMPANY_ID = COMPANY.id "
            + "WHERE PRODUCT_SUPPLIER.id = ?";
    private static final String SQL_FIND_PRODUCT_BY_ID = 
            "SELECT PRODUCT_ID FROM PRODUCT_SUPPLIER WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM PRODUCT_SUPPLIER WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT PRODUCT_SUPPLIER.id, PRODUCT_SUPPLIER.serial_number, PRODUCT_SUPPLIER.unit_price, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.active, PRODUCT_SUPPLIER.PRODUCT_ID, PRODUCT_SUPPLIER.COMPANY_ID,"
            + "PRODUCT.description, PRODUCT.unit_measure, COMPANY.name "
            + "FROM PRODUCT_SUPPLIER "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "INNER JOIN COMPANY ON PRODUCT_SUPPLIER.COMPANY_ID = COMPANY.id "
            + "WHERE PRODUCT_SUPPLIER.active = ? ORDER BY PRODUCT_SUPPLIER.id";
    private static final String SQL_LIST_PRODUCT_ORDER_BY_ID = 
            "SELECT PRODUCT_SUPPLIER.id, PRODUCT_SUPPLIER.serial_number, PRODUCT_SUPPLIER.unit_price, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.active, PRODUCT_SUPPLIER.PRODUCT_ID, PRODUCT_SUPPLIER.COMPANY_ID,"
            + "PRODUCT.description, PRODUCT.unit_measure, COMPANY.name "
            + "FROM PRODUCT_SUPPLIER "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "INNER JOIN COMPANY ON PRODUCT_SUPPLIER.COMPANY_ID = COMPANY.id "
            + "WHERE PRODUCT_SUPPLIER.PRODUCT_ID = ? ORDER BY PRODUCT_SUPPLIER.id";
    private static final String SQL_LIST_COMPANY_ORDER_BY_ID = 
            "SELECT PRODUCT_SUPPLIER.id, PRODUCT_SUPPLIER.serial_number, PRODUCT_SUPPLIER.unit_price, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.active, PRODUCT_SUPPLIER.PRODUCT_ID, PRODUCT_SUPPLIER.COMPANY_ID,"
            + "PRODUCT.description, PRODUCT.unit_measure, COMPANY.name "
            + "FROM PRODUCT_SUPPLIER "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "INNER JOIN COMPANY ON PRODUCT_SUPPLIER.COMPANY_ID = COMPANY.id "
            + "WHERE PRODUCT_SUPPLIER.COMPANY_ID = ? ORDER BY PRODUCT_SUPPLIER.id";
    private static final String SQL_INSERT = 
            "INSERT INTO PRODUCT_SUPPLIER (PRODUCT_ID, COMPANY_ID, serial_number, unit_price, quantity, active) "
            + "VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PRODUCT_SUPPLIER SET serial_number = ?, unit_price = ?, quantity = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM PRODUCT_SUPPLIER WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProductSupplier DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ProductSupplier DAO for.
     */
    ProductSupplierDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ProductSupplier find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ProductSupplier from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProductSupplier from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ProductSupplier find(String sql, Object... values) throws DAOException {
        ProductSupplier product_supplier = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                product_supplier = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return product_supplier;
    }
    
    @Override
    public Product findProduct(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException {
        if(product_supplier.getId() == null) {
                throw new IllegalArgumentException("ProductSupplier is not created yet, the ProductSupplier ID is null.");
            }

            Product product = null;

            Object[] values = {
                product_supplier.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    product = daoFactory.getProductDAO().find(resultSet.getInt("PRODUCT_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return product;
    }
    
    @Override
    public Company findCompany(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException {
        if(product_supplier.getId() == null) {
                throw new IllegalArgumentException("ProductSupplier is not created yet, the ProductSupplier ID is null.");
            }

            Company company = null;

            Object[] values = {
                product_supplier.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    company = daoFactory.getCompanyDAO().find(resultSet.getInt("COMPANY_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return company;
    }
    
    @Override
    public List<ProductSupplier> list(boolean active) throws DAOException {
        List<ProductSupplier> productsupplier_list = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                productsupplier_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return productsupplier_list;
    }

    @Override
    public List<ProductSupplier> listOfProduct(Product product) throws DAOException {
        if(product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }    
        
        List<ProductSupplier> productsupplier_list = new ArrayList<>();
        
        Object[] values = {
            product.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_PRODUCT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                productsupplier_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return productsupplier_list;
    }

    @Override
    public List<ProductSupplier> listOfCompany(Company company) throws DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<ProductSupplier> productsupplier_list = new ArrayList<>();
        
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                productsupplier_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return productsupplier_list;
    }

    @Override
    public void create(Product product, Company company, ProductSupplier product_supplier) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        if(company.getId() == null){
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        if(product_supplier.getId() != null){
            throw new IllegalArgumentException("ProductSupplier is already created, the ProductSupplier ID is not null.");
        }
        
        Object[] values = {
            product.getId(),
            company.getId(),
            product_supplier.getSerial_number(),
            product_supplier.getUnit_price(),
            product_supplier.getQuantity(),
            product_supplier.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ProductSupplier failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product_supplier.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ProductSupplier failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ProductSupplier product_supplier) throws IllegalArgumentException, DAOException {
        if (product_supplier.getId() == null) {
            throw new IllegalArgumentException("ProductSupplier is not created yet, the ProductSupplier ID is null.");
        }
        
        Object[] values = {
            product_supplier.getSerial_number(),
            product_supplier.getUnit_price(),
            product_supplier.getQuantity(),
            product_supplier.isActive(),
            product_supplier.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ProductSupplier failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ProductSupplier product_supplier) throws DAOException {
        Object[] values = {
            product_supplier.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ProductSupplier failed, no rows affected.");
            } else{
                product_supplier.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an MantainanceReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped MantainanceReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ProductSupplier map(ResultSet resultSet) throws SQLException{
        ProductSupplier product_supplier = new ProductSupplier();
        product_supplier.setId(resultSet.getInt("PRODUCT_SUPPLIER.id"));
        product_supplier.setSerial_number(resultSet.getString("PRODUCT_SUPPLIER.serial_number"));
        product_supplier.setUnit_price(resultSet.getDouble("PRODUCT_SUPPLIER.unit_price"));
        product_supplier.setQuantity(resultSet.getDouble("PRODUCT_SUPPLIER.quantity"));
        product_supplier.setActive(resultSet.getBoolean("PRODUCT_SUPPLIER.active"));
        
        //INNER JOINS
        product_supplier.setProduct_id(resultSet.getInt("PRODUCT_SUPPLIER.PRODUCT_ID"));
        product_supplier.setCompany_id(resultSet.getInt("PRODUCT_SUPPLIER.COMPANY_ID"));
        product_supplier.setProduct_description(resultSet.getString("PRODUCT.description"));
        product_supplier.setProduct_unitmeasure(resultSet.getString("PRODUCT.unit_measure"));
        product_supplier.setCompany_name(resultSet.getString("COMPANY.name"));
        return product_supplier;
    }
}
