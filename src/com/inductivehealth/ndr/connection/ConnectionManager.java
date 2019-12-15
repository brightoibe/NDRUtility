package com.inductivehealth.ndr.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author THANKGOD
 */
public class ConnectionManager {
    private static final String url = "jdbc:mysql://localhost:3316/openmrs";    
    private static final String driverName = "com.mysql.jdbc.Driver";   
    private static final String username = "openmrs";   
    private static final String password = "42Gg5Kj4a^Tw";
    private static Connection con;
  

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection."); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
        return con;
    }
}
