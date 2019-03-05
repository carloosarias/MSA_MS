/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.InvoiceItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.DepartLot;
import model.Invoice;
import model.InvoiceItem;
import model.PartRevision;
import model.Quote;

/**
 *
 * @author Pavilion Mini
 */
public class InvoiceItemDAOJDBC implements InvoiceItemDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT INVOICE_ITEM.id, INVOICE_ITEM.comments, "
            + "PRODUCT_PART.part_number, PART_REVISION.rev, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, QUOTE.id, QUOTE.estimated_total "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN DEPART_LOT ON INVOICE_ITEM.DEPART_LOT_ID = DEPART_LOT.id "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "WHERE INVOICE_ITEM.id = ?";
    private static final String SQL_FIND_INVOICE_BY_ID = 
            "SELECT INVOICE_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_DEPART_LOT_BY_ID = 
            "SELECT DEPART_LOT_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_QUOTE_BY_ID = 
            "SELECT QUOTE_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT INVOICE_ITEM.id, INVOICE_ITEM.comments, "
            + "PRODUCT_PART.part_number, PART_REVISION.rev, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, QUOTE.id, QUOTE.estimated_total "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN DEPART_LOT ON INVOICE_ITEM.DEPART_LOT_ID = DEPART_LOT.id "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "ORDER BY INVOICE_ITEM.id";
    private static final String SQL_LIST_OF_INVOICE_ORDER_BY_ID = 
            "SELECT INVOICE_ITEM.id, INVOICE_ITEM.comments, "
            + "PRODUCT_PART.part_number, PART_REVISION.rev, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, QUOTE.id, QUOTE.estimated_total "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN DEPART_LOT ON INVOICE_ITEM.DEPART_LOT_ID = DEPART_LOT.id "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "WHERE INVOICE_ITEM.INVOICE_ID = ? "
            + "ORDER BY INVOICE_ITEM.id";
    private static final String SQL_LIST_OF_INVOICE_PART_REVISION_ORDER_BY_ID = 
            "SELECT INVOICE_ITEM.id, INVOICE_ITEM.comments, "
            + "PRODUCT_PART.part_number, PART_REVISION.rev, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, QUOTE.id, QUOTE.estimated_total "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN DEPART_LOT ON INVOICE_ITEM.DEPART_LOT_ID = DEPART_LOT.id "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "WHERE INVOICE_ITEM.INVOICE_ID = ? AND DEPART_LOT.PART_REVISION_ID = ? "
            + "ORDER BY INVOICE_ITEM.id";
    private static final String SQL_LIST_OF_QUOTE_DATE_RANGE_ORDER_BY_ID = 
            "SELECT INVOICE_ITEM.id, INVOICE_ITEM.comments, "
            + "PRODUCT_PART.part_number, PART_REVISION.rev, DEPART_LOT.DEPART_REPORT_ID, DEPART_LOT.lot_number, DEPART_LOT.quantity, DEPART_LOT.box_quantity, QUOTE.id, QUOTE.estimated_total "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN DEPART_LOT ON INVOICE_ITEM.DEPART_LOT_ID = DEPART_LOT.id "
            + "INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "INNER JOIN INVOICE ON INVOICE_ITEM.INVOICE_ID = INVOICE.id "
            + "WHERE INVOICE_ITEM.QUOTE_ID = ? AND INVOICE.invoice_date BETWEEN ? AND ? "
            + "ORDER BY INVOICE_ITEM.id";
    private static final String SQL_DISTINCT_QUOTE_PROCESS_DATE_RANGE_ORDER_BY_ID = 
            "SELECT DISTINCT INVOICE_ITEM.QUOTE_ID "
            + "FROM INVOICE_ITEM "
            + "INNER JOIN INVOICE ON INVOICE_ITEM.INVOICE_ID = INVOICE.id "
            + "INNER JOIN QUOTE ON INVOICE_ITEM.QUOTE_ID = QUOTE.id "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE (SPECIFICATION.process = ? OR ? = 0) AND INVOICE.invoice_date BETWEEN ? AND ? "
            + "ORDER BY INVOICE_ITEM.QUOTE_ID";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE_ITEM (INVOICE_ID, DEPART_LOT_ID, QUOTE_ID, comments) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INVOICE_ITEM SET comments = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INVOICE_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a InvoiceItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this InvoiceItem DAO for.
     */
    InvoiceItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public InvoiceItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Invoice from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Invoice from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */    
    private InvoiceItem find(String sql, Object... values) throws DAOException {
        InvoiceItem invoice_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return invoice_item;
    }
    
    @Override
    public Invoice findInvoice(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        if(invoice_item.getId() == null) {
            throw new IllegalArgumentException("InvoiceItem is not created yet, the InvoiceItem ID is null.");
        }
        
        Invoice invoice = null;
        
        Object[] values = {
            invoice_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_INVOICE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice = daoFactory.getInvoiceDAO().find(resultSet.getInt("INVOICE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return invoice;
    }

    @Override
    public DepartLot findDepartLot(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        if(invoice_item.getId() == null) {
            throw new IllegalArgumentException("InvoiceItem is not created yet, the InvoiceItem ID is null.");
        }
        
        DepartLot depart_lot = null;
        
        Object[] values = {
            invoice_item.getId()
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
    public Quote findQuote(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        if(invoice_item.getId() == null){
            throw new IllegalArgumentException("InvoiceItem is not created yet, the InvoiceItem ID is null.");
        }
        
        Quote quote = null;
        
        Object[] values = {
            invoice_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_QUOTE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                quote = daoFactory.getQuoteDAO().find(resultSet.getInt("QUOTE_ID"));
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        
        return quote;
    }

    @Override
    public List<InvoiceItem> list() throws DAOException {
        List<InvoiceItem> invoice_item = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_item;
    }

    @Override
    public List<InvoiceItem> list(Invoice invoice) throws IllegalArgumentException, DAOException {
        if(invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }    
        
        List<InvoiceItem> invoice_item = new ArrayList<>();
        
        Object[] values = {
            invoice.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INVOICE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_item;
    }
    @Override
    public List<InvoiceItem> list(Invoice invoice, PartRevision part_revision) throws IllegalArgumentException, DAOException {
        if(invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }    
        
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        List<InvoiceItem> invoice_item = new ArrayList<>();
        
        Object[] values = {
            invoice.getId(),
            part_revision.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INVOICE_PART_REVISION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_item;
    }
    
    @Override
    public List<InvoiceItem> list(Quote quote, Date start_date, Date end_date) throws IllegalArgumentException, DAOException {
        if(quote.getId() == null) {
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }    
        
        List<InvoiceItem> invoice_item = new ArrayList<>();
        
        Object[] values = {
            quote.getId(),
            start_date,
            end_date
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_QUOTE_DATE_RANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_item;
    }
    
    @Override
    public List<Quote> listDistinctQuote_byProcessDateRange(String process, boolean process_filter, Date start_date, Date end_date) throws DAOException{
        
        List<Quote> quote = new ArrayList<>();
        
        Object[] values = {
            process,
            process_filter,
            start_date,
            end_date
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DISTINCT_QUOTE_PROCESS_DATE_RANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                quote.add(daoFactory.getQuoteDAO().find(resultSet.getInt("INVOICE_ITEM.QUOTE_ID")));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return quote;
    }
    
    @Override
    public void create(Invoice invoice, DepartLot depart_lot, Quote quote, InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
    if (invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        
        if(depart_lot.getId() == null){
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }
        
        if(invoice_item.getId() != null){
            throw new IllegalArgumentException("InvoiceItem is already created, the InvoiceItem ID is not null.");
        }
        
        Object[] values = {
            invoice.getId(),
            depart_lot.getId(),
            quote.getId(),
            invoice_item.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating InvoiceItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating InvoiceItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        if (invoice_item.getId() == null) {
            throw new IllegalArgumentException("InvoiceItem is not created yet, the InvoiceItem ID is null.");
        }
        
        Object[] values = {
            invoice_item.getComments(),
            invoice_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating InvoiceItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(InvoiceItem invoice_item) throws DAOException {
        Object[] values = {
            invoice_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting InvoiceItem failed, no rows affected.");
            } else{
                invoice_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an InvoiceItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an InvoiceItem.
     * @return The mapped Invoice from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static InvoiceItem map(ResultSet resultSet) throws SQLException{
        InvoiceItem invoice_item = new InvoiceItem();
        invoice_item.setId(resultSet.getInt("INVOICE_ITEM.id"));
        invoice_item.setComments(resultSet.getString("INVOICE_ITEM.comments"));
        
        //INNER JOINS
        invoice_item.setPart_number(resultSet.getString("PRODUCT_PART.part_number"));
        invoice_item.setPart_revision(resultSet.getString("PART_REVISION.rev"));
        invoice_item.setDepartreport_id(resultSet.getInt("DEPART_LOT.DEPART_REPORT_ID"));
        invoice_item.setLot_number(resultSet.getString("DEPART_LOT.lot_number"));
        invoice_item.setQuantity(resultSet.getInt("DEPART_LOT.quantity"));
        invoice_item.setBox_quantity(resultSet.getInt("DEPART_LOT.box_quantity"));
        invoice_item.setQuote_estimatedtotal(resultSet.getDouble("QUOTE.id"));
        invoice_item.setQuote_estimatedtotal(resultSet.getDouble("QUOTE.estimated_total"));
        return invoice_item;
    }
}
