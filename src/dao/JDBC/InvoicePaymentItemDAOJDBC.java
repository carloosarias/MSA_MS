/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.InvoicePaymentReport;

/**
 *
 * @author Pavilion Mini
 */
public class InvoicePaymentItemDAOJDBC {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, report_date, check_number, ammount_paid, comments FROM INVOICE_PAYMENT_REPORT WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM INVOICE_PAYMENT_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date, check_number, ammount_paid, comments FROM INVOICE_PAYMENT_REPORT ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, report_date, po_number, packing_list FROM INVOICE_PAYMENT_REPORT WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INVOICE_PAYMENT_REPORT (COMPANY_ID, report_date, check_number, ammount_paid, comments) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INVOICE_PAYMENT_REPORT SET report_date = ?, check_number = ?, ammount_paid = ?, comments = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INVOICE_PAYMENT_REPORT WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a InvoicePaymentReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this InvoicePaymentReport DAO for.
     */
    InvoicePaymentReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public InvoicePaymentReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private InvoicePaymentReport find(String sql, Object... values) throws DAOException {
        InvoicePaymentReport invoice_payment_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                invoice_payment_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return invoice_payment_report;
    }
    
    @Override
    public Company findCompany(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException {
        if(invoice_payment_report.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentReport is not created yet, the InvoicePaymentReport ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            invoice_payment_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                company = daoFactory.getCompanyDAO().find(resultSet.getInt("COMPANY_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return company;
    }

    @Override
    public List<InvoicePaymentReport> list() throws DAOException {
        List<InvoicePaymentReport> invoice_payment_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_payment_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_payment_report;
    }

    @Override
    public List<InvoicePaymentReport> list(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<InvoicePaymentReport> invoice_payment_report = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                invoice_payment_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return invoice_payment_report; 
    }

    @Override
    public void create(Company company, InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        if(invoice_payment_report.getId() != null){
            throw new IllegalArgumentException("InvoicePaymentReport is already created, the InvoicePaymentReport ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            DAOUtil.toSqlDate(invoice_payment_report.getReport_date()),
            invoice_payment_report.getCheck_number(),
            invoice_payment_report.getAmmount_paid(),
            invoice_payment_report.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating InvoicePaymentReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice_payment_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating InvoicePaymentReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(InvoicePaymentReport invoice_payment_report) throws IllegalArgumentException, DAOException {
        if (invoice_payment_report.getId() == null) {
            throw new IllegalArgumentException("InvoicePaymentReport is not created yet, the IncomingReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(invoice_payment_report.getReport_date()),
            invoice_payment_report.getCheck_number(),
            invoice_payment_report.getAmmount_paid(),
            invoice_payment_report.getComments(),
            invoice_payment_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating InvoicePaymentReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(InvoicePaymentReport invoice_payment_report) throws DAOException {
        Object[] values = {
            invoice_payment_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting InvoicePaymentReport failed, no rows affected.");
            } else{
                invoice_payment_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an InvoicePaymentReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an InvoicePaymentReport.
     * @return The mapped InvoicePaymentReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static InvoicePaymentReport map(ResultSet resultSet) throws SQLException{
        InvoicePaymentReport invoice_payment_report = new InvoicePaymentReport();
        invoice_payment_report.setId(resultSet.getInt("id"));
        invoice_payment_report.setReport_date(resultSet.getDate("report_date"));
        invoice_payment_report.setCheck_number(resultSet.getString("check_number"));
        invoice_payment_report.setAmmount_paid(resultSet.getDouble("ammount_paid"));
        invoice_payment_report.setComments(resultSet.getString("comments"));
        return invoice_payment_report;
    }
}
