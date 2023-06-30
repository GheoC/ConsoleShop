package com.coherent.repository.databaseservice;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DBConnector
{
    private final String jdbcURL;
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL,"sa", "1234");
        return connection;
    }
}
