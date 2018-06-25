/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.IncomingItem;
import model.IncomingReport;
import model.PartRevision;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingItemDAOJDBC implements IncomingItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_INCOMING_REPORT_BY_ID =
            "SELECT INCOMING_REPORT_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID =
            "SELECT PART_REVISION_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_INCOMING_REPORT_ORDER_BY_ID = 
                "SELECT id FROM INCOMING_ITEM WHERE INCOMING_REPORT_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_ITEM (INCOMING_REPORT_ID, PART_REVISION_ID) "
            + "VALUES (?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_ITEM SET PART_REVISION_ID = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INCOMING_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingItem DAO for.
     */
    IncomingItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public IncomingItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingItem from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingItem from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private IncomingItem find(String sql, Object... values) throws DAOException {
        IncomingItem incoming_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return incoming_item;
    }
    
    @Override
    public IncomingReport findIncomingReport(IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        if(incoming_item.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        IncomingReport incoming_report = null;
        
        Object[] values = {
            incoming_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_INCOMING_REPORT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_report = daoFactory.getIncomingReportDAO().find(resultSet.getInt("INCOMING_REPORT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return incoming_report;
    }

    @Override
    public PartRevision findPartRevision(IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        if(incoming_item.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            incoming_item.getId()
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
    public List<IncomingItem> list(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if(incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }    
        
        List<IncomingItem> incoming_item = new ArrayList<>();
        
        Object[] values = {
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INCOMING_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_item;
    }

    @Override
    public void create(IncomingReport incoming_report, PartRevision part_revision, IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }
        
        if(part_revision.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(incoming_item.getId() != null){
            throw new IllegalArgumentException("IncomingItem is already created, the IncomingItem ID is null.");
        }
        
        Object[] values = {
            incoming_report.getId(),
            part_revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating IncomingItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(PartRevision part_revision, IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        if(part_revision.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if (incoming_item.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        Object[] values = {
            part_revision.getId(),
            incoming_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating IncomingItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(IncomingItem incoming_item) throws DAOException {
        Object[] values = {
            incoming_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting IncomingItem failed, no rows affected.");
            } else{
                incoming_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an IncomingItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an IncomingItem.
     * @return The mapped IncomingItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static IncomingItem map(ResultSet resultSet) throws SQLException{
        IncomingItem incoming_item = new IncomingItem();
        incoming_item.setId(resultSet.getInt("id"));
        return incoming_item;
    }
}
