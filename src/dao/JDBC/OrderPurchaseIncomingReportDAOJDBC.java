/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.OrderPurchaseIncomingReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.OrderPurchase;
import model.OrderPurchaseIncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportDAOJDBC implements OrderPurchaseIncomingReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_REPORT.id, ORDER_PURCHASE_INCOMING_REPORT.report_date, ORDER_PURCHASE_INCOMING_REPORT.comments, ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID, "
            + "COMPANY.name, EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ORDER_PURCHASE_INCOMING_REPORT "
            + "INNER JOIN ORDER_PURCHASE ON ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID = ORDER_PURCHASE.id "
            + "INNER JOIN EMPLOYEE ON ORDER_PURCHASE_INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "WHERE ORDER_PURCHASE_INCOMING_REPORT.id = ?";
    private static final String SQL_FIND_ORDER_PURCHASE_BY_ID = 
            "SELECT ORDER_PURCHASE_ID FROM ORDER_PURCHASE_INCOMING_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM ORDER_PURCHASE_INCOMING_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_REPORT.id, ORDER_PURCHASE_INCOMING_REPORT.report_date, ORDER_PURCHASE_INCOMING_REPORT.comments, ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID, "
            + "COMPANY.name, EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ORDER_PURCHASE_INCOMING_REPORT "
            + "INNER JOIN ORDER_PURCHASE ON ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID = ORDER_PURCHASE.id "
            + "INNER JOIN EMPLOYEE ON ORDER_PURCHASE_INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "ORDER BY ORDER_PURCHASE_INCOMING_REPORT.id";
    private static final String SQL_LIST_EMPLOYEE_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_REPORT.id, ORDER_PURCHASE_INCOMING_REPORT.report_date, ORDER_PURCHASE_INCOMING_REPORT.comments, ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID, "
            + "COMPANY.name, EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ORDER_PURCHASE_INCOMING_REPORT "
            + "INNER JOIN ORDER_PURCHASE ON ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID = ORDER_PURCHASE.id "
            + "INNER JOIN EMPLOYEE ON ORDER_PURCHASE_INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "WHERE ORDER_PURCHASE_INCOMING_REPORT.EMPLOYEE_ID = ? "
            + "ORDER BY ORDER_PURCHASE_INCOMING_REPORT.id";
    private static final String SQL_LIST_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_REPORT.id, ORDER_PURCHASE_INCOMING_REPORT.report_date, ORDER_PURCHASE_INCOMING_REPORT.comments, ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID, "
            + "COMPANY.name, EMPLOYEE.id, EMPLOYEE.first_name, EMPLOYEE.last_name "
            + "FROM ORDER_PURCHASE_INCOMING_REPORT "
            + "INNER JOIN ORDER_PURCHASE ON ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID = ORDER_PURCHASE.id "
            + "INNER JOIN EMPLOYEE ON ORDER_PURCHASE_INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "WHERE ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID = ? "
            + "ORDER BY ORDER_PURCHASE_INCOMING_REPORT.id";
    private static final String SQL_INSERT = 
            "INSERT INTO ORDER_PURCHASE_INCOMING_REPORT(ORDER_PURCHASE_ID, EMPLOYEE_ID, report_date, comments) "
            + "VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ORDER_PURCHASE_INCOMING_REPORT SET report_date = ?, comments = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM ORDER_PURCHASE_INCOMING_REPORT WHERE id = ?";
    
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
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_ORDER_PURCHASE_BY_ID, false, values);
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
        if(order_purchase_incoming_report.getId() == null) {
                throw new IllegalArgumentException("OrderPurchaseIncomingReport is not created yet, the OrderPurchaseIncomingReport ID is null.");
            }

            Employee employee = null;

            Object[] values = {
                order_purchase_incoming_report.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_EMPLOYEE_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    employee = daoFactory.getEmployeeDAO().find(resultSet.getInt("EMPLOYEE_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return employee;
    }

    @Override
    public List<OrderPurchaseIncomingReport> list() throws DAOException {
        List<OrderPurchaseIncomingReport> order_purchase_incoming_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchase_incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchase_incoming_report;
    }

    @Override
    public List<OrderPurchaseIncomingReport> listOfOrderPurchase(OrderPurchase order_purchase) throws DAOException {
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }    
        
        List<OrderPurchaseIncomingReport> order_purchase_incoming_report = new ArrayList<>();
        
        Object[] values = {
            order_purchase.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ORDER_PURCHASE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchase_incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchase_incoming_report;
    }

    @Override
    public List<OrderPurchaseIncomingReport> listOfEmployee(Employee employee) throws DAOException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }    
        
        List<OrderPurchaseIncomingReport> order_purchase_incoming_report = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchase_incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchase_incoming_report;
    }

    @Override
    public void create(OrderPurchase order_purchase, Employee employee, OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        if (order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }
        if(employee.getId() == null){
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        if(order_purchase_incoming_report.getId() != null){
            throw new IllegalArgumentException("OrderPurchaseIncomingReport is already created, the OrderPurchaseIncomingReport ID is not null.");
        }
        
        Object[] values = {
            order_purchase.getId(),
            employee.getId(),
            DAOUtil.toSqlDate(order_purchase_incoming_report.getReport_date()),
            order_purchase_incoming_report.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating OrderPurchaseIncomingReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order_purchase_incoming_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating OrderPurchaseIncomingReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        if (order_purchase_incoming_report.getId() == null) {
            throw new IllegalArgumentException("OrderPurchaseIncomingReport is not created yet, the OrderPurchaseIncomingReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(order_purchase_incoming_report.getReport_date()),
            order_purchase_incoming_report.getComments(),
            order_purchase_incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating OrderPurchaseIncomingReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(OrderPurchaseIncomingReport order_purchase_incoming_report) throws DAOException {
        Object[] values = {
            order_purchase_incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting OrderPurchaseIncomingReport failed, no rows affected.");
            } else{
                order_purchase_incoming_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an OrderPurchaseIncomingReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an OrderPurchaseIncomingReport.
     * @return The mapped OrderPurchaseIncomingReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static OrderPurchaseIncomingReport map(ResultSet resultSet) throws SQLException{
        OrderPurchaseIncomingReport order_purchase_incoming_report = new OrderPurchaseIncomingReport();
        order_purchase_incoming_report.setId(resultSet.getInt("ORDER_PURCHASE_INCOMING_REPORT.id"));
        order_purchase_incoming_report.setReport_date(resultSet.getDate("ORDER_PURCHASE_INCOMING_REPORT.report_date"));
        order_purchase_incoming_report.setComments(resultSet.getString("ORDER_PURCHASE_INCOMING_REPORT.comments"));
        order_purchase_incoming_report.setOrderpurchase_id(resultSet.getInt("ORDER_PURCHASE_INCOMING_REPORT.ORDER_PURCHASE_ID"));
        
        //INNER JOINS
        order_purchase_incoming_report.setOrderpurchase_companyname(resultSet.getString("COMPANY.name"));
        order_purchase_incoming_report.setEmployee_id(resultSet.getInt("EMPLOYEE.id"));
        order_purchase_incoming_report.setEmployee_employeename(resultSet.getString("EMPLOYEE.first_name") +" "+ resultSet.getString("EMPLOYEE.last_name"));
        return order_purchase_incoming_report;
    }
}
