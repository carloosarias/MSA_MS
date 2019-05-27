/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.MetalDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Metal;

/**
 *
 * @author Pavilion Mini
 */
public class MetalDAOJDBC implements MetalDAO {
    /*
DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getMetal //
CREATE PROCEDURE getMetal(IN metal_id INT(32))
BEGIN
SELECT * 
FROM METAL
WHERE METAL.ID = metal_id;
END //
DROP PROCEDURE IF EXISTS listMetal //
CREATE PROCEDURE listMetal()
BEGIN
SELECT * 
FROM METAL
WHERE METAL.active = 1
ORDER BY METAL.id;
END //
DROP PROCEDURE IF EXISTS createMetal //
CREATE PROCEDURE createMetal(IN metal_name VARCHAR(256), IN density DOUBLE)
BEGIN
INSERT INTO METAL (METAL.metal_name, METAL.density) VALUES(metal_name, density);
END //
DROP PROCEDURE IF EXISTS updateMetal //
CREATE PROCEDURE updateMetal(IN metal_id INT(32), IN metal_name VARCHAR(256), IN density DOUBLE)
BEGIN
UPDATE METAL 
SET METAL.metal_name = metal_name, METAL.density = density
WHERE METAL.id = metal_id;
END //
DROP PROCEDURE IF EXISTS disableMetal //
CREATE PROCEDURE disableMetal(IN metal_id INT(32))
BEGIN
UPDATE METAL 
SET METAL.active = 0
WHERE METAL.id = metal_id;
END //
DELIMITER ;
    */
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_getMetal = 
            "CALL getMetal(?)";
    private static final String SQL_listMetal = 
            "CALL listMetal()";
    private static final String SQL_createMetal = 
            "CALL createMetal(?,?)";
    private static final String SQL_updateMetal = 
            "CALL updateMetal(?,?,?)";
    private static final String SQL_disableMetal = 
            "CALL disableMetal(?)";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Metal DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Metal DAO for.
     */
    MetalDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public Metal find(Integer id) throws DAOException {
        return find(SQL_getMetal, id);
    }
    
    /**
     * Returns the Specification from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Specification from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Metal find(String sql, Object... values) throws DAOException {
        Metal metal = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                metal = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return metal;
    }
    
    @Override
    public List<Metal> list() throws DAOException {
        List<Metal> metals = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_listMetal);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                metals.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return metals;
    }
    
    @Override
    public void create(Metal metal) throws IllegalArgumentException, DAOException {
        if(metal.getId() != null){
            throw new IllegalArgumentException("Metal is already created, the Metal ID is not null.");
        }
        
        Object[] values = {
            metal.getMetal_name(),
            metal.getDensity()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_createMetal, false, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Metal failed, no rows affected.");
            }
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Metal metal) throws IllegalArgumentException, DAOException {
        if (metal.getId() == null) {
            throw new IllegalArgumentException("Metal is not created yet, the Metal ID is null.");
        }
        
        Object[] values = {
            metal.getId(),
            metal.getMetal_name(),
            metal.getDensity()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_updateMetal, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Metal failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Metal metal) throws IllegalArgumentException, DAOException {
        if (metal.getId() == null) {
            throw new IllegalArgumentException("Metal is not created yet, the Metal ID is null.");
        }
        
        Object[] values = {
            metal.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_disableMetal, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Metal failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Specification.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Specification.
     * @return The mapped Specification from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Metal map(ResultSet resultSet) throws SQLException{
        Metal metal = new Metal();
        metal.setId(resultSet.getInt("METAL.id"));
        metal.setMetal_name(resultSet.getString("METAL.metal_name"));
        metal.setDensity(resultSet.getDouble("METAL.density"));
        metal.setActive(resultSet.getBoolean("METAL.active"));
        return metal;
    }
}
