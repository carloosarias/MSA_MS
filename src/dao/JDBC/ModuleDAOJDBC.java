/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ModuleDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ModuleDAOJDBC implements ModuleDAO {
    
    /*
    DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getModule //
CREATE PROCEDURE getModule(IN module_id INT(32))
BEGIN
SELECT `MODULE`.*
FROM `MODULE`
WHERE `MODULE`.ID = module_id;
END //
DROP PROCEDURE IF EXISTS listModule //
CREATE PROCEDURE listModule()
BEGIN
SELECT `MODULE`.*
FROM `MODULE`
WHERE `MODULE`.active = 1
ORDER BY `MODULE`.id;
END //
DROP PROCEDURE IF EXISTS listModuleOfEmployee //
CREATE PROCEDURE listModuleOfEmployee(IN employee_id INT(32))
BEGIN
SELECT `MODULE`.*
FROM MODULE_EMPLOYEE
INNER JOIN `MODULE` ON `MODULE`.id = MODULE_EMPLOYEE.MODULE_ID
WHERE employee_id = MODULE_EMPLOYEE.EMPLOYEE_ID
AND `MODULE`.active = 1
ORDER BY `MODULE`.id;
END //
DELIMITER ;
    */
    
    // Constants ----------------------------------------------------------------------------------
    private final String SQL_getModule =
            "CALL getModule(?)";
    private final String SQL_listModule = 
            "CALL listModule()";
    private final String SQL_listModuleOfEmployee = 
            "CALL listModuleOfEmployee(?)";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Module DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ModuleDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Module find(Integer id) throws DAOException {
        return find(SQL_getModule, id);
    }
    
    /**
     * Returns the Module from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Module from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Module find(String sql, Object... values) throws DAOException {
        Module module = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                module = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return module;
    }
    
    @Override
    public List<Module> list() throws DAOException {
        List<Module> modules = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_listModule);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                modules.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return modules;
    }
    
    @Override
    public List<Module> list(Employee employee) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        List<Module> modules = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_listModuleOfEmployee, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                modules.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return modules;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Module.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Module.
     * @return The mapped Module from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Module map(ResultSet resultSet) throws SQLException{
        Module module = new Module();
        module.setId(resultSet.getInt("MODULE.id"));
        module.setName(resultSet.getString("MODULE.name"));
        return module;
    }
}
