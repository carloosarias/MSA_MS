/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;


import dao.DAOException;
import dao.DAOUtil;
import dao.interfaces.EmployeeDAO;
import static dao.DAOUtil.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import static msa_ms.MainApp.timeFormat;

/**
 *
 * @author Pavilion Mini
 */
public class EmployeeDAOJDBC implements EmployeeDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM EMPLOYEE WHERE id = ?";
    private static final String SQL_FIND_BY_USER_AND_PASSWORD =
            "SELECT * FROM EMPLOYEE WHERE user = BINARY ? AND password = BINARY MD5(?) AND active = 1";
    private static final String SQL_LIST_ORDER_BY_ID =
            "SELECT * FROM EMPLOYEE ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID =
            "SELECT * FROM EMPLOYEE WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO EMPLOYEE (user, password, first_name, last_name, hire_date, entry_time, end_time, birth_date, curp, address, active, admin, email, phone, schedule) "
            +"VALUES (?, MD5(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE EMPLOYEE SET user = ?, first_name = ?, last_name = ?, hire_date = ?, entry_time = ?, end_time = ?, birth_date = ?, curp = ?, address = ?, active = ?, admin = ?, email = ?, phone = ?, schedule = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM EMPLOYEE WHERE id = ?";
    private static final String SQL_EXIST_USER =
            "SELECT id FROM EMPLOYEE WHERE user = BINARY ? AND active = 1";
    private static final String SQL_EXIST_ACTIVE =
            "SELECT id FROM EMPLOYEE WHERE user = ? AND active = ?";
    private static final String SQL_CHANGE_PASSWORD = 
            "UPDATE EMPLOYEE SET password = BINARY MD5(?) WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Employee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    EmployeeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public Employee find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    @Override
    public Employee find(String user, String password) throws DAOException {
        return find(SQL_FIND_BY_USER_AND_PASSWORD, user, password);
    }
    
    /**
     * Returns the Employee from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Employee from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Employee find(String sql, Object... values) throws DAOException {
        Employee employee = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                employee = map("", resultSet);
            }
            connection.close();
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return employee;
    }

    @Override
    public List<Employee> list() throws DAOException {
        List<Employee> employees = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                employees.add(map("", resultSet));
            }
            connection.close();
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return employees;
    }
    
    @Override
    public List<Employee> listActive(boolean active) throws DAOException {
        List<Employee> employees = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                employees.add(map("", resultSet));
            }
            connection.close();
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return employees;
    }
    
    @Override
    public void create(Employee employee) throws IllegalArgumentException, DAOException {
        if(employee.getId() != null){
            throw new IllegalArgumentException("Employee is already created, the Employee ID is not null.");
        }
        
        Object[] values = {
            employee.getUser(),
            employee.getPassword(),
            employee.getFirst_name(),
            employee.getLast_name(),
            toSqlDate(employee.getHire_date()),
            DAOUtil.toSqlTime(LocalTime.parse(employee.getEntry_time(), timeFormat)),
            DAOUtil.toSqlTime(LocalTime.parse(employee.getEnd_time(), timeFormat)),
            toSqlDate(employee.getBirth_date()),
            employee.getCurp(),
            employee.getAddress(),
            employee.isActive(),
            employee.isAdmin(),
            employee.getEmail(),
            employee.getPhone(),
            employee.getScheduleAsString()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Employee failed, no rows affected.");
            }
            
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Employee failed, no generated key obtained.");
                }
            }
            connection.close();
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }
    
    @Override
    public void update(Employee employee) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        Object[] values = {
            employee.getUser(),
            employee.getFirst_name(),
            employee.getLast_name(),
            toSqlDate(employee.getHire_date()),
            DAOUtil.toSqlTime(LocalTime.parse(employee.getEntry_time(), timeFormat)),
            DAOUtil.toSqlTime(LocalTime.parse(employee.getEnd_time(), timeFormat)),
            toSqlDate(employee.getBirth_date()),
            employee.getCurp(),
            employee.getAddress(),
            employee.isActive(),
            employee.isAdmin(),
            employee.getEmail(),
            employee.getPhone(),
            employee.getScheduleAsString(),
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Employee failed, no rows affected.");
            }
            connection.close();
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Employee employee) throws DAOException {
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Employee failed, no rows affected.");
            } else{
                employee.setId(null);
            }
            connection.close();
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public boolean existUser(String user) throws DAOException {
        Object[] values = {
            user
        };
        
        boolean exist = false;
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_EXIST_USER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            exist = resultSet.next();
            connection.close();
        }catch(SQLException e){
            throw new DAOException(e);
        }
        return exist;
    }
    
    @Override
    public boolean existActive(String user, boolean active) throws DAOException {
        Object[] values = {
            user,
            active
        };
        
        boolean exist = false;
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_EXIST_ACTIVE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            exist = resultSet.next();
            connection.close();
        }catch(SQLException e){
            throw new DAOException(e);
        }
        
        return exist;
    }
    
    @Override
    public void changePassword(Employee employee) throws DAOException {
        if(employee.getId() == null){
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        Object[] values = {
            employee.getPassword(),
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_CHANGE_PASSWORD, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Changing password failed, no rows affected.");
            }
            connection.close();
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Employee.
     * @param employee_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an Employee.
     * @return The mapped Employee from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Employee map(String employee_label, ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt(employee_label+"id"));
        employee.setUser(resultSet.getString(employee_label+"user"));
        employee.setFirst_name(resultSet.getString(employee_label+"first_name"));
        employee.setLast_name(resultSet.getString(employee_label+"last_name"));
        employee.setHire_date(resultSet.getDate(employee_label+"hire_date"));
        
        employee.setEntry_time(resultSet.getTime(employee_label+"entry_time").toLocalTime().format(timeFormat));
        employee.setEnd_time(resultSet.getTime(employee_label+"end_time").toLocalTime().format(timeFormat));
        employee.setBirth_date(resultSet.getDate(employee_label+"birth_date"));
        employee.setCurp(resultSet.getString(employee_label+"curp"));
        employee.setAddress(resultSet.getString(employee_label+"address"));
        employee.setActive(resultSet.getBoolean(employee_label+"active"));
        employee.setAdmin(resultSet.getBoolean(employee_label+"admin"));
        employee.setEmail(resultSet.getString(employee_label+"email"));
        employee.setPhone(resultSet.getString(employee_label+"phone"));
        employee.setScheduleFromString(resultSet.getString(employee_label+"schedule"));
        
        return employee;
    }
    
}
