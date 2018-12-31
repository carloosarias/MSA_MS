/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.OrderPurchaseIncomingReportDAO;
import java.util.List;
import model.Employee;
import model.OrderPurchase;
import model.OrderPurchaseIncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingReportDAOJDBC implements OrderPurchaseIncomingReportDAO {

    @Override
    public OrderPurchaseIncomingReport find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderPurchase findOrderPurchase(OrderPurchaseIncomingReport order_purchase_incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
