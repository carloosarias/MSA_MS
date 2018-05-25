/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.CompanyDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyDAOJDBC implements CompanyDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, rfc, tax_id, payment_terms, supplier, client, active FROM COMPANY WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, rfc, tax_id, payment_terms, supplier, client, active FROM COMPANY ORDER BY id";
    private static final String SQL_LIST_SUPPLIER_ORDER_BY_ID =
            "SELECT id, name, rfc, tax_id, payment_terms, supplier, client, active FROM COMPANY WHERE supplier = ? ORDER BY id";
    private static final String SQL_LIST_CLIENT_ORDER_BY_ID = 
            "SELECT id, name, rfc, tax_id, payment_terms, supplier, client, active FROM COMPANY WHERE client = ? ORDER BY id";
    private static final String SQL_LIST_ACTIVE_ORDER_BY_ID = 
            "SELECT id, name, rfc, tax_id, payment_terms, supplier, client, active FROM COMPANY WHERE active = ? ORDER BY id";
    private static final String SQL_INSERT = 
            "INSERT INTO COMPANY (name, rfc, tax_id, payment_terms, supplier, client, active) "
            +"VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE COMPANY SET name = ?, rfc = ?, tax_id = ?, payment_terms = ?, supplier = ?, client = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM COMPANY WHERE id = ?";

    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a Company DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Employee DAO for.
     */
    CompanyDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public Company find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the Company from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The Company from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private Company find(String sql, Object... values) throws DAOException {
        Company company = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                company = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return company;
    }
    
    @Override
    public List<Company> list() throws DAOException {
        List<Company> company = new ArrayList<>();
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                company.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return company;
    }

    @Override
    public List<Company> listSupplier(boolean supplier) throws DAOException {
        List<Company> company = new ArrayList<>();
        
        Object[] values = {
            supplier
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_SUPPLIER_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                company.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return company;
    }

    @Override
    public List<Company> listClient(boolean client) throws DAOException {
        List<Company> company = new ArrayList<>();
        
        Object[] values = {
            client
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_CLIENT_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                company.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return company;
    }

    @Override
    public List<Company> listActive(boolean active) throws DAOException {
        List<Company> company = new ArrayList<>();
        
        Object[] values = {
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ACTIVE_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                company.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return company;
    }

    @Override
    public void create(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() != null){
            throw new IllegalArgumentException("Company is already created, the Company ID is not null.");
        }
        
        Object[] values = {
            company.getName(),
            company.getRfc(),
            company.getTax_id(),
            company.getPayment_terms(),
            company.isSupplier(),
            company.isClient(),
            company.isActive()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating Company failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating Company failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Company company) throws IllegalArgumentException, DAOException {
        if(company.getId() == null){
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        Object[] values = {
            company.getName(),
            company.getRfc(),
            company.getTax_id(),
            company.getPayment_terms(),
            company.isSupplier(),
            company.isClient(),
            company.isActive(),
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating Company failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Company company) throws DAOException {
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting Company failed, no rows affected.");
            } else{
                company.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an Employee.
     * @param resultSet The ResultSet of which the current row is to be mapped to an Employee.
     * @return The mapped Employee from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static Company map(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getInt("id"));
        company.setName(resultSet.getString("name"));
        company.setRfc(resultSet.getString("rfc"));
        company.setTax_id(resultSet.getString("tax_id"));
        company.setPayment_terms(resultSet.getString("payment_terms"));
        company.setSupplier(resultSet.getBoolean("supplier"));
        company.setClient(resultSet.getBoolean("client"));
        company.setActive(resultSet.getBoolean("active"));
        return company;
    }
}
