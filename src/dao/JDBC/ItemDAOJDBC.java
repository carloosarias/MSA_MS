/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.ItemTypeDAOJDBC.map;
import dao.interfaces.ItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Item;
import model.ItemType;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ItemDAOJDBC implements ItemDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, desc, unit_price FROM ITEM WHERE id = ?";
    private static final String SQL_FIND_BY_NAME =
            "SELECT id, name, desc, unit_price FROM ITEM WHERE name = ?";
    private static final String SQL_FIND_TYPE_BY_ID =
            "SELECT ITEM_TYPE_ID FROM ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, desc, unit_price FROM ITEM ORDER BY id";
    private static final String SQL_LIST_OF_TYPE_ORDER_BY_ID = 
            "SELECT id, name, desc, unit_price FROM ITEM WHERE ITEM_TYPE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO ITEM (ITEM_TYPE_ID, name, desc, unit_price) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ITEM SET ITEM_TYPE_ID = ?, name = ?, desc = ?, unit_price = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Item DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Item DAO for.
     */
    ItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Item find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    @Override
    public Item find(String name) throws DAOException {
        return find(SQL_FIND_BY_NAME, name);
    }
    
    /**
     * Returns the Item from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Item from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Item find(String sql, Object... values) throws DAOException {
        Item item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return item;
    }
    
    @Override
    public ItemType findType(Item item) throws DAOException {
        if (item.getId() == null) {
            throw new IllegalArgumentException("Item is not created yet, the Item ID is null.");
        }
        
        ItemType type = new ItemType();
        
        Object[] values = {
            item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_TYPE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            if(resultSet.next()){
                type = daoFactory.getItemTypeDAO().find(resultSet.getInt("ITEM_TYPE_ID"));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return type;
    }

    @Override
    public List<Item> list() throws DAOException {
        List<Item> items = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                items.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return items;
    }

    @Override
    public List<Item> list(ItemType type) throws IllegalArgumentException, DAOException {
        if (type.getId() == null) {
            throw new IllegalArgumentException("ItemType is not created yet, the ItemType ID is null.");
        }
        
        List<Item> items = new ArrayList<>();
        
        Object[] values = {
            type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                items.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return items;
    }

    @Override
    public void create(ItemType type, Item item) throws IllegalArgumentException, DAOException {
        if (type.getId() == null) {
            throw new IllegalArgumentException("ItemType is not created yet, the ItemType ID is null.");
        }
        if (item.getId() != null) {
            throw new IllegalArgumentException("Item is already created, the Item ID is not null.");
        }
        
        Object[] values = {
            type.getId(),
            item.getName(),
            item.getDesc(),
            item.getUnit_price()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, false, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Item failed, no rows affected.");
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ItemType type, Item item) throws IllegalArgumentException, DAOException {
       if (type.getId() == null) {
            throw new IllegalArgumentException("ItemType is not created yet, the ItemType ID is null.");
        }
        if (item.getId() == null) {
            throw new IllegalArgumentException("Item is not created yet, the Item ID is null.");
        }
        
        Object[] values = {
            type.getId(),
            item.getName(),
            item.getDesc(),
            item.getUnit_price(),
            item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Item failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Item item) throws DAOException {
        Object[] values = {
            item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Item failed, no rows affected.");
            } else{
                item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Item.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Item.
     * @return The mapped Item from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Item map(ResultSet resultSet) throws SQLException{
        Item item = new Item();
        item.setId(resultSet.getInt("id"));
        item.setName(resultSet.getString("name"));
        item.setDesc(resultSet.getString("desc"));
        item.setUnit_price(resultSet.getDouble("unit_price"));
        return item;
    }
}
