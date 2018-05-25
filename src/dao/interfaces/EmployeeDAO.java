/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import dao.DAOException;
import java.util.List;
import model.Employee;

/**
 * This interface represents a contract for a DAO for the {@link Employee} model.
 * Note that all methods which returns the {@link Employee} from the DB, will not
 * fill the model with the password, due to security reasons.
 *
 * @author BalusC
 */
public interface EmployeeDAO {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the Employee from the database matching the given ID, otherwise null.
     * @param id The ID of the Employee to be returned.
     * @return The Employee from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Employee find(Integer id) throws DAOException;

    /**
     * Returns the user from the database matching the given email and password, otherwise null.
     * @param user The email of the user to be returned.
     * @param password The password of the user to be returned.
     * @return The user from the database matching the given email and password, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public Employee find(String user, String password) throws DAOException;

    /**
     * Returns a listActive of all Employees from the database ordered by user ID. The listActive is never null and
 is empty when the database does not contain any Employee.
     * @return A listActive of all Employees from the database ordered by Employee ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Employee> list() throws DAOException;
    
    /**
     * Returns a listActive of all Employees that are active from the database ordered by Employee ID. The listActive is never null and
 is empty when the database does not contain any Company active.
     * @param active The state of the Employee if false then return Employees that are NOT active
     * @return A listActive of all Employees that are active from the database ordered by Employee ID.
     * @throws DAOException If something fails at database level.
     */
    public List<Employee> listActive(boolean active) throws DAOException;
    
    /**
     * Create the given Employee in the database. The Employee ID must be null, otherwise it will throw
     * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given Employee.
     * @param employee The Employee to be created in the database.
     * @throws IllegalArgumentException If the employee ID is not null.
     * @throws DAOException If something fails at database level.
     */
    public void create(Employee employee) throws IllegalArgumentException, DAOException;

    /**
     * Update the given Employee in the database. The Employee ID must not be null, otherwise it will throw
     * IllegalArgumentException. Note: the password will NOT be updated. Use changePassword() instead.
     * @param employee The employee to be updated in the database.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void update(Employee employee) throws IllegalArgumentException, DAOException;

    /**
     * Delete the given Employee from the database. After deleting, the DAO will set the ID of the given
     * Employee to null.
     * @param employee The Employee to be deleted from the database.
     * @throws DAOException If something fails at database level.
     */
    public void delete(Employee employee) throws DAOException;

    /**
     * Returns true if the given user exist in the database.
     * @param user The user which is to be checked in the database.
     * @return True if the given user exist in the database.
     * @throws DAOException If something fails at database level.
     */
    public boolean existUser(String user) throws DAOException;
    
    /**
     * Returns true if the given user exist and is active/inactive in the database.
     * @param user The user which is to be checked in the database.
     * @param active The active status of the user.
     * @return True if the given user exist in the database.
     * @throws DAOException If something fails at database level.
     */
    public boolean existActive(String user, boolean active) throws DAOException;

    /**
     * Change the password of the given employee. The Employee ID must not be null, otherwise it will throw
     * IllegalArgumentException.
     * @param employee The Employee to change the password for.
     * @throws IllegalArgumentException If the Employee ID is null.
     * @throws DAOException If something fails at database level.
     */
    public void changePassword(Employee employee) throws DAOException;
    
}
