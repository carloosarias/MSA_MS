/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ItemTypeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ItemType;

/**
 *
 * @author Pavilion Mini
 */
public class ItemTypeDAOJDBC implements ItemTypeDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name FROM ITEM_TYPE WHERE id = ?";
    private static final String SQL_FIND_BY_NAME =
            "SELECT id, name FROM ITEM_TYPE WHERE name = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name FROM ITEM_TYPE ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO ITEM_TYPE (name) "
            + "VALUES (?)";
    private static final String SQL_UPDATE = 
            "UPDATE ITEM_TYPE SET name = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ITEM_TYPE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ItemType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ItemType DAO for.
     */
    ItemTypeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public ItemType find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public ItemType find(String name) throws DAOException {
        return find(SQL_FIND_BY_NAME, name);
    }
    
    /**
     * Returns the ItemType from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ItemType from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ItemType find(String sql, Object... values) throws DAOException {
        ItemType type = null;

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
    public List<ItemType> list() throws DAOException {
        List<ItemType> type = new ArrayList<>();
        
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
    public void create(ItemType type) throws IllegalArgumentException, DAOException {
        if(type.getId() != null){
            throw new IllegalArgumentException("ItemType is already created, the ItemType ID is not null.");
        }

        Object[] values = {
            type.getName()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ItemType failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    type.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ItemType failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(ItemType type) throws IllegalArgumentException, DAOException {
        if (type.getId() == null) {
            throw new IllegalArgumentException("ItemType is not created yet, the ItemType ID is null.");
        }
        
        Object[] values = {
            type.getName(),
            type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ItemType failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ItemType type) throws DAOException {
        Object[] values = {
            type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ItemType failed, no rows affected.");
            } else{
                type.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an ItemType.
     * @param resultSet The ResultSet of which the current row is to be mapped to an ItemType.
     * @return The mapped ItemType from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ItemType map(ResultSet resultSet) throws SQLException{
        ItemType type = new ItemType();
        type.setId(resultSet.getInt("id"));
        type.setName(resultSet.getString("name"));
        return type;
    }
}
