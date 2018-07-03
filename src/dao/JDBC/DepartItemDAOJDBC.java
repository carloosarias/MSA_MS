/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DepartItem;
import model.DepartReport;
import model.IncomingItem;
import model.IncomingReport;
import model.PartRevision;

/**
 *
 * @author Pavilion Mini
 */
public class DepartItemDAOJDBC implements DepartItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id FROM DEPART_ITEM WHERE id = ?";
    private static final String SQL_FIND_DEPART_REPORT_BY_ID =
            "SELECT DEPART_REPORT_ID FROM DEPART_ITEM WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID =
            "SELECT PART_REVISION_ID FROM DEPART_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_DEPART_REPORT_ORDER_BY_ID = 
                "SELECT id FROM DEPART_ITEM WHERE DEPART_REPORT_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO DEPART_ITEM (DEPART_REPORT_ID, PART_REVISION_ID) "
            + "VALUES (?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_ITEM SET PART_REVISION_ID = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM DEPART_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a DepartItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartItem DAO for.
     */
    DepartItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public DepartItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the DepartItem from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The DepartItem from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private DepartItem find(String sql, Object... values) throws DAOException {
        DepartItem depart_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_item;
    }
    
    @Override
    public DepartReport findDepartReport(DepartItem depart_item) throws IllegalArgumentException, DAOException {
        if(depart_item.getId() == null) {
            throw new IllegalArgumentException("DepartItem is not created yet, the DepartItem ID is null.");
        }
        
        DepartReport depart_report = null;
        
        Object[] values = {
            depart_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_DEPART_REPORT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_report = daoFactory.getDepartReportDAO().find(resultSet.getInt("DEPART_REPORT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return depart_report;
    }

    @Override
    public PartRevision findPartRevision(DepartItem depart_item) throws IllegalArgumentException, DAOException {
        if(depart_item.getId() == null) {
            throw new IllegalArgumentException("DepartItem is not created yet, the DepartItem ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            depart_item.getId()
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
    public List<DepartItem> list(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }    
        
        List<DepartItem> depart_item = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DEPART_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_item;
    }

    @Override
    public void create(DepartReport depart_report, PartRevision part_revision, DepartItem depart_item) throws IllegalArgumentException, DAOException {
        if (depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        if(part_revision.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(depart_item.getId() != null){
            throw new IllegalArgumentException("DepartItem is already created, the DepartItem ID is null.");
        }
        
        Object[] values = {
            depart_report.getId(),
            part_revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating DepartItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depart_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating DepartItem  failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(PartRevision part_revision, DepartItem depart_item) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if (depart_item.getId() == null) {
            throw new IllegalArgumentException("DepartItem is not created yet, the DepartItem ID is null.");
        }
        
        Object[] values = {
            part_revision.getId(),
            depart_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating DepartItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(DepartItem depart_item) throws DAOException {
        Object[] values = {
            depart_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting DepartItem failed, no rows affected.");
            } else{
                depart_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartItem.
     * @return The mapped DepartItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartItem map(ResultSet resultSet) throws SQLException{
        DepartItem depart_item = new DepartItem();
        depart_item.setId(resultSet.getInt("id"));
        return depart_item;
    }
}
