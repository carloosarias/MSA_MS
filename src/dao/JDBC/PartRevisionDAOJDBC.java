/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.PartRevisionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.Metal;
import model.PartRevision;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class PartRevisionDAOJDBC implements PartRevisionDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT * FROM PART_REVISION INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.id = ?";
    private static final String SQL_FIND_BY_PATTERN = 
            "SELECT * FROM PART_REVISION INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE (COMPANY.id = ? AND COMPANY.active = 1) AND (PRODUCT_PART.part_number = ? AND PRODUCT_PART.active = 1) AND (PART_REVISION.rev = ? AND PART_REVISION.active = 1)";
    private static final String SQL_LIST_ACTIVE = 
            "SELECT * FROM PART_REVISION INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.active = 1 AND PRODUCT_PART.active = 1 AND COMPANY.active = 1 AND METAL.active = 1 AND SPECIFICATION.active = 1 "
            + "ORDER BY COMPANY.name, PRODUCT_PART.part_number, PART_REVISION.rev";
    private static final String SQL_LIST_ACTIVE_FILTER = 
            "SELECT * FROM PART_REVISION INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.active = 1 AND (PRODUCT_PART.part_number LIKE ? AND PRODUCT_PART.active = 1) AND ((COMPANY.id = ? OR ? IS NULL) AND COMPANY.active = 1) "
            + "AND ((METAL.id = ? OR ? IS NULL) AND METAL.active = 1) AND ((SPECIFICATION.id = ? OR ? IS NULL) AND SPECIFICATION.active = 1) "
            + "ORDER BY COMPANY.name, PRODUCT_PART.part_number, PART_REVISION.rev";
    private static final String SQL_INSERT = 
            "INSERT INTO PART_REVISION (PRODUCT_PART_ID, BASE_METAL_ID, SPECIFICATION_ID, rev, rev_date, area, base_weight, final_weight, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PART_REVISION SET rev = ?, rev_date = ?, area = ?, base_weight = ?, final_weight = ?, active = ? WHERE id = ?";
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
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public PartRevision find(Company company, String part_number, String rev) throws DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }   
        return find(SQL_FIND_BY_PATTERN, company, part_number, rev);
    }
    
    /**
     * Returns the ProductPart from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProductPart from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private PartRevision find(String sql, Object... values) throws DAOException {
        PartRevision part_revision = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part_revision = map("PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return part_revision;
    }

    @Override
    public List<PartRevision> list() throws DAOException {
        List<PartRevision> partrevision_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ACTIVE);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                partrevision_list.add(map("PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return partrevision_list;
    }
    
    @Override
    public List<PartRevision> list(Company company, Metal metal, Specification specification, String pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company();
        if(metal == null) metal = new Metal();
        if(specification == null) specification = new Specification();
        
        List<PartRevision> partrevision_list = new ArrayList<>();
        
        Object[] values = {
            pattern+"%",
            company.getId(),
            company.getId(),
            metal.getId(),
            metal.getId(),
            specification.getId(),
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                partrevision_list.add(map("PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return partrevision_list;
    }

    @Override
    public void create(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() != null){
            throw new IllegalArgumentException("PartRevision is already created, the PartRevision ID is not null.");
        }
        
        Object[] values = {
            part_revision.getProduct_part().getId(),
            part_revision.getMetal().getId(),
            part_revision.getSpecification().getId(),
            part_revision.getRev(),
            DAOUtil.toSqlDate(part_revision.getRev_date()),
            part_revision.getArea(),
            part_revision.getBase_weight(),
            part_revision.getFinal_weight(),
            part_revision.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating PartRevision failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    part_revision.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating PartRevision failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if (part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Object[] values = {
            part_revision.getRev(),
            DAOUtil.toSqlDate(part_revision.getRev_date()),
            part_revision.getArea(),
            part_revision.getBase_weight(),
            part_revision.getFinal_weight(),
            part_revision.isActive(),
            part_revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating PartRevision failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(PartRevision part_revision) throws DAOException {
        Object[] values = {
            part_revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting PartRevision failed, no rows affected.");
            } else{
                part_revision.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param partrevision_label
     * @param productpart_label
     * @param productpartcompany_label
     * @param metal_label
     * @param specification_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static PartRevision map(String partrevision_label, String productpart_label, String company_label, String metal_label, String specification_label, ResultSet resultSet) throws SQLException{
        PartRevision part_revision = new PartRevision();
        part_revision.setId(resultSet.getInt(partrevision_label+"id"));
        part_revision.setRev(resultSet.getString(partrevision_label+"rev"));
        part_revision.setRev_date(resultSet.getDate(partrevision_label+"rev_date"));
        part_revision.setArea(resultSet.getDouble(partrevision_label+"area"));
        part_revision.setBase_weight(resultSet.getDouble(partrevision_label+"base_weight"));
        part_revision.setFinal_weight(resultSet.getDouble(partrevision_label+"final_weight"));
        part_revision.setActive(resultSet.getBoolean(partrevision_label+"active"));
        part_revision.setProduct_part(ProductPartDAOJDBC.map(productpart_label, company_label, resultSet));
        part_revision.setMetal(MetalDAOJDBC.map(metal_label, resultSet));
        part_revision.setSpecification(SpecificationDAOJDBC.map(specification_label, resultSet));
        
        return part_revision;
    }
}
