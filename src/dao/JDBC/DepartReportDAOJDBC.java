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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.DepartReport;

/**
 *
 * @author Pavilion Mini
 */
public class DepartReportDAOJDBC implements DepartReportDAO{
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT *, SUM(DEPART_LOT.quantity) total_qty, SUM(DEPART_LOT.box_quantity) total_box "
            +"FROM DEPART_REPORT "
            +"INNER JOIN DEPART_LOT ON DEPART_LOT.DEPART_REPORT_ID = DEPART_REPORT.id "
            +"INNER JOIN EMPLOYEE ON DEPART_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            +"INNER JOIN COMPANY ON DEPART_REPORT.COMPANY_ID = COMPANY.id "
            +"INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            +"INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            +"INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            +"WHERE (DEPART_REPORT.id = ? AND DEPART_REPORT.active = 1) AND DEPART_LOT.active = 1 "
            +"GROUP BY DEPART_REPORT.id DESC";
    private static final String SQL_LIST_ACTIVE = 
            "SELECT DEPART_REPORT.*, EMPLOYEE.*, COMPANY.*, COMPANY_ADDRESS.*, SUM(DEPART_LOT.quantity) total_qty, SUM(DEPART_LOT.box_quantity) total_box "
            +"FROM DEPART_REPORT "
            +"INNER JOIN DEPART_LOT ON DEPART_LOT.DEPART_REPORT_ID = DEPART_REPORT.id "
            +"INNER JOIN EMPLOYEE ON DEPART_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            +"INNER JOIN COMPANY ON DEPART_REPORT.COMPANY_ID = COMPANY.id "
            +"INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            +"WHERE DEPART_REPORT.active = 1 AND DEPART_LOT.active = 1 "
            +"GROUP BY DEPART_REPORT.id "
            +"ORDER BY DEPART_REPORT.id DESC";
    private static final String SQL_LIST_ACTIVE_FILTER = 
            "SELECT *, SUM(DEPART_LOT.quantity) total_qty, SUM(DEPART_LOT.box_quantity) total_box "
            +"FROM DEPART_REPORT "
            +"INNER JOIN DEPART_LOT ON DEPART_LOT.DEPART_REPORT_ID = DEPART_REPORT.id "
            +"INNER JOIN EMPLOYEE ON DEPART_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            +"INNER JOIN COMPANY ON DEPART_REPORT.COMPANY_ID = COMPANY.id "
            +"INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            +"INNER JOIN PART_REVISION ON DEPART_LOT.PART_REVISION_ID = PART_REVISION.id "
            +"INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            +"WHERE DEPART_REPORT.active = 1 AND DEPART_LOT.active = 1 AND (DEPART_LOT.rejected = ? OR ? IS NULL) "
            +"GROUP BY DEPART_REPORT.id "
            +"HAVING ((COMPANY.id = ? OR ? IS NULL) AND (DEPART_REPORT.report_date BETWEEN ? AND ?) AND (PRODUCT_PART.part_number LIKE ?) AND (DEPART_LOT.lot_number LIKE ?) AND (DEPART_LOT.po_number LIKE ?))"
            +"ORDER BY DEPART_REPORT.id DESC";
    private static final String SQL_INSERT =
            "INSERT INTO DEPART_REPORT (COMPANY_ID, COMPANY_ADDRESS_ID, EMPLOYEE_ID, report_date, active) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_REPORT SET report_date = ?, active = ? WHERE id = ?";
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
                depart_report = map("DEPART_REPORT.", "EMPLOYEE.", "COMPANY.", "COMPANY_ADDRESS.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_report;
    }

    @Override
    public List<DepartReport> list() throws DAOException {
        List<DepartReport> depart_reports = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ACTIVE);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_reports.add(map("DEPART_REPORT.", "EMPLOYEE.", "COMPANY.", "COMPANY_ADDRESS.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_reports;
    }

    @Override
    public List<DepartReport> list(Boolean rejected, Company company, Date start_date, Date end_date, String partnumber_pattern, String lotnumber_pattern, String po_pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        
        List<DepartReport> depart_reports = new ArrayList<>();
        
        Object[] values = {
            rejected,
            rejected,
            company.getId(),
            company.getId(),
            start_date,
            end_date,
            partnumber_pattern+"%",
            lotnumber_pattern+"%",
            po_pattern+"%"
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_reports.add(map("DEPART_REPORT.", "EMPLOYEE.", "COMPANY.", "COMPANY_ADDRESS.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_reports; 
    }

    @Override
    public void create(DepartReport depart_report) throws IllegalArgumentException, DAOException {
        
        if(depart_report.getId() != null){
            throw new IllegalArgumentException("DepartReport is already created, the DepartReport ID is not null.");
        }
        
        Object[] values = {
            depart_report.getCompany().getId(),
            depart_report.getCompany_address().getId(),
            depart_report.getEmployee().getId(),
            DAOUtil.toSqlDate(depart_report.getReport_date()),
            depart_report.isActive()
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
                    depart_report.setId(generatedKeys.getInt(1));
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
    public static DepartReport map(String departreport_label, String employee_label, String company_label, String companyaddress_label, ResultSet resultSet) throws SQLException{
        DepartReport depart_report = new DepartReport();
        depart_report.setId(resultSet.getInt(departreport_label+"id"));
        depart_report.setReport_date(resultSet.getDate(departreport_label+"report_date"));
        depart_report.setActive(resultSet.getBoolean(departreport_label+"active"));
        depart_report.setTotal_qty(resultSet.getInt("total_qty"));
        depart_report.setTotal_box(resultSet.getInt("total_box"));
        depart_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        depart_report.setCompany(CompanyDAOJDBC.map(company_label, resultSet));
        depart_report.setCompany_address(CompanyAddressDAOJDBC.map(companyaddress_label, resultSet));
        
        return depart_report;
    }
}
