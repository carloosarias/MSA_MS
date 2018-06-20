/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.OrderPurchaseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.Employee;
import model.OrderPurchase;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseDAOJDBC implements OrderPurchaseDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_ADDRESS_BY_ID = 
            "SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total FROM ORDER_PURCHASE ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total FROM ORDER_PURCHASE WHERE active = ? ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total FROM ORDER_PURCHASE WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total FROM ORDER_PURCHASE WHERE COMPANY_ID = ? and active = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO ORDER_PURCHASE (EMPLOYEE_ID, COMPANY_ID, COMPANY_ADDRESS_ID, order_date, description, active, exchange_rate, iva_rate, sub_total, iva, total) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ORDER_PURCHASE SET order_date = ?, description = ?, active = ?, exchange_rate = ?, iva_rate = ?, sub_total = ?, iva = ?, total = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ORDER_PURCHASE WHERE id = ?";
    
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
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the OrderPurchase from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The OrderPurchase from the database matching the given SQL query with the given values.
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
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            order_purchase.getId()
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
    public CompanyAddress findAddress(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }
        
        CompanyAddress address = null;
        
        Object[] values = {
            order_purchase.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_ADDRESS_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                address = daoFactory.getCompanyAddressDAO().find(resultSet.getInt("COMPANY_ADDRESS_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return address;
    }
    
    @Override
    public Employee findEmployee(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            order_purchase.getId()
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
    public List<OrderPurchase> list() throws DAOException {
        List<OrderPurchase> order_purchases = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchases.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchases;
    }

    @Override
    public List<OrderPurchase> list(boolean active) throws DAOException {
        List<OrderPurchase> order_purchases = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchases.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchases;
    }

    @Override
    public List<OrderPurchase> listCompany(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<OrderPurchase> order_purchases = new ArrayList<>();
        
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchases.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchases;
    }
    
    @Override
    public List<OrderPurchase> listCompany(Company company, boolean active){
        if(company.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<OrderPurchase> order_purchases = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchases.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchases;        
    }
    
    @Override
    public void create(Employee employee, Company company, CompanyAddress address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        if(employee.getId() == null){
            throw new IllegalArgumentException("Employee is not created yet, the Company ID is null.");
        }
        
        if(address.getId() == null){
            throw new IllegalArgumentException("CompanyAddress is not created yet, the CompanyAddress ID is null.");
        }
        
        if(order_purchase.getId() != null){
            throw new IllegalArgumentException("OrderPurchase is already created, the OrderPurchase ID is null.");
        }
        
        Object[] values = {
            employee.getId(),
            company.getId(),
            address.getId(),
            DAOUtil.toSqlDate(order_purchase.getOrder_date()),
            order_purchase.getDescription(),
            order_purchase.isActive(),
            order_purchase.getExchange_rate(),
            order_purchase.getIva_rate(),
            order_purchase.getSub_total(),
            order_purchase.getIva(),
            order_purchase.getTotal()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating OrderPurchase failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order_purchase.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating OrderPurchase failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if (order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(order_purchase.getOrder_date()),
            order_purchase.getDescription(),
            order_purchase.isActive(),
            order_purchase.getExchange_rate(),
            order_purchase.getIva_rate(),
            order_purchase.getSub_total(),
            order_purchase.getIva(),
            order_purchase.getTotal(),
            order_purchase.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating OrderPurchase failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(OrderPurchase order_purchase) throws DAOException {
        Object[] values = {
            order_purchase.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting OrderPurchase failed, no rows affected.");
            } else{
                order_purchase.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an OrderPurchase.
     * @param resultSet The ResultSet of which the current row is to be mapped to an OrderPurchase.
     * @return The mapped OrderPurchase from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static OrderPurchase map(ResultSet resultSet) throws SQLException{
        OrderPurchase order_purchase = new OrderPurchase();
        order_purchase.setId(resultSet.getInt("id"));
        order_purchase.setOrder_date(resultSet.getDate("order_date"));
        order_purchase.setDescription(resultSet.getString("description"));
        order_purchase.setActive(resultSet.getBoolean("active"));
        order_purchase.setExchange_rate(resultSet.getDouble("exchange_rate"));
        order_purchase.setIva_rate(resultSet.getDouble("iva_rate"));
        order_purchase.setSub_total(resultSet.getDouble("sub_total"));
        order_purchase.setIva(resultSet.getDouble("iva"));
        order_purchase.setTotal(resultSet.getDouble("total"));
        return order_purchase;
    }
    
}
