/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.CoatingDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Coating;
import model.Metal;

/**
 *
 * @author Pavilion Mini
 */
public class CoatingDAOJDBC implements CoatingDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, desc FROM COATING WHERE id = ?";
    private static final String SQL_FIND_BY_NAME =
            "SELECT id, name, desc FROM COATING WHERE name = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, desc FROM COATING ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO COATING (name, desc) "
            + "VALUES (?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE COATING SET name = ?, desc = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM COATING WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Coating DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Coating DAO for.
     */
    CoatingDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Coating find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public Coating find(String name) throws DAOException {
        return find(SQL_FIND_BY_NAME, name);
    }
    
    /**
     * Returns the Coating from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Coating from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Coating find(String sql, Object... values) throws DAOException {
        Coating coating = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                coating = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return coating;
    }
    
    @Override
    public List<Coating> list() throws DAOException {
        List<Coating> coating = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                coating.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return coating;
    }
    
    @Override
    public void create(Coating coating) throws IllegalArgumentException, DAOException {
        if(coating.getId() != null){
            throw new IllegalArgumentException("Coating is already created, the Coating ID is not null.");
        }

        Object[] values = {
            coating.getName(),
            coating.getDesc()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Coating failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    coating.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Coating failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(Coating coating) throws IllegalArgumentException, DAOException {
        if (coating.getId() == null) {
            throw new IllegalArgumentException("Coating is not created yet, the Coating ID is null.");
        }
        
        Object[] values = {
            coating.getName(),
            coating.getDesc(),
            coating.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Coating failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Coating coating) throws DAOException {
        Object[] values = {
            coating.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Coating failed, no rows affected.");
            } else{
                coating.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Coating.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Coating.
     * @return The mapped Coating from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Coating map(ResultSet resultSet) throws SQLException{
        Coating coating = new Coating();
        coating.setId(resultSet.getInt("id"));
        coating.setName(resultSet.getString("name"));
        coating.setDesc(resultSet.getString("desc"));
        return coating;
    }    
}
