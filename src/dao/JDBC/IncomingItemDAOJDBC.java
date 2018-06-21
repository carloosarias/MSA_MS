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
            "SELECT id, lot_number, quantity, box_quantity, quality_pass, details FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_INCOMING_REPORT_BY_ID =
            "SELECT INCOMING_REPORT_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID =
            "SELECT PART_REVISION_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, quality_pass, details FROM INCOMING_ITEM WHERE ORDER_PURCHASE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_ITEM (INCOMING_REPORT_ID, PART_REVISION_ID, lot_number, quantity, box_quantity, quality_pass, details) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_ITEM SET lot_number = ?, quantity = ?, box_quantity = ?, quality_pass = ?, details = ? WHERE id = ?";
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PartRevision findPartRevision(IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IncomingItem> list(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(IncomingReport incoming_report, PartRevision part_revision, IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(IncomingItem incoming_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(IncomingItem incoming_item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        incoming_item.setLot_number(resultSet.getString("lot_number"));
        incoming_item.setQuantity(resultSet.getInt("quantity"));
        incoming_item.setBox_quantity(resultSet.getInt("box_quantity"));
        incoming_item.setQuality_pass(resultSet.getBoolean("quality_pass"));
        incoming_item.setDetails(resultSet.getString("details"));
        return incoming_item;
    }    
}
