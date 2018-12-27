/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.PurchaseItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderPurchase;
import model.ProductSupplier;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public class PurchaseItemDAOJDBC implements PurchaseItemDAO {
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, report_date, comments, status, exchange_rate, iva_rate FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_ORDER_PURCHASE_BY_ID = 
            "SELECT COMPANY_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_FIND_PRODUCT_SUPPLIER_BY_ID = 
            "SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE WHERE id = ?";
    private static final String SQL_LIST_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT id, report_date, comments, status, exchange_rate, iva_rate FROM ORDER_PURCHASE WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_LIST_STATUS_ORDER_BY_ID = 
            "SELECT id, report_date, comments, status, exchange_rate, iva_rate FROM ORDER_PURCHASE WHERE status = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO ORDER_PURCHASE (EMPLOYEE_ID, EQUIPMENT_ID, report_date, comments, status, exchange_rate, iva_rate) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ORDER_PURCHASE SET report_date = ?, comments = ?, status = ?, exchange_rate = ?, iva_rate = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM ORDER_PURCHASE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a PurchaseItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this PurchaseItem DAO for.
     */
    PurchaseItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public PurchaseItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the OrderPurchase from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The OrderPurchase from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private PurchaseItem find(String sql, Object... values) throws DAOException {
        PurchaseItem purchase_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                purchase_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return purchase_item;
    }

    @Override
    public OrderPurchase findOrderPurchase(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
       if(purchase_item.getId() == null) {
                throw new IllegalArgumentException("PurchaseItem is not created yet, the PurchaseItem ID is null.");
            }

            OrderPurchase order_purchase = null;

            Object[] values = {
                purchase_item.getId()
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
    public ProductSupplier findProductSupplier(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
       if(purchase_item.getId() == null) {
                throw new IllegalArgumentException("PurchaseItem is not created yet, the PurchaseItem ID is null.");
            }

            ProductSupplier product_supplier = null;

            Object[] values = {
                purchase_item.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_SUPPLIER_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    product_supplier = daoFactory.getProductSupplierDAO().find(resultSet.getInt("PRODUCT_SUPPLIER_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return product_supplier;
    }

    @Override
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws DAOException {
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }    
        
        List<PurchaseItem> purchaseitem_list = new ArrayList<>();
        
        Object[] values = {
            order_purchase.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ORDER_PURCHASE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                purchaseitem_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return purchaseitem_list;
    }

    @Override
    public void create(OrderPurchase order_purchase, ProductSupplier product_supplier, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
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
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(PurchaseItem purchase_item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PurchaseItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped PurchaseItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static PurchaseItem map(ResultSet resultSet) throws SQLException{
        PurchaseItem purchase_item = new PurchaseItem();
        purchase_item.setId(resultSet.getInt("id"));
        purchase_item.setUnits_ordered(resultSet.getInt("units_ordered"));
        purchase_item.setPrice_timestamp(resultSet.getDouble("price_timestamp"));
        purchase_item.setPrice_updated(resultSet.getDouble("price_updated"));
        purchase_item.setDate_modified(resultSet.getDate("date_modified"));
        purchase_item.setModified(resultSet.getBoolean("modified"));
        return purchase_item;
    }
}
