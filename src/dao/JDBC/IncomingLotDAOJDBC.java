/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingLotDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.DepartLot;
import model.IncomingLot;
import model.IncomingReport;
import model.PartRevision;
import model.ProductPart;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingLotDAOJDBC implements IncomingLotDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT INCOMING_LOT.id, INCOMING_LOT.lot_number, INCOMING_LOT.quantity, INCOMING_LOT.box_quantity, INCOMING_LOT.status, INCOMING_LOT.comments, "
            + "PART_REVISION.rev, PRODUCT_PART.part_number "
            + "FROM INCOMING_LOT "
            + "INNER JOIN PART_REVISION ON INCOMING_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "WHERE INCOMING_LOT.id = ?";
    private static final String SQL_FIND_INCOMING_REPORT_BY_ID =
            "SELECT INCOMING_REPORT_ID FROM INCOMING_LOT WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID = 
            "SELECT PART_REVISION_ID FROM INCOMING_LOT WHERE id = ?";
    private static final String SQL_FIND_DEPART_LOT_BY_ID = 
            "SELECT DEPART_LOT_ID FROM INCOMING_LOT WHERE id = ?";
    private static final String SQL_LIST_OF_INCOMING_REPORT_ORDER_BY_ID = 
            "SELECT INCOMING_LOT.id, INCOMING_LOT.lot_number, INCOMING_LOT.quantity, INCOMING_LOT.box_quantity, INCOMING_LOT.status, INCOMING_LOT.comments, "
            + "PART_REVISION.rev, PRODUCT_PART.part_number "
            + "FROM INCOMING_LOT "
            + "INNER JOIN PART_REVISION ON INCOMING_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "WHERE INCOMING_REPORT_ID = ? "
            + "ORDER BY id";
    private static final String SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID = 
            "SELECT INCOMING_LOT.id, INCOMING_LOT.lot_number, INCOMING_LOT.quantity, INCOMING_LOT.box_quantity, INCOMING_LOT.status, INCOMING_LOT.comments, "
            + "PART_REVISION.rev, PRODUCT_PART.part_number "
            + "FROM INCOMING_LOT "
            + "INNER JOIN PART_REVISION ON INCOMING_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "WHERE lot_number = ? "
            + "ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO INCOMING_LOT (INCOMING_REPORT_ID, DEPART_LOT_ID, PART_REVISION_ID, lot_number, quantity, box_quantity, status, comments) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_LOT SET lot_number = ?, quantity = ?, box_quantity = ?, status = ?, comments = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INCOMING_LOT WHERE id = ?";
    private static final String LIST_INCOMING_LOT_BY_PRODUCT_PART_DATE_RANGE = 
            "SELECT INCOMING_LOT.id, INCOMING_LOT.lot_number, INCOMING_LOT.quantity, INCOMING_LOT.box_quantity, INCOMING_LOT.status, INCOMING_LOT.comments "
            + "FROM INCOMING_LOT "
            + "INNER JOIN PART_REVISION ON INCOMING_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN INCOMING_REPORT ON INCOMING_LOT.INCOMING_REPORT_ID = INCOMING_REPORT.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? AND INCOMING_REPORT.discrepancy = ? AND INCOMING_REPORT.report_date BETWEEN ? AND ? "
            + "ORDER BY INCOMING_REPORT.report_date, INCOMING_LOT.INCOMING_REPORT_ID, INCOMING_LOT.id";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingLot DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingLot DAO for.
     */
    IncomingLotDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public IncomingLot find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingLot from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingLot from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private IncomingLot find(String sql, Object... values) throws DAOException {
        IncomingLot incoming_lot = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_lot = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return incoming_lot;
    }
    
    @Override
    public IncomingReport findIncomingReport(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if(incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        IncomingReport incoming_report = null;
        
        Object[] values = {
            incoming_lot.getId()
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
    public PartRevision findPartRevision(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if(incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            incoming_lot.getId()
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
    public DepartLot findDepartLot(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if(incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        DepartLot depart_lot = null;
        
        Object[] values = {
            incoming_lot.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_DEPART_LOT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_lot = daoFactory.getDepartLotDAO().find(resultSet.getInt("DEPART_LOT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return depart_lot;        
    }
    
    @Override
    public List<IncomingLot> list(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if(incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }    
        
        List<IncomingLot> incoming_lot = new ArrayList<>();
        
        Object[] values = {
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INCOMING_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_lot;
    }
    
    @Override
    public List<IncomingLot> list(String lot_number){
        
        List<IncomingLot> incoming_lot = new ArrayList<>();
        
        Object[] values = {
            lot_number
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_lot;
    }
    
    @Override
    public void create(IncomingReport incoming_report, PartRevision part_revision, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(incoming_lot.getId() != null){
            throw new IllegalArgumentException("IncomingLot is already created, the IncomingLot ID is null.");
        }
        
        Object[] values = {
            incoming_report.getId(),
            null,
            part_revision.getId(),
            incoming_lot.getLot_number(),
            incoming_lot.getQuantity(),
            incoming_lot.getBox_quantity(),
            incoming_lot.getStatus(),
            incoming_lot.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingLot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_lot.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating IncomingLot failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }
    
    @Override
    public void create(IncomingReport incoming_report, DepartLot depart_lot, PartRevision part_revision, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingItem is not created yet, the IncomingItem ID is null.");
        }
        
        if (depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        if(depart_lot.isRejected()){
            throw new IllegalArgumentException("This DepartLot has already been marked as rejected!");
        }
        
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(incoming_lot.getId() != null){
            throw new IllegalArgumentException("IncomingLot is already created, the IncomingLot ID is null.");
        }
        
        Object[] values = {
            incoming_report.getId(),
            depart_lot.getId(),
            part_revision.getId(),
            incoming_lot.getLot_number(),
            incoming_lot.getQuantity(),
            incoming_lot.getBox_quantity(),
            incoming_lot.getStatus(),
            incoming_lot.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingLot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_lot.setId(generatedKeys.getInt(1));
                    daoFactory.getDepartLotDAO().update(depart_lot);
                } else {
                    throw new DAOException("Creating IncomingLot failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException {
        
        if (incoming_lot.getId() == null) {
            throw new IllegalArgumentException("IncomingLot is not created yet, the IncomingLot ID is null.");
        }
        
        Object[] values = {
            incoming_lot.getLot_number(),
            incoming_lot.getQuantity(),
            incoming_lot.getBox_quantity(),
            incoming_lot.getStatus(),
            incoming_lot.getComments(),
            incoming_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating IncomingLot failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(IncomingLot incoming_lot) throws DAOException {
        Object[] values = {
            incoming_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting IncomingLot failed, no rows affected.");
            } else{
                incoming_lot.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    @Override
    public List<IncomingLot> listDateRange(ProductPart product_part, boolean discrepancy, Date start, Date end){
        
        List<IncomingLot> incominglot_list = new ArrayList<>();
        
        Object[] values = {
            product_part.getId(),
            discrepancy,
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, LIST_INCOMING_LOT_BY_PRODUCT_PART_DATE_RANGE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incominglot_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incominglot_list;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an IncomingLot.
     * @param resultSet The ResultSet of which the current row is to be mapped to an IncomingLot.
     * @return The mapped IncomingLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static IncomingLot map(ResultSet resultSet) throws SQLException{
        IncomingLot incoming_lot = new IncomingLot();
        incoming_lot.setId(resultSet.getInt("INCOMING_LOT.id"));
        incoming_lot.setLot_number(resultSet.getString("INCOMING_LOT.lot_number"));
        incoming_lot.setQuantity(resultSet.getInt("INCOMING_LOT.quantity"));
        incoming_lot.setBox_quantity(resultSet.getInt("INCOMING_LOT.box_quantity"));
        incoming_lot.setStatus(resultSet.getString("INCOMING_LOT.status"));
        incoming_lot.setComments(resultSet.getString("INCOMING_LOT.comments"));
        
        //INNER JOINS
        incoming_lot.setPart_number(resultSet.getString("PRODUCT_PART.part_number"));
        incoming_lot.setPart_revision(resultSet.getString("PART_REVISION.rev"));
        return incoming_lot;
    }
}
