/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartLot_1DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.DepartLot_1;
import model.DepartReport_1;

/**
 *
 * @author Pavilion Mini
 */
public class DepartLot_1DAOJDBC implements DepartLot_1DAO {
/*
SELECT *, 
(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, 
qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_REPORT_ID`),0) as `qty_ava`,
(SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `total_qty`, 
DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `depart_open` 
FROM DEPART_REPORT_1 
INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id 
INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id 
INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id 
LEFT JOIN DEPART_LOT_1 ON DEPART_LOT_1.`DEPART_REPORT_ID` = DEPART_REPORT_1.id
INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.`INCOMING_REPORT_ID` = INCOMING_REPORT_1.id
INNER JOIN PART_REVISION ON INCOMING_REPORT_1.`PART_REVISION_ID` = PART_REVISION.id
INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id
GROUP BY DEPART_LOT_1.id 
HAVING (INCOMING_REPORT_1.id = ? OR ? IS NULL) AND (DEPART_REPORT_1.date BETWEEN ? AND ?) AND (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (PART_REVISION.rev LIKE ?) 
AND (INCOMING_REPORT_1.lot LIKE ?) AND (INCOMING_REPORT_1.packing LIKE ?) AND (INCOMING_REPORT_1.po LIKE ?) AND (INCOMING_REPORT_1.line LIKE ?)
ORDER BY DEPART_LOT_1.id DESC
    */
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT *, "
            + "(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, "
            + "COUNT(DISTINCT(INCOMING_REPORT_1.id)) as `count`, "
            + "qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_REPORT_ID`),0) as `qty_ava`, "
            + "(SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `qty_total`, "
            + "DEPART_REPORT_1.id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1) as `depart_open`, "
            + "DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `departlot_open`, "
            + "(INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_LOT_1) AND INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1)) as `open`, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM DEPART_REPORT_1 "
            + "INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "LEFT JOIN DEPART_LOT_1 ON DEPART_LOT_1.`DEPART_REPORT_ID` = DEPART_REPORT_1.id "
            + "INNER JOIN EMPLOYEE AS DEPARTLOT_EMPLOYEE ON DEPART_LOT_1.EMPLOYEE_ID = DEPARTLOT_EMPLOYEE.id "
            + "INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.`INCOMING_REPORT_ID` = INCOMING_REPORT_1.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.`PART_REVISION_ID` = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN COMPANY AS PRODUCTPART_COMPANY ON PRODUCT_PART.`COMPANY_ID` = PRODUCTPART_COMPANY.id "
            + "GROUP BY DEPART_LOT_1.id "
            + "HAVING DEPART_REPORT_1.id = ? "
            + "ORDER BY DEPART_LOT_1.id DESC ";
    private static final String SQL_LIST_OF_DEPARTREPORT = 
            "SELECT *, "
            + "(SELECT COUNT(DISTINCT(id)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `depart_count`, "
            + "COUNT(DISTINCT(INCOMING_REPORT_1.id)) as `count`, "
            + "qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_REPORT_ID`),0) as `qty_ava`, "
            + "(SELECT sum(qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE DEPART_LOT_1.id = `DEPART_LOT_ID`),0)) FROM DEPART_LOT_1 WHERE DEPART_REPORT_1.id = `DEPART_REPORT_ID`) as `qty_total`, "
            + "DEPART_REPORT_1.id NOT IN (SELECT DEPART_REPORT_ID FROM DEPART_LOT_1) as `depart_open`, "
            + "DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `departlot_open`, "
            + "(INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_LOT_1) AND INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1)) as `open`, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM DEPART_REPORT_1 "
            + "INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "INNER JOIN EMPLOYEE ON DEPART_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "LEFT JOIN DEPART_LOT_1 ON DEPART_LOT_1.`DEPART_REPORT_ID` = DEPART_REPORT_1.id "
            + "INNER JOIN EMPLOYEE AS DEPARTLOT_EMPLOYEE ON DEPART_LOT_1.EMPLOYEE_ID = DEPARTLOT_EMPLOYEE.id "
            + "INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.`INCOMING_REPORT_ID` = INCOMING_REPORT_1.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.`PART_REVISION_ID` = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN COMPANY AS PRODUCTPART_COMPANY ON PRODUCT_PART.`COMPANY_ID` = PRODUCTPART_COMPANY.id "
            + "GROUP BY DEPART_LOT_1.id "
            + "HAVING DEPART_REPORT_1.id = ? "
            + "ORDER BY DEPART_LOT_1.id DESC ";
    
    // Vars ---------------------------------------------------------------------------------------
    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------
    /**
     * Construct a DepartLot_1 DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this DepartLot_1 DAO for.
     */
    DepartLot_1DAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public DepartLot_1 find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ScrapReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ScrapReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    
    private DepartLot_1 find(String sql, Object... values) throws DAOException {
        DepartLot_1 depart_lot = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                depart_lot = map("DEPART_LOT_1.", "DEPART_REPORT_1.", "COMPANY_ADDRESS.", "COMPANY.", "EMPLOYEE.", "DEPARTLOT_EMPLOYEE.",
                        "INCOMING_REPORT_1.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_lot;
    }

    @Override
    public List<DepartLot_1> list(DepartReport_1 depart_report) throws IllegalArgumentException {
        List<DepartLot_1> depart_lot = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_DEPARTREPORT, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map("DEPART_LOT_1.", "DEPART_REPORT_1.", "COMPANY_ADDRESS.", "COMPANY.", "EMPLOYEE.", "DEPARTLOT_EMPLOYEE.",
                        "INCOMING_REPORT_1.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }

    @Override
    public List<DepartLot_1> list(Integer id, Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DepartLot_1> listAva(Integer id, Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(DepartLot_1 depart_lot) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DepartLot_1 depart_lot) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
     * @param departlot_label
     * @param departreport_label
     * @param companyaddress_label
     * @param company_label
     * @param employee_label
     * @param departlot_employee
     * @param incomingreport_label
     * @param partrevision_label
     * @param productpart_label
     * @param metal_label
     * @param specification_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartLot_1 map(String departlot_label, String departreport_label, String companyaddress_label, String company_label, String employee_label,
            String departlot_employee, String incomingreport_label, String partrevision_label, String productpart_label, String metal_label, String specification_label, ResultSet resultSet) throws SQLException{
        DepartLot_1 depart_lot = new DepartLot_1();
        depart_lot.setId(resultSet.getInt(String.format("%s%s", departlot_label, "id")));
        depart_lot.setDate(resultSet.getDate(String.format("%s%s", departlot_label, "date")));
        depart_lot.setQty_out(resultSet.getInt(String.format("%s%s", departlot_label, "qty_out")));
        depart_lot.setComments(resultSet.getString(String.format("%s%s", departlot_label, "comments")));
        depart_lot.setOpen(resultSet.getBoolean(String.format("%s", "departlot_open")));
        depart_lot.setEmployee(EmployeeDAOJDBC.map(departlot_employee, resultSet));
        depart_lot.setDepart_report(DepartReport_1DAOJDBC.map(departreport_label, companyaddress_label, company_label, employee_label, resultSet));
        depart_lot.setIncoming_report(IncomingReport_1DAOJDBC.map(incomingreport_label, employee_label, partrevision_label, productpart_label,
                metal_label, specification_label, company_label, resultSet));
        return depart_lot;
    }
}
