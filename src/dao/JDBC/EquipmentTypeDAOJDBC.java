/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.EquipmentTypeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.EquipmentType;

/**
 *
 * @author Pavilion Mini
 */
public class EquipmentTypeDAOJDBC implements EquipmentTypeDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, description, frequency FROM EQUIPMENT_TYPE WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, description, frequency FROM EQUIPMENT_TYPE ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO EQUIPMENT_TYPE (name, description, frequency) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE EQUIPMENT_TYPE SET name = ?, description = ?, frequency = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM EQUIPMENT_TYPE WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a EquipmentType DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this EquipmentType DAO for.
     */
    EquipmentTypeDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public EquipmentType find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the EquipmentType from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The EquipmentType from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private EquipmentType find(String sql, Object... values) throws DAOException {
        EquipmentType equipment_type = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                equipment_type = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return equipment_type;
    }

    @Override
    public List<EquipmentType> list() throws DAOException {
        List<EquipmentType> equipment_type = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                equipment_type.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return equipment_type;
    }

    @Override
    public void create(EquipmentType equipment_type) throws IllegalArgumentException, DAOException {
        if(equipment_type.getId() != null){
            throw new IllegalArgumentException("EquipmentType is already created, the EquipmentType ID is null.");
        }
        
        Object[] values = {
            equipment_type.getName(),
            equipment_type.getDescription(),
            equipment_type.getFrequency()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating EquipmentType failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    equipment_type.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating EquipmentType failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(EquipmentType equipment_type) throws IllegalArgumentException, DAOException {
        if (equipment_type.getId() == null) {
            throw new IllegalArgumentException("EquipmentType is not created yet, the EquipmentType ID is null.");
        }
        
        Object[] values = {
            equipment_type.getName(),
            equipment_type.getDescription(),
            equipment_type.getFrequency(),
            equipment_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating EquipmentType failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(EquipmentType equipment_type) throws DAOException {
        Object[] values = {
            equipment_type.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting EquipmentType failed, no rows affected.");
            } else{
                equipment_type.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an EquipmentType.
     * @param resultSet The ResultSet of which the current row is to be mapped to an EquipmentType.
     * @return The mapped EquipmentType from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static EquipmentType map(ResultSet resultSet) throws SQLException{
        EquipmentType equipment_type = new EquipmentType();
        equipment_type.setId(resultSet.getInt("id"));
        equipment_type.setName(resultSet.getString("name"));
        equipment_type.setDescription(resultSet.getString("description"));
        equipment_type.setFrequency(resultSet.getInt("frequency"));
        return equipment_type;
    }    
}
