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
import model.OrderPurchase;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseDAOJDBC implements OrderPurchaseDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT ORDER_PURCHASE.id, ORDER_PURCHASE.report_date, ORDER_PURCHASE.comments, ORDER_PURCHASE.status, ORDER_PURCHASE.exchange_rate, ORDER_PURCHASE.iva_rate, "
            + "COMPANY.name, COMPANY_ADDRESS.address "
            + "FROM ORDER_PURCHASE "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "INNER JOIN COMPANY_ADDRESS ON ORDER_PURCHASE.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "WHERE ORDER_PURCHASE.id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_ADDRESS_BY_ID = 
            "SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE.id, ORDER_PURCHASE.report_date, ORDER_PURCHASE.comments, ORDER_PURCHASE.status, ORDER_PURCHASE.exchange_rate, ORDER_PURCHASE.iva_rate, "
            + "COMPANY.name, COMPANY_ADDRESS.address "
            + "FROM ORDER_PURCHASE "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "INNER JOIN COMPANY_ADDRESS ON ORDER_PURCHASE.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "ORDER BY ORDER_PURCHASE.id";
    private static final String SQL_LIST_COMPANY_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE.id, ORDER_PURCHASE.report_date, ORDER_PURCHASE.comments, ORDER_PURCHASE.status, ORDER_PURCHASE.exchange_rate, ORDER_PURCHASE.iva_rate, "
            + "COMPANY.name, COMPANY_ADDRESS.address "
            + "FROM ORDER_PURCHASE "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "INNER JOIN COMPANY_ADDRESS ON ORDER_PURCHASE.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "WHERE ORDER_PURCHASE.COMPANY_ID = ? "
            + "ORDER BY ORDER_PURCHASE.id";
    private static final String SQL_LIST_STATUS_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE.id, ORDER_PURCHASE.report_date, ORDER_PURCHASE.comments, ORDER_PURCHASE.status, ORDER_PURCHASE.exchange_rate, ORDER_PURCHASE.iva_rate, "
            + "COMPANY.name, COMPANY_ADDRESS.address "
            + "FROM ORDER_PURCHASE "
            + "INNER JOIN COMPANY ON ORDER_PURCHASE.COMPANY_ID = COMPANY.id "
            + "INNER JOIN COMPANY_ADDRESS ON ORDER_PURCHASE.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "WHERE ORDER_PURCHASE.status = ? "
            + "ORDER BY ORDER_PURCHASE.id";
    private static final String SQL_INSERT = 
            "INSERT INTO ORDER_PURCHASE (COMPANY_ID, COMPANY_ADDRESS_ID, report_date, comments, status, exchange_rate, iva_rate) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ORDER_PURCHASE SET report_date = ?, comments = ?, status = ?, exchange_rate = ?, iva_rate = ? WHERE id = ?";
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
    public CompanyAddress findCompanyAddress(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if(order_purchase.getId() == null) {
                throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
            }

            CompanyAddress company_address = null;

            Object[] values = {
                order_purchase.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_ADDRESS_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    company_address = daoFactory.getCompanyAddressDAO().find(resultSet.getInt("COMPANY_ADDRESS_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return company_address;
    }

    @Override
    public List<OrderPurchase> list() throws DAOException {
        List<OrderPurchase> orderpurchase_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                orderpurchase_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return orderpurchase_list;
    }

    @Override
    public List<OrderPurchase> listOfCompany(Company company) throws DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<OrderPurchase> orderpurchase_list = new ArrayList<>();
        
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                orderpurchase_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return orderpurchase_list;
    }

    @Override
    public List<OrderPurchase> listOfStatus(String status) throws DAOException {
        List<OrderPurchase> orderpurchase_list = new ArrayList<>();
        
        Object[] values = {
            status
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_STATUS_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                orderpurchase_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return orderpurchase_list;
    }

    @Override
    public void create(Company company, CompanyAddress company_address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        if(company_address.getId() == null){
            throw new IllegalArgumentException("CompanyAddress is not created yet, the CompanyAddress ID is null.");
        }
        if(order_purchase.getId() != null){
            throw new IllegalArgumentException("OrderPurchase is already created, the OrderPurchase ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            company_address.getId(),
            DAOUtil.toSqlDate(order_purchase.getReport_date()),
            order_purchase.getComments(),
            order_purchase.getStatus(),
            order_purchase.getExchange_rate(),
            order_purchase.getIva_rate()
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
            throw new IllegalArgumentException("OrderPurchase is not created yet, the MantainanceReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(order_purchase.getReport_date()),
            order_purchase.getComments(),
            order_purchase.getStatus(),
            order_purchase.getExchange_rate(),
            order_purchase.getIva_rate(),
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
     * Map the current row of the given ResultSet to an MantainanceReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped MantainanceReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static OrderPurchase map(ResultSet resultSet) throws SQLException{
        OrderPurchase order_purchase = new OrderPurchase();
        order_purchase.setId(resultSet.getInt("ORDER_PURCHASE.id"));
        order_purchase.setReport_date(resultSet.getDate("ORDER_PURCHASE.report_date"));
        order_purchase.setComments(resultSet.getString("ORDER_PURCHASE.comments"));
        order_purchase.setStatus(resultSet.getString("ORDER_PURCHASE.status"));
        order_purchase.setExchange_rate(resultSet.getDouble("ORDER_PURCHASE.exchange_rate"));
        order_purchase.setIva_rate(resultSet.getDouble("ORDER_PURCHASE.iva_rate"));
        
        //INNER JOINS
        order_purchase.setCompany_name(resultSet.getString("COMPANY.name"));
        order_purchase.setCompanyaddress_address(resultSet.getString("COMPANY_ADDRESS.address"));
        return order_purchase;
    }
}
