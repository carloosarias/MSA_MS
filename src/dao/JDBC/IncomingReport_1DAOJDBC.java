/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingReport_1DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.IncomingReport_1;

/**
 *
 * @author Pavilion Mini
 */
public class IncomingReport_1DAOJDBC implements IncomingReport_1DAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT * FROM INCOMING_REPORT_1 "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "WHERE INCOMING_REPORT_1.id = ?";
    private static final String SQL_LIST_ACTIVE_FILTER = 
            "SELECT *, (INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_LOT_1) "
            +"AND INCOMING_REPORT_1.id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1)) as open FROM INCOMING_REPORT_1 "
            + "INNER JOIN EMPLOYEE ON INCOMING_REPORT_1.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON INCOMING_REPORT_1.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "HAVING (INCOMING_REPORT_1.date BETWEEN ? AND ?) AND (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (PART_REVISION.rev LIKE ?) "
            + "AND (INCOMING_REPORT_1.lot LIKE ?) AND (INCOMING_REPORT_1.packing LIKE ?) AND (INCOMING_REPORT_1.po LIKE ?) AND (INCOMING_REPORT_1.line LIKE ?) "
            + "ORDER BY INCOMING_REPORT_1.id";
    private static final String SQL_INSERT = 
            "INSERT INTO INCOMING_REPORT_1 (EMPLOYEE_ID, PART_REVISION_ID, date, packing, po, line, lot, qty_in, comments) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_REPORT_1"
            +"SET packing = ?, po = ?, line = ?, lot = ?, qty_in = ?, comments = ? WHERE id = ? "
            +"AND (id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_REPORT_1) "
            +"AND id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1))";
    private static final String SQL_DELETE = 
            "DELETE FROM INCOMING_REPORT_1 WHERE id = ? AND (id NOT IN (SELECT INCOMING_REPORT_ID FROM DEPART_REPORT_1) "
            +"AND id NOT IN (SELECT INCOMING_REPORT_ID FROM SCRAP_REPORT_1))";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a IncomingReport_1 DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this IncomingReport_1 DAO for.
     */
    IncomingReport_1DAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public IncomingReport_1 find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ScrapReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ScrapReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private IncomingReport_1 find(String sql, Object... values) throws DAOException {
        IncomingReport_1 incoming_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                incoming_report = map("INCOMING_REPORT_1.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return incoming_report;
    }
    
    @Override
    public List<IncomingReport_1> list(Date start_date, Date end_date, Company company, String part_number, String rev, String lot, String packing, String po, String line) throws IllegalArgumentException {
        if(company == null) company = new Company();
        if(start_date == null) start_date = DAOUtil.toUtilDate(LocalDate.MIN);
        if(end_date == null) end_date = DAOUtil.toUtilDate(LocalDate.now().plusDays(1));
        
        List<IncomingReport_1> incoming_report = new ArrayList<>();
        
        Object[] values = {
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
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                incoming_report.add(map("INCOMING_REPORT_1.", "EMPLOYEE.", "PART_REVISION.", "PRODUCT_PART.", "METAL.", "SPECIFICATION.", "COMPANY.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return incoming_report;
    }

    @Override
    public void create(IncomingReport_1 incoming_report) throws IllegalArgumentException, DAOException {
        if(incoming_report.getId() != null){
            throw new IllegalArgumentException("IncomingReport_1 is already created, the IncomingReport_1 ID is not null.");
        }
        
        Object[] values = {
            incoming_report.getEmployee().getId(),
            incoming_report.getPart_revision().getId(),
            DAOUtil.toSqlDate(incoming_report.getDate()),
            incoming_report.getPacking(),
            incoming_report.getPo(),
            incoming_report.getLine(),
            incoming_report.getLot(),
            incoming_report.getQty_in(),
            incoming_report.getComments(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating IncomingReport_1 failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    incoming_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating IncomingReport_1 failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(IncomingReport_1 incoming_report) throws IllegalArgumentException, DAOException {
        if (incoming_report.getId() == null) {
            throw new IllegalArgumentException("IncomingReport_1 is not created yet, the IncomingReport_1 ID is null.");
        }
        
        Object[] values = {
            incoming_report.getPacking(),
            incoming_report.getPo(),
            incoming_report.getLine(),
            incoming_report.getLot(),
            incoming_report.getQty_in(),
            incoming_report.getComments(),
            incoming_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating IncomingReport_1 failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(IncomingReport_1 incoming_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an PartRevision.
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
    public static IncomingReport_1 map(String incomingreport_label, String employee_label, String partrevision_label, String productpart_label, 
            String metal_label, String specification_label, String company_label, ResultSet resultSet) throws SQLException{
        IncomingReport_1 incoming_report = new IncomingReport_1();
        incoming_report.setId(resultSet.getInt(String.format("%s%s",incomingreport_label, "id")));
        incoming_report.setDate(resultSet.getDate(String.format("%s%s",incomingreport_label, "date")));
        incoming_report.setLot(resultSet.getString(String.format("%s%s",incomingreport_label, "lot")));
        incoming_report.setPacking(resultSet.getString(String.format("%s%s",incomingreport_label, "packing")));
        incoming_report.setPo(resultSet.getString(String.format("%s%s",incomingreport_label, "po")));
        incoming_report.setLine(resultSet.getString(String.format("%s%s",incomingreport_label, "line")));
        incoming_report.setQty_in(resultSet.getInt(String.format("%s%s",incomingreport_label, "qty_in")));
        incoming_report.setComments(resultSet.getString(String.format("%s%s",incomingreport_label, "comments")));
        incoming_report.setOpen(resultSet.getBoolean(String.format("%s", "open")));
        incoming_report.setEmployee(EmployeeDAOJDBC.map(employee_label, resultSet));
        incoming_report.setPart_revision(PartRevisionDAOJDBC.map(partrevision_label, productpart_label, company_label, metal_label, specification_label, resultSet));
        return incoming_report;
    }
}
