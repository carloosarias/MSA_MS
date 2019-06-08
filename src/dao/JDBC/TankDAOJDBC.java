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
    /*
    DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getTank //
CREATE PROCEDURE getTank(IN tank_id INT(32))
BEGIN
SELECT *
FROM TANK
WHERE TANK.ID = tank_id;
END //
DROP PROCEDURE IF EXISTS listTank //
CREATE PROCEDURE listTank(IN tank_name VARCHAR(256), IN description VARCHAR(256))
BEGIN
SELECT *
FROM TANK
WHERE (TANK.tank_name LIKE concat(IFNULL(tank_name,''),'%'))
AND (TANK.description LIKE concat(IFNULL(description,''),'%')) 
AND TANK.active = 1
ORDER BY TANK.id;
END //
DROP PROCEDURE IF EXISTS createTank //
CREATE PROCEDURE createTank(IN tank_name VARCHAR(256), IN description VARCHAR(256), IN volume DOUBLE)
BEGIN
INSERT INTO TANK (TANK.tank_name, TANK.description, TANK.volume, TANK.active) VALUES(tank_name, description, volume, 1);
END //
DROP PROCEDURE IF EXISTS updateTank //
CREATE PROCEDURE updateTank(IN tank_id INT(32), IN tank_name VARCHAR(256), IN description VARCHAR(256), IN volume DOUBLE)
BEGIN
UPDATE TANK 
SET TANK.tank_name = tank_name, TANK.description = description, TANK.volume = volume
WHERE TANK.id = tank_id;
END //
DROP PROCEDURE IF EXISTS disableTank //
CREATE PROCEDURE disableTank(IN tank_id INT(32))
BEGIN
UPDATE TANK 
SET TANK.active = 0
WHERE TANK.id = tank_id;
END //
DELIMITER ;
    */
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_getTank = 
            "CALL getTank(?)";
    private static final String SQL_listTank = 
            "CALL listTank(?,?)";
    private static final String SQL_createTank =
            "CALL createTank(?,?,?)";
    private static final String SQL_updateTank = 
            "CALL updateTank(?,?,?,?)";
    private static final String SQL_disableTank =
            "CALL disableTank";
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
        return find(SQL_getTank, id);
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
    public List<Tank> list() throws DAOException {
        return search(null, null);
    }
    
    @Override
    public List<Tank> search(String tank_name, String description) throws DAOException {
        List<Tank> tank = new ArrayList<>();
        
        Object[] values = {
            tank_name,
            description
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_listTank, false, values);
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
            PreparedStatement statement = prepareStatement(connection, SQL_createTank, true, values);          
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
            PreparedStatement statement = prepareStatement(connection, SQL_updateTank, false, values);
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
            PreparedStatement statement = prepareStatement(connection, SQL_disableTank, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Tank failed, no rows affected.");
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
