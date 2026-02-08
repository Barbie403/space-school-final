package it.spaceschool.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/space_school";

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "root");

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}