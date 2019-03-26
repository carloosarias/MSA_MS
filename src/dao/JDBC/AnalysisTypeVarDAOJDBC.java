/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.AnalysisTypeDAOJDBC.map;
import dao.interfaces.AnalysisTypeVarDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.AnalysisType;
import model.AnalysisTypeVar;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisTypeVarDAOJDBC implements AnalysisTypeVarDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT ANALYSIS_TYPE_VAR.id, ANALYSIS_TYPE_VAR.name, ANALYSIS_TYPE_VAR.description, ANALYSIS_TYPE_VAR.default_value, ANALYSIS_TYPE_VAR.active, "
            + "ANALYSIS_TYPE.id "
            + "FROM ANALYSIS_TYPE_VAR "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_TYPE_VAR.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "WHERE ANALYSIS_TYPE_VAR.id = ?";
    private static final String SQL_FIND_ANALYSIS_TYPE_BY_ID = 
            "SELECT ANALYSIS_TYPE_ID FROM ANALYSIS_TYPE_VAR WHERE id = ?";
    private static final String SQL_LIST_ANALYSISTYPE_ACTIVE_ORDER_BY_ID = 
            "SELECT ANALYSIS_TYPE_VAR.id, ANALYSIS_TYPE_VAR.name, ANALYSIS_TYPE_VAR.description, ANALYSIS_TYPE_VAR.default_value, ANALYSIS_TYPE_VAR.active, "
            + "ANALYSIS_TYPE.id "
            + "FROM ANALYSIS_TYPE_VAR "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_TYPE_VAR.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "WHERE ANALYSIS_TYPE_VAR.ANALYSIS_TYPE_ID = ? AND ANALYSIS_TYPE_VAR.active = ? "
            + "ORDER BY ANALYSIS_TYPE_VAR.id";
    private static final String SQL_INSERT =
            "INSERT INTO ANALYSIS_TYPE_VAR (ANALYSIS_TYPE_ID, name, description, default_value, active) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ANALYSIS_TYPE_VAR SET name = ?, description = ?, default_value = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ANALYSIS_TYPE_VAR WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a AnalysisTypeVar DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this AnalysisTypeVar DAO for.
     */
    AnalysisTypeVarDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public AnalysisTypeVar find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the AnalysisTypeVar from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The AnalysisTypeVar from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private AnalysisTypeVar find(String sql, Object... values) throws DAOException {
        AnalysisTypeVar analysistype_var = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysistype_var = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return analysistype_var;
    }
    
    @Override
    public AnalysisType findAnalysisType(AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException {
        if(analysistype_var.getId() == null) {
            throw new IllegalArgumentException("AnalysisTypeVar is not created yet, the AnalysisTypeVar ID is null.");
        }
        
        AnalysisType analysis_type = null;
        
        Object[] values = {
            analysistype_var.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_ANALYSIS_TYPE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysis_type = daoFactory.getAnalysisTypeDAO().find(resultSet.getInt("ANALYSIS_TYPE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return analysis_type;
    }
    
    @Override
    public List<AnalysisTypeVar> list(AnalysisType analysis_type, boolean active) throws DAOException {
        if(analysis_type.getId() == null) {
            throw new IllegalArgumentException("AnalysisType is not created yet, the AnalysisType ID is null.");
        }
        
        List<AnalysisTypeVar> analysistype_var = new ArrayList<>();
        
        Object[] values = {
            analysis_type.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ANALYSISTYPE_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                analysistype_var.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return analysistype_var;
    }

    @Override
    public void create(AnalysisType analysis_type, AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException {
        if(analysis_type.getId() == null){
            throw new IllegalArgumentException("AnalysisType is not created yet, the AnalysisType ID is null");
        }
        if(analysistype_var.getId() != null){
            throw new IllegalArgumentException("AnalysisTypeVar is already created, the AnalysisTypeVar ID is not null.");
        }
        Object[] values = {
            analysis_type.getId(),
            analysistype_var.getName(),
            analysistype_var.getDescription(),
            analysistype_var.getDefault_value(),
            analysistype_var.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating AnalysisTypeVar failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    analysistype_var.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating AnalysisTypeVar failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(AnalysisTypeVar analysistype_var) throws IllegalArgumentException, DAOException {
        if (analysistype_var.getId() == null) {
            throw new IllegalArgumentException("AnalysisTypeVar is not created yet, the AnalysisTypeVar ID is null.");
        }
        
        Object[] values = {
            analysistype_var.getName(),
            analysistype_var.getDescription(),
            analysistype_var.getDefault_value(),
            analysistype_var.isActive(),
            analysistype_var.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating AnalysisTypeVar failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(AnalysisTypeVar analysistype_var) throws DAOException {
        Object[] values = {
            analysistype_var.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting AnalysisTypeVar failed, no rows affected.");
            } else{
                analysistype_var.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------
    
    /**
     * Map the current row of the given ResultSet to an AnalysisTypeVar.
     * @param resultSet The ResultSet of which the current row is to be mapped to an AnalysisTypeVar.
     * @return The mapped AnalysisTypeVar from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static AnalysisTypeVar map(ResultSet resultSet) throws SQLException{
        AnalysisTypeVar analysistype_var = new AnalysisTypeVar();
        analysistype_var.setId(resultSet.getInt("id"));
        analysistype_var.setName(resultSet.getString("name"));
        analysistype_var.setDescription(resultSet.getString("description"));
        analysistype_var.setDefault_value(resultSet.getDouble("default_value"));
        analysistype_var.setActive(resultSet.getBoolean("active"));
        
        //INNER JOINS
        analysistype_var.setAnalysistype_id(resultSet.getInt("ANALYSIS_TYPE.id"));
        return analysistype_var;
    }
}
