/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.QuoteDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.Company;
import model.CompanyContact;
import model.InvoiceItem;
import model.PartRevision;
import model.Quote;

/**
 *
 * @author Pavilion Mini
 */
public class QuoteDAOJDBC implements QuoteDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, comments FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_INVOICE_BY_ID = 
            "SELECT INVOICE_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_DEPART_LOT_BY_ID = 
            "SELECT DEPART_LOT_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_QUOTE_BY_ID = 
            "SELECT QUOTE_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, comments FROM INVOICE_ITEM ORDER BY id";
    private static final String SQL_LIST_OF_INVOICE_ORDER_BY_ID = 
            "SELECT id, comments FROM INVOICE_ITEM WHERE INVOICE_ID = ? ORDER BY id";
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
     * Construct a Quote DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Quote DAO for.
     */
    QuoteDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Quote find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Returns the Invoice from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Invoice from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */    
    private Quote find(String sql, Object... values) throws DAOException {
        Quote quote = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                quote = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return quote;
    }
    
    @Override
    public PartRevision findPartRevision(Quote quote) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CompanyContact findCompanyContact(Quote quote) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Quote> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Quote> list(PartRevision part_revision) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(PartRevision part_revision, CompanyContact company_contact, Quote quote) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Quote quote) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Quote quote) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Quote.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Quote.
     * @return The mapped Quote from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Quote map(ResultSet resultSet) throws SQLException{
        Quote quote = new Quote();
        quote.setId(resultSet.getInt("id"));
        quote.setQuote_date(resultSet.getDate("quote_date"));
        quote.setUnit_price(resultSet.getDouble("unit_price"));
        quote.setApproved(resultSet.getBoolean("approved"));
        quote.setComments(resultSet.getString("comments"));
        return quote;
    }
}
