/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.TankDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Tank;

/**
 *
 * @author Pavilion Mini
 */
public class TankDAOJDBC implements TankDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, tank_name, description, volume, active FROM TANK WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, tank_name, description, volume, active FROM TANK WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO TANK (tank_name, description, volume, active) "
            + "VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = 
            "UPDATE TANK SET tank_name = ?, description = ?, volume = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM TANK WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Tank DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Container DAO for.
     */
    TankDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Tank find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Tank from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Tank from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Tank find(String sql, Object... values) throws DAOException {
        Tank tank = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                tank = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return tank;
    }
    
    @Override
    public List<Tank> list(boolean active) throws DAOException {
        List<Tank> tank = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                tank.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return tank;
    }
    
    @Override
    public void create(Tank tank) throws IllegalArgumentException, DAOException {

        if(tank.getId() != null){
            throw new IllegalArgumentException("Tank is already created, the Tank ID is not null.");
        }

        Object[] values = {
            tank.getTank_name(),
            tank.getDescription(),
            tank.getVolume(),
            tank.getActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Tank failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tank.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Tank failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(Tank tank) throws IllegalArgumentException, DAOException {
        if (tank.getId() == null) {
            throw new IllegalArgumentException("Tank is not created yet, the Tank ID is null.");
        }
        
        Object[] values = {
            tank.getTank_name(),
            tank.getDescription(),
            tank.getVolume(),
            tank.getActive(),
            tank.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Tank failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Tank tank) throws DAOException {
        Object[] values = {
            tank.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Tank failed, no rows affected.");
            } else{
                tank.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Container.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Container.
     * @return The mapped Container from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Tank map(ResultSet resultSet) throws SQLException{
        Tank container = new Tank();
        container.setId(resultSet.getInt("id"));
        container.setTank_name(resultSet.getString("tank_name"));
        container.setDescription(resultSet.getString("description"));
        container.setVolume(resultSet.getDouble("volume"));
        container.setActive(resultSet.getBoolean("active"));
        return container;
    }
}
