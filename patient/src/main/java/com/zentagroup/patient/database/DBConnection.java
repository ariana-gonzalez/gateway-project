package com.zentagroup.patient.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.zentagroup.patient.util.Constant.*;


public class DBConnection {

    public static Connection connectionToDatabase(){
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USERNAME);
        connectionProps.put("password", DB_PASSWORD);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:" + DBMS +
                            "://" + SERVER_NAME +
                            ":" + PORT_NUMBER +
                            "/" + DB_NAME,
                    connectionProps);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return conn;
    }

}
