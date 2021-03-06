/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.InvoiceDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Invoice;
import model.InvoiceItem;

/**
 *
 * @author Pavilion Mini
 */
public class InvoiceDAOJDBC implements InvoiceDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT INVOICE.id, INVOICE.invoice_date, INVOICE.terms, INVOICE.payment_date, INVOICE.check_number, INVOICE.quantity_paid, INVOICE.comments, INVOICE.pending, "
            + "COMPANY.name "
            + "FROM INVOICE "
            + "INNER JOIN COMPANY ON INVOICE.COMPANY_ID = COMPANY.id "
            + "WHERE INVOICE.id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM INVOICE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT INVOICE.id, INVOICE.invoice_date, INVOICE.terms, INVOICE.payment_date, INVOICE.check_number, INVOICE.quantity_paid, INVOICE.comments, INVOICE.pending, "
            + "COMPANY.name "
            + "FROM INVOICE "
            + "INNER JOIN COMPANY ON INVOICE.COMPANY_ID = COMPANY.id "
            + "ORDER BY INVOICE.id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT INVOICE.id, INVOICE.invoice_date, INVOICE.terms, INVOICE.payment_date, INVOICE.check_number, INVOICE.quantity_paid, INVOICE.comments, INVOICE.pending, "
            + "COMPANY.name "
            + "FROM INVOICE "
            + "INNER JOIN COMPANY ON INVOICE.COMPANY_ID = COMPANY.id "
            + "WHERE INVOICE.COMPANY_ID = ? "
            + "ORDER BY INVOICE.id";
    private static final String SQL_LIST_OF_PENDING_ORDER_BY_ID = 
            "SELECT INVOICE.id, INVOICE.invoice_date, INVOICE.terms, INVOICE.payment_date, INVOICE.check_number, INVOICE.quantity_paid, INVOICE.comments, INVOICE.pending, "
            + "COMPANY.name "
            + "FROM INVOICE "
            + "INNER JOIN COMPANY ON INVOICE.COMPANY_ID = COMPANY.id "
            + "WHERE INVOICE.pending = ? "
            + "ORDER BY INVOICE.id";
    private static final String SQL_LIST_OF_DATE_ORDER_BY_ID = 
            "SELECT INVOICE.id, INVOICE.invoice_date, INVOICE.terms, INVOICE.payment_date, INVOICE.check_number, INVOICE.quantity_paid, INVOICE.comments, INVOICE.pending, "
            + "COMPANY.name "
            + "FROM INVOICE "
            + "INNER JOIN COMPANY ON INVOICE.COMPANY_ID = COMPANY.id "
            + "WHERE INVOICE.invoice_date BETWEEN ? AND ? "
            + "ORDER BY INVOICE.id";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE (COMPANY_ID, invoice_date, terms, payment_date, check_number, quantity_paid, comments, pending) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INVOICE SET invoice_date = ?, terms = ?, payment_date = ?, check_number = ?, quantity_paid = ?, comments = ?, pending = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INVOICE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Invoice DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Invoice DAO for.
     */
    InvoiceDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public Invoice find(Integer id) throws DAOException{
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Invoice from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Invoice from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Invoice find(String sql, Object... values) throws DAOException {
        Invoice invoice = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return invoice;
    }
    
    @Override
    public Company findCompany(Invoice invoice) throws IllegalArgumentException, DAOException {
        if(invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            invoice.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                company = daoFactory.getCompanyDAO().find(resultSet.getInt("COMPANY_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return company;
    }

    @Override
    public List<Invoice> list() throws DAOException {
        List<Invoice> invoice = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice;
    }

    @Override
    public List<Invoice> listCompany(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<Invoice> invoice = new ArrayList<>();
        
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice; 
    }
    
    @Override
    public List<Invoice> listPending(boolean pending) throws DAOException {
        
        List<Invoice> invoice = new ArrayList<>();
        
        Object[] values = {
            pending
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PENDING_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice; 
    }
    @Override
    public List<Invoice> listDateRange(Date start_date, Date end_date) throws DAOException {
        List<Invoice> invoice = new ArrayList<>();
        
        Object[] values = {
            start_date,
            end_date
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DATE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while (resultSet.next()){
                invoice.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice;
    }
    
    @Override
    public Double findTotal(Invoice invoice){
        double total = 0;
        List<InvoiceItem> invoiceitems = daoFactory.getInvoiceItemDAO().list(invoice);
        for(InvoiceItem invoice_item : invoiceitems){
            total += invoice_item.getQuote_estimatedtotal()*invoice_item.getQuantity();
        }
        return total;
    }

    @Override
    public List<Invoice> filterListByCompany(List<Invoice> list, Company company){
        List<Invoice> filtered_list = new ArrayList<Invoice>();
        for(Invoice invoice: list){
            if(findCompany(invoice).getId() == company.getId()){
                filtered_list.add(invoice);
            }
        }
        return filtered_list;
    }
    
    @Override
    public void create(Company company, Invoice invoice) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        if(invoice.getId() != null){
            throw new IllegalArgumentException("Invoice is already created, the Invoice ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            DAOUtil.toSqlDate(invoice.getInvoice_date()),
            invoice.getTerms(),
            DAOUtil.toSqlDate(invoice.getPayment_date()),
            invoice.getCheck_number(),
            invoice.getQuantity_paid(),
            invoice.getComments(),
            invoice.isPending()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Invoice failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Invoice failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Invoice invoice) throws IllegalArgumentException, DAOException {
        if (invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(invoice.getInvoice_date()),
            invoice.getTerms(),
            DAOUtil.toSqlDate(invoice.getPayment_date()),
            invoice.getCheck_number(),
            invoice.getQuantity_paid(),
            invoice.getComments(),
            invoice.isPending(),
            invoice.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Invoice failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Invoice invoice) throws DAOException {
        Object[] values = {
            invoice.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Invoice failed, no rows affected.");
            } else{
                invoice.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Invoice.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Invoice.
     * @return The mapped Invoice from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Invoice map(ResultSet resultSet) throws SQLException{
        Invoice invoice = new Invoice();
        invoice.setId(resultSet.getInt("INVOICE.id"));
        invoice.setInvoice_date(resultSet.getDate("INVOICE.invoice_date"));
        invoice.setTerms(resultSet.getString("INVOICE.terms"));
        invoice.setPayment_date(resultSet.getDate("INVOICE.payment_date"));
        invoice.setCheck_number(resultSet.getString("INVOICE.check_number"));
        invoice.setQuantity_paid(resultSet.getDouble("INVOICE.quantity_paid"));
        invoice.setComments(resultSet.getString("INVOICE.comments"));
        invoice.setPending(resultSet.getBoolean("INVOICE.pending"));
        
        //INNER JOINS
        invoice.setCompany_name(resultSet.getString("COMPANY.name"));
        return invoice;
    }
}
