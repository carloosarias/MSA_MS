/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.CompanyContactDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.CompanyContact;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyContactDAOJDBC implements CompanyContactDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT id, name, position, email, phone_number, active FROM COMPANY_CONTACT WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT id, name, position, email, phone_number, active FROM COMPANY_CONTACT WHERE COMPANY_ID = ? AND active = ? ORDER BY id";
    private static final String SQL_FIND_COMPANY_BY_ID = 
            "SELECT COMPANY_ID FROM COMPANY_CONTACT WHERE id = ?";
    private static final String SQL_INSERT = 
            "INSERT INTO COMPANY_CONTACT (COMPANY_ID, name, position, email, phone_number, active) "
            +"VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE COMPANY_CONTACT SET name = ?, position = ?, email = ?, phone_number = ?, active = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM COMPANY_CONTACT WHERE id = ?";
    
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a CompanyContact DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this CompanyContact DAO for.
     */
    CompanyContactDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public CompanyContact find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the CompanyContact from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The CompanyContact from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private CompanyContact find(String sql, Object... values) throws DAOException {
        CompanyContact contact = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                contact = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return contact;
    }
    
    @Override
    public List<CompanyContact> list(Company company, boolean active) throws IllegalArgumentException, DAOException {
        if(company.getId() == null){
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        
        List<CompanyContact> companyContacts = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
            active
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                companyContacts.add(map(resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return companyContacts;
    }
    
    @Override
    public Company findCompany(CompanyContact contact) throws IllegalArgumentException, DAOException {
        if(contact.getId() == null) {
            throw new IllegalArgumentException("CompanyContact is not created yet, the CompanyContact ID is null.");
        }
        
        Company company = null;
        
        Object[] values = {
            contact.getId()
        };
        
        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_FIND_COMPANY_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                company = daoFactory.getCompanyDAO().find(resultSet.getInt("COMPANY_ID"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }        
        
        return company;
    }

    @Override
    public void create(Company company, CompanyContact contact) throws IllegalArgumentException, DAOException {
        if(company.getId() == null){
            throw new IllegalArgumentException("Company is not created yet, the Company ID is null.");
        }
        if(contact.getId() != null){
            throw new IllegalArgumentException("CompanyContact is already created, the CompanyContact ID is not null.");
        }
        
        Object[] values = {
            company.getId(),
            contact.getName(),
            contact.getPosition(),
            contact.getEmail(),
            contact.getPhone_number()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating CompanyContact failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating CompanyContact failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(CompanyContact contact) throws IllegalArgumentException, DAOException {
        if(contact.getId() == null){
            throw new IllegalArgumentException("CompanyContact is not created yet, the CompanyContact ID is null.");
        }
        
        Object[] values = {
            contact.getName(),
            contact.getPosition(),
            contact.getEmail(),
            contact.getPhone_number(),
            contact.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating CompanyContact failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(CompanyContact contact) throws DAOException {
        Object[] values = {
            contact.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting CompanyContact failed, no rows affected.");
            } else{
                contact.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to a CompanyContact.
     * @param resultSet The ResultSet of which the current row is to be mapped to a CompanyContact.
     * @return The mapped CompanyContact from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static CompanyContact map(ResultSet resultSet) throws SQLException {
        CompanyContact contact = new CompanyContact();
        contact.setId(resultSet.getInt("id"));
        contact.setName(resultSet.getString("name"));
        contact.setPosition(resultSet.getString("position"));
        contact.setEmail(resultSet.getString("email"));
        contact.setPhone_number(resultSet.getString("phone_number"));
        contact.setActive(resultSet.getBoolean("active"));
        return contact;
    }    
}
