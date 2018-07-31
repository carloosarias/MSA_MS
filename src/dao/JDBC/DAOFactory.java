package dao.JDBC;

import dao.DAOConfigurationException;
import dao.DAOProperties;
import dao.interfaces.EmployeeDAO;
import dao.interfaces.CompanyDAO;
import dao.interfaces.ModuleEmployeeDAO;
import dao.interfaces.ModuleDAO;
import dao.interfaces.CompanyAddressDAO;
import dao.interfaces.CompanyContactDAO;
import dao.interfaces.ContainerDAO;
import dao.interfaces.DepartLotDAO;
import dao.interfaces.DepartReportDAO;
import dao.interfaces.IncomingLotDAO;
import dao.interfaces.IncomingReportDAO;
import dao.interfaces.InvoiceDAO;
import dao.interfaces.InvoiceItemDAO;
import dao.interfaces.InvoicePaymentItemDAO;
import dao.interfaces.InvoicePaymentReportDAO;
import dao.interfaces.OrderPurchaseDAO;
import dao.interfaces.PartRevisionDAO;
import dao.interfaces.ProcessReportDAO;
import dao.interfaces.ProductDAO;
import dao.interfaces.ProductPartDAO;
import dao.interfaces.ProductTypeDAO;
import dao.interfaces.PurchaseItemDAO;
import dao.interfaces.QuoteDAO;
import dao.interfaces.SpecificationDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class represents a DAO factory for a SQL database. You can use {@link #getInstance(String)}
 * to obtain a new instance for the given database name. The specific instance returned depends on
 * the properties file configuration. You can obtain DAO's for the DAO factory instance using the 
 * DAO getters.
 * <p>
 * This class requires a properties file named 'dao.properties' in the classpath with among others
 * the following properties:
 * <pre>
 * name.url *
 * name.driver
 * name.username
 * name.password
 * </pre>
 * Those marked with * are required, others are optional and can be left away or empty. Only the
 * username is required when any password is specified.
 * <ul>
 * <li>The 'name' must represent the database name in {@link #getInstance(String)}.</li>
 * <li>The 'name.url' must represent either the JDBC URL or JNDI name of the database.</li>
 * <li>The 'name.driver' must represent the full qualified class name of the JDBC driver.</li>
 * <li>The 'name.username' must represent the username of the database login.</li>
 * <li>The 'name.password' must represent the password of the database login.</li>
 * </ul>
 * If you specify the driver property, then the url property will be assumed as JDBC URL. If you
 * omit the driver property, then the url property will be assumed as JNDI name. When using JNDI
 * with username/password preconfigured, you can omit the username and password properties as well.
 * <p>
 * Here are basic examples of valid properties for a database with the name 'javabase':
 * <pre>
 * javabase.jdbc.url = jdbc:mysql://localhost:3306/javabase
 * javabase.jdbc.driver = com.mysql.jdbc.Driver
 * javabase.jdbc.username = java
 * javabase.jdbc.password = d$7hF_r!9Y
 * </pre>
 * <pre>
 * javabase.jndi.url = jdbc/javabase
 * </pre>
 * Here is a basic use example:
 * <pre>
 * DAOFactory javabase = DAOFactory.getInstance("javabase.jdbc");
 * UserDAO userDAO = javabase.getUserDAO();
 * </pre>
 *
 * @author BalusC
 */
public abstract class DAOFactory {
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_DRIVER = "driver";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";
    
    /**
     * Returns a new DAOFactory instance for the given database name.
     * @param name The database name to return a new DAOFactory instance for.
     * @return A new DAOFactory instance for the given database name.
     * @throws DAOConfigurationException If the database name is null, or if the properties file is
     * missing in the classpath or cannot be loaded, or if a required property is missing in the
     * properties file, or if either the driver cannot be loaded or the datasource cannot be found.
     */
    public static DAOFactory getInstance(String name) throws DAOConfigurationException {
        if (name == null) {
            throw new DAOConfigurationException("Database name is null.");
        }

        DAOProperties properties = new DAOProperties(name);
        String url = properties.getProperty(PROPERTY_URL, true);
        String driverClassName = properties.getProperty(PROPERTY_DRIVER, false);
        String password = properties.getProperty(PROPERTY_PASSWORD, false);
        String username = properties.getProperty(PROPERTY_USERNAME, password != null);
        DAOFactory instance;

        // If driver is specified, then load it to let it register itself with DriverManager.
        if (driverClassName != null) {
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new DAOConfigurationException(
                    "Driver class '" + driverClassName + "' is missing in classpath.", e);
            }
            instance = new DriverManagerDAOFactory(url, username, password);
        }

        // Else assume URL as DataSource URL and lookup it in the JNDI.
        else {
            DataSource dataSource;
            try {
                dataSource = (DataSource) new InitialContext().lookup(url);
            } catch (NamingException e) {
                throw new DAOConfigurationException(
                    "DataSource '" + url + "' is missing in JNDI.", e);
            }
            if (username != null) {
                instance = new DataSourceWithLoginDAOFactory(dataSource, username, password);
            } else {
                instance = new DataSourceDAOFactory(dataSource);
            }
        }

        return instance;
    }
    
    /**
     * Returns a connection to the database. Package private so that it can be used inside the DAO
     * package only.
     * @return A connection to the database.
     * @throws SQLException If acquiring the connection fails.
     */
    abstract Connection getConnection() throws SQLException;
    
    // DAO implementation getters -----------------------------------------------------------------
    /**
     * Returns the Employee DAO associated with the current DAOFactory.
     * @return The Employee DAO associated with the current DAOFactory.
     */
    public EmployeeDAO getEmployeeDAO() {
        return new EmployeeDAOJDBC(this);
    }
    
    /**
     * Returns the Module DAO associated with the current DAOFactory.
     * @return The Module DAO associated with the current DAOFactory.
     */
    public ModuleDAO getModuleDAO() {
        return new ModuleDAOJDBC(this);
    }
    
    /**
     * Returns the ModuleEmployee DAO associated with the current DAOFactory.
     * @return The ModuleEmployee DAO associated with the current DAOFactory.
     */
    public ModuleEmployeeDAO getModuleEmployeeDAO() {
        return new ModuleEmployeeDAOJDBC(this);
    }
    
    /**
     * Returns the Company DAO associated with the current DAOFactory.
     * @return The Company DAO associated with the current DAOFactory.
     */
    public CompanyDAO getCompanyDAO() {
        return new CompanyDAOJDBC(this);
    }
    
    /**
     * Returns the CompanyAddress DAO associated with the current DAOFactory.
     * @return The CompanyAddress DAO associated with the current DAOFactory.
     */
    public CompanyAddressDAO getCompanyAddressDAO() {
        return new CompanyAddressDAOJDBC(this);
    }
    
    /**
     * Returns the CompanyContact DAO associated with the current DAOFactory.
     * @return The CompanyContact DAO associated with the current DAOFactory.
     */
    public CompanyContactDAO getCompanyContactDAO() {
        return new CompanyContactDAOJDBC(this);
    }
    
    /**
     * Returns the Specification DAO associated with the current DAOFactory.
     * @return The Specification DAO associated with the current DAOFactory.
     */
    public SpecificationDAO getSpecificationDAO() {
        return new SpecificationDAOJDBC(this);
    }
    
    /**
     * Returns the ProductType DAO associated with the current DAOFactory.
     * @return The ProductType DAO associated with the current DAOFactory.
     */
    public ProductTypeDAO getProductTypeDAO() {
        return new ProductTypeDAOJDBC(this);
    }
    
    /**
     * Returns the Product DAO associated with the current DAOFactory.
     * @return The Product DAO associated with the current DAOFactory.
     */
    public ProductDAO getProductDAO() {
        return new ProductDAOJDBC(this);
    }
    
    /**
     * Returns the ProductPart DAO associated with the current DAOFactory.
     * @return The ProductPart DAO associated with the current DAOFactory.
     */    
    public ProductPartDAO getProductPartDAO(){
        return new ProductPartDAOJDBC(this);
    }
    
    /**
     * Returns the PartRevision DAO associated with the current DAOFactory.
     * @return The PartRevision DAO associated with the current DAOFactory.
     */   
    public PartRevisionDAO getPartRevisionDAO(){
        return new PartRevisionDAOJDBC(this);
    }

    /**
     * Returns the OrderPurchase DAO associated with the current DAOFactory.
     * @return The OrderPurchase DAO associated with the current DAOFactory.
     */   
    public OrderPurchaseDAO getOrderPurchaseDAO(){
        return new OrderPurchaseDAOJDBC(this);
    }
    
    /**
     * Returns the PurchaseItem DAO associated with the current DAOFactory.
     * @return The PurchaseItem DAO associated with the current DAOFactory.
     */   
    public PurchaseItemDAO getPurchaseItemDAO(){
        return new PurchaseItemDAOJDBC(this);
    }
    
    /**
     * Returns the IncomingReport DAO associated with the current DAOFactory.
     * @return The IncomingReport DAO associated with the current DAOFactory.
     */   
    public IncomingReportDAO getIncomingReportDAO(){
        return new IncomingReportDAOJDBC(this);
    }
    
    /**
     * Returns the IncomingLot DAO associated with the current DAOFactory.
     * @return The IncomingLot DAO associated with the current DAOFactory.
     */   
    public IncomingLotDAO getIncomingLotDAO(){
        return new IncomingLotDAOJDBC(this);
    }
    
    /**
     * Returns the DepartReport DAO associated with the current DAOFactory.
     * @return The DepartReport DAO associated with the current DAOFactory.
     */   
    public DepartReportDAO getDepartReportDAO(){
        return new DepartReportDAOJDBC(this);
    }
    
    /**
     * Returns the DepartLot DAO associated with the current DAOFactory.
     * @return The DepartLot DAO associated with the current DAOFactory.
     */   
    public DepartLotDAO getDepartLotDAO(){
        return new DepartLotDAOJDBC(this);
    }
    
    /**
     * Returns the Invoice DAO associated with the current DAOFactory.
     * @return The Invoice DAO associated with the current DAOFactory.
     */   
    public InvoiceDAO getInvoiceDAO(){
        return new InvoiceDAOJDBC(this);
    }
    
    /**
     * Returns the InvoiceItem DAO associated with the current DAOFactory.
     * @return The InvoiceItem DAO associated with the current DAOFactory.
     */   
    public InvoiceItemDAO getInvoiceItemDAO(){
        return new InvoiceItemDAOJDBC(this);
    }    
    
    /**
     * Returns the Quote DAO associated with the current DAOFactory.
     * @return The Quote DAO associated with the current DAOFactory.
     */   
    public QuoteDAO getQuoteDAO(){
        return new QuoteDAOJDBC(this);
    }
    
    /**
     * Returns the InvoicePaymentReport DAO associated with the current DAOFactory.
     * @return The InvoicePaymentReport DAO associated with the current DAOFactory.
     */   
    public InvoicePaymentReportDAO getInvoicePaymentReportDAO(){
        return new InvoicePaymentReportDAOJDBC(this);
    }
    
    /**
     * Returns the InvoicePaymentItem DAO associated with the current DAOFactory.
     * @return The InvoicePaymentItem DAO associated with the current DAOFactory.
     */   
    public InvoicePaymentItemDAO getInvoicePaymentItemDAO(){
        return new InvoicePaymentItemDAOJDBC(this);
    }
    
    /**
     * Returns the Container DAO associated with the current DAOFactory.
     * @return The Container DAO associated with the current DAOFactory.
     */   
    public ContainerDAO getContainerDAO(){
        return new ContainerDAOJDBC(this);
    }
   
    /**
     * Returns the ProcessReport DAO associated with the current DAOFactory.
     * @return The ProcessReport DAO associated with the current DAOFactory.
     */   
    public ProcessReportDAO getProcessReportDAO(){
        return new ProcessReportDAOJDBC(this);
    }
    
    // You can add more DAO implementation getters here.
}

// Default DAOFactory implementations -------------------------------------------------------------

/**
 * The DriverManager based DAOFactory.
 */
class DriverManagerDAOFactory extends DAOFactory {
    private String url;
    private String username;
    private String password;

    DriverManagerDAOFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        }

    @Override
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
    
    /**
 * The DataSource based DAOFactory.
 */
class DataSourceDAOFactory extends DAOFactory {
    private DataSource dataSource;

    DataSourceDAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

/**
 * The DataSource-with-Login based DAOFactory.
 */
class DataSourceWithLoginDAOFactory extends DAOFactory {
    private DataSource dataSource;
    private String username;
    private String password;

    DataSourceWithLoginDAOFactory(DataSource dataSource, String username, String password) {
        this.dataSource = dataSource;
        this.username = username;
        this.password = password;
    }

    @Override
    Connection getConnection() throws SQLException {
        return dataSource.getConnection(username, password);
    }
}
