/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.MantainanceItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.EquipmentTypeCheck;
import model.MantainanceItem;
import model.MantainanceReport;

/**
 *
 * @author Pavilion Mini
 */
public class MantainanceItemDAOJDBC implements MantainanceItemDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT MANTAINANCE_ITEM.id, MANTAINANCE_ITEM.details, MANTAINANCE_ITEM.active, "
            + "EQUIPMENT_TYPE_CHECK.name "
            + "FROM MANTAINANCE_ITEM "
            + "INNER JOIN EQUIPMENT_TYPE_CHECK ON MANTAINANCE_ITEM.EQUIPMENT_TYPE_CHECK_ID = EQUIPMENT_TYPE_CHECK.id "
            + "WHERE MANTAINANCE_ITEM.id = ?";
    private static final String SQL_FIND_MANTAINANCE_REPORT_BY_ID = 
            "SELECT MANTAINANCE_REPORT_ID FROM MANTAINANCE_ITEM WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_TYPE_CHECK_BY_ID = 
            "SELECT EQUIPMENT_TYPE_CHECK_ID FROM MANTAINANCE_ITEM WHERE id = ?";
    private static final String SQL_LIST_MANTAINANCE_REPORT_ORDER_BY_ID = 
            "SELECT MANTAINANCE_ITEM.id, MANTAINANCE_ITEM.details, MANTAINANCE_ITEM.active, "
            + "EQUIPMENT_TYPE_CHECK.name "
            + "FROM MANTAINANCE_ITEM "
            + "INNER JOIN EQUIPMENT_TYPE_CHECK ON MANTAINANCE_ITEM.EQUIPMENT_TYPE_CHECK_ID = EQUIPMENT_TYPE_CHECK.id "
            + "WHERE MANTAINANCE_ITEM.MANTAINANCE_REPORT_ID = ? AND MANTAINANCE_ITEM.active = ? "
            + "ORDER BY MANTAINANCE_ITEM.id";
    private static final String SQL_INSERT = 
            "INSERT INTO MANTAINANCE_ITEM (MANTAINANCE_REPORT_ID, EQUIPMENT_TYPE_CHECK_ID, details, active) "
            + "VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE MANTAINANCE_ITEM SET details = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM MANTAINANCE_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a MantainanceItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this MantainanceItem DAO for.
     */
    MantainanceItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public MantainanceItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the MantainanceReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The MantainanceReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private MantainanceItem find(String sql, Object... values) throws DAOException {
        MantainanceItem mantainance_item = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                mantainance_item = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return mantainance_item;
    }

    @Override
    public MantainanceReport findMantainanceReport(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException {
       if(mantainance_item.getId() == null) {
                throw new IllegalArgumentException("MantainanceItem is not created yet, the MantainanceItem ID is null.");
            }

            MantainanceReport mantainance_report = null;

            Object[] values = {
                mantainance_item.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_MANTAINANCE_REPORT_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    mantainance_report = daoFactory.getMantainanceReportDAO().find(resultSet.getInt("MANTAINANCE_REPORT_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return mantainance_report;
    }

    @Override
    public EquipmentTypeCheck findEquipmentTypeCheck(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException {
       if(mantainance_item.getId() == null) {
                throw new IllegalArgumentException("MantainanceItem is not created yet, the MantainanceItem ID is null.");
            }

            EquipmentTypeCheck equipment_type_check = null;

            Object[] values = {
                mantainance_item.getId()
            };

            try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = prepareStatement(connection, SQL_FIND_EQUIPMENT_TYPE_CHECK_BY_ID, false, values);
                ResultSet resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    equipment_type_check = daoFactory.getEquipmentTypeCheckDAO().find(resultSet.getInt("EQUIPMENT_TYPE_CHECK_ID"));
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }        

            return equipment_type_check;
    }

    @Override
    public List<MantainanceItem> list(MantainanceReport mantainance_report, boolean active) throws IllegalArgumentException, DAOException {
        if(mantainance_report.getId() == null) {
            throw new IllegalArgumentException("MantainanceReport is not created yet, the MantainanceReport ID is null.");
        }    
        
        List<MantainanceItem> mantainanceitem_list = new ArrayList<>();
        
        Object[] values = {
            mantainance_report.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_MANTAINANCE_REPORT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mantainanceitem_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return mantainanceitem_list;
    }

    @Override
    public void create(MantainanceReport mantainance_report, EquipmentTypeCheck equipment_type_check, MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException {
        if (mantainance_report.getId() == null) {
            throw new IllegalArgumentException("MantainanceReport is not created yet, the MantainanceReport ID is null.");
        }
        if(equipment_type_check.getId() == null){
            throw new IllegalArgumentException("EquipmentTypeCheck is not created yet, the EquipmentTypeCheck ID is null.");
        }
        if(mantainance_item.getId() != null){
            throw new IllegalArgumentException("MantainanceItem is already created, the MantainanceItem ID is not null.");
        }
        
        Object[] values = {
            mantainance_report.getId(),
            equipment_type_check.getId(),
            mantainance_item.getDetails(),
            mantainance_item.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating MantainanceItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mantainance_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating MantainanceItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(MantainanceItem mantainance_item) throws IllegalArgumentException, DAOException {
        if (mantainance_item.getId() == null) {
            throw new IllegalArgumentException("MantainanceItem is not created yet, the MantainanceItem ID is null.");
        }
        
        Object[] values = {
            mantainance_item.getDetails(),
            mantainance_item.isActive(),
            mantainance_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating MantainanceItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(MantainanceItem mantainance_item) throws DAOException {
        Object[] values = {
            mantainance_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting MantainanceItem failed, no rows affected.");
            } else{
                mantainance_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an MantainanceItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an MantainanceItem.
     * @return The mapped MantainanceItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static MantainanceItem map(ResultSet resultSet) throws SQLException{
        MantainanceItem mantainance_item = new MantainanceItem();
        mantainance_item.setId(resultSet.getInt("id"));
        mantainance_item.setDetails(resultSet.getString("details"));
        mantainance_item.setActive(resultSet.getBoolean("active"));
        
        //INNER JOIN
        mantainance_item.setTypecheck_name(resultSet.getString("EQUIPMENT_TYPE_CHECK.name"));
        return mantainance_item;
    }
}
