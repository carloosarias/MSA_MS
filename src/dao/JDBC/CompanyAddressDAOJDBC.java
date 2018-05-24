/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.interfaces.CompanyAddressDAO;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyAddressDAOJDBC implements CompanyAddressDAO {

    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct an Employee DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    CompanyAddressDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
