/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ActivityReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ActivityReport;
import model.Employee;
import static msa_ms.MainApp.timeFormat;

/**
 *
 * @author Pavilion Mini
 */
public class ActivityReportDAOJDBC implements ActivityReportDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT ACTIVITY_REPORT.id, ACTIVITY_REPORT.report_date, ACTIVITY_REPORT.start_time, ACTIVITY_REPORT.end_time, ACTIVITY_REPORT.job_description, ACTIVITY_REPORT.physical_location, ACTIVITY_REPORT.action_taken, ACTIVITY_REPORT.comments, ACTIVITY_REPORT.active, "
            + "EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ACTIVITY_REPORT "
            + "INNER JOIN EMPLOYEE ON ACTIVITY_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE ACTIVITY_REPORT.id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM ACTIVITY_REPORT WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT ACTIVITY_REPORT.id, ACTIVITY_REPORT.report_date, ACTIVITY_REPORT.start_time, ACTIVITY_REPORT.end_time, ACTIVITY_REPORT.job_description, ACTIVITY_REPORT.physical_location, ACTIVITY_REPORT.action_taken, ACTIVITY_REPORT.comments, ACTIVITY_REPORT.active, "
            + "EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ACTIVITY_REPORT "
            + "INNER JOIN EMPLOYEE ON ACTIVITY_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE ACTIVITY_REPORT.active = ? "
            + "ORDER BY ACTIVITY_REPORT.id";
    private static final String SQL_LIST_ACTIVE_DATERANGE_ORDER_BY_ID = 
            "SELECT ACTIVITY_REPORT.id, ACTIVITY_REPORT.report_date, ACTIVITY_REPORT.start_time, ACTIVITY_REPORT.end_time, ACTIVITY_REPORT.job_description, ACTIVITY_REPORT.physical_location, ACTIVITY_REPORT.action_taken, ACTIVITY_REPORT.comments, ACTIVITY_REPORT.active, "
            + "EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ACTIVITY_REPORT "
            + "INNER JOIN EMPLOYEE ON ACTIVITY_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE (ACTIVITY_REPORT.report_date BETWEEN ? AND ? OR ? = 0) AND ACTIVITY_REPORT.active = ? "
            + "ORDER BY ACTIVITY_REPORT.id";  
    private static final String SQL_INSERT = 
            "INSERT INTO ACTIVITY_REPORT (EMPLOYEE_ID, report_date, start_time, end_time, job_description, physical_location, action_taken, comments, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ACTIVITY_REPORT SET report_date = ?, start_time = ?, end_time = ?, job_description = ?, physical_location = ?, action_taken = ?, comments = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM ACTIVITY_REPORT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ActivityReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ActivityReport DAO for.
     */
    ActivityReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ActivityReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ActivityReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ActivityReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ActivityReport find(String sql, Object... values) throws DAOException {
        ActivityReport activity_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                activity_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return activity_report;
    }
    
    @Override
    public Employee findEmployee(ActivityReport activity_report) throws IllegalArgumentException, DAOException {
        if(activity_report.getId() == null) {
            throw new IllegalArgumentException("ActivityReport is not created yet, the ActivityReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            activity_report.getId()
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
    public List<ActivityReport> list(boolean active) throws DAOException {
        List<ActivityReport> activity_report = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                activity_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return activity_report;
    }

    @Override
    public List<ActivityReport> list(Date start, Date end, boolean date_filter, boolean active) throws DAOException {
   
        List<ActivityReport> activity_report = new ArrayList<>();
        
        Object[] values = {
            start,
            end,
            date_filter,
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_DATERANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                activity_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return activity_report; 
    }

    @Override
    public void create(Employee employee, ActivityReport activity_report) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        if(activity_report.getId() != null){
            throw new IllegalArgumentException("ActivityReport is already created, the ActivityReport ID is not null.");
        }
        
        Object[] values = {
            employee.getId(),
            DAOUtil.toSqlDate(activity_report.getReport_date()),
            DAOUtil.toSqlTime(LocalTime.parse(activity_report.getStart_time(), timeFormat)),
            DAOUtil.toSqlTime(LocalTime.parse(activity_report.getEnd_time(), timeFormat)),
            activity_report.getJob_description(),
            activity_report.getPhysical_location(),
            activity_report.getAction_taken(),
            activity_report.getComments(),
            activity_report.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ActivityReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    activity_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ActivityReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ActivityReport activity_report) throws IllegalArgumentException, DAOException {
        if (activity_report.getId() == null) {
            throw new IllegalArgumentException("ActivityReport is not created yet, the ActivityReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(activity_report.getReport_date()),
            DAOUtil.toSqlTime(LocalTime.parse(activity_report.getStart_time(), timeFormat)),
            DAOUtil.toSqlTime(LocalTime.parse(activity_report.getEnd_time(), timeFormat)),
            activity_report.getJob_description(),
            activity_report.getPhysical_location(),
            activity_report.getAction_taken(),
            activity_report.getComments(),
            activity_report.isActive(),
            activity_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ActivityReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ActivityReport activity_report) throws DAOException {
        Object[] values = {
            activity_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ActivityReport failed, no rows affected.");
            } else{
                activity_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an ActivityReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an ActivityReport.
     * @return The mapped ActivityReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ActivityReport map(ResultSet resultSet) throws SQLException{
        ActivityReport activity_report = new ActivityReport();
        activity_report.setId(resultSet.getInt("ACTIVITY_REPORT.id"));
        activity_report.setReport_date(resultSet.getDate("ACTIVITY_REPORT.report_date"));
        activity_report.setStart_time(resultSet.getTime("ACTIVITY_REPORT.start_time").toLocalTime().format(timeFormat));
        activity_report.setEnd_time(resultSet.getTime("ACTIVITY_REPORT.end_time").toLocalTime().format(timeFormat));
        activity_report.setJob_description(resultSet.getString("ACTIVITY_REPORT.job_description"));
        activity_report.setPhysical_location(resultSet.getString("ACTIVITY_REPORT.physical_location"));
        activity_report.setAction_taken(resultSet.getString("ACTIVITY_REPORT.action_taken"));
        activity_report.setComments(resultSet.getString("ACTIVITY_REPORT.comments"));
        activity_report.setActive(resultSet.getBoolean("ACTIVITY_REPORT.active"));
        
        //INNER JOINS
        activity_report.setEmployee_id(resultSet.getInt("EMPLOYEE.id"));
        activity_report.setEmployee_name(resultSet.getString("EMPLOYEE.first_name")+" "+resultSet.getString("EMPLOYEE.last_name"));
        return activity_report;
    }    
}
