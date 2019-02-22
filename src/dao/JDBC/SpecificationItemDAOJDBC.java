/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.SpecificationItemDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Metal;
import model.Specification;
import model.SpecificationItem;

/**
 *
 * @author Pavilion Mini
 */
public class SpecificationItemDAOJDBC implements SpecificationItemDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT SPECIFICATION_ITEM.id, SPECIFICATION_ITEM.minimum_thickness, SPECIFICATION_ITEM.maximum_thickness, SPECIFICATION_ITEM.active, "
            + "METAL.metal_name, METAL.density "
            + "FROM SPECIFICATION_ITEM "
            + "INNER JOIN METAL ON SPECIFICATION_ITEM.METAL_ID = METAL.id "
            + "WHERE SPECIFICATION_ITEM.id = ?";
    private static final String SQL_FIND_SPECIFICATION_BY_ID = 
            "SELECT SPECIFICATION_ID FROM SPECIFICATION_ITEM WHERE id = ?";
    private static final String SQL_FIND_METAL_BY_ID = 
            "SELECT METAL_ID FROM SPECIFICATION_ITEM WHERE id = ?";
    private static final String SQL_LIST_SPECIFICATION_ORDER_BY_ID = 
            "SELECT SPECIFICATION_ITEM.id, SPECIFICATION_ITEM.minimum_thickness, SPECIFICATION_ITEM.maximum_thickness, SPECIFICATION_ITEM.active, "
            + "METAL.metal_name, METAL.density "
            + "FROM SPECIFICATION_ITEM "
            + "INNER JOIN METAL ON SPECIFICATION_ITEM.METAL_ID = METAL.id "
            + "WHERE SPECIFICATION_ITEM.SPECIFICATION_ID = ? "
            + "ORDER BY SPECIFICATION_ITEM.id";
    private static final String SQL_LIST_ACTIVE_SPECIFICATION_ORDER_BY_ID = 
            "SELECT SPECIFICATION_ITEM.id, SPECIFICATION_ITEM.minimum_thickness, SPECIFICATION_ITEM.maximum_thickness, SPECIFICATION_ITEM.active, "
            + "METAL.metal_name, METAL.density "
            + "FROM SPECIFICATION_ITEM "
            + "INNER JOIN METAL ON SPECIFICATION_ITEM.METAL_ID = METAL.id "
            + "WHERE SPECIFICATION_ITEM.SPECIFICATION_ID = ? AND SPECIFICATION_ITEM.active = ? "
            + "ORDER BY SPECIFICATION_ITEM.id";
    private static final String SQL_INSERT = 
            "INSERT INTO SPECIFICATION_ITEM (SPECIFICATION_ID, METAL_ID, minimum_thickness, maximum_thickness, active) "
            + "VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE SPECIFICATION_ITEM SET specification_number = ?, process = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM SPECIFICATION_ITEM WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a SpecificationItem DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this SpecificationItem DAO for.
     */
    SpecificationItemDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public SpecificationItem find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Specification from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Specification from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private SpecificationItem find(String sql, Object... values) throws DAOException {
        SpecificationItem specification = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return specification;
    }
    
    @Override
    public Specification findSpecification(SpecificationItem specification_item) throws IllegalArgumentException, DAOException {
        if(specification_item.getId() == null) {
            throw new IllegalArgumentException("SpecificationItem is not created yet, the SpecificationItem ID is null.");
        }
        
        Specification specification = null;
        
        Object[] values = {
            specification_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_SPECIFICATION_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification = daoFactory.getSpecificationDAO().find(resultSet.getInt("SPECIFICATION_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return specification;
    }

    @Override
    public Metal findMetal(SpecificationItem specification_item) throws IllegalArgumentException, DAOException {
        if(specification_item.getId() == null) {
            throw new IllegalArgumentException("SpecificationItem is not created yet, the SpecificationItem ID is null.");
        }
        
        Metal metal = null;
        
        Object[] values = {
            specification_item.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_METAL_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                metal = daoFactory.getMetalDAO().find(resultSet.getInt("METAL_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return metal;
    }
    
    @Override
    public List<SpecificationItem> list(Specification specification) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        List<SpecificationItem> specification_items = new ArrayList<>();
        
        Object[] values = {
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                specification_items.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return specification_items;
    }
    
    @Override
    public List<SpecificationItem> list(Specification specification, boolean active) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        List<SpecificationItem> specification_items = new ArrayList<>();
        
        Object[] values = {
            specification.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                specification_items.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return specification_items;
    }

    @Override
    public void create(Specification specification, Metal metal,SpecificationItem specification_item) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        if(metal.getId() == null){
            throw new IllegalArgumentException("Metal is not created yet, the Metal ID is null.");
        }
        if(specification_item.getId() != null){
            throw new IllegalArgumentException("SpecificationItem is already created, the SpecificationItem ID is not null.");
        }
        
        Object[] values = {
            specification.getId(),
            metal.getId(),
            specification_item.getMinimum_thickness(),
            specification_item.getMaximum_thickness(),
            specification_item.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating SpecificationItem failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    specification_item.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating SpecificationItem failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(SpecificationItem specification_item) throws IllegalArgumentException, DAOException {
        if (specification_item.getId() == null) {
            throw new IllegalArgumentException("SpecificationItem is not created yet, the SpecificationItem ID is null.");
        }
        
        Object[] values = {
            specification_item.getMinimum_thickness(),
            specification_item.getMaximum_thickness(),
            specification_item.isActive(),
            specification_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating SpecificationItem failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(SpecificationItem specification_item) throws DAOException {
        Object[] values = {
            specification_item.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting SpecificationItem failed, no rows affected.");
            } else{
                specification_item.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an SpecificationItem.
     * @param resultSet The ResultSet of which the current row is to be mapped to an SpecificationItem.
     * @return The mapped SpecificationItem from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static SpecificationItem map(ResultSet resultSet) throws SQLException{
        SpecificationItem specification_item = new SpecificationItem();
        specification_item.setId(resultSet.getInt("SPECIFICATION_ITEM.id"));
        specification_item.setMinimum_thickness(resultSet.getDouble("SPECIFICATION_ITEM.minimum_thickness"));
        specification_item.setMaximum_thickness(resultSet.getDouble("SPECIFICATION_ITEM.maximum_thickness"));
        specification_item.setActive(resultSet.getBoolean("SPECIFICATION_ITEM.active"));
        
        //INNER JOINS
        specification_item.setMetal_name(resultSet.getString("METAL.metal_name"));
        specification_item.setMetal_density(resultSet.getDouble("METAL.density"));
        return specification_item;
    }
}
