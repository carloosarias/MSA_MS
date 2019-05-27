/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import model.Employee;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public interface ModuleEmployeeDAO {
    
    // Actions ------------------------------------------------------------------------------------
     
    /**
     * Grant Employee access to Module.
     * @param module
     * @param employee
     * @throws IllegalArgumentException
     * @throws DAOException
     */
    public void create(Module module, Employee employee) throws IllegalArgumentException, DAOException;
    
    /**
     * Remove access to Module from Employee.
     * @param module
     * @param employee
     * @throws DAOException
     */
    public void delete(Module module, Employee employee) throws DAOException;
}
