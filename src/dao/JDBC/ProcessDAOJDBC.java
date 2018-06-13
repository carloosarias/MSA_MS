/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.ProcessDAO;
import java.util.List;
import model.Process;

/**
 *
 * @author Pavilion Mini
 */
public class ProcessDAOJDBC implements ProcessDAO {
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a PartRevision DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this PartRevision DAO for.
     */
    ProcessDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    // Actions ------------------------------------------------------------------------------------    
    @Override
    public Process find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Process find(String name) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Process> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Process> listActive(boolean active) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Process process) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Process process) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Process process) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
