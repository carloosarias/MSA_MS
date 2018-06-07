/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.PartRevisionDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.PartRevision;
import model.ProductPart;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class PartRevisionDAOJDBC implements PartRevisionDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_BY_PART_REV = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PRODUCT_PART_ID = ? AND rev = ?";
    private static final String SQL_FIND_PRODUCT_BY_ID = 
            "SELECT PRODUCT_PART_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_SPECIFICATION_BY_ID = 
            "SELECT SPECIFICATION_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE active = ? ORDER BY id";
    private static final String SQL_LIST_OF_PART_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PRODUCT_PART_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_PART_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PRODUCT_PART_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO PART_REVISION (rev, rev_date, base_metal, area, base_weight, final_weight, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PART_REVISION SET rev = ?, rev_date = ?, base_metal = ?, area = ?, base_weight = ?, final_weight = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM PART_REVISION WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a PartRevision DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this PartRevision DAO for.
     */
    PartRevisionDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    @Override
    public PartRevision find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PartRevision find(ProductPart part, String rev) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductPart findProductPart(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Specification findSpecification(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list(boolean active) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list(ProductPart part) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list(ProductPart part, boolean active) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list(Specification specification) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PartRevision> list(Specification specification, boolean active) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(ProductPart part, Specification specification, PartRevision part_revision) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(PartRevision part_revision) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static PartRevision map(ResultSet resultSet) throws SQLException{
        PartRevision part_revision = new PartRevision();
        part_revision.setId(resultSet.getInt("id"));
        part_revision.setRev(resultSet.getString("rev"));
        part_revision.setRev_date(resultSet.getDate("rev_date"));
        part_revision.setBase_metal(resultSet.getString("base_metal"));
        part_revision.setArea(resultSet.getDouble("area"));
        part_revision.setBase_weight(resultSet.getDouble("base_weight"));
        part_revision.setFinal_weight(resultSet.getDouble("final_weight"));
        part_revision.setActive(resultSet.getBoolean("active"));
        return part_revision;
    }    
}
