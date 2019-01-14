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
            "SELECT id, unit_price, quantity, active, COMPANY_ID, PRODUCT_ID FROM PRODUCT_SUPPLIER WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, unit_price, quantity, active, COMPANY_ID, PRODUCT_ID FROM PRODUCT_SUPPLIER WHERE active = ? ORDER BY id";
    private static final String SQL_LIST_PRODUCT_ORDER_BY_ID = 
            "SELECT id, unit_price, quantity, active, COMPANY_ID, PRODUCT_ID FROM PRODUCT_SUPPLIER WHERE PRODUCT_ID = ? ORDER BY id";
    private static final String SQL_LIST_COMPANY_ORDER_BY_ID = 
            "SELECT id, unit_price, quantity, active, COMPANY_ID, PRODUCT_ID FROM PRODUCT_SUPPLIER WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO PRODUCT_SUPPLIER (PRODUCT_ID, COMPANY_ID, unit_price, quantity, active) "
            + "VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PRODUCT_SUPPLIER SET unit_price = ?, quantity = ?, active = ? WHERE id = ?";
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
            product_supplier.getQuantity(),
            product_supplier.getUnit_price(),
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
            product_supplier.getQuantity(),
            product_supplier.getUnit_price(),
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
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        ProductSupplier product_supplier = new ProductSupplier();
        product_supplier.setId(resultSet.getInt("id"));
        product_supplier.setUnit_price(resultSet.getDouble("unit_price"));
        product_supplier.setQuantity(resultSet.getDouble("quantity"));
        product_supplier.setActive(resultSet.getBoolean("active"));
        product_supplier.setCompany(msabase.getCompanyDAO().find(resultSet.getInt("COMPANY_ID")));
        product_supplier.setProduct(msabase.getProductDAO().find(resultSet.getInt("PRODUCT_ID")));
        return product_supplier;
    }
}
