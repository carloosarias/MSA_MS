/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.OrderPurchaseDAO;
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
public class OrderPurchaseDAOJDBC implements OrderPurchaseDAO{
    // Constants ----------------------------------------------------------------------------------
    
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

    @Override
    public List<OrderPurchase> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchase> listActive(boolean active) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderPurchase> listCompany(Company company) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Company company, CompanyAddress address, OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
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
        return order_purchase;
    }
    
}
