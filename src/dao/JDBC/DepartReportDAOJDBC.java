/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.CompanyAddress;
import model.DepartReport;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public class DepartReportDAOJDBC implements DepartReportDAO{
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, report_date FROM DEPART_REPORT WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM DEPART_REPORT WHERE id = ?";
    private static final String SQL_FIND_COMPANY_ADDRESS_BY_ID = 
            "SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM DEPART_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date FROM DEPART_REPORT ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, report_date FROM DEPART_REPORT WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO DEPART_REPORT (COMPANY_ID, COMPANY_ADDRESS_ID, EMPLOYEE_ID, report_date) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_REPORT SET report_date = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM DEPART_REPORT WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a DepartReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartReport DAO for.
     */
    DepartReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public DepartReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the DepartReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The DepartReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private DepartReport find(String sql, Object... values) throws DAOException {
        DepartReport depart_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_report;
    }
    
    @Override
    public Company findCompany(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            depart_report.getId()
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
    public CompanyAddress findCompanyAddress(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        CompanyAddress address = null;
        
        Object[] values = {
            depart_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_ADDRESS_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                address = daoFactory.getCompanyAddressDAO().find(resultSet.getInt("COMPANY_ADDRESS_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return address;
    }
    
    @Override
    public Employee findEmployee(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            depart_report.getId()
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
    public List<DepartReport> list() throws DAOException {
        List<DepartReport> depart_reports = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_reports.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_reports;
    }

    @Override
    public List<DepartReport> listCompany(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }    
        
        List<DepartReport> depart_reports = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_COMPANY_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_reports.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_reports; 
    }

    @Override
    public void create(Employee employee, Company company, CompanyAddress address, DepartReport depart_Report) throws IllegalArgumentException, DAOException {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        if(employee.getId() == null){
            throw new IllegalArgumentException("Employee is not created yet, the Company ID is null.");
        }
        
        if(depart_Report.getId() != null){
            throw new IllegalArgumentException("DepartReport is already created, the DepartReport ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            address.getId(),
            employee.getId(),
            DAOUtil.toSqlDate(depart_Report.getReport_date()),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating DepartReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depart_Report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating DepartReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(DepartReport depart_report) throws IllegalArgumentException, DAOException {
    if (depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport is not created yet, the DepartReport ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(depart_report.getReport_date()),
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating DepartReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(DepartReport depart_report) throws DAOException {
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting DepartReport failed, no rows affected.");
            } else{
                depart_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartReport.
     * @return The mapped DepartReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartReport map(ResultSet resultSet) throws SQLException{
        DepartReport depart_report = new DepartReport();
        depart_report.setId(resultSet.getInt("id"));
        depart_report.setReport_date(resultSet.getDate("report_date"));
        return depart_report;
    }
}
