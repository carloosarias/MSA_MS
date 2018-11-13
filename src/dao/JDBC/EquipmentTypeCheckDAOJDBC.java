/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.EquipmentTypeCheckDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.EquipmentType;
import model.EquipmentTypeCheck;

/**
 *
 * @author Pavilion Mini
 */
public class EquipmentTypeCheckDAOJDBC implements EquipmentTypeCheckDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT_TYPE_CHECK WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_TYPE_BY_ID = 
            "SELECT EQUIPMENT_TYPE_ID FROM EQUIPMENT_TYPE_CHECK WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT_TYPE_CHECK ORDER BY id";
    private static final String SQL_LIST_OF_EQUIPMENT_TYPE_ORDER_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT_TYPE_CHECK WHERE EQUIPMENT_TYPE_ID = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO EQUIPMENT_TYPE_CHECK (name, description) "
            + "VALUES(?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE EQUIPMENT_TYPE_CHECK SET name = ?, description = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM EQUIPMENT_TYPE_CHECK WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a EquipmentTypeCheck DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this EquipmentTypeCheck DAO for.
     */
    EquipmentTypeCheckDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    @Override
    public EquipmentTypeCheck find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the EquipmentTypeCheck from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The EquipmentTypeCheck from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private EquipmentTypeCheck find(String sql, Object... values) throws DAOException {
        EquipmentTypeCheck process_report = null;

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
    public EquipmentType findEquipmentType(EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException {
    if(equipment_type_check.getId() == null) {
            throw new IllegalArgumentException("EquipmentTypeCheck is not created yet, the EquipmentTypeCheck ID is null.");
        }
        
        EquipmentType equipment_type = null;
        
        Object[] values = {
            equipment_type_check.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_EQUIPMENT_TYPE_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                equipment_type = daoFactory.getEquipmentTypeDAO().find(resultSet.getInt("EQUIPMENT_TYPE_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return equipment_type;
    }

    @Override
    public List<EquipmentTypeCheck> list() throws DAOException {
       List<EquipmentTypeCheck> equipment_type_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                equipment_type_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return equipment_type_list;
    }

    @Override
    public List<EquipmentTypeCheck> list(EquipmentType equipment_type) throws IllegalArgumentException, DAOException {
    if(equipment_type.getId() == null) {
            throw new IllegalArgumentException("EquipmentType is not created yet, the EquipmentType ID is null.");
        }    
        
        List<EquipmentTypeCheck> equipment_type_check = new ArrayList<>();
        
        Object[] values = {
            equipment_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_EQUIPMENT_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                equipment_type_check.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return equipment_type_check;
    }

    @Override
    public void create(EquipmentType equipment_type, EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException {
        if(equipment_type.getId() == null) {
            throw new IllegalArgumentException("EquipmentType is not created yet, the EquipmentType ID is null.");
        }
        if(equipment_type_check.getId() != null){
            throw new IllegalArgumentException("EquipmentTypeCheck is already created, the EquipmentTypeCheck ID is not null.");
        }
        Object[] values = {
            equipment_type.getId(),
            equipment_type_check.getName(),
            equipment_type_check.getDescription()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating EquipmentTypeCheck failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    equipment_type_check.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating EquipmentTypeCheck failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(EquipmentTypeCheck equipment_type_check) throws IllegalArgumentException, DAOException {
        if (equipment_type_check.getId() == null) {
            throw new IllegalArgumentException("EquipmentTypeCheck is not created yet, the EquipmentTypeCheck ID is null.");
        }
        
        Object[] values = {
            equipment_type_check.getName(),
            equipment_type_check.getDescription(),
            equipment_type_check.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating EquipmentTypeCheck failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(EquipmentTypeCheck equipment_type_check) throws DAOException {
        Object[] values = {
            equipment_type_check.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting EquipmentTypeCheck failed, no rows affected.");
            } else{
                equipment_type_check.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an EquipmentTypeCheck.
     * @param resultSet The ResultSet of which the current row is to be mapped to an EquipmentTypeCheck.
     * @return The mapped EquipmentTypeCheck from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static EquipmentTypeCheck map(ResultSet resultSet) throws SQLException{
        EquipmentTypeCheck equipment_type_check = new EquipmentTypeCheck();
        equipment_type_check.setId(resultSet.getInt("id"));
        equipment_type_check.getName();
        equipment_type_check.getDescription();
        return equipment_type_check;
    }  
}
