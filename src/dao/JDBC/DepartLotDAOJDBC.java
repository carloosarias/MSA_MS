/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartLotDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.DepartLot;
import model.DepartReport;
import model.PartRevision;
import model.ProductPart;

/**
 *
 * @author Pavilion Mini
 */
public class DepartLotDAOJDBC implements DepartLotDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, process, comments, rejected, pending FROM DEPART_LOT WHERE id = ?";
    private static final String SQL_FIND_DEPART_REPORT_BY_ID =
            "SELECT DEPART_REPORT_ID FROM DEPART_LOT WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID = 
            "SELECT PART_REVISION_ID FROM DEPART_LOT WHERE id = ?";
    private static final String SQL_LIST_OF_DEPART_REPORT_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, process, comments, rejected, pending FROM DEPART_LOT WHERE DEPART_REPORT_ID = ? ORDER BY id";
    private static final String SQL_LIST_OF_LOT_NUMBER_ORDER_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, process, comments, rejected, pending FROM DEPART_LOT WHERE lot_number = ? ORDER BY id";
    private static final String SQL_LIST_OF_DEPART_REPORT_REJECTED_ORDER_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, process, comments, rejected, pending FROM DEPART_LOT WHERE DEPART_REPORT_ID = ? AND rejected = ? ORDER BY id";
    private static final String SQL_LIST_OF_PENDING_REJECTED_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, process, comments, rejected, pending FROM DEPART_LOT WHERE rejected = ? AND pending = ? ORDER BY id";
    private static final String SQL_LIST_PART_REVISIONS = 
            "SELECT DISTINCT PART_REVISION_ID FROM DEPART_LOT WHERE DEPART_REPORT_ID = ?";
    private static final String SQL_LIST_DEPART_REPORTS = 
            "SELECT DISTINCT DEPART_REPORT_ID FROM DEPART_LOT WHERE rejected = ?";
    private static final String SQL_INSERT =
            "INSERT INTO DEPART_LOT (DEPART_REPORT_ID, PART_REVISION_ID, lot_number, quantity, box_quantity, process, comments, rejected, pending) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_LOT SET lot_number = ?, quantity = ?, box_quantity = ?, process = ?, comments = ?, rejected = ?, pending = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM DEPART_LOT WHERE id = ?";
    private static final String LIST_DEPART_LOT_BY_PRODUCT_PART_DATE_RANGE = 
            "SELECT DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.id, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, DEPART_LOT.process, DEPART_LOT.comments, DEPART_LOT.rejected, DEPART_LOT.pending "
            + "FROM DEPART_LOT "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN DEPART_REPORT ON DEPART_LOT.DEPART_REPORT_ID = DEPART_REPORT.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? AND DEPART_REPORT.report_date BETWEEN ? AND ? "
            + "ORDER BY DEPART_REPORT.report_date, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.id";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a DepartLot DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartLot DAO for.
     */
    DepartLotDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
    
    @Override
    public DepartLot find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the DepartLot from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The DepartLot from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private DepartLot find(String sql, Object... values) throws DAOException {
        DepartLot depart_lot = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_lot = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_lot;
    }
    
    @Override
    public DepartReport findDepartReport(DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        if(depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        DepartReport depart_report = null;
        
        Object[] values = {
            depart_lot.getId()
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
    
    @Override public PartRevision findPartRevision(DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        if(depart_lot.getId() == null){
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            depart_lot.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PART_REVISION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part_revision = daoFactory.getPartRevisionDAO().find(resultSet.getInt("PART_REVISION_ID"));
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        
        return part_revision;
    }

    @Override
    public List<DepartLot> list(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }    
        
        List<DepartLot> depart_lot = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DEPART_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }
    
    @Override
    public List<DepartLot> list(DepartReport depart_report, boolean rejected) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }    
        
        List<DepartLot> depart_lot = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId(),
            rejected
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DEPART_REPORT_REJECTED_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }
    
    
    @Override
    public List<DepartLot> list(Company company, boolean pending, boolean rejected) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<DepartLot> depart_lot = new ArrayList<>();
        
        Object[] values = {
            rejected,
            pending
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PENDING_REJECTED_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                if(daoFactory.getDepartReportDAO().findCompany(findDepartReport(map(resultSet))).equals(company)){
                    depart_lot.add(map(resultSet));
                }
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }
    
    @Override
    public List<DepartLot> list(String lot_number){
        
        List<DepartLot> incoming_lot = new ArrayList<>();
        
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
    public List<DepartReport> listDepartReport(boolean rejected) throws IllegalArgumentException{
        
        List<DepartReport> depart_report = new ArrayList<>();
        
        Object[] values = {
            rejected
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_DEPART_REPORTS, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_report.add(daoFactory.getDepartReportDAO().find(resultSet.getInt("DEPART_REPORT_ID")));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_report;
    }
    
    @Override
    public List<PartRevision> listPartRevision(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        List<PartRevision> part_revision = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_PART_REVISIONS, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part_revision.add(daoFactory.getPartRevisionDAO().find(resultSet.getInt("PART_REVISION_ID")));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part_revision;
    }
    
    @Override
    public void create(DepartReport depart_report, PartRevision part_revision, DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        if (depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(depart_lot.getId() != null){
            throw new IllegalArgumentException("DepartLot is already created, the DepartLot ID is null.");
        }
        
        Object[] values = {
            depart_report.getId(),
            part_revision.getId(),
            depart_lot.getLot_number(),
            depart_lot.getQuantity(),
            depart_lot.getBox_quantity(),
            depart_lot.getProcess(),
            depart_lot.getComments(),
            depart_lot.isRejected(),
            depart_lot.isPending()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating DepartLot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depart_lot.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating DepartLot failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(DepartLot depart_lot) throws IllegalArgumentException, DAOException {
        
        if (depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        Object[] values = {
            depart_lot.getLot_number(),
            depart_lot.getQuantity(),
            depart_lot.getBox_quantity(),
            depart_lot.getProcess(),
            depart_lot.getComments(),
            depart_lot.isRejected(),
            depart_lot.isPending(),
            depart_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating DepartLot failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(DepartLot depart_lot) throws DAOException {
        Object[] values = {
            depart_lot.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting DepartLot failed, no rows affected.");
            } else{
                depart_lot.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }   
    
    @Override
    public Integer getPartRevisionQuantity(DepartReport depart_report, PartRevision part_revision){
        Integer total = 0;
        for(DepartLot depart_lot : list(depart_report)){
            if(findPartRevision(depart_lot).equals(part_revision)){
                total += depart_lot.getQuantity();
            }
        }
        return total;
    }
    
    @Override
    public Integer getPartRevisionBoxQuantity(DepartReport depart_report, PartRevision part_revision){
        Integer total = 0;
        for(DepartLot depart_lot : list(depart_report)){
            if(findPartRevision(depart_lot).equals(part_revision)){
                total += depart_lot.getBox_quantity();
            }
        }
        return total;
    }
    
    @Override
    public List<DepartLot> listDateRange(ProductPart product_part, Date start, Date end){
        List<DepartLot> departlot_list = new ArrayList<DepartLot>();
        System.out.println("we are here");
        System.out.println(start);
        System.out.println(end);
        Object[] values = {
            product_part.getId(),
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, LIST_DEPART_LOT_BY_PRODUCT_PART_DATE_RANGE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                System.out.println("Item match #"+resultSet.getRow());
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getInt("DEPART_REPORT_ID"));
                System.out.println(resultSet.getInt("quantity"));
                departlot_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return departlot_list;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartLot.
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartLot.
     * @return The mapped DepartLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartLot map(ResultSet resultSet) throws SQLException{
        DepartLot depart_lot = new DepartLot();
        depart_lot.setId(resultSet.getInt("id"));
        depart_lot.setLot_number(resultSet.getString("lot_number"));
        depart_lot.setQuantity(resultSet.getInt("quantity"));
        depart_lot.setBox_quantity(resultSet.getInt("box_quantity"));
        depart_lot.setProcess(resultSet.getString("process"));
        depart_lot.setComments(resultSet.getString("comments"));
        depart_lot.setRejected(resultSet.getBoolean("rejected"));
        depart_lot.setPending(resultSet.getBoolean("pending"));
        return depart_lot;
    }    
}
