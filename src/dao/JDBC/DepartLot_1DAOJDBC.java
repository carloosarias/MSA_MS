/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.DepartLot_1DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getDepartLot //
CREATE PROCEDURE getDepartLot(IN departlot_id INT(32))
BEGIN
SELECT DEPART_LOT_1.*,
DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `open`,
(DEPART_LOT_1.qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE REJECT_REPORT.DEPART_LOT_ID = DEPART_LOT_1.id),0)) as qty_ava,
CONCAT(EMPLOYEE.first_name,' ', EMPLOYEE.last_name) as employee_name,
CONCAT(PRODUCT_PART.part_number,'-',PART_REVISION.rev) as incomingreport_partnumber,
INCOMING_REPORT_1.lot,INCOMING_REPORT_1.po,INCOMING_REPORT_1.line,INCOMING_REPORT_1.packing
FROM DEPART_LOT_1
INNER JOIN EMPLOYEE ON DEPART_LOT_1.EMPLOYEE_ID = EMPLOYEE.id
INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.INCOMING_REPORT_ID = INCOMING_REPORT_1.id
INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id
INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id
WHERE DEPART_LOT_1.id = departlot_id;
END //
DROP PROCEDURE IF EXISTS listDepartLot //
CREATE PROCEDURE listDepartLot(IN departreport_id INT(32))
BEGIN
SELECT DEPART_LOT_1.*,
DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `open`,
(DEPART_LOT_1.qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE REJECT_REPORT.DEPART_LOT_ID = DEPART_LOT_1.id),0)) as qty_ava,
CONCAT(EMPLOYEE.first_name,' ', EMPLOYEE.last_name) as employee_name,
CONCAT(PRODUCT_PART.part_number,'-',PART_REVISION.rev) as incomingreport_partnumber,
INCOMING_REPORT_1.lot,INCOMING_REPORT_1.po,INCOMING_REPORT_1.line,INCOMING_REPORT_1.packing
FROM DEPART_LOT_1
INNER JOIN EMPLOYEE ON DEPART_LOT_1.EMPLOYEE_ID = EMPLOYEE.id
INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.INCOMING_REPORT_ID = INCOMING_REPORT_1.id
INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id
INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id
WHERE DEPART_LOT_1.DEPART_REPORT_ID = departreport_id
ORDER BY DEPART_LOT_1.id;
END //
DROP PROCEDURE IF EXISTS searchAvaDepartLot //
CREATE PROCEDURE searchAvaDepartLot(IN departreport_id INT(32), IN start_date DATE, IN end_date DATE, IN company_id INT(32),
IN part_number VARCHAR(256), IN rev VARCHAR(256), IN lot VARCHAR(256), IN packing VARCHAR(256), IN po VARCHAR(256), IN line VARCHAR(256))
BEGIN
SELECT DEPART_LOT_1.*,
DEPART_LOT_1.id NOT IN (SELECT DEPART_LOT_ID FROM REJECT_REPORT) as `open`,
(DEPART_LOT_1.qty_out - IFNULL((SELECT sum(qty_rej) FROM REJECT_REPORT WHERE REJECT_REPORT.DEPART_LOT_ID = DEPART_LOT_1.id),0)) as qty_ava,
CONCAT(EMPLOYEE.first_name,' ', EMPLOYEE.last_name) as employee_name,
CONCAT(PRODUCT_PART.part_number,'-',PART_REVISION.rev) as incomingreport_partnumber,
INCOMING_REPORT_1.lot,INCOMING_REPORT_1.po,INCOMING_REPORT_1.line,INCOMING_REPORT_1.packing
FROM DEPART_LOT_1
INNER JOIN EMPLOYEE ON DEPART_LOT_1.EMPLOYEE_ID = EMPLOYEE.id
INNER JOIN INCOMING_REPORT_1 ON DEPART_LOT_1.INCOMING_REPORT_ID = INCOMING_REPORT_1.id
INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id
INNER JOIN PRODUCT_PART ON PART_REVISION.`PRODUCT_PART_ID` = PRODUCT_PART.id
INNER JOIN DEPART_REPORT_1 ON DEPART_LOT_1.DEPART_REPORT_ID = DEPART_REPORT.id
INNER JOIN COMPANY_ADDRESS ON DEPART_REPORT_1.COMPANY_ADDRESS_ID = COMPANY_ADDRESS.id
INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id
WHERE (DEPART_LOT_1.DEPART_REPORT_ID = departreport_id OR departreport_id IS NULL)
AND DEPART_LOT_1.`date` BETWEEN start_date AND end_date
AND (COMPANY.id = company_id OR company_id IS NULL)
AND PRODUCT_PART.part_number LIKE part_number
AND PART_REVISION.rev LIKE rev
AND INCOMING_REPORT_1.lot LIKE lot
AND INCOMING_REPORT_1.packing LIKE packing
AND INCOMING_REPORT_1.po LIKE po
AND INCOMING_REPORT_1.line LIKE line
AND qty_ava > 0
ORDER BY DEPART_LOT_1.id;
END //
DELIMITER ;*/
    
    // Constants ----------------------------------------------------------------------------------
    private final String SQL_getDepartLot =
            "CALL getDepartLot(?)";
    private final String SQL_listDepartLot = 
            "CALL listDepartLot(?)";
    private final String SQL_searchAvaDepartLot =
            "CALL searchAvaDepartLot(?,?,?,?,?,?,?,?,?,?)";
    
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
        return find(SQL_getDepartLot, id);
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
                depart_lot = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return depart_lot;
    }

    @Override
    public List<DepartLot_1> list(DepartReport_1 depart_report) throws IllegalArgumentException {
        if (depart_report.getId() == null) {
            throw new IllegalArgumentException("DepartReport_1 is not created yet, the DepartReport_1 ID is null.");
        }
        
        List<DepartLot_1> depart_lot = new ArrayList<>();
        
        Object[] values = {
            depart_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_listDepartLot, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
    }

    @Override
    public List<DepartLot_1> searchAva(Integer departreport_id, Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        
        List<DepartLot_1> depart_lot = new ArrayList<>();
        
        Object[] values = {
            departreport_id,
            start_date,
            end_date,
            company.getId(),
            part_number,
            rev,
            lot,
            packing,
            po,
            line
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_searchAvaDepartLot, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                depart_lot.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return depart_lot;
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
     * @param resultSet The ResultSet of which the current row is to be mapped to an PartRevision.
     * @return The mapped PartRevision from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartLot_1 map(ResultSet resultSet) throws SQLException{
        DepartLot_1 depart_lot = new DepartLot_1();
        depart_lot.setId(resultSet.getInt("DEPART_LOT_1.id"));
        depart_lot.setEmployee_id(resultSet.getInt("EMPLOYEE_ID"));
        depart_lot.setDepartreport_id(resultSet.getInt("DEPART_REPORT_ID"));
        depart_lot.setIncomingreport_id(resultSet.getInt("INCOMING_REPORT_ID"));
        depart_lot.setDate(resultSet.getDate("DEPART_LOT_1.date"));
        depart_lot.setQty_out(resultSet.getInt("DEPART_LOT_1.qty_out"));
        depart_lot.setComments(resultSet.getString("DEPART_LOT_1.comments"));
        
        //CALCULATED VALUES
        depart_lot.setQty_ava(resultSet.getInt("qty_ava"));
        depart_lot.setOpen(resultSet.getBoolean("open"));
        
        //INNER JOINS
        depart_lot.setEmployee_name(resultSet.getString("employee_name"));
        depart_lot.setIncomingreport_partnumber(resultSet.getString("incomingreport_partnumber"));
        depart_lot.setIncomingreport_lot(resultSet.getString("INCOMING_REPORT_1.lot"));
        depart_lot.setIncomingreport_po(resultSet.getString("INCOMING_REPORT_1.po"));
        depart_lot.setIncomingreport_line(resultSet.getString("INCOMING_REPORT_1.line"));
        depart_lot.setIncomingreport_packing(resultSet.getString("INCOMING_REPORT_1.packing"));
        return depart_lot;
    }
}
