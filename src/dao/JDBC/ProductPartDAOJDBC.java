/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.ProductDAOJDBC.map;
import dao.interfaces.ProductPartDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.ProductPart;
import model.ProductType;
import model.Specification;

/**
 *
 * @author Pavilion Mini
 */
public class ProductPartDAOJDBC implements ProductPartDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID =
            "SELECT id, part_number, rev, base_metal, area, base_weight, final_weight FROM PRODUCT_PART WHERE id = ?";
    private static final String SQL_FIND_BY_PRODUCT_REV = 
            "SELECT id, part_number, rev, base_metal, area, base_weight, final_weight FROM PRODUCT_PART WHERE PRODUCT_ID = ? AND rev = ?";
    private static final String SQL_FIND_PRODUCT_BY_ID =
            "SELECT PRODUCT_ID FROM PRODUCT_PART WHERE id = ?";
    private static final String SQL_FIND_SPECIFICATION_BY_ID = 
            "SELECT SPECIFICATION_ID FROM PRODUCT_PART WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, part_number, rev, base_metal, area, base_weight, final_weight FROM PRODUCT_PART ORDER BY id";
    private static final String SQL_LIST_OF_PRODUCT_ORDER_BY_ID = 
            "SELECT id, part_number, rev, base_metal, area, base_weight, final_weight FROM PRODUCT_PART WHERE PRODUCT_ID = ? ORDER BY id";
    private static final String SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID = 
            "SELECT id, part_number, rev, base_metal, area, base_weight, final_weight FROM PRODUCT_PART WHERE SPECIFICATION_ID = ? ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO PRODUCT (PRODUCT_ID, part_number, rev, SPECIFICATION_ID, base_metal, area, base_weight, final_weight) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE PRODUCT_PART SET part_number = ?, rev = ?, base_metal = ?, area = ?, base_weight = ?, final_weight = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM PRODUCT_PART WHERE id = ?";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a ProductPart DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this ProductPart DAO for.
     */
    ProductPartDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    
    @Override
    public ProductPart find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    @Override
    public ProductPart find(Product product, String rev) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        
        ProductPart part = null;
        
        Object[] values = {
            product.getId(),
            rev
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_BY_PRODUCT_REV, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return part;
    }
    
    /**
     * Returns the ProductPart from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The ProductPart from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private ProductPart find(String sql, Object... values) throws DAOException {
        ProductPart part = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                part = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return part;
    }
    
    @Override
    public Product findProduct(ProductPart part) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        Product product = null;
        
        Object[] values = {
            part.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                product = daoFactory.getProductDAO().find(resultSet.getInt("PRODUCT_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        
        return product;
    }

    @Override
    public Specification findSpecification(ProductPart part) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        Specification specification = null;
        
        Object[] values = {
            part.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_PRODUCT_BY_ID, false, values);
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
    public List<ProductPart> list() throws DAOException {
        List<ProductPart> part = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part;
    }

    @Override
    public List<ProductPart> list(Product product) throws IllegalArgumentException, DAOException {
        if(product.getId() == null){
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        List<ProductPart> part = new ArrayList<>();
        
        Object[] values = {
            product
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_PRODUCT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part;
    }

    @Override
    public List<ProductPart> list(Specification specification) throws IllegalArgumentException, DAOException {
        if(specification.getId() == null){
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        List<ProductPart> part = new ArrayList<>();
        
        Object[] values = {
            specification
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_OF_SPECIFICATION_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                part.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return part;
    }

    @Override
    public void create(Product product, Specification specification, ProductPart part) throws IllegalArgumentException, DAOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product is not created yet, the Product ID is null.");
        }
        if (specification.getId() == null) {
            throw new IllegalArgumentException("Specification is not created yet, the Specification ID is null.");
        }
        if(part.getId() != null){
            throw new IllegalArgumentException("ProductPart is already created, the ProductPart ID is not null.");
        }
        
        Object[] values = {
            product.getId(),
            part.getPart_number(),
            part.getRev(),
            specification.getId(),
            part.getBase_metal(),
            part.getArea(),
            part.getBase_weight(),
            part.getFinal_weight()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating ProductPart failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    part.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating ProductPart failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(ProductPart part) throws IllegalArgumentException, DAOException {
        if (part.getId() == null) {
            throw new IllegalArgumentException("ProductPart is not created yet, the ProductPart ID is null.");
        }
        
        Object[] values = {
            part.getPart_number(),
            part.getRev(),
            part.getBase_metal(),
            part.getArea(),
            part.getBase_weight(),
            part.getFinal_weight(),
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating ProductPart failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(ProductPart part) throws DAOException {
        Object[] values = {
            part.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting ProductPart failed, no rows affected.");
            } else{
                part.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an ProductPart.
     * @param resultSet The ResultSet of which the current row is to be mapped to an ProductPart.
     * @return The mapped ProductPart from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static ProductPart map(ResultSet resultSet) throws SQLException{
        ProductPart part = new ProductPart();
        part.setId(resultSet.getInt("id"));
        part.setPart_number(resultSet.getString("part_number"));
        part.setRev(resultSet.getString("rev"));
        part.setBase_metal(resultSet.getString("base_metal"));
        part.setArea(resultSet.getDouble("area"));
        part.setBase_weight(resultSet.getDouble("base_weight"));
        part.setFinal_weight(resultSet.getDouble("final_weight"));
        return part;
    }
    
}
