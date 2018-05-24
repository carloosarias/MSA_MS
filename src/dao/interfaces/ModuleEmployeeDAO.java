/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Employee;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public interface ModuleEmployeeDAO {
    
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns a list of all Modules Employee is part of from the database ordered by user ID. The list is never null and
     * is empty when the database does not contain any Employee.
     * @param employee The employee to be searched for.
     * @return A list of Modules Employee is part of from the database ordered by Module ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Module> list(Employee employee) throws DAOException;
    
    /**
     * Returns a list of Employee that are part of Module from the database ordered by Employee ID. The list is never null and
     * is empty when the database does not contain any Module.
     * @param module The module to be searched for.
     * @return A list of Employee that are part of Module from the database ordered by Employee ID.
     * @throws DAOException If something fails at database level.
     */ 
    public List<Employee> list(Module module) throws DAOException;
    
    /**
     * Returns a list of Employee that are NOT part of Module from the database ordered by Employee ID. The list is never null and
     * is empty when the database does not contain any Module.
     * @param module The module to be searched for.
     * @return A list of Employee that are NOT part of Module from the database ordered by Employee ID.
     * @throws DAOException If something fails at database level.
     */ 
    public List<Employee> listInverse(Module module) throws DAOException;
    
    /**
     * Returns a list of all Modules Employee is NOT part of from the database ordered by user ID. The list is never null and
     * is empty when the database does not contain any Employee.
     * @param employee The employee to be searched for.
     * @return A list of Modules Employee is NOT part of from the database ordered by Module ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Module> listInverse(Employee employee) throws DAOException;
    
    /**
     * Create the given Module_employee in the database. The Module ID and Employee ID must not be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Module.
     * @param module The Module to be registered.
     * @param employee The Employee to be registered
     * @throws IllegalArgumentException If the Module ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Module module, Employee employee) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given Module from the database. After deleting, the DAO will set the ID of the given
     * Module to null.
     * @param module The Module to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Module module, Employee employee) throws DAOException;
}
