/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for DAO's. This class contains commonly used DAO logic which is been refactored in
 * single static methods. As far it contains a PreparedStatement values setter and a
 * <code>java.util.Date</code> to <code>java.sql.Date</code> converter.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public final class DAOUtil {

    // Constructors -------------------------------------------------------------------------------

    private DAOUtil() {
        // Utility class, hide constructor.
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns a PreparedStatement of the given connection, set with the given SQL query and the
     * given parameter values.
     * @param connection The Connection to create the PreparedStatement from.
     * @param sql The SQL query to construct the PreparedStatement with.
     * @param returnGeneratedKeys Set whether to return generated keys or not.
     * @param values The parameter values to be set in the created PreparedStatement.
     * @throws SQLException If something fails during creating the PreparedStatement.
     */
    public static PreparedStatement prepareStatement
        (Connection connection, String sql, boolean returnGeneratedKeys, Object... values)
            throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(sql,
            returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        setValues(statement, values);
        return statement;
    }

    /**
     * Set the given parameter values in the given PreparedStatement.
     * @param connection The PreparedStatement to set the given parameter values in.
     * @param values The parameter values to be set in the created PreparedStatement.
     * @throws SQLException If something fails during setting the PreparedStatement values.
     */
    public static void setValues(PreparedStatement statement, Object... values)
        throws SQLException
    {
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
    }

    /**
     * Converts the given java.util.Date to java.sql.Date.
     * @param date The java.util.Date to be converted to java.sql.Date.
     * @return The converted java.sql.Date.
     */
    public static Date toSqlDate(java.util.Date date) {
     return (date != null) ? new Date(date.getTime()) : null;
    }

    public static java.sql.Time toSqlTime(LocalTime time){
        return Time.valueOf(time);
}
    
    public static LocalTime toLocalTime(java.sql.Time time) {
        return time.toLocalTime();
    }
    
    public static java.util.Date toUtilDate(LocalDate date){
        java.sql.Date sqlDate = java.sql.Date.valueOf(date); 
        java.util.Date utilDate = new java.util.Date();
        utilDate.setDate(sqlDate.getDate());
        utilDate.setHours(17);
        utilDate.setMinutes(0);
        return utilDate;
    }
    
    public static void setProperty(String file, String key, String value){
        Properties props = new Properties();
        String path = "./src/";
        try {
            //first load old one:
            FileInputStream configStream = new FileInputStream(path+file);
            props.load(configStream);
            configStream.close();
            //modifies existing or adds new property
            props.setProperty(key, value);
      
            //save modified property file
            FileOutputStream output = new FileOutputStream(path+file);
            props.store(output, "Edited "+key+" with "+value);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String getProperty(String file, String key){
        Properties props = new Properties();
        String path = "./src/";
        try{
            FileInputStream configStream = new FileInputStream(path+file);
            props.load(configStream);
            configStream.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return props.getProperty(key);
    }
}
