/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.Module;

/**
 *
 * @author Pavilion Mini
 */
public interface CompanyDAO {
    // Actions ------------------------------------------------------------------------------------
    
    /**
     * Returns the Company from the database matching the given ID, otherwise null.
     * @param id The ID of the Company to be returned.
     * @return The Company from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public Company find(Integer id) throws DAOException;
    
    public void create(Module module) throws IllegalArgumentException, DAOException;
    public void update(Module module) throws IllegalArgumentException, DAOException;
    public void delete(Module module) throws DAOException;
    public List<Company> list() throws DAOException;
    public List<Company> listSupplier() throws DAOException;
    public List<Company> listClient() throws DAOException;
}
