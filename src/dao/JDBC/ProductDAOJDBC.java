/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.ProductTypeDAOJDBC.map;
import dao.interfaces.ProductDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.ProductType;

/**
 *
 * @author Pavilion Mini
 */
public class ProductDAOJDBC implements ProductDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, active FROM PRODUCT WHERE id = ?";
    private static final String SQL_FIND_TYPE_BY_ID = 
            "SELECT TYPE_ID FROM PRODUCT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT ORDER BY id";
    private static final String SQL_LIST_OF_TYPE_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT WHERE TYPE_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT WHERE active = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_TYPE_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT WHERE TYPE_ID = ? and active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO PRODUCT (TYPE_ID, name, active) "
            + "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PRODUCT SET name = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM PRODUCT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProductType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ProductDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public Product find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public ProductType find(Product product) throws IllegalArgumentException, DAOException{
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        ProductType type = null;
        
        Object[] values = {
            product.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_TYPE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                type = daoFactory.getProductTypeDAO().find(resultSet.getInt("TYPE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return type;

    }
    
    /**
     * Returns the Product from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Product from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Product find(String sql, Object... values) throws DAOException {
        Product product = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                product = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return product;
    }
    
    @Override
    public List<Product> list() throws DAOException {
        List<Product> product = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public List<Product> list(ProductType type) throws IllegalArgumentException, DAOException {
        if(type.getId() == null){
            throw new IllegalArgumentException("ProductType is not created yet, the ProductType ID is null.");
        }
        List<Product> product = new ArrayList<>();
        
        Object[] values = {
            type
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public List<Product> listActive(boolean active) throws DAOException {
        List<Product> product = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public List<Product> listActive(ProductType type, boolean active) throws DAOException {
        if(type.getId() == null){
            throw new IllegalArgumentException("ProductType is not created yet, the ProductType ID is null.");
        }
        List<Product> product = new ArrayList<>();
        
        Object[] values = {
            type,
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public void create(ProductType type, Product product) throws IllegalArgumentException, DAOException {
        if (type.getId() == null) {
            throw new IllegalArgumentException("ProductType is not created yet, the ProductType ID is null.");
        }
        if(product.getId() != null){
            throw new IllegalArgumentException("Product is already created, the Product ID is not null.");
        }
        Object[] values = {
            type.getId(),
            product.getName(),
            product.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Product failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Product failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Product product) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        Object[] values = {
            product.getName(),
            product.isActive(),
            product.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Product failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Product product) throws DAOException {
        Object[] values = {
            product.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ProductType failed, no rows affected.");
            } else{
                product.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Product.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Product.
     * @return The mapped Product from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Product map(ResultSet resultSet) throws SQLException{
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setActive(resultSet.getBoolean("active"));
        return product;
    }
}
