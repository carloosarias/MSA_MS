/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;


import dao.DAOException;
import dao.interfaces.EmployeeDAO;
import static dao.DAOUtil.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public class EmployeeDAOJDBC implements EmployeeDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, user, first_name, last_name, hire_date, entry_time, end_time, birth_date, curp, address, active FROM EMPLOYEE WHERE id = ?";
    private static final String SQL_FIND_BY_USER_AND_PASSWORD =
            "SELECT id, user, first_name, last_name, hire_date, entry_time, end_time, birth_date, curp, address, active FROM EMPLOYEE WHERE user = ? AND password =  MD5(?)";
    private static final String SQL_LIST_ORDER_BY_ID =
            "SELECT id, user, first_name, last_name, hire_date, entry_time, end_time, birth_date, curp, address, active FROM EMPLOYEE ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID =
            "SELECT id, user, first_name, last_name, hire_date, entry_time, end_time, birth_date, curp, address, active FROM EMPLOYEE WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO EMPLOYEE (user, password, first_name, last_name, entry_time, end_time, hire_date, birth_date, curp, address) "
            +"VALUES (?, MD5(?), ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE EMPLOYEE SET user = ?, first_name = ?, last_name = ?, hire_date = ?, entry_time = ?, end_time = ?, birth_date = ?, curp = ?, address = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM EMPLOYEE WHERE id = ?";
    private static final String SQL_EXIST_USER =
            "SELECT id FROM EMPLOYEE WHERE user = ?";
    private static final String SQL_EXIST_ACTIVE =
            "SELECT id FROM EMPLOYEE WHERE user = ? AND active = ?";
    private static final String SQL_CHANGE_PASSWORD = 
            "UPDATE EMPLOYEE SET password = MD5(?) WHERE id = ?";
    
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
     * @return The Module from the database matching the given SQL query with the given values.
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
                employee = map(resultSet);
            }
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
                employees.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return employees;
    }
    
    @Override
    public List<Employee> list(boolean active) throws DAOException {
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
                employees.add(map(resultSet));
            }
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
            employee.getEntry_time(),
            employee.getEnd_time(),
            toSqlDate(employee.getBirth_date()),
            employee.getCurp(),
            employee.getAddress(),
            employee.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Employee failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Employee failed, no generated key obtained.");
                }
            }
            
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
            employee.getEntry_time(),
            employee.getEnd_time(),
            toSqlDate(employee.getBirth_date()),
            employee.getCurp(),
            employee.getAddress(),
            employee.isActive(),
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
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Employee.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Employee.
     * @return The mapped Employee from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static Employee map(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setUser(resultSet.getString("user"));
        employee.setFirst_name(resultSet.getString("first_name"));
        employee.setLast_name(resultSet.getString("last_name"));
        employee.setHire_date(resultSet.getDate("hire_date"));
        employee.setEntry_time(resultSet.getTime("entry_time"));
        employee.setEnd_time(resultSet.getTime("end_time"));
        employee.setBirth_date(resultSet.getDate("birth_date"));
        employee.setCurp(resultSet.getString("curp"));
        employee.setAddress(resultSet.getString("address"));
        employee.setActive(resultSet.getBoolean("active"));
        return employee;
    }
    
}
