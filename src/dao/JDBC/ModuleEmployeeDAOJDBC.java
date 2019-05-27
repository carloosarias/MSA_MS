/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.ModuleEmployeeDAO;
import static dao.DAOUtil.prepareStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Employee;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public class ModuleEmployeeDAOJDBC implements ModuleEmployeeDAO {
    /*
    DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS createModuleEmployee //
CREATE PROCEDURE createModuleEmployee(IN module_id INT(32), IN employee_id INT(32))
BEGIN
INSERT INTO MODULE_EMPLOYEE (MODULE_EMPLOYEE.MODULE_ID, MODULE_EMPLOYEE.EMPLOYEE_ID) 
VALUES (module_id,employee_id);
END //
DROP PROCEDURE IF EXISTS deleteModuleEmployee //
CREATE PROCEDURE deleteModuleEmployee(IN module_id INT(32), IN employee_id INT(32))
BEGIN
DELETE FROM MODULE_EMPLOYEE 
WHERE MODULE_EMPLOYEE.MODULE_ID = module_id 
AND MODULE_EMPLOYEE.EMPLOYEE_ID = employee_id;
END //
DELIMITER ;
    */
    
    // Constants ----------------------------------------------------------------------------------
    private final String SQL_createModuleEmployee =
            "CALL createModuleEmployee(?,?)";
    private final String SQL_deleteModuleEmployee =
            "CALL deleteModuleEmployee(?,?)";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an ModuleEmployee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    ModuleEmployeeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public void create(Module module, Employee employee) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        if (module.getId() == null) {
            throw new IllegalArgumentException("Module is not created yet, the Module ID is null.");
        }
        
        Object[] values = {
            module.getId(),
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_createModuleEmployee, false, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Module Employee failed, no rows affected.");
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }  
    }
    
    @Override
    public void delete(Module module, Employee employee) throws DAOException {
        Object[] values = {
            module.getId(),
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_deleteModuleEmployee, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Module Employee failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
}
