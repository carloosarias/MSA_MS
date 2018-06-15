/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.OrderPurchase;
import model.Product;
import model.PurchaseItem;

/**
 *
 * @author Pavilion Mini
 */
public interface PurchaseItemDAO {
    /**
     * Returns the PurchaseItem from the database matching the given ID, otherwise null.
     * @param id The ID of the PurchaseItem to be returned.
     * @return The PurchaseItem from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public PurchaseItem find(Integer id) throws DAOException;
    
    public OrderPurchase findOrderPurchase(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    public Product findProduct(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    public List<PurchaseItem> list(OrderPurchase order_purchase) throws IllegalArgumentException, DAOException;
    
    public void create(OrderPurchase order_purchase, Product product, PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    public void update(PurchaseItem purchase_item) throws IllegalArgumentException, DAOException;
    
    public void delete(PurchaseItem purchase_item) throws DAOException;
}
