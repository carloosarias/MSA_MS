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
public interface ModuleDAO {
    // Actions ------------------------------------------------------------------------------------
   
    /**
     * Return Module From ID.
     * @param id
     * @return Module
     * @throws DAOException
     */
    public Module find(Integer id) throws DAOException;
    
    /**
     * Returns List.
     * @return List(Of Module)
     * @throws DAOException
     */    
    public List<Module> list() throws DAOException;
    
    /**
     * Returns List From Employee.
     * @param employee
     * @return List(Of Module)
     * @throws IllegalArgumentException
     * @throws DAOException 
     */
    public List<Module> list(Employee employee) throws IllegalArgumentException, DAOException;
    
}
