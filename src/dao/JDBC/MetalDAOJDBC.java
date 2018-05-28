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
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name FROM METAL WHERE id = ?";
    private static final String SQL_FIND_BY_NAME =
            "SELECT id, name FROM METAL WHERE name = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name FROM METAL ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO METAL (name) "
            + "VALUES (?)";
    private static final String SQL_UPDATE = 
            "UPDATE METAL SET name = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM METAL WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Metal DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    MetalDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Metal find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public Metal find(String name) throws DAOException {
        return find(SQL_FIND_BY_NAME, name);
    }
    
    /**
     * Returns the Metal from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Metal from the database matching the given SQL query with the given values.
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
        List<Metal> metal = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                metal.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return metal;
    }
    
    @Override
    public void create(Metal metal) throws IllegalArgumentException, DAOException {
        if(metal.getId() != null){
            throw new IllegalArgumentException("Metal is already created, the Metal ID is not null.");
        }

        Object[] values = {
            metal.getName()
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
            metal.getName(),
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
     * Map the current row of the given ResultSet to an Metal.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Metal.
     * @return The mapped Metal from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Metal map(ResultSet resultSet) throws SQLException{
        Metal metal = new Metal();
        metal.setId(resultSet.getInt("id"));
        metal.setName(resultSet.getString("name"));
        return metal;
    }
}
