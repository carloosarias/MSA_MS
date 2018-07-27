/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ProcessReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Container;
import model.Employee;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class ProcessReportDAOJDBC implements ProcessReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID = 
            "SELECT PART_REVISION_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_FIND_CONTAINER_BY_ID = 
            "SELECT TANK_ID, CONTAINER_ID FROM PART_REVISION WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE active = ? ORDER BY id";
    private static final String SQL_LIST_OF_PART_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PRODUCT_PART_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_PART_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE PRODUCT_PART_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active FROM PART_REVISION WHERE SPECIFICATION_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO PART_REVISION (PRODUCT_PART_ID, SPECIFICATION_ID, rev, rev_date, final_process, base_metal, area, base_weight, final_weight, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PART_REVISION SET rev = ?, rev_date = ?, final_process = ?, base_metal = ?, area = ?, base_weight = ?, final_weight = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM PART_REVISION WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProcessReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ProcessReport DAO for.
     */
    ProcessReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ProcessReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ProcessReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProcessReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ProcessReport find(String sql, Object... values) throws DAOException {
        ProcessReport process_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                process_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return process_report;
    }
    
    @Override
    public Employee findEmployee(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_EMPLOYEE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                employee = daoFactory.getEmployeeDAO().find(resultSet.getInt("EMPLOYEE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return employee;
    }
    
    @Override
    public PartRevision findPartRevision(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PART_REVISION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part_revision = daoFactory.getPartRevisionDAO().find(resultSet.getInt("PART_REVISION_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return part_revision;
    }
    
    @Override
    public Container findTank(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Container container = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_CONTAINER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                container = daoFactory.getContainerDAO().find(resultSet.getInt("TANK_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return container;
    }
    @Override
    public Container findContainer(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Container container = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_CONTAINER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                container = daoFactory.getContainerDAO().find(resultSet.getInt("CONTAINER_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return container;
    }
    
    @Override
    public List<ProcessReport> list() throws DAOException {
        List<ProcessReport> process_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;
    }

    //CONTINUE HERE-0-0-0-0-0-0-0-0-0--0-0-0-0-0-0
    @Override
    public List<ProcessReport> listEmployee(Employee employee) throws DAOException {
        List<PartRevision> revisions = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
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
    public List<PartRevision> list(Specification specification) throws IllegalArgumentException, DAOException {
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
    public List<PartRevision> list(Specification specification, boolean active) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }    
        
        List<PartRevision> revisions = new ArrayList<>();
        
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
                revisions.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return revisions;
    }

    @Override
    public void create(ProductPart part, Specification specification, PartRevision revision) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        if(revision.getId() != null){
            throw new IllegalArgumentException("PartRevision is already created, the PartRevision ID is null.");
        }
        
        Object[] values = {
            part.getId(),
            specification.getId(),
            revision.getRev(),
            DAOUtil.toSqlDate(revision.getRev_date()),
            revision.getFinal_process(),
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
            revision.getFinal_process(),
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
        revision.setFinal_process(resultSet.getString("final_process"));
        revision.setBase_metal(resultSet.getString("base_metal"));
        revision.setArea(resultSet.getDouble("area"));
        revision.setBase_weight(resultSet.getDouble("base_weight"));
        revision.setFinal_weight(resultSet.getDouble("final_weight"));
        revision.setActive(resultSet.getBoolean("active"));
        return revision;
    }    
}
