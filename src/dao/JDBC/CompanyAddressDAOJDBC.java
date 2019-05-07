/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.CompanyAddressDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.CompanyAddress;

/**
 *
 * @author Pavilion Mini
 */
public class CompanyAddressDAOJDBC implements CompanyAddressDAO {
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_FIND_BY_ID = 
            "SELECT *, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM COMPANY_ADDRESS "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "WHERE COMPANY_ADDRESS.id = ?";
    private static final String SQL_LIST_ORDER_BY_ID = 
            "SELECT *, "
            + "(COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) AND COMPANY_ADDRESS.id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)) as `add_open` "
            + "FROM COMPANY_ADDRESS "
            + "INNER JOIN COMPANY ON COMPANY_ADDRESS.COMPANY_ID = COMPANY.id "
            + "WHERE COMPANY_ADDRESS.COMPANY_ID = ? "
            + "ORDER BY COMPANY_ADDRESS.id";
    private static final String SQL_INSERT = 
            "INSERT INTO COMPANY_ADDRESS (COMPANY_ID, address) "
            + "VALUES (?, ?)";
    private static final String SQL_UPDATE = 
            "UPDATE COMPANY_ADDRESS SET address = ? WHERE id = ?";
    private static final String SQL_DELETE = 
            "DELETE FROM COMPANY_ADDRESS WHERE id = ? "
            + "AND id NOT IN (SELECT COMPANY_ADDRESS_ID FROM DEPART_REPORT_1) "
            + "AND id NOT IN (SELECT COMPANY_ADDRESS_ID FROM ORDER_PURCHASE)";

    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a CompanyAddress DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this CompanyAddress DAO for.
     */
    CompanyAddressDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------

    @Override
    public CompanyAddress find(Integer id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    /**
     * Returns the CompanyAddress from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The CompanyAddress from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private CompanyAddress find(String sql, Object... values) throws DAOException {
        CompanyAddress address = null;

        try (
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, sql, false, values);
            ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                address = map("", "COMPANY.", resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return address;
    }    

    @Override
    public List<CompanyAddress> list(Company company) throws DAOException {
        if(company == null) company = new Company();
        
        List<CompanyAddress> companyAddresses = new ArrayList<>();
        
        Object[] values = {
            company.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_ORDER_BY_ID, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                companyAddresses.add(map("", "COMPANY.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return companyAddresses;
    }

    @Override
    public void create(CompanyAddress address) throws IllegalArgumentException, DAOException {
        if(address.getId() != null){
            throw new IllegalArgumentException("CompanyAddress is already created, the CompanyAddress ID is not null.");
        }
        
        Object[] values = {
            address.getCompany().getId(),
            address.getAddress()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);          
        ){
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0){
                throw new DAOException("Creating CompanyAddress failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    address.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating CompanyAddress failed, no generated key obtained.");
                }
            }
            
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(CompanyAddress address) throws IllegalArgumentException, DAOException {
        if(address.getId() == null){
            throw new IllegalArgumentException("CompanyAddress is not created yet, the CompanyContact ID is null.");
        }
        
        Object[] values = {
            address.getAddress(),
            address.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Updating CompanyAddress failed, no rows affected.");
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(CompanyAddress address) throws DAOException {
        Object[] values = {
            address.getId()
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
        ){
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                throw new DAOException("Deleting CompanyAddress failed, no rows affected.");
            } else{
                address.setId(null);
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an CompanyAddress.
     * @param companyaddress_label
     * @param company_label
     * @param resultSet The ResultSet of which the current row is to be mapped to a CompanyAddress.
     * @return The mapped CompanyAddress from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static CompanyAddress map(String companyaddress_label, String company_label, ResultSet resultSet) throws SQLException {
        CompanyAddress address = new CompanyAddress();
        address.setId(resultSet.getInt(String.format("%s%s", companyaddress_label, "id")));
        address.setAddress(resultSet.getString(String.format("%s%s", companyaddress_label, "address")));
        address.setOpen(resultSet.getBoolean(String.format("%s", "add_open")));
        address.setCompany(CompanyDAOJDBC.map(company_label, resultSet));
        return address;
    }
}
