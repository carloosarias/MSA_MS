/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ScrapReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.Employee;
import model.PartRevision;
import model.ProductPart;
import model.ScrapReport;

/**
 *
 * @author Pavilion Mini
 */
public class ScrapReportDAOJDBC implements ScrapReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT * FROM SCRAP_REPORT "
            + "INNER JOIN EMPLOYEE ON SCRAP_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON SCRAP_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE SCRAP_REPORT.id = ?";
    private static final String SQL_LIST_ACTIVE_FILTER = 
            "SELECT * FROM SCRAP_REPORT "
            + "INNER JOIN EMPLOYEE ON SCRAP_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON SCRAP_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE SCRAP_REPORT.active = 1 "
            + "HAVING (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (SCRAP_REPORT.po_number LIKE ?) AND (SCRAP_REPORT.report_date BETWEEN ? AND ?) "
            + "ORDER BY SCRAP_REPORT.id";
    private static final String SQL_INSERT = 
            "INSERT INTO SCRAP_REPORT (EMPLOYEE_ID, PART_REVISION_ID, report_date, quantity, comments, po_number, active) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE SCRAP_REPORT SET report_date = ?, quantity = ?, comments = ?, po_number = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM SCRAP_REPORT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ScrapReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ScrapReport DAO for.
     */
    ScrapReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ScrapReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ScrapReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ScrapReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ScrapReport find(String sql, Object... values) throws DAOException {
        ScrapReport scrap_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                scrap_report = map("SCRAP_REPORT.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return scrap_report;
    }

    @Override
    public List<ScrapReport> list(Company company, String partnumber_pattern, String ponumber_pattern, Date start_date, Date end_date) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        List<ScrapReport> scrap_report = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
            company.getId(),
            partnumber_pattern+"%",
            ponumber_pattern+"%",
            start_date,
            end_date
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                scrap_report.add(map("SCRAP_REPORT.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return scrap_report;
    }

    @Override
    public void create(ScrapReport scrap_report) throws IllegalArgumentException, DAOException {
        if(scrap_report.getId() != null){
            throw new IllegalArgumentException("ScrapReport is already created, the ScrapReport ID is not null.");
        }
        
        Object[] values = {
            scrap_report.getEmployee().getId(),
            scrap_report.getPart_revision().getId(),
            DAOUtil.toSqlDate(scrap_report.getReport_date()),
            scrap_report.getQuantity(),
            scrap_report.getComments(),
            scrap_report.getPo_number(),
            scrap_report.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ScrapReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    scrap_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ScrapReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ScrapReport scrap_report) throws IllegalArgumentException, DAOException {
        if (scrap_report.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Object[] values = {
            DAOUtil.toSqlDate(scrap_report.getReport_date()),
            scrap_report.getQuantity(),
            scrap_report.getComments(),
            scrap_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ScrapReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ScrapReport scrap_report) throws DAOException {
        Object[] values = {
            scrap_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ScrapReport failed, no rows affected.");
            } else{
                scrap_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param scrapreport_label
     * @param employee_label
     * @param partrevision_label
     * @param productpart_label
     * @param metal_label
     * @param specification_label
     * @param company_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ScrapReport map(String scrapreport_label, String employee_label, String partrevision_label, String productpart_label, 
            String metal_label, String specification_label, String company_label, ResultSet resultSet) throws SQLException{
        ScrapReport scrap_report = new ScrapReport();
        scrap_report.setId(resultSet.getInt(scrapreport_label+"id"));
        scrap_report.setReport_date(resultSet.getDate(scrapreport_label+"report_date"));
        scrap_report.setPo_number(resultSet.getString(scrapreport_label+"po_number"));
        scrap_report.setQuantity(resultSet.getInt(scrapreport_label+"quantity"));
        scrap_report.setComments(resultSet.getString(scrapreport_label+"comments"));
        scrap_report.setActive(resultSet.getBoolean(scrapreport_label+"active"));
        scrap_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        scrap_report.setPart_revision(PartRevisionDAOJDBC.map(partrevision_label, productpart_label, company_label, metal_label, specification_label, resultSet));
        return scrap_report;
    }
    
}
