package dao.JDBC;

import dao.DAOConfigurationException;
import dao.DAOProperties;
import dao.interfaces.ActivityReportDAO;
import dao.interfaces.AnalysisReportDAO;
import dao.interfaces.AnalysisReportVarDAO;
import dao.interfaces.AnalysisTypeDAO;
import dao.interfaces.AnalysisTypeVarDAO;
import dao.interfaces.EmployeeDAO;
import dao.interfaces.CompanyDAO;
import dao.interfaces.ModuleDAO;
import dao.interfaces.CompanyAddressDAO;
import dao.interfaces.CompanyContactDAO;
import dao.interfaces.DepartLotDAO;
import dao.interfaces.DepartLot_1DAO;
import dao.interfaces.DepartReportDAO;
import dao.interfaces.DepartReport_1DAO;
import dao.interfaces.EquipmentDAO;
import dao.interfaces.EquipmentTypeCheckDAO;
import dao.interfaces.EquipmentTypeDAO;
import dao.interfaces.IncomingLotDAO;
import dao.interfaces.IncomingReportDAO;
import dao.interfaces.IncomingReport_1DAO;
import dao.interfaces.InvoiceDAO;
import dao.interfaces.InvoiceItemDAO;
import dao.interfaces.MantainanceItemDAO;
import dao.interfaces.MantainanceReportDAO;
import dao.interfaces.MetalDAO;
import dao.interfaces.ModuleEmployeeDAO;
import dao.interfaces.OrderPurchaseDAO;
import dao.interfaces.OrderPurchaseIncomingItemDAO;
import dao.interfaces.OrderPurchaseIncomingReportDAO;
import dao.interfaces.POQueryDAO;
import dao.interfaces.PartRevisionDAO;
import dao.interfaces.ProcessReportDAO;
import dao.interfaces.ProductDAO;
import dao.interfaces.ProductPartDAO;
import dao.interfaces.ProductSupplierDAO;
import dao.interfaces.PurchaseItemDAO;
import dao.interfaces.QuoteDAO;
import dao.interfaces.QuoteItemDAO;
import dao.interfaces.ScrapReportDAO;
import dao.interfaces.ScrapReport_1DAO;
import dao.interfaces.SpecificationDAO;
import dao.interfaces.SpecificationItemDAO;
import dao.interfaces.TankDAO;
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
     * Returns the ProductPart DAO associated with the current DAOFactory.
     * @return The ProductPart DAO associated with the current DAOFactory.
     */    
    public ProductPartDAO getProductPartDAO(){
        return new ProductPartDAOJDBC(this);
    }
    
    /**
     * Returns the Metal DAO associated with the current DAOFactory.
     * @return The Metal DAO associated with the current DAOFactory.
     */    
    public MetalDAO getMetalDAO(){
        return new MetalDAOJDBC(this);
    }
    
    /**
     * Returns the Specification DAO associated with the current DAOFactory.
     * @return The Specification DAO associated with the current DAOFactory.
     */    
    public SpecificationDAO getSpecificationDAO(){
        return new SpecificationDAOJDBC(this);
    }
    
    /**
     * Returns the SpecificationItem DAO associated with the current DAOFactory.
     * @return The SpecificationItem DAO associated with the current DAOFactory.
     */    
    public SpecificationItemDAO getSpecificationItemDAO(){
        return new SpecificationItemDAOJDBC(this);
    }
    
    /**
     * Returns the PartRevision DAO associated with the current DAOFactory.
     * @return The PartRevision DAO associated with the current DAOFactory.
     */   
    public PartRevisionDAO getPartRevisionDAO(){
        return new PartRevisionDAOJDBC(this);
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
     * Returns the QuoteItem DAO associated with the current DAOFactory.
     * @return The QuoteItem DAO associated with the current DAOFactory.
     */   
    public QuoteItemDAO getQuoteItemDAO(){
        return new QuoteItemDAOJDBC(this);
    }
   
    /**
     * Returns the ProcessReport DAO associated with the current DAOFactory.
     * @return The ProcessReport DAO associated with the current DAOFactory.
     */   
    public ProcessReportDAO getProcessReportDAO(){
        return new ProcessReportDAOJDBC(this);
    }
    
    /**
     * Returns the ScrapReport DAO associated with the current DAOFactory.
     * @return The ScrapReport DAO associated with the current DAOFactory.
     */   
    public ScrapReportDAO getScrapReportDAO(){
        return new ScrapReportDAOJDBC(this);
    }
    
    /**
     * Returns the Tank DAO associated with the current DAOFactory.
     * @return The Tank DAO associated with the current DAOFactory.
     */   
    public TankDAO getTankDAO(){
        return new TankDAOJDBC(this);
    }
    
    /**
     * Returns the AnalysisType DAO associated with the current DAOFactory.
     * @return The AnalysisType DAO associated with the current DAOFactory.
     */   
    public AnalysisTypeDAO getAnalysisTypeDAO(){
        return new AnalysisTypeDAOJDBC(this);
    }
    
    /**
     * Returns the AnalysisTypeVar DAO associated with the current DAOFactory.
     * @return The AnalysisTypeVar DAO associated with the current DAOFactory.
     */   
    public AnalysisTypeVarDAO getAnalysisTypeVarDAO(){
        return new AnalysisTypeVarDAOJDBC(this);
    }
    
    /**
     * Returns the AnalysisReport DAO associated with the current DAOFactory.
     * @return The AnalysisReport DAO associated with the current DAOFactory.
     */   
    public AnalysisReportDAO getAnalysisReportDAO(){
        return new AnalysisReportDAOJDBC(this);
    }
    
    /**
     * Returns the AnalysisReport DAO associated with the current DAOFactory.
     * @return The AnalysisReport DAO associated with the current DAOFactory.
     */   
    public AnalysisReportVarDAO getAnalysisReportVarDAO(){
        return new AnalysisReportVarDAOJDBC(this);
    }
    
    /**
     * Returns the EquipmentType DAO associated with the current DAOFactory.
     * @return The EquipmentType DAO associated with the current DAOFactory.
     */   
    public EquipmentTypeDAO getEquipmentTypeDAO(){
        return new EquipmentTypeDAOJDBC(this);
    }
    
    /**
     * Returns the Equipment DAO associated with the current DAOFactory.
     * @return The Equipment DAO associated with the current DAOFactory.
     */   
    public EquipmentDAO getEquipmentDAO(){
        return new EquipmentDAOJDBC(this);
    }
    
    /**
     * Returns the EquipmentTypeCheck DAO associated with the current DAOFactory.
     * @return The EquipmentTypeCheck DAO associated with the current DAOFactory.
     */   
    public EquipmentTypeCheckDAO getEquipmentTypeCheckDAO(){
        return new EquipmentTypeCheckDAOJDBC(this);
    }
    
    /**
     * Returns the MantainanceReport DAO associated with the current DAOFactory.
     * @return The MantainanceReport DAO associated with the current DAOFactory.
     */   
    public MantainanceReportDAO getMantainanceReportDAO(){
        return new MantainanceReportDAOJDBC(this);
    }
    
    /**
     * Returns the MantainanceItem DAO associated with the current DAOFactory.
     * @return The MantainanceItem DAO associated with the current DAOFactory.
     */   
    public MantainanceItemDAO getMantainanceItemDAO(){
        return new MantainanceItemDAOJDBC(this);
    }
    
    /**
     * Returns the Product DAO associated with the current DAOFactory.
     * @return The Product DAO associated with the current DAOFactory.
     */   
    public ProductDAO getProductDAO(){
        return new ProductDAOJDBC(this);
    }
    
    /**
     * Returns the ProductSupplier DAO associated with the current DAOFactory.
     * @return The ProductSupplier DAO associated with the current DAOFactory.
     */   
    public ProductSupplierDAO getProductSupplierDAO(){
        return new ProductSupplierDAOJDBC(this);
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
     * Returns the OrderPurchaseIncomingReport DAO associated with the current DAOFactory.
     * @return The OrderPurchaseIncomingReport DAO associated with the current DAOFactory.
     */   
    public OrderPurchaseIncomingReportDAO getOrderPurchaseIncomingReportDAO(){
        return new OrderPurchaseIncomingReportDAOJDBC(this);
    }
    
    /**
     * Returns the OrderPurchaseIncomingItem DAO associated with the current DAOFactory.
     * @return The OrderPurchaseIncomingItem DAO associated with the current DAOFactory.
     */   
    public OrderPurchaseIncomingItemDAO getOrderPurchaseIncomingItemDAO(){
        return new OrderPurchaseIncomingItemDAOJDBC(this);
    }
    
    /**
     * Returns the ActivityReport DAO associated with the current DAOFactory.
     * @return The ActivityReport DAO associated with the current DAOFactory.
     */   
    public ActivityReportDAO getActivityReportDAO(){
        return new ActivityReportDAOJDBC(this);
    }
    
    /**
     * Returns the POQuery DAO associated with the current DAOFactory.
     * @return The POQuery DAO associated with the current DAOFactory.
     */   
    public POQueryDAO getPOQueryDAO(){
        return new POQueryDAOJDBC(this);
    }
    
    /**
     * Returns the IncomingReport_1 DAO associated with the current DAOFactory.
     * @return The IncomingReport_1 DAO associated with the current DAOFactory.
     */   
    public IncomingReport_1DAO getIncomingReport_1DAO(){
        return new IncomingReport_1DAOJDBC(this);
    }
    
    /**
     * Returns the ScrapReport_1 DAO associated with the current DAOFactory.
     * @return The ScrapReport_1 DAO associated with the current DAOFactory.
     */   
    public ScrapReport_1DAO getScrapReport_1DAO(){
        return new ScrapReport_1DAOJDBC(this);
    }
    
    /**
     * Returns the DepartReport_1 DAO associated with the current DAOFactory.
     * @return The DepartReport_1 DAO associated with the current DAOFactory.
     */   
    public DepartReport_1DAO getDepartReport_1DAO(){
        return new DepartReport_1DAOJDBC(this);
    }
    
    /**
     * Returns the DepartLot_1 DAO associated with the current DAOFactory.
     * @return The DepartLot_1 DAO associated with the current DAOFactory.
     */   
    public DepartLot_1DAO getDepartLot_1DAO(){
        return new DepartLot_1DAOJDBC(this);
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
