/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.Date;
import java.util.List;
import model.ProductPart;
import model.TransactionHistory;

/**
 *
 * @author Pavilion Mini
 */
public interface TransactionHistoryDAO {
        
    /**
     * Returns the total IncomingReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @return The total IncovmingReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if product_part_id equals null
     * @throws DAOException If something fails at database level.
     */ 
    public Integer IncomingQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    public Integer DepartQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    public Integer DepartQuantityDateRange(ProductPart product_part, Date start_date, Date end_date, boolean rejected) throws IllegalArgumentException, DAOException;
    
    public Integer ProcessQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    public Integer ProcessQuantityDateRange(ProductPart product_part, Date start_date, Date end_date, boolean quaility_passed) throws IllegalArgumentException, DAOException;
    
    public List<TransactionHistory> List();
    
    /*
        User selects part number
        User selects start/end date
        Tableview fills up with all transactions, showing date, type of transaction and quantity; ordered by date
        Details show the totals calculated from the tableview
    */
}
