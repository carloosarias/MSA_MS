/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.InvoicePaymentItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Invoice;
import model.InvoicePaymentItem;
import model.InvoicePaymentReport;

/**
 *
 * @author Pavilion Mini
 */
public class InvoicePaymentItemDAOJDBC implements InvoicePaymentItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT INVOICE_PAYMENT_ITEM.id, INVOICE_PAYMENT_ITEM.INVOICE_ID, "
            + "INVOICE.invoice_date, INVOICE.payment_terms "
            + "FROM INVOICE_PAYMENT_ITEM "
            + "INNER JOIN INVOICE ON INVOICE_PAYMENT_ITEM.INVOICE_ID = INVOICE.id "
            + "WHERE INVOICE_PAYMENT_ITEM.id = ?";
    private static final String SQL_FIND_INVOICE_BY_ID = 
            "SELECT INVOICE_ID FROM INVOICE_PAYMENT_ITEM WHERE id = ?";
    private static final String SQL_FIND_INVOICE_PAYMENT_REPORT_BY_ID = 
            "SELECT INVOICE_PAYMENT_REPORT_ID FROM INVOICE_PAYMENT_ITEM";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT INVOICE_PAYMENT_ITEM.id, INVOICE_PAYMENT_ITEM.INVOICE_ID, "
            + "INVOICE.invoice_date, INVOICE.terms "
            + "FROM INVOICE_PAYMENT_ITEM "
            + "INNER JOIN INVOICE ON INVOICE_PAYMENT_ITEM.INVOICE_ID = INVOICE.id "
            + "ORDER BY INVOICE_PAYMENT_ITEM.id";
    private static final String SQL_LIST_OF_INVOICE_PAYMENT_REPORT_ORDER_BY_ID = 
            "SELECT INVOICE_PAYMENT_ITEM.id, INVOICE_PAYMENT_ITEM.INVOICE_ID, "
            + "INVOICE.invoice_date, INVOICE.terms "
            + "FROM INVOICE_PAYMENT_ITEM "
            + "INNER JOIN INVOICE ON INVOICE_PAYMENT_ITEM.INVOICE_ID = INVOICE.id "
            + "WHERE INVOICE_PAYMENT_ITEM.INVOICE_PAYMENT_REPORT_ID = ? "
            + "ORDER BY INVOICE_PAYMENT_ITEM.id";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE_PAYMENT_ITEM (INVOICE_ID, INVOICE_PAYMENT_REPORT_ID)"
            + "VALUES (?, ?)";
    private static final String SQL_DELETE =
            "DELETE FROM INVOICE_PAYMENT_ITEM WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a InvoicePaymentItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this InvoicePaymentItem DAO for.
     */
    InvoicePaymentItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public InvoicePaymentItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private InvoicePaymentItem find(String sql, Object... values) throws DAOException {
        InvoicePaymentItem invoice_payment_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice_payment_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return invoice_payment_item;
    }
    
    @Override
    public InvoicePaymentReport findInvoicePaymentReport(InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException {
        if(invoice_payment_item.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentItem is not created yet, the InvoicePaymentItem ID is null.");
        }
        
        InvoicePaymentReport invoice_payment_report = null;
        
        Object[] values = {
            invoice_payment_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_INVOICE_PAYMENT_REPORT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice_payment_report = daoFactory.getInvoicePaymentReportDAO().find(resultSet.getInt("INVOICE_PAYMENT_REPORT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return invoice_payment_report;
    }
    @Override
    public Invoice findInvoice(InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException {
        if(invoice_payment_item.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentItem is not created yet, the InvoicePaymentItem ID is null.");
        }
        
        Invoice invoice = null;
        
        Object[] values = {
            invoice_payment_item.getId()
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
    public List<InvoicePaymentItem> list() throws DAOException {
        List<InvoicePaymentItem> invoice_payment_item = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_payment_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_payment_item;
    }

    @Override
    public List<InvoicePaymentItem> list(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException {
        if(invoice_payment_report.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentReport is not created yet, the InvoicePaymentReport ID is null.");
        }    
        
        List<InvoicePaymentItem> invoice_payment_item = new ArrayList<>();
        
        Object[] values = {
            invoice_payment_report.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_INVOICE_PAYMENT_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_payment_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_payment_item; 
    }

    @Override
    public void create(Invoice invoice, InvoicePaymentReport invoice_payment_report, InvoicePaymentItem invoice_payment_item) throws IllegalArgumentException, DAOException {
        if (invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        if (invoice_payment_report.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentReport is not created yet, the InvoicePaymentReport ID is null.");
        }
        if(invoice_payment_item.getId() != null){
            throw new IllegalArgumentException("InvoicePaymentItem is already created, the InvoicePaymentItem ID is not null.");
        }
        
        Object[] values = {
            invoice.getId(),
            invoice_payment_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating InvoicePaymentReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice_payment_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating InvoicePaymentReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(InvoicePaymentItem invoice_payment_report) throws DAOException {
        Object[] values = {
            invoice_payment_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting InvoicePaymentReport failed, no rows affected.");
            } else{
                invoice_payment_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an InvoicePaymentItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an InvoicePaymentItem.
     * @return The mapped InvoicePaymentItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static InvoicePaymentItem map(ResultSet resultSet) throws SQLException{
        InvoicePaymentItem invoice_payment_item = new InvoicePaymentItem();
        invoice_payment_item.setId(resultSet.getInt("INVOICE_PAYMENT_ITEM.id"));
        invoice_payment_item.setInvoice_id(resultSet.getInt("INVOICE_PAYMENT_ITEM.INVOICE_ID"));
        invoice_payment_item.setInvoice_date(resultSet.getDate("INVOICE.invoice_date"));
        invoice_payment_item.setInvoice_terms(resultSet.getString("INVOICE.terms"));
        return invoice_payment_item;
    }
}
