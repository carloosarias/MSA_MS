/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.PurchaseItemDAO;
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
            "SELECT id, description, quantity FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_FIND_ORDER_PURCHASE_BY_ID =
            "SELECT ORDER_PURCHASE_ID FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_FIND_PRODUCT_BY_ID =
            "SELECT PRODUCT_ID FROM PURCHASE_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT id, description, quantity FROM PURCHASE_ITEM WHERE ORDER_PURCHASE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO PURCHASE_ITEM (ORDER_PURCHASE_ID, PRODUCT_ID, description, quantity) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PURCHASE_ITEM SET description = ?, quantity = ? WHERE id = ?";
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderPurchase findOrderPurchase(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product findProduct(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(OrderPurchase order_purchase, Product product, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(PurchaseItem purchase_item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
