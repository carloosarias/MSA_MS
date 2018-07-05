/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingLotDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.IncomingItem;
import model.IncomingLot;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingLotDAOJDBC implements IncomingLotDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, status, comments FROM INCOMING_LOT WHERE id = ?";
    private static final String SQL_FIND_INCOMING_ITEM_BY_ID =
            "SELECT INCOMING_ITEM_ID FROM INCOMING_LOT WHERE id = ?";
    private static final String SQL_LIST_OF_INCOMING_ITEM_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, status, comments FROM INCOMING_LOT WHERE INCOMING_ITEM_ID = ? ORDER BY id";
    private static final String SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, status, comments FROM INCOMING_LOT WHERE INCOMING_ITEM_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_LOT (INCOMING_ITEM_ID, lot_number, quantity, box_quantity, status, comments) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_LOT SET lot_number = ?, quantity = ?, box_quantity = ?, status = ?, comments = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INCOMING_LOT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingLot DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingLot DAO for.
     */
    IncomingLotDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public IncomingLot find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingLot from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingLot from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private IncomingLot find(String sql, Object... values) throws DAOException {
        IncomingLot incoming_lot = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_lot = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return incoming_lot;
    }
    
    @Override
    public IncomingItem findIncomingItem(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if(incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        IncomingItem incoming_item = null;
        
        Object[] values = {
            incoming_lot.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_INCOMING_ITEM_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_item = daoFactory.getIncomingItemDAO().find(resultSet.getInt("INCOMING_ITEM_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return incoming_item;
    }

    @Override
    public List<IncomingLot> list(IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        if(incoming_item.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }    
        
        List<IncomingLot> incoming_lot = new ArrayList<>();
        
        Object[] values = {
            incoming_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INCOMING_ITEM_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_lot;
    }
    
    @Override
    public List<IncomingLot> list(String lot_number){
        
        List<IncomingLot> incoming_lot = new ArrayList<>();
        
        Object[] values = {
            lot_number
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_lot;
    }
    
    @Override
    public void create(IncomingItem incoming_item, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if (incoming_item.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        if(incoming_lot.getId() != null){
            throw new IllegalArgumentException("IncomingLot is already created, the IncomingLot ID is null.");
        }
        
        Object[] values = {
            incoming_item.getId(),
            incoming_lot.getLot_number(),
            incoming_lot.getQuantity(),
            incoming_lot.getBox_quantity(),
            incoming_lot.getStatus(),
            incoming_lot.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingLot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_lot.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating IncomingLot failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        
        if (incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        Object[] values = {
            incoming_lot.getLot_number(),
            incoming_lot.getQuantity(),
            incoming_lot.getBox_quantity(),
            incoming_lot.getStatus(),
            incoming_lot.getComments(),
            incoming_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating IncomingLot failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(IncomingLot incoming_lot) throws DAOException {
        Object[] values = {
            incoming_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting IncomingLot failed, no rows affected.");
            } else{
                incoming_lot.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    @Override
    public Integer getTotalQuantity(IncomingItem incoming_item){
        Integer total = 0;
        for(IncomingLot incoming_lot : list(incoming_item)){
            total += incoming_lot.getQuantity();
        }
        return total;
    }
    
    @Override
    public Integer getTotalBoxQuantity(IncomingItem incoming_item){
        Integer total = 0;
        for(IncomingLot incoming_lot : list(incoming_item)){
            total += incoming_lot.getBox_quantity();
        }
        return total;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an IncomingLot.
     * @param resultSet The ResultSet of which the current row is to be mapped to an IncomingLot.
     * @return The mapped IncomingLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static IncomingLot map(ResultSet resultSet) throws SQLException{
        IncomingLot incoming_lot = new IncomingLot();
        incoming_lot.setId(resultSet.getInt("id"));
        incoming_lot.setLot_number(resultSet.getString("lot_number"));
        incoming_lot.setQuantity(resultSet.getInt("quantity"));
        incoming_lot.setBox_quantity(resultSet.getInt("box_quantity"));
        incoming_lot.setStatus(resultSet.getString("status"));
        incoming_lot.setComments(resultSet.getString("comments"));
        return incoming_lot;
    }
}
