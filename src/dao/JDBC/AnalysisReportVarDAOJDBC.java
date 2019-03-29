/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.AnalysisReportVarDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisReportVar;
import model.AnalysisTypeVar;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportVarDAOJDBC implements AnalysisReportVarDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT ANALYSIS_REPORT_VAR.id, ANALYSIS_REPORT_VAR.value, "
            + "ANALYSIS_REPORT.id, ANALYSIS_TYPE_VAR.id, ANALYSIS_TYPE_VAR.name, ANALYSIS_TYPE_VAR.description "
            + "FROM ANALYSIS_REPORT_VAR "
            + "INNER JOIN ANALYSIS_TYPE_VAR ON ANALYSIS_REPORT_VAR.ANALYSIS_TYPE_VAR_ID = ANALYSIS_TYPE_VAR.id "
            + "INNER JOIN ANALYSIS_REPORT ON ANALYSIS_REPORT_VAR.ANALYSIS_REPORT_ID = ANALYSIS_REPORT.id "
            + "WHERE ANALYSIS_REPORT_VAR.id = ?";
    private static final String SQL_FIND_ANALYSIS_TYPE_VAR_BY_ID = 
            "SELECT ANALYSIS_TYPE_VAR_ID FROM ANALYSIS_REPORT_VAR WHERE id = ?";
    private static final String SQL_FIND_ANALYSIS_REPORT_BY_ID = 
            "SELECT ANALYSIS_REPORT_ID FROM ANALYSIS_REPORT_VAR WHERE id = ?";
    private static final String SQL_LIST_ANALYSISTYPE_ACTIVE_ORDER_BY_ID = 
            "SELECT ANALYSIS_REPORT_VAR.id, ANALYSIS_REPORT_VAR.value, "
            + "ANALYSIS_REPORT.id, ANALYSIS_TYPE_VAR.id, ANALYSIS_TYPE_VAR.name, ANALYSIS_TYPE_VAR.description "
            + "FROM ANALYSIS_REPORT_VAR "
            + "INNER JOIN ANALYSIS_TYPE_VAR ON ANALYSIS_REPORT_VAR.ANALYSIS_TYPE_VAR_ID = ANALYSIS_TYPE_VAR.id "
            + "INNER JOIN ANALYSIS_REPORT ON ANALYSIS_REPORT_VAR.ANALYSIS_REPORT_ID = ANALYSIS_REPORT.id "
            + "WHERE ANALYSIS_REPORT_VAR.ANALYSIS_REPORT_ID = ? "
            + "ORDER BY ANALYSIS_REPORT_VAR.id";
    private static final String SQL_INSERT =
            "INSERT INTO ANALYSIS_REPORT_VAR (ANALYSIS_TYPE_VAR_ID, ANALYSIS_REPORT_ID, value) "
            + "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ANALYSIS_REPORT_VAR SET value = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ANALYSIS_REPORT_VAR WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a AnalysisReportVar DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this AnalysisReportVar DAO for.
     */
    AnalysisReportVarDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    @Override
    public AnalysisReportVar find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the AnalysisReportVar from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The AnalysisReportVar from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private AnalysisReportVar find(String sql, Object... values) throws DAOException {
        AnalysisReportVar analysisreport_var = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysisreport_var = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return analysisreport_var;
    }
    
    @Override
    public AnalysisTypeVar findAnalysisTypeVar(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException {
        if(analysisreport_var.getId() == null) {
            throw new IllegalArgumentException("AnalysisReportVar is not created yet, the AnalysisReportVar ID is null.");
        }
        
        AnalysisTypeVar analysistype_var = null;
        
        Object[] values = {
            analysisreport_var.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_ANALYSIS_TYPE_VAR_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysistype_var = daoFactory.getAnalysisTypeVarDAO().find(resultSet.getInt("ANALYSIS_TYPE_VAR_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return analysistype_var;
    }

    @Override
    public AnalysisReport findAnalysisReport(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException {
        if(analysisreport_var.getId() == null) {
            throw new IllegalArgumentException("AnalysisReportVar is not created yet, the AnalysisReportVar ID is null.");
        }
        
        AnalysisReport analysis_report = null;
        
        Object[] values = {
            analysisreport_var.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_ANALYSIS_TYPE_VAR_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysis_report = daoFactory.getAnalysisReportDAO().find(resultSet.getInt("ANALYSIS_REPORT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return analysis_report;
    }

    @Override
    public List<AnalysisReportVar> list(AnalysisReport analysis_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(AnalysisTypeVar analysistype_var, AnalysisReport analysis_report, AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(AnalysisReportVar analysisreport_var) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(AnalysisReportVar analysisreport_var) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // Helpers ------------------------------------------------------------------------------------
    
    /**
     * Map the current row of the given ResultSet to an AnalysisTypeVar.
     * @param resultSet The ResultSet of which the current row is to be mapped to an AnalysisTypeVar.
     * @return The mapped AnalysisTypeVar from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static AnalysisReportVar map(ResultSet resultSet) throws SQLException{
        AnalysisReportVar analysisreport_var = new AnalysisReportVar();
        analysisreport_var.setId(resultSet.getInt("ANALYSIS_REPORT_VAR.id"));
        analysisreport_var.setValue(resultSet.getDouble("ANALYSIS_REPORT_VAR.value"));
        
        //INNER JOINS
        analysisreport_var.setAnalysisreport_id(resultSet.getInt("ANALYSIS_REPORT.id"));
        analysisreport_var.setAnalysistypevar_id(resultSet.getInt("ANALYSIS_TYPE_VAR.id"));
        analysisreport_var.setAnalysistypevar_name(resultSet.getString("ANALYSIS_TYPE_VAR.name"));
        analysisreport_var.setAnalysistypevar_description(resultSet.getString("ANALYSIS_TYPE_VAR.description"));
        
        return analysisreport_var;
    }
}
