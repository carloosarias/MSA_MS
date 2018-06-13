/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.SpecificationDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class SpecificationDAOJDBC implements SpecificationDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, specification_number, process, details, active FROM SPECIFICATION WHERE id = ?";
    private static final String SQL_FIND_BY_SPECIFICATION_NUMBER = 
            "SELECT id, specification_number, process, details, active FROM SPECIFICATION WHERE specification_number = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, specification_number, process, details, active FROM SPECIFICATION ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, specification_number, process, details, active FROM SPECIFICATION WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO SPECIFICATION (specification_number, process, details, active) "
            + "VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = 
            "UPDATE SPECIFICATION SET specification_number = ?, process = ?, details = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM SPECIFICATION WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Specification DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    SpecificationDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Specification find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public Specification find(String specification_number) throws DAOException {
        return find(SQL_FIND_BY_SPECIFICATION_NUMBER, specification_number);
    }
    
    /**
     * Returns the Specification from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Specification from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Specification find(String sql, Object... values) throws DAOException {
        Specification specification = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return specification;
    }
    
    @Override
    public List<Specification> list() throws DAOException {
        List<Specification> specification = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                specification.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return specification;
    }
    
    @Override
    public List<Specification> list(boolean active) throws DAOException {
        List<Specification> specification = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                specification.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return specification;
    }
    
    @Override
    public void create(Specification specification) throws IllegalArgumentException, DAOException {

        if(specification.getId() != null){
            throw new IllegalArgumentException("Specification is already created, the Specification ID is not null.");
        }

        Object[] values = {
            specification.getSpecification_number(),
            specification.getProcess(),
            specification.getDetails(),
            specification.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Specification failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    specification.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Specification failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }    
    }

    @Override
    public void update(Specification specification) throws IllegalArgumentException, DAOException {
        if (specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        Object[] values = {
            specification.getSpecification_number(),
            specification.getProcess(),
            specification.getDetails(),
            specification.isActive(),
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Specification failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Specification specification) throws DAOException {
        Object[] values = {
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Specification failed, no rows affected.");
            } else{
                specification.setId(null);
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
    public static Specification map(ResultSet resultSet) throws SQLException{
        Specification specification = new Specification();
        specification.setId(resultSet.getInt("id"));
        specification.setSpecification_number(resultSet.getString("specification_number"));
        specification.setProcess(resultSet.getString("process"));
        specification.setDetails(resultSet.getString("details"));
        specification.setActive(resultSet.getBoolean("active"));
        return specification;
    }
    
}
