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
     * Returns the total IncomingReport quantity in a range of dates from the database matching the given ProductPart. 
     * ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @return The total IncomingReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */ 
    public Integer IncomingQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the total DepartReport quantity in a range of dates from the database matching the given ProductPart. 
     * ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @return The total DepartReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */ 
    public Integer DepartQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the total DepartReport quantity in a range of dates from the database matching the given ProductPart. 
     * ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @param rejected The rejected status for the search.
     * @return The total DepartReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */ 
    public Integer DepartQuantityDateRange(ProductPart product_part, Date start_date, Date end_date, boolean rejected) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the total ProcessReport quantity in a range of dates from the database matching the given ProductPart. 
     * ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @return The total ProcessReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */ 
    public Integer ProcessQuantityDateRange(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    /**
     * Returns the total DepartReport quantity in a range of dates from the database matching the given ProductPart. 
     * ProductPart ID must not be null, otherwise it will throw IllegalArgumentException.
     * @param product_part The ProductPart to be searched for.
     * @param start_date The start date for the search.
     * @param end_date The end date for the search.
     * @param quality_passed The quality_passed status for the search.
     * @return The total DepartReport quantity in a range of dates from the database matching the given ProductPart, otherwise null.
     * @throws IllegalArgumentException if ProductPart ID is null.
     * @throws DAOException If something fails at database level.
     */ 
    public Integer ProcessQuantityDateRange(ProductPart product_part, Date start_date, Date end_date, boolean quality_passed) throws IllegalArgumentException, DAOException;
    /**
    * Generates a list of TransactionHistory objects generated from the input, it includes IncomingReport,
    * DepartReport and ProcessReport items of a given ProductPart and between the given range of dates.
    * @param product_part the ProductPart 
    * @param start_date The start date for the search.
    * @param end_date The end date for the search.
    * @return A list of TransactionHistory including IncomingReport, DepartReport and ProcessReport items of a given ProductPart and between a given range of dates
    * @throws IllegalArgumentException If ProductPart ID is null.
    * @throws DAOException If Something fails at database level.
    */
    public List<TransactionHistory> List(ProductPart product_part, Date start_date, Date end_date) throws IllegalArgumentException, DAOException;
    
    /*
        User selects part number
        User selects start/end date
        Tableview fills up with all transactions, showing date, type of transaction and quantity; ordered by date
        Details show the totals calculated from the tableview
    */
}
