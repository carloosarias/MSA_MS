/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ProductTypeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ProductType;

/**
 *
 * @author Pavilion Mini
 */
public class ProductTypeDAOJDBC implements ProductTypeDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, active FROM PRODUCT_TYPE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT_TYPE ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, name, active FROM PRODUCT_TYPE WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO PRODUCT_TYPE (name, active) "
            + "VALUES (?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PRODUCT_TYPE SET name = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM PRODUCT_TYPE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProductType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ProductTypeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public ProductType find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ProductType from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProductType from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ProductType find(String sql, Object... values) throws DAOException {
        ProductType type = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                type = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return type;
    }
    
    @Override
    public List<ProductType> list() throws DAOException {
        List<ProductType> type = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                type.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return type;
    }

    @Override
    public List<ProductType> listActive(boolean active) throws DAOException {
        List<ProductType> type = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                type.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return type;
    }

    @Override
    public void create(ProductType type) throws IllegalArgumentException, DAOException {
        if(type.getId() != null){
            throw new IllegalArgumentException("ProductType is already created, the ProductType ID is not null.");
        }

        Object[] values = {
            type.getName(),
            type.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ProductType failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    type.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ProductType failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ProductType type) throws IllegalArgumentException, DAOException {
        if (type.getId() == null) {
            throw new IllegalArgumentException("ProductType is not created yet, the ProductType ID is null.");
        }
        
        Object[] values = {
            type.getName(),
            type.isActive(),
            type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ProductType failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ProductType type) throws DAOException {
        Object[] values = {
            type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ProductType failed, no rows affected.");
            } else{
                type.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an ProductType.
     * @param resultSet The ResultSet of which the current row is to be mapped to an ProductType.
     * @return The mapped ProductType from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ProductType map(ResultSet resultSet) throws SQLException{
        ProductType type = new ProductType();
        type.setId(resultSet.getInt("id"));
        type.setName(resultSet.getString("name"));
        type.setActive(resultSet.getBoolean("active"));
        return type;
    }
}
