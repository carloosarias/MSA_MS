/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.QuoteDAOJDBC.map;
import dao.interfaces.QuoteItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Quote;
import model.QuoteItem;
import model.SpecificationItem;

/**
 *
 * @author Pavilion Mini
 */
public class QuoteItemDAOJDBC implements QuoteItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, unit_price FROM QUOTE_ITEM WHERE id = ?";
    private static final String SQL_FIND_QUOTE_BY_ID = 
            "SELECT QUOTE_ID FROM QUOTE_ITEM WHERE id = ?";
    private static final String SQL_FIND_SPECIFICATION_ITEM_BY_ID = 
            "SELECT SPECIFICATION_ITEM_ID FROM QUOTE_ITEM WHERE id = ?";
    private static final String SQL_LIST_OF_QUOTE_ORDER_BY_ID = 
            "SELECT id, unit_price FROM QUOTE_ITEM WHERE QUOTE_ID = ? ORDER BY ID";
    private static final String SQL_INSERT =
            "INSERT INTO QUOTE_ITEM (SPECIFICATION_ITEM_ID, QUOTE_ID, unit_price) "
            + "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE QUOTE_ITEM SET unit_price = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM QUOTE_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a QuoteItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this QuoteItem DAO for.
     */
    QuoteItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public QuoteItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the QuoteItem from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The QuoteItem from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */    
    private QuoteItem find(String sql, Object... values) throws DAOException {
        QuoteItem quote_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                quote_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return quote_item;
    }

    @Override
    public Quote findQuote(QuoteItem quote_item) throws IllegalArgumentException, DAOException {
        if(quote_item.getId() == null) {
            throw new IllegalArgumentException("QuoteItem is not created yet, the QuoteItem ID is null.");
        }
        
        Quote quote = null;
        
        Object[] values = {
            quote_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_QUOTE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                quote = daoFactory.getQuoteDAO().find(resultSet.getInt("QUOTE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return quote;
    }

    @Override
    public SpecificationItem findSpecificationItem(QuoteItem quote_item) throws IllegalArgumentException, DAOException {
        if(quote_item.getId() == null) {
            throw new IllegalArgumentException("QuoteItem is not created yet, the QuoteItem ID is null.");
        }
        
        SpecificationItem specification_item = null;
        
        Object[] values = {
            quote_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_SPECIFICATION_ITEM_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification_item = daoFactory.getSpecificationItemDAO().find(resultSet.getInt("SPECIFICATION_ITEM_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return specification_item;
    }

    @Override
    public List<QuoteItem> list(Quote quote) throws DAOException {
        if(quote.getId() == null) {
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }    
        
        List<QuoteItem> quote_item = new ArrayList<>();
        
        Object[] values = {
            quote.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_QUOTE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                quote_item.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return quote_item;
    }

    @Override
    public void create(SpecificationItem specification_item, Quote quote, QuoteItem quote_item) throws IllegalArgumentException, DAOException {
        if (specification_item.getId() == null) {
            throw new IllegalArgumentException("SpecificationItem is not created yet, the SpecificationItem ID is null.");
        }
        
        if(quote.getId() == null){
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }
        
        if(quote_item.getId() != null){
            throw new IllegalArgumentException("QuoteItem is already created, the QuoteItem ID is not null.");
        }
        
        Object[] values = {
            specification_item.getId(),
            quote.getId(),
            quote_item.getUnit_price(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating QuoteItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quote_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating QuoteItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(QuoteItem quote_item) throws IllegalArgumentException, DAOException {
        if (quote_item.getId() == null) {
            throw new IllegalArgumentException("QuoteItem is not created yet, the QuoteItem ID is null.");
        }
        
        Object[] values = {
            quote_item.getUnit_price(),
            quote_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating QuoteItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(QuoteItem quote_item) throws DAOException {
        Object[] values = {
            quote_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting QuoteItem failed, no rows affected.");
            } else{
                quote_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Quote.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Quote.
     * @return The mapped Quote from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static QuoteItem map(ResultSet resultSet) throws SQLException{
        QuoteItem quote_item = new QuoteItem();
        quote_item.setId(resultSet.getInt("id"));
        quote_item.setUnit_price(resultSet.getDouble("unit_price"));
        return quote_item;
    }
}
