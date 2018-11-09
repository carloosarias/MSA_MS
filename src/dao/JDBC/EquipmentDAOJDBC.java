/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.EquipmentDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Equipment;
import model.EquipmentType;

/**
 *
 * @author Pavilion Mini
 */
public class EquipmentDAOJDBC implements EquipmentDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_TYPE_BY_ID = 
            "SELECT EQUIPMENT_TYPE_ID FROM EQUIPMENT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT ORDER BY id";
    private static final String SQL_LIST_OF_EQUIPMENT_TYPE_ORDER_BY_ID = 
            "SELECT id, name, description FROM EQUIPMENT WHERE EQUIPMENT_TYPE_ID = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO EQUIPMENT (name, description) "
            + "VALUES(?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE EQUIPMENT SET name = ?, description = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM EQUIPMENT WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a EquipmentType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this EquipmentType DAO for.
     */
    EquipmentDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
@Override
    public Equipment find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Equipment from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Equipment from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Equipment find(String sql, Object... values) throws DAOException {
        Equipment equipment = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                equipment = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return equipment;
    }

    @Override
    public EquipmentType findEquipmentType(Equipment equipment) throws IllegalArgumentException, DAOException {
        if(equipment.getId() == null) {
            throw new IllegalArgumentException("Equipment is not created yet, the Equipment ID is null.");
        }
        
        EquipmentType equipment_type = null;
        
        Object[] values = {
            equipment.getId()
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
    public List<Equipment> list() throws DAOException {
        List<Equipment> equipment_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                equipment_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return equipment_list;}

    @Override
    public List<Equipment> list(EquipmentType equipment_type) throws IllegalArgumentException, DAOException {
    if(equipment_type.getId() == null) {
            throw new IllegalArgumentException("EquipmentType is not created yet, the EquipmentType ID is null.");
        }    
        
        List<Equipment> equipment = new ArrayList<>();
        
        Object[] values = {
            equipment_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_EQUIPMENT_TYPE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                equipment.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return equipment;
    }

    @Override
    public void create(EquipmentType equipment_type, Equipment equipment) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Equipment equipment) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Equipment equipment) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Equipment.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Equipment.
     * @return The mapped Equipment from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Equipment map(ResultSet resultSet) throws SQLException{
        Equipment equipment = new Equipment();
        equipment.setId(resultSet.getInt("id"));
        equipment.setName(resultSet.getString("name"));
        equipment.setDescription(resultSet.getString("description"));
        return equipment;
    }  
}
