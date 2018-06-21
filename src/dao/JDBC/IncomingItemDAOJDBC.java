/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingItemDAOJDBC {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, lot_number, quantity, box_quantity, quality_pass, details FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_INCOMING_REPORT_BY_ID =
            "SELECT INCOMING_REPORT_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID =
            "SELECT PRODUCT_ID FROM INCOMING_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_ORDER_PURCHASE_ORDER_BY_ID = 
            "SELECT id, lot_number, quantity, box_quantity, quality_pass, details FROM INCOMING_ITEM WHERE ORDER_PURCHASE_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_ITEM (INCOMING_REPORT_ID, PART_REVISION_ID, lot_number, quantity, box_quantity, quality_pass, details) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_ITEM SET lot_number = ?, quantity = ?, box_quantity = ?, quality_pass = ?, details = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INCOMING_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingItem DAO for.
     */
    IncomingItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------    
}
