/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Company;
import model.POQuery;

/**
 *
 * @author Pavilion Mini
 */
public interface POQueryDAO {
    
    // Actions ------------------------------------------------------------------------------------
    
    public List<POQuery> list(String ponumber_pattern, Company company, String partnumber_pattern) throws IllegalArgumentException, DAOException;
    
    public List<POQuery> listAvailable(Company company, String partnumber_pattern, String rev_pattern) throws IllegalArgumentException, DAOException;
}
