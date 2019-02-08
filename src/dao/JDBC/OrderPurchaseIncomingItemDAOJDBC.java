/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.OrderPurchaseIncomingItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderPurchaseIncomingItem;
import model.OrderPurchaseIncomingReport;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public class OrderPurchaseIncomingItemDAOJDBC implements OrderPurchaseIncomingItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_ITEM.id, ORDER_PURCHASE_INCOMING_ITEM.units_arrived, "
            + "PURCHASE_ITEM.units_ordered, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.serial_number, PRODUCT.description, PRODUCT.unit_measure "
            + "FROM ORDER_PURCHASE_INCOMING_ITEM "
            + "INNER JOIN PURCHASE_ITEM ON ORDER_PURCHASE_INCOMING_ITEM.PURCHASE_ITEM_ID = PURCHASE_ITEM.id "
            + "INNER JOIN PRODUCT_SUPPLIER ON PURCHASE_ITEM.PRODUCT_SUPPLIER_ID = PRODUCT_SUPPLIER.id "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "WHERE ORDER_PURCHASE_INCOMING_ITEM.id = ?";
    private static final String SQL_FIND_ORDER_PURCHASE_INCOMING_REPORT_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_REPORT_ID FROM ORDER_PURCHASE_INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_PURCHASE_ITEM_BY_ID = 
            "SELECT PURCHASE_ITEM_ID FROM ORDER_PURCHASE_INCOMING_ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_PURCHASE_INCOMING_REPORT_ORDER_BY_ID = 
            "SELECT ORDER_PURCHASE_INCOMING_ITEM.id, ORDER_PURCHASE_INCOMING_ITEM.units_arrived, "
            + "PURCHASE_ITEM.units_ordered, PRODUCT_SUPPLIER.quantity, PRODUCT_SUPPLIER.serial_number, PRODUCT.description, PRODUCT.unit_measure "
            + "FROM ORDER_PURCHASE_INCOMING_ITEM "
            + "INNER JOIN PURCHASE_ITEM ON ORDER_PURCHASE_INCOMING_ITEM.PURCHASE_ITEM_ID = PURCHASE_ITEM.id "
            + "INNER JOIN PRODUCT_SUPPLIER ON PURCHASE_ITEM.PRODUCT_SUPPLIER_ID = PRODUCT_SUPPLIER.id "
            + "INNER JOIN PRODUCT ON PRODUCT_SUPPLIER.PRODUCT_ID = PRODUCT.id "
            + "WHERE ORDER_PURCHASE_INCOMING_ITEM.ORDER_PURCHASE_INCOMING_REPORT_ID = ? "
            + "ORDER BY ORDER_PURCHASE_INCOMING_ITEM.id";
    private static final String SQL_INSERT = 
            "INSERT INTO ORDER_PURCHASE_INCOMING_ITEM (ORDER_PURCHASE_INCOMING_REPORT_ID, PURCHASE_ITEM_ID, units_arrived) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ORDER_PURCHASE_INCOMING_ITEM SET units_arrived = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM ORDER_PURCHASE_INCOMING_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a OrderPurchaseIncomingItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this OrderPurchaseIncomingItem DAO for.
     */
    OrderPurchaseIncomingItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public OrderPurchaseIncomingItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the MantainanceReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The MantainanceReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private OrderPurchaseIncomingItem find(String sql, Object... values) throws DAOException {
        OrderPurchaseIncomingItem order_purchase_incoming_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                order_purchase_incoming_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return order_purchase_incoming_item;
    }
    
    @Override
    public OrderPurchaseIncomingReport findOrderPurchaseIncomingReport(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException {
        if(order_purchase_incoming_item.getId() == null) {
                throw new IllegalArgumentException("OrderPurchaseIncomingItem is not created yet, the OrderPurchaseIncomingItem ID is null.");
            }

            OrderPurchaseIncomingReport order_purchase_incoming_report = null;

            Object[] values = {
                order_purchase_incoming_item.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_ORDER_PURCHASE_INCOMING_REPORT_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    order_purchase_incoming_report = daoFactory.getOrderPurchaseIncomingReportDAO().find(resultSet.getInt("ORDER_PURCHASE_INCOMING_REPORT_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return order_purchase_incoming_report;
    }

    @Override
    public PurchaseItem findPurchaseItem(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException {
        if(order_purchase_incoming_item.getId() == null) {
                throw new IllegalArgumentException("OrderPurchaseIncomingItem is not created yet, the OrderPurchaseIncomingItem ID is null.");
            }

            PurchaseItem order_purchase_incoming_report = null;

            Object[] values = {
                order_purchase_incoming_item.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_PURCHASE_ITEM_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    order_purchase_incoming_report = daoFactory.getPurchaseItemDAO().find(resultSet.getInt("PURCHASE_ITEM_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return order_purchase_incoming_report;
    }

    @Override
    public List<OrderPurchaseIncomingItem> list(OrderPurchaseIncomingReport order_purchase_incoming_report) throws DAOException {
        if(order_purchase_incoming_report.getId() == null) {
            throw new IllegalArgumentException("OrderPurchaseIncomingReport is not created yet, the OrderPurchaseIncomingReport ID is null.");
        }    
        
        List<OrderPurchaseIncomingItem> order_purchase_incoming_item = new ArrayList<>();
        
        Object[] values = {
            order_purchase_incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ORDER_PURCHASE_INCOMING_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                order_purchase_incoming_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return order_purchase_incoming_item;
    }

    @Override
    public void create(OrderPurchaseIncomingReport order_purchase_incoming_report, PurchaseItem purchase_item, OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException {
        if (order_purchase_incoming_report.getId() == null) {
            throw new IllegalArgumentException("OrderPurchaseIncomingReport is not created yet, the OrderPurchaseIncomingReport ID is null.");
        }
        if(purchase_item.getId() == null){
            throw new IllegalArgumentException("PurchaseItem is not created yet, the PurchaseItem ID is null.");
        }
        if(order_purchase_incoming_item.getId() != null){
            throw new IllegalArgumentException("OrderPurchaseIncomingItem is already created, the OrderPurchaseIncomingItem ID is not null.");
        }
        
        Object[] values = {
            order_purchase_incoming_report.getId(),
            purchase_item.getId(),
            order_purchase_incoming_item.getUnits_arrived(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating OrderPurchaseIncomingItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order_purchase_incoming_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating OrderPurchaseIncomingItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(OrderPurchaseIncomingItem order_purchase_incoming_item) throws IllegalArgumentException, DAOException {
        if (order_purchase_incoming_item.getId() == null) {
            throw new IllegalArgumentException("OrderPurchaseIncomingItem is not created yet, the OrderPurchaseIncomingItem ID is null.");
        }
        
        Object[] values = {
            order_purchase_incoming_item.getUnits_arrived(),
            order_purchase_incoming_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating OrderPurchaseIncomingItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(OrderPurchaseIncomingItem order_purchase_incoming_item) throws DAOException {
        Object[] values = {
            order_purchase_incoming_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting OrderPurchaseIncomingItem failed, no rows affected.");
            } else{
                order_purchase_incoming_item.setId(null);
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
    public static OrderPurchaseIncomingItem map(ResultSet resultSet) throws SQLException{
        OrderPurchaseIncomingItem order_purchase_incoming_item = new OrderPurchaseIncomingItem();
        order_purchase_incoming_item.setId(resultSet.getInt("ORDER_PURCHASE_INCOMING_ITEM.id"));
        order_purchase_incoming_item.setUnits_arrived(resultSet.getInt("ORDER_PURCHASE_INCOMING_ITEM.units_arrived"));
        
        order_purchase_incoming_item.setPurchaseitem_unitsordered(resultSet.getInt("PURCHASE_ITEM.units_ordered"));
        order_purchase_incoming_item.setProductsupplier_quantity(resultSet.getDouble("PRODUCT_SUPPLIER.quantity"));
        order_purchase_incoming_item.setProductsupplier_serialnumber(resultSet.getString("PRODUCT_SUPPLIER.serial_number"));
        order_purchase_incoming_item.setProduct_description(resultSet.getString("PRODUCT.description"));
        order_purchase_incoming_item.setProduct_unitmeasure(resultSet.getString("PRODUCT.unit_measure"));
        return order_purchase_incoming_item;
    }
}
