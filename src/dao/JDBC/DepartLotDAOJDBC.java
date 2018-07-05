/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartLotDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DepartItem;
import model.DepartLot;

/**
 *
 * @author Pavilion Mini
 */
public class DepartLotDAOJDBC implements DepartLotDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, process, comments FROM DEPART_LOT WHERE id = ?";
    private static final String SQL_FIND_DEPART_ITEM_BY_ID =
            "SELECT DEPART_ITEM_ID FROM DEPART_LOT WHERE id = ?";
    private static final String SQL_LIST_OF_DEPART_ITEM_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, process, comments FROM DEPART_LOT WHERE DEPART_ITEM_ID = ? ORDER BY id";
    private static final String SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, process, comments FROM DEPART_LOT WHERE DEPART_ITEM_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO DEPART_LOT (DEPART_ITEM_ID, lot_number, quantity, box_quantity, process, comments) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_LOT SET lot_number = ?, quantity = ?, box_quantity = ?, process = ?, comments = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM DEPART_LOT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a DepartLot DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartLot DAO for.
     */
    DepartLotDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public DepartLot find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the DepartLot from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The DepartLot from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private DepartLot find(String sql, Object... values) throws DAOException {
        DepartLot depart_lot = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_lot = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_lot;
    }
    
    @Override
    public DepartItem findDepartItem(DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        if(depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        DepartItem depart_item = null;
        
        Object[] values = {
            depart_lot.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_DEPART_ITEM_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_item = daoFactory.getDepartItemDAO().find(resultSet.getInt("DEPART_ITEMf_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return depart_item;
    }

    @Override
    public List<DepartLot> list(DepartItem depart_item) throws IllegalArgumentException, DAOException {
        if(depart_item.getId() == null) {
            throw new IllegalArgumentException("DepartItem is not created yet, the DepartItem ID is null.");
        }    
        
        List<DepartLot> depart_lot = new ArrayList<>();
        
        Object[] values = {
            depart_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DEPART_ITEM_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }
    
    @Override
    public List<DepartLot> list(String lot_number){
        
        List<DepartLot> incoming_lot = new ArrayList<>();
        
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
    public void create(DepartItem depart_item, DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        if (depart_item.getId() == null) {
            throw new IllegalArgumentException("DepartItem is not created yet, the DepartItem ID is null.");
        }
        
        if(depart_lot.getId() != null){
            throw new IllegalArgumentException("DepartLot is already created, the DepartLot ID is null.");
        }
        
        Object[] values = {
            depart_item.getId(),
            depart_lot.getLot_number(),
            depart_lot.getQuantity(),
            depart_lot.getBox_quantity(),
            depart_lot.getProcess(),
            depart_lot.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating DepartLot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depart_lot.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating DepartLot failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        
        if (depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        Object[] values = {
            depart_lot.getLot_number(),
            depart_lot.getQuantity(),
            depart_lot.getBox_quantity(),
            depart_lot.getProcess(),
            depart_lot.getComments(),
            depart_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating DepartLot failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(DepartLot depart_lot) throws DAOException {
        Object[] values = {
            depart_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting DepartLot failed, no rows affected.");
            } else{
                depart_lot.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    @Override
    public Integer getTotalQuantity(DepartItem depart_item){
        Integer total = 0;
        for(DepartLot depart_lot : list(depart_item)){
            total += depart_lot.getQuantity();
        }
        return total;
    }
    
    @Override
    public Integer getTotalBoxQuantity(DepartItem depart_item){
        Integer total = 0;
        for(DepartLot depart_lot : list(depart_item)){
            total += depart_lot.getBox_quantity();
        }
        return total;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartLot.
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartLot.
     * @return The mapped DepartLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartLot map(ResultSet resultSet) throws SQLException{
        DepartLot depart_lot = new DepartLot();
        depart_lot.setId(resultSet.getInt("id"));
        depart_lot.setLot_number(resultSet.getString("lot_number"));
        depart_lot.setQuantity(resultSet.getInt("quantity"));
        depart_lot.setBox_quantity(resultSet.getInt("box_quantity"));
        depart_lot.setProcess(resultSet.getString("process"));
        depart_lot.setComments(resultSet.getString("comments"));
        return depart_lot;
    }    
}
