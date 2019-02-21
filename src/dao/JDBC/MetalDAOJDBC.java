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
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class MetalDAOJDBC implements MetalDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, metal_name, density, active FROM METAL WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, metal_name, density, active FROM METAL ORDER BY metal_name, density";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, metal_name, density, active FROM METAL WHERE active = ? ORDER BY metal_name, density";
    private static final String SQL_INSERT = 
            "INSERT INTO METAL (metal_name, density, active) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE METAL SET  metal_name = ?, density = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM METAL WHERE id = ?";
    
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
        return find(SQL_FIND_BY_ID, id);
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
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
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
    public List<Metal> list(boolean active) throws DAOException {
        List<Metal> metals = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
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
            metal.getDensity(),
            metal.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Metal failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    metal.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Metal failed, no generated key obtained.");
                }
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
            metal.getMetal_name(),
            metal.getDensity(),
            metal.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
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
    public void delete(Metal metal) throws DAOException {
        Object[] values = {
            metal.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Metal failed, no rows affected.");
            } else{
                metal.setId(null);
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
        metal.setId(resultSet.getInt("id"));
        metal.setMetal_name(resultSet.getString("metal_name"));
        metal.setDensity(resultSet.getDouble("density"));
        metal.setActive(resultSet.getBoolean("active"));
        return metal;
    }
}
