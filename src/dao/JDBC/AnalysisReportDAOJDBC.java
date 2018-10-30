/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.AnalysisTypeDAOJDBC.map;
import dao.interfaces.AnalysisReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisType;
import model.Employee;
import model.Tank;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportDAOJDBC implements AnalysisReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, report_date, quantity_used, result, estimated_adjust, applied_adjust FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_FIND_TANK_BY_ID = 
            "SELECT TANK_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_FIND_ANALYSIS_TYPE_BY_ID = 
            "SELECT ANALYSIS_TYPE_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date, quantity_used, result, estimated_adjust, applied_adjust FROM ANALYSIS_REPORT ORDER BY id";
    private static final String SQL_LIST_TANK_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date, quantity_used, result, estimated_adjust, applied_adjust FROM PROCESS_REPORT WHERE TANK_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO ANALYSIS_REPORT (TANK_ID, ANALYSIS_TYPE_ID, EMPLOYEE_ID, report_date, quantity_used, result, estimated_adjust, applied_adjust) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ANALYSIS_REPORT SET report_date = ?, quantity_used = ?, result = ?, estimated_adjust = ?, applied_adjust = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ANALYSIS_REPORT WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a AnalysisReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this AnalysisReport DAO for.
     */
    AnalysisReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public AnalysisReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the AnalysisReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The AnalysisReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private AnalysisReport find(String sql, Object... values) throws DAOException {
        AnalysisReport analysis_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                analysis_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return analysis_report;
    }
    
    @Override
    public Tank findTank(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisType findAnalysisType(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee findEmployee(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisReport> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisReport> listTankDateRange(Tank tank, Date start, Date end) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Tank tank, AnalysisType analysis_type, Employee employee, AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(AnalysisReport analysis_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an AnalysisReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an AnalysisReport.
     * @return The mapped AnalysisReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static AnalysisReport map(ResultSet resultSet) throws SQLException{
        AnalysisReport analysis_report = new AnalysisReport();
        analysis_report.setId(resultSet.getInt("id"));
        analysis_report.setReport_date(resultSet.getDate("report_date"));
        analysis_report.setQuantity_used(resultSet.getDouble("quantity_used"));
        analysis_report.setResult(resultSet.getDouble("result"));
        analysis_report.setEstimated_adjust(resultSet.getDouble("estimated_adjust"));
        analysis_report.setApplied_adjust(resultSet.getDouble("applied_adjust"));
        return analysis_report;
    }
}
