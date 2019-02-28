/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.MantainanceReportDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Employee;
import model.Equipment;
import model.EquipmentType;
import model.MantainanceReport;

/**
 *
 * @author Pavilion Mini
 */
public class MantainanceReportDAOJDBC implements MantainanceReportDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_BY_ID = 
            "SELECT EQUIPMENT_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT ORDER BY id";
    private static final String SQL_LIST_ACTIVE_EQUIPMENTTYPE_EQUIPMENT_ORDER_BY_DATE = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE (EQUIPMENT.EQUIPMENT_TYPE_ID = ? OR ? = 0) AND (EQUIPMENT_ID = ? OR ? = 0) AND active = ? ORDER BY report_date";
    private static final String SQL_LIST_EMPLOYEE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? ORDER BY id";
    private static final String SQL_LIST_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE report_date BETWEEN ? AND ?  ORDER BY id";
    private static final String SQL_LIST_EMPLOYEE_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
    private static final String SQL_LIST_EQUIPMENT_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EQUIPMENT_ID = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO MANTAINANCE_REPORT (EMPLOYEE_ID, EQUIPMENT_ID, report_date) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE MANTAINANCE_REPORT SET report_date = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM MANTAINANCE_REPORT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a MantainanceReport DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this MantainanceReport DAO for.
     */
    MantainanceReportDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public MantainanceReport find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the MantainanceReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The MantainanceReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private MantainanceReport find(String sql, Object... values) throws DAOException {
        MantainanceReport mantainance_report = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                mantainance_report = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return mantainance_report;
    }

    @Override
    public Employee findEmployee(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException {
        if(mantainance_report.getId() == null) {
                throw new IllegalArgumentException("MantainanceReport is not created yet, the MantainanceReport ID is null.");
            }

            Employee employee = null;

            Object[] values = {
                mantainance_report.getId()
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
    public Equipment findEquipment(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException {
        if(mantainance_report.getId() == null) {
                throw new IllegalArgumentException("MantainanceReport is not created yet, the MantainanceReport ID is null.");
            }

            Equipment equipment = null;

            Object[] values = {
                mantainance_report.getId()
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
    public List<MantainanceReport> list() throws DAOException {
        List<MantainanceReport> mantainancereport_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }
    
    @Override
    public List<MantainanceReport> list(EquipmentType equipment_type, Equipment equipment, boolean type_filter, boolean equipment_filter, boolean active) throws IllegalArgumentException, DAOException {
        if(equipment_type.getId() == null) {
            throw new IllegalArgumentException("EquipmentType is not created yet, the EquipmentType ID is null.");
        }    
        if(equipment.getId() == null) {
            throw new IllegalArgumentException("Equipment is not created yet, the Equipment ID is null.");
        }
        
        List<MantainanceReport> mantainancereport_list = new ArrayList<>();
        
        Object[] values = {
            equipment_type.getId(),
            type_filter,
            equipment.getId(),
            equipment_filter,
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_EQUIPMENTTYPE_EQUIPMENT_ORDER_BY_DATE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }
    
    @Override
    public List<MantainanceReport> listEmployee(Employee employee) throws IllegalArgumentException, DAOException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }    
        
        List<MantainanceReport> mantainancereport_list = new ArrayList<>();
        
        Object[] values = {
            employee.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EMPLOYEE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }

    @Override
    public List<MantainanceReport> listDateRange(Date start, Date end) throws IllegalArgumentException, DAOException {
        List<MantainanceReport> mantainancereport_list = new ArrayList<>();
        
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
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }

    @Override
    public List<MantainanceReport> listEmployeeDateRange(Employee employee, Date start, Date end) throws IllegalArgumentException, DAOException {
        if(employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }    
        
        List<MantainanceReport> mantainancereport_list = new ArrayList<>();
        
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
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }

    @Override
    public List<MantainanceReport> listEquipment(Equipment equipment) throws IllegalArgumentException, DAOException {
        if(equipment.getId() == null) {
            throw new IllegalArgumentException("Equipment is not created yet, the Equipment ID is null.");
        }    
        
        List<MantainanceReport> mantainancereport_list = new ArrayList();
        
        Object[] values = {
            equipment.getId(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_EQUIPMENT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mantainancereport_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainancereport_list;
    }

    @Override
    public void create(Employee employee, Equipment equipment, MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee is not created yet, the Employee ID is null.");
        }
        if(equipment.getId() == null){
            throw new IllegalArgumentException("Equipment is not created yet, the Equipment ID is null.");
        }
        if(mantainance_report.getId() != null){
            throw new IllegalArgumentException("MantainanceReport is already created, the MantainanceReport ID is not null.");
        }
        
        Object[] values = {
            employee.getId(),
            equipment.getId(),
            mantainance_report.getReport_date(),
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating MantainanceReport failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mantainance_report.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating MantainanceReport failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(MantainanceReport mantainance_report) throws IllegalArgumentException, DAOException {
        if (mantainance_report.getId() == null) {
            throw new IllegalArgumentException("MantainanceReport is not created yet, the MantainanceReport ID is null.");
        }

        Object[] values = {
            mantainance_report.getReport_date(),
            mantainance_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating MantainanceReport failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(MantainanceReport mantainanance_report) throws DAOException {
        Object[] values = {
            mantainanance_report.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting MantainanceReport failed, no rows affected.");
            } else{
                mantainanance_report.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an MantainanceReport.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceReport.
     * @return The mapped MantainanceReport from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static MantainanceReport map(ResultSet resultSet) throws SQLException{
        MantainanceReport mantainance_report = new MantainanceReport();
        mantainance_report.setId(resultSet.getInt("id"));
        mantainance_report.setReport_date(resultSet.getDate("report_date"));
        return mantainance_report;
    }
}
