/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.AnalysisReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisType;

import model.Tank;
import static msa_ms.MainApp.timeFormat;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportDAOJDBC implements AnalysisReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE ANALYSIS_REPORT.id = ?";
    private static final String SQL_LIST_ACTIVE = 
            "SELECT * FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE ANALYSIS_REPORT.active = 1 "
            + "ORDER BY ANALYSIS_REPORT.id DESC";
    private static final String SQL_LIST_ACTIVE_FILTER = 
            "SELECT * FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE (ANALYSIS_REPORT.TANK_ID = ? OR ? IS NULL) AND (ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ? OR ? IS NULL) AND (ANALYSIS_REPORT.report_date BETWEEN ? AND ?) AND ANALYSIS_REPORT.active = 1 "
            + "ORDER BY ANALYSIS_REPORT.id DESC";
    private static final String SQL_INSERT =
            "INSERT INTO ANALYSIS_REPORT (TANK_ID, ANALYSIS_TYPE_ID, EMPLOYEE_ID, report_date, report_time, applied_adjust, result, ph, formula_timestamp, active) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ANALYSIS_REPORT SET report_date = ?, report_time = ?, applied_adjust = ?, result = ?, ph = ?, formula_timestamp = ?, active = ? WHERE id = ?";
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
    public List<AnalysisReport> list() throws DAOException {
        List<AnalysisReport> analysis_report = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ACTIVE);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                analysis_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return analysis_report;
    }

    @Override
    public List<AnalysisReport> list(Tank tank, AnalysisType analysis_type, Date start_date, Date end_date) throws IllegalArgumentException, DAOException {
        if(tank == null) tank = new Tank();
        if(analysis_type == null) analysis_type = new AnalysisType();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        List<AnalysisReport> analysis_report = new ArrayList<>();
        
        Object[] values = {
            tank.getId(),
            tank.getId(),
            analysis_type.getId(),
            analysis_type.getId(),
            start_date,
            end_date
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                analysis_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return analysis_report;
    }

    @Override
    public void create(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if(analysis_report.getId() != null){
            throw new IllegalArgumentException("ProcessReport is already created, the ProcessReport ID is null.");
        }
        
        Object[] values = {
            analysis_report.getTank().getId(),
            analysis_report.getAnalysis_type().getId(),
            analysis_report.getEmployee().getId(),
            analysis_report.getReport_date(),
            DAOUtil.toSqlTime(LocalTime.parse(analysis_report.getReport_time(), timeFormat)),
            analysis_report.getApplied_adjust(),
            analysis_report.getResult(),
            analysis_report.getPh(),
            analysis_report.getFormula_timestamp(),
            analysis_report.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating AnalysisReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    analysis_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating AnalysisReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if (analysis_report.getId() == null) {
            throw new IllegalArgumentException("AnalysisReport is not created yet, the AnalysisReport ID is null.");
        }
        
        Object[] values = {
            analysis_report.getReport_date(),
            DAOUtil.toSqlTime(LocalTime.parse(analysis_report.getReport_time(), timeFormat)),
            analysis_report.getApplied_adjust(),
            analysis_report.getResult(),
            analysis_report.getPh(),
            analysis_report.getFormula_timestamp(),
            analysis_report.isActive(),
            analysis_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating AnalysisReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(AnalysisReport analysis_report) throws DAOException {
        Object[] values = {
            analysis_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting AnalysisReport failed, no rows affected.");
            } else{
                analysis_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
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
        analysis_report.setId(resultSet.getInt("ANALYSIS_REPORT.id"));
        analysis_report.setReport_date(resultSet.getDate("ANALYSIS_REPORT.report_date"));
        analysis_report.setReport_time(resultSet.getTime("ANALYSIS_REPORT.report_time").toLocalTime().format(timeFormat));
        analysis_report.setApplied_adjust(resultSet.getDouble("ANALYSIS_REPORT.applied_adjust"));
        analysis_report.setResult(resultSet.getDouble("ANALYSIS_REPORT.result"));
        analysis_report.setPh(resultSet.getDouble("ANALYSIS_REPORT.ph"));
        analysis_report.setFormula_timestamp(resultSet.getString("ANALYSIS_REPORT.formula_timestamp"));
        analysis_report.setActive(resultSet.getBoolean("ANALYSIS_REPORT.active"));
        analysis_report.setTank(TankDAOJDBC.map(resultSet));
        analysis_report.setAnalysis_type(AnalysisTypeDAOJDBC.map(resultSet));
        analysis_report.setEmployee(EmployeeDAOJDBC.map(resultSet));
        
        return analysis_report;
    }
}
