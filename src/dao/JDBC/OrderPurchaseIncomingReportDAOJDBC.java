/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.MantainanceReportDAOJDBC.map;
import dao.interfaces.OrderPurchaseIncomingReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.Employee;
import model.MantainanceReport;
import model.OrderPurchase;
import model.OrderPurchaseIncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportDAOJDBC implements OrderPurchaseIncomingReportDAO {
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
     * Construct a OrderPurchaseIncomingReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this OrderPurchaseIncomingReport DAO for.
     */
    OrderPurchaseIncomingReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public OrderPurchaseIncomingReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    /**
     * Returns the OrderPurchaseIncomingReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The OrderPurchaseIncomingReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private OrderPurchaseIncomingReport find(String sql, Object... values) throws DAOException {
        OrderPurchaseIncomingReport mantainance_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                mantainance_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return mantainance_report;
    }
    
    @Override
    public OrderPurchase findOrderPurchase(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        if(order_purchase_incoming_report.getId() == null) {
                throw new IllegalArgumentException("OrderPurchaseIncomingReport is not created yet, the OrderPurchaseIncomingReport ID is null.");
            }

            OrderPurchase order_purchase = null;

            Object[] values = {
                order_purchase_incoming_report.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_EMPLOYEE_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    order_purchase = daoFactory.getOrderPurchaseDAO().find(resultSet.getInt("ORDER_PURCHASE_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return order_purchase;
    }

    @Override
    public Employee findEmployee(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchaseIncomingReport> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchaseIncomingReport> listOfOrderPurchase(OrderPurchase order_purchase) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchaseIncomingReport> listOfEmployee(Employee employee) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(OrderPurchase order_purchase, Employee employee, OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(OrderPurchaseIncomingReport order_purchase_incoming_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an MantainanceReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped MantainanceReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static OrderPurchaseIncomingReport map(ResultSet resultSet) throws SQLException{
        OrderPurchaseIncomingReport order_purchase_incoming_report = new OrderPurchaseIncomingReport();
        order_purchase_incoming_report.setId(resultSet.getInt("id"));
        order_purchase_incoming_report.setReport_date(resultSet.getDate("report_date"));
        order_purchase_incoming_report.setComments(resultSet.getString("comments"));
        return order_purchase_incoming_report;
    }
}
