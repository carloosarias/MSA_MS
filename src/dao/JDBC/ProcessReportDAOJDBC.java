/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import dao.DAOUtil;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.ProcessReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Employee;
import model.Equipment;
import model.PartRevision;
import model.ProcessReport;
import model.ProductPart;
import model.Tank;
import static msa_ms.MainApp.timeFormat;

/**
 *
 * @author Pavilion Mini
 */
public class ProcessReportDAOJDBC implements ProcessReportDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE PROCESS_REPORT.id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM PROCESS_REPORT WHERE id = ?";
    private static final String SQL_FIND_PART_REVISION_BY_ID = 
            "SELECT PART_REVISION_ID FROM PROCESS_REPORT WHERE id = ?";
    private static final String SQL_FIND_TANK_BY_ID = 
            "SELECT TANK_ID FROM PROCESS_REPORT WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_BY_ID = 
            "SELECT EQUIPMENT_ID FROM PROCESS_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "ORDER BY PROCESS_REPORT.id";
    private static final String SQL_LIST_EMPLOYEE_DATERANGE_ORDER_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE (PROCESS_REPORT.EMPLOYEE_ID = ? OR ? = 1) AND ((PROCESS_REPORT.report_date BETWEEN ? AND ?) OR ? = 0) "
            + "ORDER BY PROCESS_REPORT.id";
    private static final String SQL_LIST_EMPLOYEE_ORDER_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE PROCESS_REPORT.EMPLOYEE_ID = ? "
            + "ORDER BY PROCESS_REPORT.id";
    private static final String SQL_LIST_DATE_RANGE_ORDER_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE PROCESS_REPORT.report_date BETWEEN ? AND ? "
            + "ORDER BY PROCESS_REPORT.id";
    private static final String SQL_LIST_EMPLOYEE_DATE_RANGE_ORDER_BY_ID = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE PROCESS_REPORT.EMPLOYEE_ID = ? AND PROCESS_REPORT.report_date BETWEEN ? AND ? "
            + "ORDER BY PROCESS_REPORT.id";
    private static final String SQL_LIST_PRODUCT_PART_DATE_RANGE = 
            "SELECT PROCESS_REPORT.id, PROCESS_REPORT.process, PROCESS_REPORT.report_date, PROCESS_REPORT.lot_number, PROCESS_REPORT.quantity, PROCESS_REPORT.amperage, PROCESS_REPORT.voltage, PROCESS_REPORT.start_time, PROCESS_REPORT.end_time, PROCESS_REPORT.comments, PROCESS_REPORT.quality_passed, "
            + "EMPLOYEE.first_name, EMPLOYEE.last_name, PRODUCT_PART.part_number, PART_REVISION.rev, TANK.tank_name, EQUIPMENT.name "
            + "FROM PROCESS_REPORT "
            + "INNER JOIN EMPLOYEE ON PROCESS_REPORT.EMPLOYEE_ID = EMPLOYEE.id "
            + "INNER JOIN PART_REVISION ON PROCESS_REPORT.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN TANK ON PROCESS_REPORT.TANK_ID = TANK.id "
            + "INNER JOIN EQUIPMENT ON PROCESS_REPORT.EQUIPMENT_ID = EQUIPMENT.id "
            + "WHERE PART_REVISION.PRODUCT_PART_ID = ? AND PROCESS_REPORT.report_date BETWEEN ? AND ? "
            + "ORDER BY PROCESS_REPORT.report_date, PROCESS_REPORT.id";    
    private static final String SQL_INSERT = 
            "INSERT INTO PROCESS_REPORT (EMPLOYEE_ID, PART_REVISION_ID, TANK_ID, EQUIPMENT_ID, process, report_date, lot_number, quantity, amperage, voltage, start_time, end_time, comments, quality_passed) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PROCESS_REPORT SET process = ?, report_date = ?, lot_number = ?, quantity = ?, amperage = ?, voltage = ?, start_time = ?, end_time = ?, comments = ?, quality_passed = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM PROCESS_REPORT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProcessReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ProcessReport DAO for.
     */
    ProcessReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public ProcessReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the ProcessReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProcessReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ProcessReport find(String sql, Object... values) throws DAOException {
        ProcessReport process_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                process_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return process_report;
    }
    
    @Override
    public Employee findEmployee(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Employee employee = null;
        
        Object[] values = {
            process_report.getId()
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
    public PartRevision findPartRevision(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        PartRevision part_revision = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PART_REVISION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part_revision = daoFactory.getPartRevisionDAO().find(resultSet.getInt("PART_REVISION_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return part_revision;
    }
    
    @Override
    public Tank findTank(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Tank tank = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_TANK_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                tank = daoFactory.getTankDAO().find(resultSet.getInt("TANK_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return tank;
    }
    
    @Override
    public Equipment findEquipment(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if(process_report.getId() == null) {
            throw new IllegalArgumentException("ProcessReport is not created yet, the ProcessReport ID is null.");
        }
        
        Equipment equipment = null;
        
        Object[] values = {
            process_report.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_EQUIPMENT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                equipment = daoFactory.getEquipmentDAO().find(resultSet.getInt("EQUIPMENT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return equipment;
    }
    
    @Override
    public List<ProcessReport> list() throws DAOException {
        List<ProcessReport> process_report = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;
    }

    @Override
    public List<ProcessReport> list(Employee employee, Date start, Date end, boolean date_filter) throws DAOException, IllegalArgumentException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        List<ProcessReport> process_report = new ArrayList<>();
        
        Object[] values = {
            employee.getId(),
            employee.isAdmin(),
            start,
            end,
            date_filter
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_DATERANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;
    }
    
    @Override
    public List<ProcessReport> listEmployee(Employee employee) throws DAOException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }    
        
        List<ProcessReport> process_report = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;
    }
    @Override
    public List<ProcessReport> listDateRange(Date start, Date end) throws IllegalArgumentException, DAOException {
        List<ProcessReport> process_report = new ArrayList<>();
        
        Object[] values = {
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_DATE_RANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;
    }

    @Override
    public List<ProcessReport> listEmployeeDateRange(Employee employee, Date start, Date end) throws IllegalArgumentException, DAOException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }    
        
        List<ProcessReport> process_report = new ArrayList<>();
        
        Object[] values = {
            employee.getId(),
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_DATE_RANGE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                process_report.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return process_report;        
    }
    
    @Override
    public List<ProcessReport> listProductPartDateRange(ProductPart product_part, Date start, Date end){
        if(product_part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }    
        
        List<ProcessReport> processreport_list = new ArrayList();
        
        Object[] values = {
            product_part.getId(),
            start,
            end
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_PRODUCT_PART_DATE_RANGE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                processreport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return processreport_list;
    }
    
    @Override
    public void create(Employee employee, PartRevision part_revision, Tank tank, Equipment equipment, ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        
        if(part_revision.getId() == null){
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        if(tank.getId() == null){
            throw new IllegalArgumentException("Tank is not created yet, the Tank ID is null.");
        }
        
        if(equipment.getId() == null){
            throw new IllegalArgumentException("Equipment is not created yet, the Equipment ID is null.");
        }
        if(process_report.getId() != null){
            throw new IllegalArgumentException("ProcessReport is already created, the ProcessReport ID is not null.");
        }
        
        Object[] values = {
            employee.getId(),
            part_revision.getId(),
            tank.getId(),
            equipment.getId(),
            process_report.getProcess(),
            DAOUtil.toSqlDate(process_report.getReport_date()),
            process_report.getLot_number(),
            process_report.getQuantity(),
            process_report.getAmperage(),
            process_report.getVoltage(),
            process_report.getStart_time(),
            process_report.getEnd_time(),
            process_report.getComments(),
            process_report.isQuality_passed()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ProcessReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    process_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ProcessReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ProcessReport process_report) throws IllegalArgumentException, DAOException {
        if (process_report.getId() == null) {
            throw new IllegalArgumentException("PartRevision is not created yet, the PartRevision ID is null.");
        }
        
        Object[] values = {
            process_report.getProcess(),
            DAOUtil.toSqlDate(process_report.getReport_date()),
            process_report.getLot_number(),
            process_report.getQuantity(),
            process_report.getAmperage(),
            process_report.getVoltage(),
            process_report.getStart_time(),
            process_report.getEnd_time(),
            process_report.getComments(),
            process_report.isQuality_passed(),
            process_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ProcessReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ProcessReport process_report) throws DAOException {
        Object[] values = {
            process_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ProcessReport failed, no rows affected.");
            } else{
                process_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an ProcessReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an ProcessReport.
     * @return The mapped ProcessReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ProcessReport map(ResultSet resultSet) throws SQLException{
        ProcessReport process_report = new ProcessReport();
        process_report.setId(resultSet.getInt("id"));
        process_report.setProcess(resultSet.getString("process"));
        process_report.setReport_date(resultSet.getDate("report_date"));
        process_report.setLot_number(resultSet.getString("lot_number"));
        process_report.setQuantity(resultSet.getInt("quantity"));
        process_report.setAmperage(resultSet.getDouble("amperage"));
        process_report.setVoltage(resultSet.getDouble("voltage"));
        process_report.setStart_time(resultSet.getTime("start_time").toLocalTime().format(timeFormat));
        process_report.setEnd_time(resultSet.getTime("end_time").toLocalTime().format(timeFormat));
        process_report.setComments(resultSet.getString("comments"));
        process_report.setQuality_passed(resultSet.getBoolean("quality_passed"));
        
        //INNER JOINS
        process_report.setEmployee_name(resultSet.getString("EMPLOYEE.first_name")+" "+resultSet.getString("EMPLOYEE.last_name"));
        process_report.setPart_number(resultSet.getString("PRODUCT_PART.part_number"));
        process_report.setRev(resultSet.getString("PART_REVISION.rev"));
        process_report.setTank_name(resultSet.getString("TANK.tank_name"));
        process_report.setEquipment_name(resultSet.getString("EQUIPMENT.name"));
        return process_report;
    }    
}
