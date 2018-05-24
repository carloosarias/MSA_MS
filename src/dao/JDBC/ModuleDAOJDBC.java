/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.ModuleDAO;
import static dao.DAOUtil.prepareStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ModuleDAOJDBC implements ModuleDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name FROM MODULE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name FROM MODULE ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO MODULE (name) "
            + "VALUES (?)";
    private static final String SQL_UPDATE = 
            "UPDATE MODULE SET name = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM MODULE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Employee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ModuleDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Module find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Module from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Module from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Module find(String sql, Object... values) throws DAOException {
        Module module = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                module = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return module;
    }
    
    @Override
    public List<Module> list() throws DAOException {
        List<Module> modules = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                modules.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return modules;
    }
    
    @Override
    public void create(Module module) throws IllegalArgumentException, DAOException {
        if(module.getId() != null){
            throw new IllegalArgumentException("Module is already created, the Module ID is not null.");
        }
        
        Object[] values = {
            module.getName()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Module failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    module.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Module failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(Module module) throws IllegalArgumentException, DAOException {
        if (module.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        Object[] values = {
            module.getName(),
            module.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Employee failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Module module) throws DAOException {
        Object[] values = {
            module.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Module failed, no rows affected.");
            } else{
                module.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Module.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Module.
     * @return The mapped Module from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Module map(ResultSet resultSet) throws SQLException{
        Module module = new Module();
        module.setId(resultSet.getInt("id"));
        module.setName(resultSet.getString("name"));
        return module;
    }
}
