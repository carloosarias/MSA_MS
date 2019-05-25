/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.SpecificationDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class SpecificationDAOJDBC implements SpecificationDAO {
    /*
DELIMITER //
Use msadb //
DROP PROCEDURE IF EXISTS getSpecification //
CREATE PROCEDURE getSpecification(IN specification_id INT(32))
BEGIN
SELECT *
FROM SPECIFICATION
WHERE SPECIFICATION.ID = specification_id;
END //
DROP PROCEDURE IF EXISTS listSpecification //
CREATE PROCEDURE listSpecification(IN specification_number VARCHAR(256), IN specification_name VARCHAR(256), IN process VARCHAR(256))
BEGIN
SELECT *
FROM SPECIFICATION
WHERE (SPECIFICATION.specification_number LIKE concat(IFNULL(specification_number,''),'%'))
AND (SPECIFICATION.specification_name LIKE concat(IFNULL(specification_name,''),'%')) 
AND (SPECIFICATION.process LIKE concat(IFNULL(process,''),'%'))
AND SPECIFICATION.active = 1
ORDER BY SPECIFICATION.id;
END //
DROP PROCEDURE IF EXISTS createSpecification //
CREATE PROCEDURE createSpecification(IN specification_number VARCHAR(256), IN specification_name VARCHAR(256), IN process VARCHAR(256))
BEGIN
INSERT INTO SPECIFICATION (SPECIFICATION.specification_number, SPECIFICATION.specification_name, SPECIFICATION.process) VALUES(specification_number, specification_name, process);
END //
DROP PROCEDURE IF EXISTS updateSpecification //
CREATE PROCEDURE updateSpecification(IN specification_id INT(32), IN specification_number VARCHAR(256), IN specification_name VARCHAR(256), IN process VARCHAR(256))
BEGIN
UPDATE SPECIFICATION 
SET SPECIFICATION.specification_number = specification_number, SPECIFICATION.specification_name = specification_name, SPECIFICATION.process = process
WHERE SPECIFICATION.id = specification_id;
END //
DROP PROCEDURE IF EXISTS disableSpecification //
CREATE PROCEDURE disableSpecification(IN specification_id INT(32))
BEGIN
UPDATE SPECIFICATION 
SET SPECIFICATION.active = 0
WHERE SPECIFICATION.id = specification_id;
END //
DELIMITER ;
    */
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_getSpecification = 
            "CALL getSpecification(?)";
    private static final String SQL_listSpecification = 
            "CALL listSpecification(?,?,?)";
    private static final String SQL_createSpecification = 
            "CALL createSpecification(?,?,?)";
    private static final String SQL_updateSpecification = 
            "CALL updateSpecification(?,?,?,?)";
    private static final String SQL_disableSpecification = 
            "CALL disableSpecification(?)";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Specification DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Specification DAO for.
     */
    SpecificationDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public Specification find(Integer id) throws DAOException {
        return find(SQL_getSpecification, id);
    }
    
    /**
     * Returns the Specification from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Specification from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Specification find(String sql, Object... values) throws DAOException {
        Specification specification = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                specification = map("", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return specification;
    }
    
    @Override 
    public List<Specification> list() throws DAOException {
        return list(null, null, null);
    }
    
    @Override
    public List<Specification> list(String specification_number, String specification_name, String process) throws DAOException {
        List<Specification> specifications = new ArrayList<>();

        Object[] values = {
            specification_number,
            specification_name,
            process
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_listSpecification, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                specifications.add(map("", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return specifications;
    }

    @Override
    public void create(Specification specification) throws IllegalArgumentException, DAOException {
        if(specification.getId() != null){
            throw new IllegalArgumentException("Specification is already created, the Specification ID is not null.");
        }
        
        Object[] values = {
            specification.getSpecification_number(),
            specification.getSpecification_name(),
            specification.getProcess()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_createSpecification, false, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Specification failed, no rows affected.");
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Specification specification) throws IllegalArgumentException, DAOException {
        if (specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        
        Object[] values = {
            specification.getId(),
            specification.getSpecification_number(),
            specification.getSpecification_name(),
            specification.getProcess()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_updateSpecification, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Specification failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Specification specification) throws DAOException {
        Object[] values = {
            specification.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_disableSpecification, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Specification failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Specification.
     * @param specification_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an Specification.
     * @return The mapped Specification from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static Specification map(String specification_label, ResultSet resultSet) throws SQLException{
        Specification specification = new Specification();
        specification.setId(resultSet.getInt(specification_label+"id"));
        specification.setSpecification_number(resultSet.getString(specification_label+"specification_number"));
        specification.setSpecification_name(resultSet.getString(specification_label+"specification_name"));
        specification.setProcess(resultSet.getString(specification_label+"process"));
        specification.setActive(resultSet.getBoolean(specification_label+"active"));
        return specification;
    }
}
