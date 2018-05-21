/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msa_ms;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Pavilion Mini
 */
public class ConnectionFactory {
    public static final String URL = "url";
    public static final String USER = "user";
    public static final String PASS = "password";
    
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            return (Connection) DriverManager.getConnection(URL, USER, PASS);
        } catch(SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
