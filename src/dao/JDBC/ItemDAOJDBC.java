/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.interfaces.ItemDAO;
import java.util.List;
import model.Item;
import model.ItemType;

/**
 *
 * @author Pavilion Mini
 */
public class ItemDAOJDBC implements ItemDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, desc, unit_price FROM ITEM WHERE id = ?";
    private static final String SQL_FIND_BY_NAME =
            "SELECT id, name, desc, unit_price FROM ITEM WHERE name = ?";
    private static final String SQL_FIND_TYPE_BY_ID =
            "SELECT ITEM_TYPE_ID FROM ITEM WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, desc, unit_price FROM ITEM ORDER BY id";
    private static final String SQL_LIST_BY_TYPE_ORDER_BY_ID = 
            "SELECT id, name, desc, unit_price FROM ITEM WHERE ITEM_TYPE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO ITEM (ITEM_TYPE_ID, name, desc, unit_price) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE ITEM SET ITEM_TYPE_ID = ?, name = ?, desc = ?, unit_price = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Item DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Item DAO for.
     */
    ItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Item find(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Item find(String name) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItemType findType(Item item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Item> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Item> list(ItemType type) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(ItemType type, Item item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(ItemType type, Item item) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Item item) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
