/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.DepartItem;
import model.DepartLot;

/**
 *
 * @author Pavilion Mini
 */
public interface DepartLotDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the DepartLot from the database matching the given ID, otherwise null.
     * @param id The ID of the DepartLot to be returned.
     * @return The DepartLot from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */    
    public DepartLot find(Integer id) throws DAOException;
    
    /**
     * Returns the DepartItem from the database matching the given DepartLot ID, otherwise null.
     * DepartLot ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param depart_lot The DepartLot ID to get the DepartItem from.
     * @return The DepartItem from the database matching the given DepartLot ID, otherwise null.
     * @throws IllegalArgumentException If DepartLot ID is null.
     * @throws DAOException If something fails at database level.
     */      
    public DepartItem findDepartItem(DepartLot depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all DepartLot from a given DepartItem from the database ordered by DepartLot ID. The DepartItem ID must not be null
     * otherwise it will throw IllegalArgumentException. The list is never null and
     * is empty when the database does not contain any DepartLot matching DepartItem.
     * @param depart_item The DepartItem to be searched for.
     * @return A list of all DepartLot matching DepartItem from the database ordered by DepartLot ID.
     * @throws IllegalArgumentException If the DepartItem ID is null.
     * @throws DAOException If something fails at database level.
     */
    public List<DepartLot> list(DepartItem depart_item) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns a list of all DepartLot matching a given lot_number from the database ordered by IncomingLot ID.
     * The list is never null and is empty when the database does not contain any DepartLot matching lot_number.
     * @param lot_number The lot_number to be searched for.
     * @return A list of all DepartLot matching lot_number from the database ordered by DepartLot ID.
     * @throws DAOException If something fails at database level.
     */    
    public List<DepartLot> list(String lot_number) throws IllegalArgumentException;
    
    /**
     * Create the given DepartLot for a given DepartItem in the database. 
     * The DepartItem ID must not be null and the DepartItem ID must be null,
     * otherwise it will throw IllegalArgumentException. 
     * After creating, the DAO will set the obtained ID in the given CompanyAddress.
     * @param depart_item The DepartItem to be assigned to the DepartLot.
     * @param depart_lot The DepartLot to be created.
     * @throws IllegalArgumentException If the DepartItem ID is null.
     * @throws IllegalArgumentException If the DepartLot ID is not null.
     * @throws DAOException If something fails at database level.
     */    
    public void create(DepartItem depart_item, DepartLot depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Update the given DepartLot in the database.
     * The DepartLot ID must not be null otherwise it will throw IllegalArgumentException.
     * @param depart_lot The DepartLot to be updated in the database.
     * @throws IllegalArgumentException If the DepartLot ID is null.
     * @throws DAOException If something fails at database level.
     */    
    public void update(DepartLot depart_lot) throws IllegalArgumentException, DAOException;
    
    /**
     * Delete the given DepartLot from the database. After deleting,
     * the DAO will set the ID of the given DepartLot to null.
     * @param depart_lot The DepartLot to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */    
    public void delete(DepartLot depart_lot) throws DAOException;
    
    public Integer getTotalQuantity(DepartItem depart_item);
    
    public Integer getTotalBoxQuantity(DepartItem depart_item);    
}
