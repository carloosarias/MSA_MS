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
import model.Metal;
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
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.id = ?";
    private static final String SQL_FIND_BY_PART_REV = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? AND PART_REVISION.rev = ?";
    private static final String SQL_FIND_PRODUCT_PART_BY_ID = 
            "SELECT PRODUCT_PART_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_SPECIFICATION_BY_ID = 
            "SELECT SPECIFICATION_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_BASE_METAL_BY_ID = 
            "SELECT BASE_METAL_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "ORDER BY PRODUCT_PART.part_number, PART_REVISION.rev_date";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.active = ? "
            + "ORDER BY PRODUCT_PART.part_number, PART_REVISION.rev_date";
    private static final String SQL_LIST_OF_PART_ORDER_BY_ID = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? "
            + "ORDER BY PRODUCT_PART.part_number, PART_REVISION.rev_date";
    private static final String SQL_LIST_ACTIVE_OF_PART_ORDER_BY_ID = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? AND PART_REVISION.active = ? "
            + "ORDER BY PRODUCT_PART.part_number, PART_REVISION.rev_date";
    private static final String SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT PART_REVISION.id, PART_REVISION.rev, PART_REVISION.rev_date, PART_REVISION.area, PART_REVISION.base_weight, PART_REVISION.final_weight, PART_REVISION.active, "
            + "PRODUCT_PART.part_number, METAL.metal_name, SPECIFICATION.process, SPECIFICATION.specification_number "
            + "FROM PART_REVISION "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE PART_REVISION.SPECIFICATION_ID = ? "
            + "ORDER BY PRODUCT_PART.part_number, PART_REVISION.rev_date";
    private static final String SQL_INSERT = 
            "INSERT INTO PART_REVISION (PRODUCT_PART_ID, SPECIFICATION_ID, BASE_METAL_ID, rev, rev_date, area, base_weight, final_weight, active) "
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
    public PartRevision find(ProductPart part, String rev) throws IllegalArgumentException, DAOException {
        if(part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        return find(SQL_FIND_BY_PART_REV, part.getId(), rev);
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
                part_revision = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return part_revision;
    }
    
    @Override
    public ProductPart findProductPart(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        ProductPart part = null;
        
        Object[] values = {
            part_revision.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_PART_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part = daoFactory.getProductPartDAO().find(resultSet.getInt("PRODUCT_PART_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return part;
    }
    
    @Override
    public Specification findSpecification(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Specification specification = null;
        
        Object[] values = {
            part_revision.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_SPECIFICATION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification = daoFactory.getSpecificationDAO().find(resultSet.getInt("SPECIFICATION_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return specification;
    }
    
    @Override
    public Metal findMetal(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Metal metal = null;
        
        Object[] values = {
            part_revision.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_BASE_METAL_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                metal = daoFactory.getMetalDAO().find(resultSet.getInt("BASE_METAL_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return metal;
    }
    
    @Override
    public List<PartRevision> list() throws DAOException {
        List<PartRevision> revisions = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;
    }

    @Override
    public List<PartRevision> list(boolean active) throws DAOException {
        List<PartRevision> revisions = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;
    }
    
    @Override
    public List<PartRevision> list(ProductPart part) throws IllegalArgumentException, DAOException {
        if(part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<PartRevision> revisions = new ArrayList<>();
        
        Object[] values = {
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PART_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;
    }

    @Override
    public List<PartRevision> list(ProductPart part, boolean active) throws IllegalArgumentException, DAOException {
        if(part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<PartRevision> revisions = new ArrayList<>();
        
        Object[] values = {
            part.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_PART_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;        
    }

    @Override
    public List<PartRevision> listOfSpecification(Specification specification) throws DAOException { 
        if(specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }    
        
        List<PartRevision> revisions = new ArrayList<>();
        
        Object[] values = {
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;
    }

    @Override
    public void create(ProductPart part, Specification specification, Metal metal, PartRevision revision) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        if (specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        if (metal.getId() == null) {
            throw new IllegalArgumentException("Metal is not created yet, the Metal ID is null.");
        }
        
        if(revision.getId() != null){
            throw new IllegalArgumentException("PartRevision is already created, the PartRevision ID is not null.");
        }
        
        Object[] values = {
            part.getId(),
            specification.getId(),
            metal.getId(),
            revision.getRev(),
            DAOUtil.toSqlDate(revision.getRev_date()),
            revision.getArea(),
            revision.getBase_weight(),
            revision.getFinal_weight(),
            revision.isActive()
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
                    revision.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating PartRevision failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(PartRevision revision) throws IllegalArgumentException, DAOException {
        if (revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Object[] values = {
            revision.getRev(),
            DAOUtil.toSqlDate(revision.getRev_date()),
            revision.getArea(),
            revision.getBase_weight(),
            revision.getFinal_weight(),
            revision.isActive(),
            revision.getId()
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
    public void delete(PartRevision revision) throws DAOException {
        Object[] values = {
            revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting PartRevision failed, no rows affected.");
            } else{
                revision.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static PartRevision map(ResultSet resultSet) throws SQLException{
        PartRevision revision = new PartRevision();
        revision.setId(resultSet.getInt("PART_REVISION.id"));
        revision.setRev(resultSet.getString("PART_REVISION.rev"));
        revision.setRev_date(resultSet.getDate("PART_REVISION.rev_date"));
        revision.setArea(resultSet.getDouble("PART_REVISION.area"));
        revision.setBase_weight(resultSet.getDouble("PART_REVISION.base_weight"));
        revision.setFinal_weight(resultSet.getDouble("PART_REVISION.final_weight"));
        revision.setActive(resultSet.getBoolean("PART_REVISION.active"));
        
        //INNER JOINS
        revision.setPart_number(resultSet.getString("PRODUCT_PART.part_number"));
        revision.setMetal_metalname(resultSet.getString("METAL.metal_name"));
        revision.setSpecification_specificationnumber(resultSet.getString("SPECIFICATION.specification_number"));
        revision.setSpecification_process(resultSet.getString("SPECIFICATION.process"));
        return revision;
    }
}
