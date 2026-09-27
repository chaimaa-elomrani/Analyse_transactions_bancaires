package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/solubank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void main(String[] args){
        try{
            Connection conn = getConnection();
            System.out.println("Connexion réunion !");
            conn.close();
        }catch(SQLException e){
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}