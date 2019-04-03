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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.AnalysisReport;
import model.AnalysisType;
import model.Employee;
import model.Tank;
import static msa_ms.MainApp.timeFormat;

/**
 *
 * @author Pavilion Mini
 */
public class AnalysisReportDAOJDBC implements AnalysisReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT ANALYSIS_REPORT.id, ANALYSIS_REPORT.report_date, ANALYSIS_REPORT.report_time, ANALYSIS_REPORT.applied_adjust, ANALYSIS_REPORT.result, ANALYSIS_REPORT.ph, ANALYSIS_REPORT.formula_timestamp, ANALYSIS_REPORT.active, "
            + "TANK.tank_name, TANK.volume, ANALYSIS_TYPE.name, ANALYSIS_TYPE.optimal, ANALYSIS_TYPE.min_range, ANALYSIS_TYPE.max_range, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE id = ?";
    private static final String SQL_FIND_TANK_BY_ID = 
            "SELECT TANK_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_FIND_ANALYSIS_TYPE_BY_ID = 
            "SELECT ANALYSIS_TYPE_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM ANALYSIS_REPORT WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT ANALYSIS_REPORT.id, ANALYSIS_REPORT.report_date, ANALYSIS_REPORT.report_time, ANALYSIS_REPORT.applied_adjust, ANALYSIS_REPORT.result, ANALYSIS_REPORT.ph, ANALYSIS_REPORT.formula_timestamp, ANALYSIS_REPORT.active, "
            + "TANK.tank_name, TANK.volume, ANALYSIS_TYPE.name, ANALYSIS_TYPE.optimal, ANALYSIS_TYPE.min_range, ANALYSIS_TYPE.max_range, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE ANALYSIS_REPORT.active = ? "
            + "ORDER BY report_date, report_time";
    private static final String SQL_LIST_ACTIVE_TANK_DATE_RANGE_ORDER_BY_ID = 
            "SELECT ANALYSIS_REPORT.id, ANALYSIS_REPORT.report_date, ANALYSIS_REPORT.report_time, ANALYSIS_REPORT.applied_adjust, ANALYSIS_REPORT.result, ANALYSIS_REPORT.ph, ANALYSIS_REPORT.formula_timestamp, ANALYSIS_REPORT.active, "
            + "TANK.tank_name, TANK.volume, ANALYSIS_TYPE.name, ANALYSIS_TYPE.optimal, ANALYSIS_TYPE.min_range, ANALYSIS_TYPE.max_range, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ANALYSIS_REPORT "
            + "INNER JOIN TANK ON ANALYSIS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN ANALYSIS_TYPE ON ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ANALYSIS_TYPE.id "
            + "INNER JOIN EMPLOYEE ON ANALYSIS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE (ANALYSIS_REPORT.TANK_ID = ? OR ? = 0) AND (ANALYSIS_REPORT.ANALYSIS_TYPE_ID = ? OR ? = 0) AND ((ANALYSIS_REPORT.report_date BETWEEN ? AND ?) OR ? = 0) AND ANALYSIS_REPORT.active = ? "
            + "ORDER BY report_date, report_time";
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
    public Tank findTank(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if(analysis_report.getId() == null) {
            throw new IllegalArgumentException("AnalysisReport is not created yet, the AnalysisReport ID is null.");
        }
        
        Tank tank = null;
        
        Object[] values = {
            analysis_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_TANK_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                tank = daoFactory.getTankDAO().find(resultSet.getInt("TANK_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return tank;
    }

    @Override
    public AnalysisType findAnalysisType(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if(analysis_report.getId() == null) {
            throw new IllegalArgumentException("AnalysisReport is not created yet, the AnalysisReport ID is null.");
        }
        
        AnalysisType analysis_type = null;
        
        Object[] values = {
            analysis_report.getId()
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
    public Employee findEmployee(AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if(analysis_report.getId() == null) {
            throw new IllegalArgumentException("AnalysisReport is not created yet, the AnalysisReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            analysis_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_EMPLOYEE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                employee = daoFactory.getEmployeeDAO().find(resultSet.getInt("EMPLOYEE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return employee;
    }

    @Override
    public List<AnalysisReport> list(boolean active) throws DAOException {
        List<AnalysisReport> analysis_report = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
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
    public List<AnalysisReport> list(Tank tank, AnalysisType type, Date start, Date end, boolean tank_filter, boolean analysistype_filter, boolean date_filter, boolean active) throws IllegalArgumentException, DAOException {
        if(tank.getId() == null) {
            throw new IllegalArgumentException("Tank is not created yet, the Tank ID is null.");
        }
        
        if(type.getId() == null) {
            throw new IllegalArgumentException("AnalysisType is not created yet, the AnalysisType ID is null.");
        }
        
        List<AnalysisReport> analysis_report = new ArrayList<>();
        
        Object[] values = {
            tank.getId(),
            tank_filter,
            type.getId(),
            analysistype_filter,
            start,
            end,
            date_filter,
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_TANK_DATE_RANGE_ORDER_BY_ID, false, values);
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
    public void create(Tank tank, AnalysisType analysis_type, Employee employee, AnalysisReport analysis_report) throws IllegalArgumentException, DAOException {
        if(tank.getId() == null){
            throw new IllegalArgumentException("Container is not created yet, the Container ID is null.");
        }
        
        if(analysis_type.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        if(analysis_report.getId() != null){
            throw new IllegalArgumentException("ProcessReport is already created, the ProcessReport ID is null.");
        }
        
        Object[] values = {
            tank.getId(),
            analysis_type.getId(),
            employee.getId(),
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
        
        //INNER JOINS
        analysis_report.setEmployee_name(resultSet.getString("EMPLOYEE.first_name")+" "+resultSet.getString("EMPLOYEE.last_name"));
        analysis_report.setTank_name(resultSet.getString("TANK.tank_name"));
        analysis_report.setTank_volume(resultSet.getDouble("TANK.volume"));
        analysis_report.setAnalysistype_name(resultSet.getString("ANALYSIS_TYPE.name"));
        analysis_report.setAnalysistype_optimal(resultSet.getDouble("ANALYSIS_TYPE.optimal"));
        analysis_report.setAnalysistype_minrange(resultSet.getDouble("ANALYSIS_TYPE.min_range"));
        analysis_report.setAnalysistype_minrange(resultSet.getDouble("ANALYSIS_TYPE.max_range"));
        
        return analysis_report;
    }
}
