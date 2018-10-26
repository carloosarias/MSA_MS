/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ContainerDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Container;

/**
 *
 * @author Pavilion Mini
 */
public class ContainerDAOJDBC implements ContainerDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, type, container_name, description FROM CONTAINER WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, type, container_name, description FROM CONTAINER ORDER BY id";
    private static final String SQL_LIST_OF_TYPE_ORDER_BY_ID = 
            "SELECT id, type, container_name, description FROM CONTAINER WHERE type = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO CONTAINER (type, container_name, description) "
            + "VALUES (?,?,?)";
    private static final String SQL_UPDATE = 
            "UPDATE CONTAINER SET type = ?, container_name = ?, description = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM CONTAINER WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Container DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Container DAO for.
     */
    ContainerDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Container find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Container from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Container from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Container find(String sql, Object... values) throws DAOException {
        Container container = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                container = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return container;
    }
    
    @Override
    public List<Container> list() throws DAOException {
        List<Container> container = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                container.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return container;
    }
    
    @Override
    public List<Container> listType(String type) throws DAOException {
        List<Container> container = new ArrayList<>();
        
        Object[] values = {
            type
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                container.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return container;
    }
    
    @Override
    public void create(Container container) throws IllegalArgumentException, DAOException {

        if(container.getId() != null){
            throw new IllegalArgumentException("Container is already created, the Container ID is not null.");
        }

        Object[] values = {
            container.getType(),
            container.getContainer_name(),
            container.getDescription()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Container failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    container.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Container failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(Container container) throws IllegalArgumentException, DAOException {
        if (container.getId() == null) {
            throw new IllegalArgumentException("Container is not created yet, the Container ID is null.");
        }
        
        Object[] values = {
            container.getType(),
            container.getContainer_name(),
            container.getDescription(),
            container.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Container failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Container container) throws DAOException {
        Object[] values = {
            container.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Container failed, no rows affected.");
            } else{
                container.setId(null);
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
    public static Container map(ResultSet resultSet) throws SQLException{
        Container container = new Container();
        container.setId(resultSet.getInt("id"));
        container.setType(resultSet.getString("type"));
        container.setContainer_name(resultSet.getString("container_name"));
        container.setDescription(resultSet.getString("description"));
        return container;
    }
    
}
