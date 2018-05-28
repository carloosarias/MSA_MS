/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ItemPartDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Item;
import model.ItemPart;
import model.Metal;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ItemPartDAOJDBC implements ItemPartDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, part_number, area, initial_weight, after_weight FROM ITEM_PART WHERE id = ?";
    private static final String SQL_FIND_BY_ITEM_ID = 
            "SELECT id, part_number, area, initial_weight, after_weight FROM ITEM_PART WHERE ITEM_ID = ?";
    private static final String SQL_FIND_ITEM_BY_ID = 
            "SELECT ITEM_ID FROM ITEM_PART WHERE id = ?";
    private static final String SQL_FIND_METAL_BY_ID = 
            "SELECT METAL_ID FROM ITEM_PART WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ItemPart DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ItemPart DAO for.
     */
    ItemPartDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public ItemPart find(Integer id) throws DAOException{
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public ItemPart find(Item item) throws DAOException {
        if (item.getId() == null) {
            throw new IllegalArgumentException("Item is not created yet, the Item ID is null.");
        }
        
        return find(SQL_FIND_BY_ITEM_ID, item.getId());
    }
    
    /**
     * Returns the ItemPart from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ItemPart from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ItemPart find(String sql, Object... values) throws DAOException {
        ItemPart part = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return part;
    }
    
    @Override
    public Item findItem(ItemPart part) throws DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("Part is not created yet, the Part ID is null.");
        }
        
        Item item = new Item();
        
        Object[] values = {
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_ITEM_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            if(resultSet.next()){
                item = daoFactory.getItemDAO().find(resultSet.getInt("ITEM_ID"));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return item;
    }

    @Override
    public Metal findMetal(ItemPart part) throws DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("Part is not created yet, the Part ID is null.");
        }
        
        Metal metal = new Metal();
        
        Object[] values = {
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_METAL_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            if(resultSet.next()){
                metal = daoFactory.getMetalDAO().find(resultSet.getInt("METAL_ID"));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return metal;
    }
    
    @Override
    public ItemPart list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItemPart list(Metal metal) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void create(Item item, Metal metal, ItemPart part) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Metal metal, ItemPart part) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void delete(ItemPart part) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Item.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Item.
     * @return The mapped Item from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ItemPart map(ResultSet resultSet) throws SQLException{
        ItemPart part = new ItemPart();
        part.setId(resultSet.getInt("id"));
        part.setPart_number(resultSet.getString("part_number"));
        part.setArea(resultSet.getDouble("area"));
        part.setInitial_weight(resultSet.getDouble("initial_weight"));
        part.setAfter_weight(resultSet.getDouble("after_weight"));
        return part;
    }

}
