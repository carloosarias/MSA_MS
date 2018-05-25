/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.CompanyContactDAO;
import java.util.List;
import model.Company;
import model.CompanyContact;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyContactDAOJDBC implements CompanyContactDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, position, email, phone_number FROM COMPANY_CONTACT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, position, email, phone_number FROM COMPANY_CONTACT WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO COMPANY_CONTACT (COMPANY_ID, name, position, email, phone_number) "
            +"VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE COMPANY_CONTACT name = ?, position = ?, email = ?, phone_number = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM COMPANY_CONTACT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a CompanyContact DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this CompanyAddress DAO for.
     */
    CompanyContactDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public CompanyContact find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CompanyContact> list(Company company) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Company company, CompanyContact contact) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(CompanyContact contact) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(CompanyContact contact) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
