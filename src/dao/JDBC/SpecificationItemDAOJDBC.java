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
import model.Quote;
import model.Specification;
import model.SpecificationItem;

/**
 *
 * @author Pavilion Mini
 */
public class SpecificationItemDAOJDBC implements SpecificationItemDAO {
    /*
DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getSpecificationItem //
CREATE PROCEDURE getSpecificationItem(IN specificationitem_id INT(32))
BEGIN
SELECT SPECIFICATION_ITEM.*, METAL.metal_name, METAL.density
FROM SPECIFICATION_ITEM
INNER JOIN METAL ON METAL_ID = METAL.id
WHERE SPECIFICATION_ITEM.ID = specificationitem_id;
END //
DROP PROCEDURE IF EXISTS listSpecificationItem //
CREATE PROCEDURE listSpecificationItem(IN specification_id INT(32))
BEGIN
SELECT SPECIFICATION_ITEM.*, METAL.metal_name, METAL.density
FROM SPECIFICATION_ITEM
INNER JOIN METAL ON METAL_ID = METAL.id
WHERE SPECIFICATION_ITEM.SPECIFICATION_ID = specification_id
AND SPECIFICATION_ITEM.active = 1
ORDER BY SPECIFICATION_ITEM.id;
END //
DROP PROCEDURE IF EXISTS listSpecificationItemOfQuote //
CREATE PROCEDURE listSpecificationItemOfQuote(IN quote_id INT(32))
BEGIN
SELECT SPECIFICATION_ITEM.*, METAL.metal_name, METAL.density
FROM SPECIFICATION_ITEM
INNER JOIN METAL ON METAL_ID = METAL.id
INNER JOIN QUOTE ON quote_id = QUOTE.id
INNER JOIN PART_REVISION ON QUOTE.PART_REVISION_ID = PART_REVISION.id
WHERE PART_REVISION.SPECIFICATION_ID = SPECIFICATION_ITEM.SPECIFICATION_ID
AND SPECIFICATION_ITEM.active = 1
ORDER BY SPECIFICATION_ITEM.id;
END //
DROP PROCEDURE IF EXISTS createSpecificationItem //
CREATE PROCEDURE createSpecificationItem(IN specification_id INT(32), IN metal_id INT(32), minimum_thickness DOUBLE, maximum_thickness DOUBLE)
BEGIN
INSERT INTO SPECIFICATION_ITEM (SPECIFICATION_ID, METAL_ID, minimum_thickness, maximum_thickness) VALUES(specification_id, metal_id, minimum_thickness, maximum_thickness);
END //
DROP PROCEDURE IF EXISTS updateSpecificationItem //
CREATE PROCEDURE updateSpecificationItem( IN specificationitem_id INT(32), IN metal_id INT(32), IN minimum_thickness DOUBLE, IN maximum_thickness DOUBLE)
BEGIN
UPDATE SPECIFICATION_ITEM 
SET SPECIFICATION_ITEM.METAL_ID = metal_id, SPECIFICATION_ITEM.minimum_thickness = minimum_thickness, SPECIFICATION_ITEM.maximum_thickness = maximum_thickness
WHERE SPECIFICATION_ITEM.id = specificationitem_id;
END //
DROP PROCEDURE IF EXISTS disableSpecificationItem
 //
CREATE PROCEDURE disableSpecificationItem(IN specificationitem_id INT(32))
BEGIN
UPDATE SPECIFICATION_ITEM 
SET SPECIFICATION_ITEM.active = 0
WHERE SPECIFICATION_ITEM.id = specificationitem_id;
END //
DELIMITER ;
    */
    
    // Constants ----------------------------------------------------------------------------------
    private final String SQL_getSpecificationItem = 
            "CALL getSpecificationItem(?)";
    private final String SQL_listSpecificationItem = 
            "CALL listSpecificationItem(?)";
    private final String SQL_listSpecificationItemOfQuote = 
            "CALL listSpecificationItemOfQuote(?)";
    private final String SQL_createSpecificationItem = 
            "CALL createSpecificationItem(?,?,?,?)";
    private final String SQL_updateSpecificationItem = 
            "CALL updateSpecificationUpdate(?,?,?,?)";
    private final String SQL_disableSpecificationItem = 
            "CALL disableSpecificationItem(?)";
    
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
        return find(SQL_getSpecificationItem, id);
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
            PreparedStatement statement = prepareStatement(connection, SQL_listSpecificationItem, false, values);
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
    public List<SpecificationItem> list(Quote quote) throws IllegalArgumentException, DAOException {
        if(quote.getId() == null){
            throw new IllegalArgumentException("Quote is not created yet, the Quote ID is null.");
        }
        
        List<SpecificationItem> specification_items = new ArrayList<>();
        
        Object[] values = {
            quote.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_listSpecificationItemOfQuote, false, values);
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
    public void create(Specification specification, Metal metal, SpecificationItem specification_item) throws IllegalArgumentException, DAOException {
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
            PreparedStatement statement = prepareStatement(connection, SQL_createSpecificationItem, true, values);          
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
            specification_item.getId(),
            specification_item.getMinimum_thickness(),
            specification_item.getMaximum_thickness()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_updateSpecificationItem, false, values);
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
            PreparedStatement statement = prepareStatement(connection, SQL_disableSpecificationItem, false, values);
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
        specification_item.setMetal_id(resultSet.getInt("SPECIFICATION_ITEM.METAL_ID"));
        specification_item.setSpecification_id(resultSet.getInt("SPECIFICATION_ITEM.SPECIFICATION_ID"));
        
        //INNER JOINS
        specification_item.setMetal_name(resultSet.getString("METAL.metal_name"));
        specification_item.setMetal_density(resultSet.getString("METAL.density"));
        
        return specification_item;
    }
}
