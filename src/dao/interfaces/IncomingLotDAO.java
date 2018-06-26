/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.IncomingItem;
import model.IncomingLot;


/**
 *
 * @author Pavilion Mini
 */
public interface IncomingLotDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the IncomingLot from the database matching the given ID, otherwise null.
     * @param id The ID of the IncomingLot to be returned.
     * @return The IncomingLot from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public IncomingLot find(Integer id) throws DAOException;
    
    /**
     * Returns the IncomingItem from the database matching the given IncomingLot ID, otherwise null.
     * IncomingLot ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot ID to get the IncomingItem from.
     * @return The IncomingItem from the database matching the given IncomingLot ID, otherwise null.
     * @throws IllegalArgumentException If IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public IncomingItem findIncomingItem(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all IncomingLot from a given IncomingItem from the database ordered by IncomingLot ID. The IncomingItem ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any IncomingLot matching IncomingItem.
     * @param incoming_item The IncomingItem to be searched for.
     * @return A list of all IncomingLot matching IncomingItem from the database ordered by IncomingLot ID.
     * @throws IllegalArgumentException If the IncomingItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<IncomingLot> list(IncomingItem incoming_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Create the given IncomingLot for a given IncomingItem in the database. 
     * The IncomingItem ID must not be null and the IncomingItem ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param incoming_item The IncomingItem to be assigned to the IncomingLot.
     * @param incoming_lot The IncomingLot to be created.
     * @throws IllegalArgumentException If the IncomingItem ID is null.
     * @throws IllegalArgumentException If the IncomingLot ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(IncomingItem incoming_item, IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given IncomingLot in the database.
     * The IncomingLot ID must not be null otherwise it will throw IllegalArgumentException.
     * @param incoming_lot The IncomingLot to be updated in the database.
     * @throws IllegalArgumentException If the IncomingLot ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(IncomingLot incoming_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given IncomingLot from the database. After deleting,
     * the DAO will set the ID of the given IncomingLot to null.
     * @param incoming_lot The IncomingLot to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(IncomingLot incoming_lot) throws DAOException;
    
    public Integer getTotalQuantity(IncomingItem incoming_item);
    
    public Integer getTotalBoxQuantity(IncomingItem incoming_item);
}
