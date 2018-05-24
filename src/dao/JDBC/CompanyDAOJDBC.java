/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.interfaces.CompanyDAO;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyDAOJDBC implements CompanyDAO{
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Employee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    CompanyDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
