/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.IncomingReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            "SELECT id, report_date, po_number, packing_list FROM INCOMING_REPORT WHERE id = ?";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM INCOMING_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM INCOMING_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date, po_number, packing_list FROM INCOMING_REPORT ORDER BY id";
    private static final String SQL_LIST_OF_COMPANY_ORDER_BY_ID = 
            "SELECT id, report_date, po_number, packing_list FROM INCOMING_REPORT WHERE COMPANY_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO INCOMING_REPORT (COMPANY_ID, EMPLOYEE_ID, report_date, po_number, packing_list) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE INCOMING_REPORT SET report_date = ?, po_number = ?, packing_list = ? WHERE id = ?";
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee findEmployee(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IncomingReport> list() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IncomingReport> listCompany(Company company) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Employee employee, Company company, IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(IncomingReport incoming_report) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(IncomingReport incoming_report) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return incoming_report;
    }
}
