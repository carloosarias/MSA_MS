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
import model.PartRevision;
import model.ProductPart;
import model.Specification;
import model.Process;

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
    private static final String SQL_FIND_PRODUCT_PART_BY_ID = 
            "SELECT PRODUCT_PART_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_PROCESS_BY_ID = 
            "SELECT PROCESS_ID FROM PART_REVISION WHERE id = ?";
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
    private static final String SQL_LIST_OF_PROCESS_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PROCESS_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_PROCESS_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PROCESS_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO PART_REVISION (PRODUCT_PART_ID, PROCESS_ID, SPECIFICATION_ID, rev, rev_date, base_metal, area, base_weight, final_weight, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            throw new IllegalArgumentException("ProductRevision is not created yet, the ProductRevision ID is null.");
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
    public Process findProcess(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("ProductRevision is not created yet, the ProductRevision ID is null.");
        }
        
        Process process = null;
        
        Object[] values = {
            part_revision.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PROCESS_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                process = daoFactory.getProcessDAO().find(resultSet.getInt("PROCESS_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return process;
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
    public List<PartRevision> list() throws DAOException {
        List<PartRevision> part_revision = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<PartRevision> list(boolean active) throws DAOException {
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }
    @Override
    public List<PartRevision> list(ProductPart part) throws IllegalArgumentException, DAOException {
        if(part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PART_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<PartRevision> list(ProductPart part, boolean active) throws IllegalArgumentException, DAOException {
        if(part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
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
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;        
    }

    @Override
    public List<PartRevision> list(Process process) throws IllegalArgumentException, DAOException {
        if(process.getId() == null) {
            throw new IllegalArgumentException("Process is not created yet, the Process ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            process.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PROCESS_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<PartRevision> list(Process process, boolean active) throws IllegalArgumentException, DAOException {
        if(process.getId() == null) {
            throw new IllegalArgumentException("Process is not created yet, the Process ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            process.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_PROCESS_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<PartRevision> list(Specification specification) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<PartRevision> list(Specification specification, boolean active) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }    
        
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            specification.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public void create(ProductPart part, Process process, Specification specification, PartRevision revision) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        if(process.getId() == null){
            throw new IllegalArgumentException("Process is not created yet, the Process ID is null.");
        }   
        
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        if(revision.getId() != null){
            throw new IllegalArgumentException("PartRevision is already created, the PartRevision ID is null.");
        }
        
        Object[] values = {
            part.getId(),
            process.getId(),
            specification.getId(),
            revision.getRev(),
            DAOUtil.toSqlDate(revision.getRev_date()),
            revision.getBase_metal(),
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
                    part.setId(generatedKeys.getInt(1));
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
            revision.getBase_metal(),
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
        revision.setId(resultSet.getInt("id"));
        revision.setRev(resultSet.getString("rev"));
        revision.setRev_date(resultSet.getDate("rev_date"));
        revision.setBase_metal(resultSet.getString("base_metal"));
        revision.setArea(resultSet.getDouble("area"));
        revision.setBase_weight(resultSet.getDouble("base_weight"));
        revision.setFinal_weight(resultSet.getDouble("final_weight"));
        revision.setActive(resultSet.getBoolean("active"));
        return revision;
    }    
}
