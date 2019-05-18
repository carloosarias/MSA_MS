/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartReport_1DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.DepartReport_1;

/**
 *
 * @author Pavilion Mini
 */
public class DepartReport_1DAOJDBC implements DepartReport_1DAO {
    /*SELECT *, 
(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, 
(SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `total_qty`, 
DEPART_REPORT_1.id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1) as `depart_open` 
FROM DEPART_REPORT_1 
INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id 
INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id 
INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id 
LEFT JOIN DEPART_LOT_1 ON DEPART_LOT_1.`DEPART_REPORT_ID` = DEPART_REPORT_1.id
INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.`INCOMING_REPORT_ID` = INCOMING_REPORT_1.id
INNER JOIN PART_REVISION ON INCOMING_REPORT_1.`PART_REVISION_ID` = PART_REVISION.id
INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id
GROUP BY DEPART_REPORT_1.id 
HAVING (INCOMING_REPORT_1.id = ? OR ? IS NULL) AND (DEPART_REPORT_1.date BETWEEN ? AND ?) AND (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (PART_REVISION.rev LIKE ?) 
AND (INCOMING_REPORT_1.lot LIKE ?) AND (INCOMING_REPORT_1.packing LIKE ?) AND (INCOMING_REPORT_1.po LIKE ?) AND (INCOMING_REPORT_1.line LIKE ?) 
ORDER BY DEPART_REPORT_1.id DESC*/
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT *, "
            + "(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, "
            + "IFNULL((SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`),0) as `qty_total`, "
            + "DEPART_REPORT_1.id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1) as `depart_open`, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM DEPART_REPORT_1 "
            + "INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "WHERE DEPART_REPORT_1.id = ? "
            + "GROUP BY DEPART_REPORT_1.id";
    private static final String SQL_LIST_FILTER = 
            "SELECT *, "
            + "(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, "
            + "IFNULL((SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`),0) as `qty_total`, "
            + "DEPART_REPORT_1.id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1) as `depart_open`, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM DEPART_REPORT_1 "
            + "INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "LEFT JOIN DEPART_LOT_1 ON DEPART_LOT_1.`DEPART_REPORT_ID` = DEPART_REPORT_1.id "
            + "LEFT JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.`INCOMING_REPORT_ID` = INCOMING_REPORT_1.id "
            + "LEFT JOIN PART_REVISION ON INCOMING_REPORT_1.`PART_REVISION_ID` = PART_REVISION.id "
            + "LEFT JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id "
            + "LEFT JOIN COMPANY AS PRODUCTPART_COMPANY ON PRODUCT_PART.`COMPANY_ID` = PRODUCTPART_COMPANY.id "
            + "GROUP BY DEPART_REPORT_1.id "
            + "HAVING (INCOMING_REPORT_1.id = ? OR ? IS NULL) AND (DEPART_REPORT_1.date BETWEEN ? AND ?) AND (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ? OR ? = '') AND (PART_REVISION.rev LIKE ? OR ? = '') "
            + "AND (INCOMING_REPORT_1.lot LIKE ? OR ? = '') AND (INCOMING_REPORT_1.packing LIKE ? OR ? = '') AND (INCOMING_REPORT_1.po LIKE ? OR ? = '') AND (INCOMING_REPORT_1.line LIKE ? OR ? = '') "
            + "ORDER BY DEPART_REPORT_1.id DESC";
    private static final String SQL_INSERT = 
            "INSERT INTO DEPART_REPORT_1 (EMPLOYEE_ID, COMPANY_ADDRESS_ID, date, comments) "
            +"VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE DEPART_REPORT_1"
            +"SET comments = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM DEPART_REPORT_1 WHERE id = ? "
            +"AND id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1)";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a DepartReport_1 DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartReport_1 DAO for.
     */
    DepartReport_1DAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public DepartReport_1 find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ScrapReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ScrapReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    
    private DepartReport_1 find(String sql, Object... values) throws DAOException {
        DepartReport_1 depart_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_report = map("DEPART_REPORT_1.", "COMPANY_ADDRESS.", "COMPANY.", "EMPLOYEE.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_report;
    }
    
    @Override
    public List<DepartReport_1> list(Integer id, Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        
        List<DepartReport_1> depart_report = new ArrayList<>();
        System.out.println(String.format("%s%%", part_number));
        System.out.println(part_number.isEmpty());
        Object[] values = {
            id,
            id,
            start_date,
            end_date,
            company.getId(),
            company.getId(),
            String.format("%s%%", part_number),
            part_number,
            String.format("%s%%", rev),
            rev,
            String.format("%s%%", lot),
            lot,
            String.format("%s%%", packing),
            packing,
            String.format("%s%%", po),
            po,
            String.format("%s%%", line),
            line
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_report.add(map("DEPART_REPORT_1.", "COMPANY_ADDRESS.", "COMPANY.", "EMPLOYEE.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_report;
    }
    
    @Override
    public void create(DepartReport_1 depart_report) throws IllegalArgumentException, DAOException {
        if(depart_report.getId() != null){
            throw new IllegalArgumentException("DepartReport_1 is already created, the DepartReport_1 ID is not null.");
        }
        
        Object[] values = {
            depart_report.getEmployee().getId(),
            depart_report.getCompany_address().getId(),
            DAOUtil.toSqlDate(depart_report.getDate()),
            depart_report.getComments()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating DepartReport_1 failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depart_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating DepartReport_1 failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(DepartReport_1 incoming_report) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport_1 is not created yet, the DepartReport_1 ID is null.");
        }
        
        Object[] values = {
            incoming_report.getComments(),
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating DepartReport_1 failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(DepartReport_1 incoming_report) throws DAOException {
        Object[] values = {
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting DepartReport_1 failed, no rows affected.");
            } else{
                incoming_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param departreport_label
     * @param companyaddress_label
     * @param company_label
     * @param employee_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartReport_1 map(String departreport_label, String companyaddress_label, String company_label, String employee_label, ResultSet resultSet) throws SQLException{
        DepartReport_1 depart_report = new DepartReport_1();
        depart_report.setId(resultSet.getInt(String.format("%s%s",departreport_label, "id")));
        depart_report.setCount(resultSet.getInt(String.format("%s", "depart_count")));
        depart_report.setDate(resultSet.getDate(String.format("%s%s",departreport_label, "date")));
        depart_report.setQty_total(resultSet.getInt(String.format("%s", "qty_total")));
        depart_report.setComments(resultSet.getString(String.format("%s%s",departreport_label, "comments")));
        depart_report.setOpen(resultSet.getBoolean(String.format("%s", "depart_open")));
        depart_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        depart_report.setCompany_address(CompanyAddressDAOJDBC.map(companyaddress_label, company_label, resultSet));
        depart_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        return depart_report;
    }

}
