/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.InvoiceDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Invoice;

/**
 *
 * @author Pavilion Mini
 */
public class InvoiceDAOJDBC implements InvoiceDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, invoice_date, terms, shipping_method, fob FROM INVOICE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM INVOICE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_ADDRESS_BY_ID = 
            "SELECT BILLING_ADDRESS_ID, SHIPPING_ADDRESS_ID FROM INVOICE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, invoice_date, terms, shipping_method, fob FROM INVOICE ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, invoice_date, terms, shipping_method, fob FROM INVOICE WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE (COMPANY_ID, BILLING_ADDRESS_ID, SHIPPING_ADDRESS_ID, invoice_date, terms, shipping_method, fob) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INVOICE SET invoice_date = ?, terms = ?, shipping_method = ?, fob = ? WHERE id = ?";
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
    public CompanyAddress findBillingAddress(Invoice invoice) throws IllegalArgumentException, DAOException {
        if(invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        
        CompanyAddress billing_address = null;
        
        Object[] values = {
            invoice.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_ADDRESS_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                billing_address = daoFactory.getCompanyAddressDAO().find(resultSet.getInt("BILLING_ADDRESS_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return billing_address;
    }

    @Override
    public CompanyAddress findShippingAddress(Invoice invoice) throws IllegalArgumentException, DAOException {
        if(invoice.getId() == null) {
            throw new IllegalArgumentException("Invoice is not created yet, the Invoice ID is null.");
        }
        
        CompanyAddress shipping_address = null;
        
        Object[] values = {
            invoice.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_ADDRESS_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                shipping_address = daoFactory.getCompanyAddressDAO().find(resultSet.getInt("SHIPPING_ADDRESS_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return shipping_address;
    }

    @Override
    public List<Invoice> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Invoice> listCompany(Company company) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Company company, CompanyAddress billing_address, CompanyAddress shipping_address, Invoice invoice) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Invoice invoice) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Invoice depart_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        invoice.setId(resultSet.getInt("id"));
        invoice.setInvoice_date(resultSet.getDate("invoice_date"));
        invoice.setTerms(resultSet.getString("terms"));
        invoice.setShipping_method(resultSet.getString("shipping_method"));
        invoice.setFob(resultSet.getString("fob"));
        return invoice;
    }
}
