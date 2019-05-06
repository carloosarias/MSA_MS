/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ScrapReport_1DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.ScrapReport_1;

/**
 *
 * @author Pavilion Mini
 */
public class ScrapReport_1DAOJDBC implements ScrapReport_1DAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT *, "
            + "COUNT(DISTINCT(INCOMING_REPORT_1.id)) as `count`, "
            + "COUNT(DISTINCT(SCRAP_REPORT_1.id)) as `scrap_count`, "
            + "(IFNULL(sum(qty_in),0) - IFNULL(sum(qty_scrap),0) - IFNULL(sum(qty_out),0) + IFNULL(sum(qty_rej),0)) as `qty_ava`, "
            + "(INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_LOT_1) AND INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1)) as `open` "
            + "FROM INCOMING_REPORT_1 "
            + "LEFT JOIN SCRAP_REPORT_1 ON INCOMING_REPORT_1.id = SCRAP_REPORT_1.INCOMING_REPORT_ID "
            + "LEFT JOIN DEPART_LOT_1 ON INCOMING_REPORT_1.id = DEPART_LOT_1.INCOMING_REPORT_ID "
            + "LEFT JOIN REJECT_REPORT ON DEPART_LOT_1.id = REJECT_REPORT.DEPART_LOT_ID "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE SCRAP_REPORT_1.id = ?"
            + "GROUP BY SCRAP_REPORT_1.id";
    private static final String SQL_LIST_FILTER = 
            "SELECT *, "
            + "COUNT(DISTINCT(INCOMING_REPORT_1.id)) as `count`, "
            + "COUNT(DISTINCT(SCRAP_REPORT_1.id)) as `scrap_count`, "
            + "(IFNULL(sum(qty_in),0) - IFNULL(sum(qty_scrap),0) - IFNULL(sum(qty_out),0) + IFNULL(sum(qty_rej),0)) as `qty_ava`, "
            + "(INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_LOT_1) AND INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1)) as `open` "
            + "FROM INCOMING_REPORT_1 "
            + "LEFT JOIN SCRAP_REPORT_1 ON INCOMING_REPORT_1.id = SCRAP_REPORT_1.INCOMING_REPORT_ID "
            + "LEFT JOIN DEPART_LOT_1 ON INCOMING_REPORT_1.id = DEPART_LOT_1.INCOMING_REPORT_ID "
            + "LEFT JOIN REJECT_REPORT ON DEPART_LOT_1.id = REJECT_REPORT.DEPART_LOT_ID "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "GROUP BY INCOMING_REPORT_1.id "
            + "HAVING (INCOMING_REPORT_1.id = ? OR ? IS NULL) AND (SCRAP_REPORT_1.date BETWEEN ? AND ?) AND (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (PART_REVISION.rev LIKE ?) "
            + "AND (INCOMING_REPORT_1.lot LIKE ?) AND (INCOMING_REPORT_1.packing LIKE ?) AND (INCOMING_REPORT_1.po LIKE ?) AND (INCOMING_REPORT_1.line LIKE ?) "
            + "ORDER BY SCRAP_REPORT_1.id DESC";
    private static final String SQL_INSERT = 
            "INSERT INTO SCRAP_REPORT_1 (EMPLOYEE_ID, INCOMING_REPORT_ID, date, qty_scrap, comments) "
            +"VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE SCRAP_REPORT_1"
            +"SET qty_scrap = ?, comments = ? WHERE id = ? ";
    private static final String SQL_DELETE = 
            "DELETE FROM SCRAP_REPORT_1 WHERE id = ? ";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ScrapReport_1 DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ScrapReport_1 DAO for.
     */
    ScrapReport_1DAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ScrapReport_1 find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ScrapReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ScrapReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ScrapReport_1 find(String sql, Object... values) throws DAOException {
        ScrapReport_1 scrap_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                scrap_report = map("SCRAP_REPORT_1.", "INCOMING_REPORT_1.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return scrap_report;
    }
    
    @Override
    public List<ScrapReport_1> list(Integer id, Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        
        List<ScrapReport_1> scrap_report = new ArrayList<>();
        
        Object[] values = {
            id,
            id,
            start_date,
            end_date,
            company.getId(),
            company.getId(),
            String.format("%s%%", part_number),
            String.format("%s%%", rev),
            String.format("%s%%", lot),
            String.format("%s%%", packing),
            String.format("%s%%", po),
            String.format("%s%%", line)
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                scrap_report.add(map("SCRAP_REPORT_1.", "INCOMING_REPORT_1.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return scrap_report;
    }
    
    @Override
    public void create(ScrapReport_1 scrap_report) throws IllegalArgumentException, DAOException {
        if(scrap_report.getId() != null){
            throw new IllegalArgumentException("ScrapReport_1 is already created, the ScrapReport_1 ID is not null.");
        }
        
        Object[] values = {
            scrap_report.getEmployee().getId(),
            scrap_report.getIncoming_report().getId(),
            DAOUtil.toSqlDate(scrap_report.getDate()),
            scrap_report.getQty_scrap(),
            scrap_report.getComments(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ScrapReport_1 failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    scrap_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ScrapReport_1 failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ScrapReport_1 scrap_report) throws IllegalArgumentException, DAOException {
        if (scrap_report.getId() == null) {
            throw new IllegalArgumentException("ScrapReport_1 is not created yet, the ScrapReport_1 ID is null.");
        }
        
        Object[] values = {
            scrap_report.getQty_scrap(),
            scrap_report.getComments(),
            scrap_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ScrapReport_1 failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ScrapReport_1 scrap_report) throws DAOException {
        Object[] values = {
            scrap_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ScrapReport_1 failed, no rows affected.");
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
     * @param incomingreport_label
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
    public static ScrapReport_1 map(String scrapreport_label, String incomingreport_label, String employee_label, String partrevision_label, String productpart_label, 
            String metal_label, String specification_label, String company_label, ResultSet resultSet) throws SQLException{
        ScrapReport_1 scrap_report = new ScrapReport_1();
        scrap_report.setId(resultSet.getInt(String.format("%s%s",scrapreport_label, "id")));
        scrap_report.setCount(resultSet.getInt(String.format("%s", "scrap_count")));
        scrap_report.setDate(resultSet.getDate(String.format("%s%s",scrapreport_label, "date")));
        scrap_report.setQty_scrap(resultSet.getInt(String.format("%s%s",scrapreport_label, "qty_scrap")));
        scrap_report.setComments(resultSet.getString(String.format("%s%s",scrapreport_label, "comments")));
        scrap_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        scrap_report.setIncoming_report(IncomingReport_1DAOJDBC.map(incomingreport_label, employee_label, partrevision_label, productpart_label, metal_label, specification_label, company_label, resultSet));
        return scrap_report;
    }
}
