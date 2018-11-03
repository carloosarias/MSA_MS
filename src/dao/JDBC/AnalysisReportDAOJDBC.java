/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.AnalysisReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            "SELECT id, report_date, quantity_used, result, estimated_adjust, applied_adjust FROM ANALYSIS_REPORT WHERE TANK_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
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
    public List<AnalysisReport> list() throws DAOException {
        List<AnalysisReport> analysis_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
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
    public List<AnalysisReport> listTankDateRange(Tank tank, Date start, Date end) throws IllegalArgumentException, DAOException {
        if(tank.getId() == null) {
            throw new IllegalArgumentException("Tank is not created yet, the Tank ID is null.");
        }
        
        List<AnalysisReport> analysis_report = new ArrayList<>();
        
        Object[] values = {
            tank.getId(),
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_TANK_DATE_RANGE_ORDER_BY_ID, false, values);
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
            analysis_report.getQuantity_used(),
            analysis_report.getResult(),
            analysis_report.getEstimated_adjust(),
            analysis_report.getApplied_adjust(),
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
            analysis_report.getQuantity_used(),
            analysis_report.getResult(),
            analysis_report.getEstimated_adjust(),
            analysis_report.getApplied_adjust(),
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
        analysis_report.setId(resultSet.getInt("id"));
        analysis_report.setReport_date(resultSet.getDate("report_date"));
        analysis_report.setQuantity_used(resultSet.getDouble("quantity_used"));
        analysis_report.setResult(resultSet.getDouble("result"));
        analysis_report.setEstimated_adjust(resultSet.getDouble("estimated_adjust"));
        analysis_report.setApplied_adjust(resultSet.getDouble("applied_adjust"));
        return analysis_report;
    }
}
