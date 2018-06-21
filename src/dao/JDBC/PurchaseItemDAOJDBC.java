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
import model.Product;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public class PurchaseItemDAOJDBC implements PurchaseItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, delivery_date, unit_price, description, quantity FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_FIND_ORDER_PURCHASE_BY_ID =
            "SELECT ORDER_PURCHASE_ID FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_FIND_PRODUCT_BY_ID =
            "SELECT PRODUCT_ID FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT id, delivery_date, unit_price, description, quantity FROM PURCHASE_ITEM WHERE ORDER_PURCHASE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO PURCHASE_ITEM (ORDER_PURCHASE_ID, PRODUCT_ID, delivery_date, unit_price, description, quantity) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PURCHASE_ITEM SET delivery_date = ?, unit_price = ?, description = ?, quantity = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM PURCHASE_ITEM WHERE id = ?";
    
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
     * Returns the PurchaseItem from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The PurchaseItem from the database matching the given SQL query with the given values.
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
    public Product findProduct(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        if(purchase_item.getId() == null) {
            throw new IllegalArgumentException("PurchaseItem is not created yet, the PurchaseItem ID is null.");
        }
        
        Product product = null;
        
        Object[] values = {
            purchase_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                product = daoFactory.getProductDAO().find(resultSet.getInt("PRODUCT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        if(order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }    
        
        List<PurchaseItem> purchase_item = new ArrayList<>();
        
        Object[] values = {
            order_purchase.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_ORDER_PURCHASE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                purchase_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return purchase_item;
    }

    @Override
    public void create(OrderPurchase order_purchase, Product product, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        if (order_purchase.getId() == null) {
            throw new IllegalArgumentException("OrderPurchase is not created yet, the OrderPurchase ID is null.");
        }
        
        if(product.getId() == null){
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        if(purchase_item.getId() != null){
            throw new IllegalArgumentException("PurchaseItem is already created, the PurchaseItem ID is null.");
        }
        
        Object[] values = {
            order_purchase.getId(),
            product.getId(),
            purchase_item.getDelivery_date(),
            purchase_item.getUnit_price(),
            purchase_item.getDescription(),
            purchase_item.getQuantity()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating PurchaseItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    purchase_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating PurchaseItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        if (purchase_item.getId() == null) {
            throw new IllegalArgumentException("PurchaseItem is not created yet, the PurchaseItem ID is null.");
        }
        
        Object[] values = {
            purchase_item.getDelivery_date(),
            purchase_item.getUnit_price(),
            purchase_item.getDescription(),
            purchase_item.getQuantity(),
            purchase_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating PurchaseItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(PurchaseItem purchase_item) throws DAOException {
        Object[] values = {
            purchase_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting PurchaseItem failed, no rows affected.");
            } else{
                purchase_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PurchaseItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an PurchaseItem.
     * @return The mapped PurchaseItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static PurchaseItem map(ResultSet resultSet) throws SQLException{
        PurchaseItem purchase_item = new PurchaseItem();
        purchase_item.setId(resultSet.getInt("id"));
        purchase_item.setDelivery_date(resultSet.getDate("delivery_date"));
        purchase_item.setUnit_price(resultSet.getDouble("unit_price"));
        purchase_item.setDescription(resultSet.getString("description"));
        purchase_item.setQuantity(resultSet.getInt("quantity"));
        return purchase_item;
    }    
    
}
