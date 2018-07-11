/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.InvoiceDAOJDBC.map;
import dao.interfaces.InvoiceItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.DepartLot;
import model.Invoice;
import model.InvoiceItem;

/**
 *
 * @author Pavilion Mini
 */
public class InvoiceItemDAOJDBC implements InvoiceItemDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, unit_price, comments FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_INVOICE_BY_ID = 
            "SELECT INVOICE_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_FIND_DEPART_LOT_BY_ID = 
            "SELECT DEPART_LOT_ID FROM INVOICE_ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, unit_price, comments FROM INVOICE_ITEM ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, unit_price, comments FROM INVOICE_ITEM WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE_ITEM (INVOICE_ID, DEPART_LOT_ID, id, unit_price, comments) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INVOICE_ITEM SET unit_price = ?, comments = ? WHERE id = ?";
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
    public List<InvoiceItem> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InvoiceItem> list(Invoice invoice) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Invoice invoice, DepartLot depart_lot, InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(InvoiceItem invoice_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(InvoiceItem invoice_item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        invoice_item.setId(resultSet.getInt("id"));
        invoice_item.setUnit_price(resultSet.getDouble("unit_price"));
        invoice_item.setComments(resultSet.getString("comments"));
        return invoice_item;
    }
}
