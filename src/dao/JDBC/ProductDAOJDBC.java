/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.MantainanceReportDAOJDBC.map;
import dao.interfaces.ProductDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MantainanceReport;
import model.Product;

/**
 *
 * @author Pavilion Mini
 */
public class ProductDAOJDBC implements ProductDAO {
    
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EMPLOYEE_BY_ID = 
            "SELECT EMPLOYEE_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_FIND_EQUIPMENT_BY_ID = 
            "SELECT EQUIPMENT_ID FROM MANTAINANCE_REPORT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? ORDER BY id";
    private static final String SQL_LIST_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE report_date BETWEEN ? AND ?  ORDER BY id";
    private static final String SQL_LIST_EMPLOYEE_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EMPLOYEE_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
    private static final String SQL_LIST_EQUIPMENT_DATE_RANGE_ORDER_BY_ID = 
            "SELECT id, report_date FROM MANTAINANCE_REPORT WHERE EQUIPMENT_ID = ? AND report_date BETWEEN ? AND ? ORDER BY id";
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
     * Construct a Product DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Product DAO for.
     */
    ProductDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Actions ------------------------------------------------------------------------------------
    @Override
    public Product find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the MantainanceReport from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The MantainanceReport from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Product find(String sql, Object... values) throws DAOException {
        Product product = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                product = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return product;
    }
    
    @Override
    public List<Product> list() throws DAOException {
        List<Product> product_list = new ArrayList<>();

        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product_list;
    }

    @Override
    public List<Product> list(boolean active) throws DAOException {
    List<Product> product_list = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                product_list.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return product_list;
    }

    @Override
    public void create(Product product) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        Object[] values = {
            product.getDescription(),
            product.getUnit_measure(),
            product.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Product failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Product failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Product product) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        Object[] values = {
            product.getDescription(),
            product.getUnit_measure(),
            product.isActive(),
            product.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Product failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Product product) throws DAOException {
        Object[] values = {
            product.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Product failed, no rows affected.");
            } else{
                product.setId(null);
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
    public static Product map(ResultSet resultSet) throws SQLException{
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setDescription(resultSet.getString("description"));
        product.setUnit_measure(resultSet.getString("unit_measure"));
        product.setActive(resultSet.getBoolean("active"));
        return product;
    }
}
