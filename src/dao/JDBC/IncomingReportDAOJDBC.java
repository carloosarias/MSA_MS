/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.Employee;
import model.IncomingReport;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingReportDAOJDBC implements IncomingReportDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT INCOMING_REPORT.id, INCOMING_REPORT.report_date, INCOMING_REPORT.po_number, INCOMING_REPORT.packing_list, INCOMING_REPORT.discrepancy, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, COMPANY.name "
            + "FROM INCOMING_REPORT "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON INCOMING_REPORT.COMPANY_ID = COMPANY.id "
            + "WHERE INCOMING_REPORT.id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM INCOMING_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM INCOMING_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT INCOMING_REPORT.id, INCOMING_REPORT.report_date, INCOMING_REPORT.po_number, INCOMING_REPORT.packing_list, INCOMING_REPORT.discrepancy, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, COMPANY.name "
            + "FROM INCOMING_REPORT "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON INCOMING_REPORT.COMPANY_ID = COMPANY.id "
            + "ORDER BY INCOMING_REPORT.id";
    private static final String SQL_LIST_OF_DISCREPANCY_ORDER_BY_ID = 
            "SELECT INCOMING_REPORT.id, INCOMING_REPORT.report_date, INCOMING_REPORT.po_number, INCOMING_REPORT.packing_list, INCOMING_REPORT.discrepancy, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, COMPANY.name "
            + "FROM INCOMING_REPORT "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON INCOMING_REPORT.COMPANY_ID = COMPANY.id "
            + "WHERE INCOMING_REPORT.discrepancy = ?"
            + "ORDER BY INCOMING_REPORT.id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT INCOMING_REPORT.id, INCOMING_REPORT.report_date, INCOMING_REPORT.po_number, INCOMING_REPORT.packing_list, INCOMING_REPORT.discrepancy, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, COMPANY.name "
            + "FROM INCOMING_REPORT "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN COMPANY ON INCOMING_REPORT.COMPANY_ID = COMPANY.id "
            + "WHERE INCOMING_REPORT.COMPANY_ID = ? "
            + "ORDER BY INCOMING_REPORT.id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_REPORT (COMPANY_ID, EMPLOYEE_ID, report_date, po_number, packing_list, discrepancy) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_REPORT SET report_date = ?, po_number = ?, packing_list = ?, discrepancy = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM INCOMING_REPORT WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingReport DAO for.
     */
    IncomingReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public IncomingReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the IncomingReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The IncomingReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private IncomingReport find(String sql, Object... values) throws DAOException {
        IncomingReport incoming_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return incoming_report;
    }
    
    @Override
    public Company findCompany(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if(incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            incoming_report.getId()
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
    public Employee findEmployee(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if(incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            incoming_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_EMPLOYEE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                employee = daoFactory.getEmployeeDAO().find(resultSet.getInt("EMPLOYEE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return employee;
    }

    @Override
    public List<IncomingReport> list() throws DAOException {
        List<IncomingReport> incoming_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_report;
    }

    @Override
    public List<IncomingReport> list(boolean discrepancy) throws DAOException {
        
        List<IncomingReport> incoming_report = new ArrayList<>();
        
        Object[] values = {
            discrepancy
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DISCREPANCY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_report; 
    }
    
    @Override
    public List<IncomingReport> listCompany(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<IncomingReport> incoming_report = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_report; 
    }

    @Override
    public void create(Employee employee, Company company, IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        if(employee.getId() == null){
            throw new IllegalArgumentException("Employee is not created yet, the Company ID is null.");
        }
        
        if(incoming_report.getId() != null){
            throw new IllegalArgumentException("IncomingReport is already created, the IncomingReport ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            employee.getId(),
            incoming_report.getReport_date(),
            incoming_report.getPo_number(),
            incoming_report.getPacking_list(),
            incoming_report.getDiscrepancy()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating IncomingReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport is not created yet, the IncomingReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(incoming_report.getReport_date()),
            incoming_report.getPo_number(),
            incoming_report.getPacking_list(),
            incoming_report.getId(),
            incoming_report.getDiscrepancy()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating IncomingReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(IncomingReport incoming_report) throws DAOException {
        Object[] values = {
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting IncomingReport failed, no rows affected.");
            } else{
                incoming_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an IncomingReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an IncomingReport.
     * @return The mapped IncomingReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static IncomingReport map(ResultSet resultSet) throws SQLException{
        IncomingReport incoming_report = new IncomingReport();
        incoming_report.setId(resultSet.getInt("id"));
        incoming_report.setReport_date(resultSet.getDate("report_date"));
        incoming_report.setPo_number(resultSet.getString("po_number"));
        incoming_report.setPacking_list(resultSet.getString("packing_list"));
        incoming_report.setDiscrepancy(resultSet.getBoolean("discrepancy"));
        
        //INNER JOINS
        incoming_report.setEmployee_name(resultSet.getString("EMPLOYEE.first_name")+" "+resultSet.getString("EMPLOYEE.last_name"));
        incoming_report.setClient_name(resultSet.getString("COMPANY.name"));
        return incoming_report;
    }
}
