/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.DAOUtil.prepareStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import model.Employee;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ModuleEmployeeDAOJDBC implements ModuleEmployeeDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_LIST_MODULE_ORDER_BY_ID = 
            "SELECT MODULE_ID FROM EMPLOYEE_MODULE WHERE EMPLOYEE_ID = ? ORDER BY MODULE_ID";
    private static final String SQL_LIST_EMPLOYEE_ORDER_BY_ID = 
            "SELECT EMPLOYEE_ID FROM EMPLOYEE_MODULE WHERE MODULE_ID = ? ORDER BY EMPLOYEE_ID";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Employee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ModuleEmployeeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public void create(Module module, Employee employee) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Module> list(Employee employee) throws DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        List<Module> modules = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_MODULE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                modules.add(daoFactory.getModuleDAO().find(resultSet.getInt("MODULE_ID")));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return modules;
    }

    @Override
    public List<Employee> list(Module module) throws DAOException {
        if (module.getId() == null) {
            throw new IllegalArgumentException("Module is not created yet, the Module ID is null.");
        }
        
        List<Employee> employees = new ArrayList<>();
        
        Object[] values = {
            module.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                employees.add(daoFactory.getEmployeeDAO().find(resultSet.getInt("EMPLOYEE_ID")));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return employees;
    }

    @Override
    public List<Employee> listInverse(Module module) throws DAOException {
        List<Employee> employee = list(module);
        employee.removeAll(new HashSet(daoFactory.getModuleDAO().list()));
        return employee;
    }

    @Override
    public List<Module> listInverse(Employee employee) throws DAOException {
        List<Module> modules = list(employee);
        modules.removeAll(new HashSet(daoFactory.getModuleDAO().list()));
        return modules;
    }

    @Override
    public void delete(Module module, Employee employee) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
