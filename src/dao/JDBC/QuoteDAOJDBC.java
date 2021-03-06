/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.QuoteDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CompanyContact;
import model.DepartLot;
import model.PartRevision;
import model.Quote;

/**
 *
 * @author Pavilion Mini
 */
public class QuoteDAOJDBC implements QuoteDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT QUOTE.id, QUOTE.quote_date, QUOTE.estimated_annual_usage, QUOTE.comments, QUOTE.estimated_total, QUOTE.active, "
            + "SPECIFICATION.specification_number, SPECIFICATION.process, PART_REVISION.area, PART_REVISION.rev, COMPANY.id, COMPANY.name, COMPANY_CONTACT.name, COMPANY_CONTACT.email, "
            + "COMPANY_CONTACT.phone_number, PRODUCT_PART.part_number, PRODUCT_PART.description "
            + "FROM QUOTE "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY_CONTACT ON QUOTE.COMPANY_CONTACT_ID = COMPANY_CONTACT.id "
            + "INNER JOIN COMPANY ON COMPANY_CONTACT.COMPANY_ID = COMPANY.id "
            + "WHERE QUOTE.id = ?";
    private static final String SQL_FIND_LATEST = 
            "SELECT QUOTE.id, QUOTE.quote_date, QUOTE.estimated_annual_usage, QUOTE.comments, QUOTE.estimated_total, QUOTE.active, "
            + "SPECIFICATION.specification_number, SPECIFICATION.process, PART_REVISION.area, PART_REVISION.rev, COMPANY.id, COMPANY.name, COMPANY_CONTACT.name, COMPANY_CONTACT.email, "
            + "COMPANY_CONTACT.phone_number, PRODUCT_PART.part_number, PRODUCT_PART.description "
            + "FROM QUOTE "
            + "INNER JOIN DEPART_REPORT ON ? = DEPART_REPORT.id "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY_CONTACT ON QUOTE.COMPANY_CONTACT_ID = COMPANY_CONTACT.id "
            + "INNER JOIN COMPANY ON COMPANY_CONTACT.COMPANY_ID = COMPANY.id "
            + "WHERE COMPANY.id = DEPART_REPORT.COMPANY_ID AND QUOTE.PART_REVISION_ID = ? AND QUOTE.active = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID = 
            "SELECT PART_REVISION_ID FROM QUOTE WHERE id = ?";
    private static final String SQL_FIND_COMPANY_CONTACT_BY_ID = 
            "SELECT COMPANY_CONTACT_ID FROM QUOTE WHERE id = ?";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_DATE = 
            "SELECT QUOTE.id, QUOTE.quote_date, QUOTE.estimated_annual_usage, QUOTE.comments, QUOTE.estimated_total, QUOTE.active, "
            + "SPECIFICATION.specification_number, SPECIFICATION.process, PART_REVISION.area, PART_REVISION.rev, COMPANY.id, COMPANY.name, COMPANY_CONTACT.name, COMPANY_CONTACT.email, "
            + "COMPANY_CONTACT.phone_number, PRODUCT_PART.part_number, PRODUCT_PART.description "
            + "FROM QUOTE "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY_CONTACT ON QUOTE.COMPANY_CONTACT_ID = COMPANY_CONTACT.id "
            + "INNER JOIN COMPANY ON COMPANY_CONTACT.COMPANY_ID = COMPANY.id "
            + "WHERE QUOTE.active = ? "
            + "ORDER BY QUOTE.quote_date DESC";
    private static final String SQL_LIST_ACTIVE_OF_DEPARTLOT_PARTREVISION_ORDER_BY_DATE = 
            "SELECT QUOTE.id, QUOTE.quote_date, QUOTE.estimated_annual_usage, QUOTE.comments, QUOTE.estimated_total, QUOTE.active, "
            + "SPECIFICATION.specification_number, SPECIFICATION.process, PART_REVISION.area, PART_REVISION.rev, COMPANY.id, COMPANY.name, COMPANY_CONTACT.name, COMPANY_CONTACT.email, "
            + "COMPANY_CONTACT.phone_number, PRODUCT_PART.part_number, PRODUCT_PART.description "
            + "FROM QUOTE "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY_CONTACT ON QUOTE.COMPANY_CONTACT_ID = COMPANY_CONTACT.id "
            + "INNER JOIN COMPANY ON COMPANY_CONTACT.COMPANY_ID = COMPANY.id "
            + "INNER JOIN DEPART_LOT ON ? = DEPART_LOT.id "
            + "WHERE QUOTE.PART_REVISION_ID = DEPART_LOT.PART_REVISION_ID AND QUOTE.active = ? "
            + "ORDER BY QUOTE.quote_date DESC";
    private static final String SQL_LIST_ACTIVE_OF_PARTREVISION_ORDER_BY_DATE = 
            "SELECT QUOTE.id, QUOTE.quote_date, QUOTE.estimated_annual_usage, QUOTE.comments, QUOTE.estimated_total, QUOTE.active, "
            + "SPECIFICATION.specification_number, SPECIFICATION.process, PART_REVISION.area, PART_REVISION.rev, COMPANY.id, COMPANY.name, COMPANY_CONTACT.name, COMPANY_CONTACT.email, "
            + "COMPANY_CONTACT.phone_number, PRODUCT_PART.part_number, PRODUCT_PART.description "
            + "FROM QUOTE "
            + "INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY_CONTACT ON QUOTE.COMPANY_CONTACT_ID = COMPANY_CONTACT.id "
            + "INNER JOIN COMPANY ON COMPANY_CONTACT.COMPANY_ID = COMPANY.id "
            + "WHERE QUOTE.PART_REVISION_ID = ? AND QUOTE.active = ? "
            + "ORDER BY QUOTE.quote_date DESC";
    private static final String SQL_INSERT =
            "INSERT INTO QUOTE (PART_REVISION_ID, COMPANY_CONTACT_ID, quote_date, estimated_annual_usage, comments, estimated_total, active) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE QUOTE SET quote_date = ?, estimated_annual_usage = ?, comments = ?, estimated_total = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM QUOTE WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Quote DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Quote DAO for.
     */
    QuoteDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Quote find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    @Override
    public Quote findLatest(Integer depart_report_id, Integer part_revision_id, boolean active) throws DAOException{
        return find(SQL_FIND_LATEST, depart_report_id, part_revision_id, active);
    }
    
    /**
     * Returns the Quote from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Quote from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */    
    private Quote find(String sql, Object... values) throws DAOException {
        Quote quote = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                quote = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return quote;
    }
            
    @Override
    public PartRevision findPartRevision(Quote quote) throws IllegalArgumentException, DAOException {
        if(quote.getId() == null) {
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            quote.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PART_REVISION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part_revision = daoFactory.getPartRevisionDAO().find(resultSet.getInt("PART_REVISION_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return part_revision;
    }

    @Override
    public CompanyContact findCompanyContact(Quote quote) throws IllegalArgumentException, DAOException {
        if(quote.getId() == null) {
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }
        
        CompanyContact company_contact = null;
        
        Object[] values = {
            quote.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_CONTACT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                company_contact = daoFactory.getCompanyContactDAO().find(resultSet.getInt("COMPANY_CONTACT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return company_contact;
    }

    @Override
    public List<Quote> list(boolean active) throws DAOException {
        List<Quote> quote = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_DATE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                quote.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return quote;
    }
    
    @Override
    public List<Quote> list(PartRevision part_revision, boolean active) throws IllegalArgumentException, DAOException {
        
        if(part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }    
        
        List<Quote> quote = new ArrayList<>();
        
        Object[] values = {
            part_revision.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_PARTREVISION_ORDER_BY_DATE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                quote.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return quote;
    }
    
    @Override
    public List<Quote> list(DepartLot depart_lot, boolean active) throws IllegalArgumentException, DAOException {
        if(depart_lot.getId() == null) {
            throw new IllegalArgumentException("DepartLot is not created yet, the DepartLot ID is null.");
        }    
        
        List<Quote> quote = new ArrayList<>();
        
        Object[] values = {
            depart_lot.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_OF_DEPARTLOT_PARTREVISION_ORDER_BY_DATE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                quote.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return quote;
    }
    
    @Override
    public void create(PartRevision part_revision, CompanyContact company_contact, Quote quote) throws IllegalArgumentException, DAOException {
        if (part_revision.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(company_contact.getId() == null){
            throw new IllegalArgumentException("CompanyContact is not created yet, the CompanyContact ID is null.");
        }
        
        if(quote.getId() != null){
            throw new IllegalArgumentException("Quote is already created, the Quote ID is not null.");
        }
        
        Object[] values = {
            part_revision.getId(),
            company_contact.getId(),
            DAOUtil.toSqlDate(quote.getQuote_date()),
            quote.getEstimated_annual_usage(),
            quote.getComments(),
            quote.getEstimated_total(),
            quote.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Quote failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quote.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Quote failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Quote quote) throws IllegalArgumentException, DAOException {
        if (quote.getId() == null) {
            throw new IllegalArgumentException("Quote is not created yet, the Invoice ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(quote.getQuote_date()),
            quote.getEstimated_annual_usage(),
            quote.getComments(),
            quote.getEstimated_total(),
            quote.isActive(),
            quote.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Quote failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Quote quote) throws DAOException {
        Object[] values = {
            quote.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Quote failed, no rows affected.");
            } else{
                quote.setId(null);
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
    public static Quote map(ResultSet resultSet) throws SQLException{
        Quote quote = new Quote();
        quote.setId(resultSet.getInt("id"));
        quote.setQuote_date(resultSet.getDate("quote_date"));
        quote.setEstimated_annual_usage(resultSet.getInt("estimated_annual_usage"));
        quote.setComments(resultSet.getString("comments"));
        quote.setEstimated_total(resultSet.getDouble("estimated_total"));
        quote.setActive(resultSet.getBoolean("active"));
        
        //INNER JOINS
        quote.setSpec_number(resultSet.getString("SPECIFICATION.specification_number"));
        quote.setSpec_process(resultSet.getString("SPECIFICATION.process"));
        quote.setPartrev_area(resultSet.getDouble("PART_REVISION.area"));
        quote.setCompany_id(resultSet.getInt("COMPANY.id"));
        quote.setCompany_name(resultSet.getString("COMPANY.name"));
        quote.setContact_name(resultSet.getString("COMPANY_CONTACT.name"));
        quote.setContact_email(resultSet.getString("COMPANY_CONTACT.email"));
        quote.setContact_number(resultSet.getString("COMPANY_CONTACT.phone_number"));
        quote.setPart_number(resultSet.getString("PRODUCT_PART.part_number"));
        quote.setPart_description(resultSet.getString("PRODUCT_PART.description"));
        quote.setPart_rev(resultSet.getString("PART_REVISION.rev"));
        return quote;
    }
}
