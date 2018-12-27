/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.OrderPurchaseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.OrderPurchase;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseDAOJDBC implements OrderPurchaseDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_BY_ID = 
            "SELECT EQUIPMENT_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT ORDER BY id";
    private static final String SQL_LIST_EMPLOYEE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? ORDER BY id";
    private static final String SQL_LIST_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE report_date BETWEEN ? AND ?  ORDER BY id";
    private static final String SQL_LIST_EMPLOYEE_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
    private static final String SQL_LIST_EQUIPMENT_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EQUIPMENT_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO MANTAINANCE_REPORT (EMPLOYEE_ID, EQUIPMENT_ID, report_date) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE MANTAINANCE_REPORT SET report_date = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM MANTAINANCE_REPORT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a OrderPurchase DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this OrderPurchase DAO for.
     */
    OrderPurchaseDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public OrderPurchase find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Returns the MantainanceReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The MantainanceReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private OrderPurchase find(String sql, Object... values) throws DAOException {
        OrderPurchase order_purchase = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                order_purchase = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return order_purchase;
    }
    
    @Override
    public Company findCompany(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CompanyAddress findCompanyAddress(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchase> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchase> listOfCompany(Company company) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchase> listOfStatus(String status) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Company company, CompanyAddress company_address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(OrderPurchase order_purchase) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an MantainanceReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped MantainanceReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static OrderPurchase map(ResultSet resultSet) throws SQLException{
        OrderPurchase order_purchase = new OrderPurchase();
        order_purchase.setId(resultSet.getInt("id"));
        order_purchase.setReport_date(resultSet.getDate("report_date"));
        order_purchase.setComments(resultSet.getString("comments"));
        order_purchase.setStatus(resultSet.getString("status"));
        order_purchase.setExchange_rate(resultSet.getDouble("exchange_rate"));
        order_purchase.setIva_rate(resultSet.getDouble("iva_rate"));
        return order_purchase;
    }
}
