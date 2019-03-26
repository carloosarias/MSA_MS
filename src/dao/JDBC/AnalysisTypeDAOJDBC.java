/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.AnalysisTypeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.AnalysisType;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisTypeDAOJDBC implements AnalysisTypeDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, description, min_range, optimal, max_range, formula, active FROM ANALYSIS_TYPE WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_NAME = 
            "SELECT id, name, description, min_range, optimal, max_range, formula, active FROM ANALYSIS_TYPE WHERE active = ? ORDER BY name";
    private static final String SQL_INSERT =
            "INSERT INTO ANALYSIS_TYPE (name, description, min_range, optimal, max_range, formula, active) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ANALYSIS_TYPE SET name = ?, description = ?, min_range = ?, optimal = ?, max_range = ?, formula = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ANALYSIS_TYPE WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a AnalysisType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this AnalysisType DAO for.
     */
    AnalysisTypeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public AnalysisType find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the AnalysisType from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The AnalysisType from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private AnalysisType find(String sql, Object... values) throws DAOException {
        AnalysisType analysis_type = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysis_type = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return analysis_type;
    }
    
    @Override
    public List<AnalysisType> list(boolean active) throws DAOException {
        List<AnalysisType> analysis_type = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_NAME, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                analysis_type.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return analysis_type;
    }

    @Override
    public void create(AnalysisType analysis_type) throws IllegalArgumentException, DAOException {
        if(analysis_type.getId() != null){
            throw new IllegalArgumentException("AnalysisType is already created, the AnalysisType ID is not null.");
        }
        Object[] values = {
            analysis_type.getName(),
            analysis_type.getDescription(),
            analysis_type.getMin_range(),
            analysis_type.getOptimal(),
            analysis_type.getMax_range(),
            analysis_type.getFormula(),
            analysis_type.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating AnalysisType failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    analysis_type.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating AnalysisType failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(AnalysisType analysis_type) throws IllegalArgumentException, DAOException {
        if (analysis_type.getId() == null) {
            throw new IllegalArgumentException("AnalysisType is not created yet, the AnalysisType ID is null.");
        }
        
        Object[] values = {
            analysis_type.getName(),
            analysis_type.getDescription(),
            analysis_type.getMin_range(),
            analysis_type.getOptimal(),
            analysis_type.getMax_range(),
            analysis_type.getFormula(),
            analysis_type.isActive(),
            analysis_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating AnalysisType failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(AnalysisType analysis_type) throws DAOException {
        Object[] values = {
            analysis_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting AnalysisType failed, no rows affected.");
            } else{
                analysis_type.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an AnalysisType.
     * @param resultSet The ResultSet of which the current row is to be mapped to an AnalysisType.
     * @return The mapped AnalysisType from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static AnalysisType map(ResultSet resultSet) throws SQLException{
        AnalysisType analysis_type = new AnalysisType();
        analysis_type.setId(resultSet.getInt("id"));
        analysis_type.setName(resultSet.getString("name"));
        analysis_type.setDescription(resultSet.getString("description"));
        analysis_type.setMin_range(resultSet.getDouble("min_range"));
        analysis_type.setOptimal(resultSet.getDouble("optimal"));
        analysis_type.setMax_range(resultSet.getDouble("max_range"));
        analysis_type.setFormula(resultSet.getString("formula"));
        analysis_type.setActive(resultSet.getBoolean("active"));
        return analysis_type;
    }
}
