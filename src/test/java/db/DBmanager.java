package db;

import helpers.SetupFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBmanager {


    public Connection connect() throws SQLException {
        SetupFunctions setupFunctions = new SetupFunctions();

        String host = setupFunctions.getDbHost();
        String port = setupFunctions.getDbPort();
        String dbName = setupFunctions.getDbName();

        String user = setupFunctions.getDbUser();
        String password = setupFunctions.getDbPassword();

        String connectionString = host + ":" + port + "/" + dbName;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load class");
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, user, password);
            System.out.println("Connected to the PostgresSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

     //   Statement s = connection.createStatement();

        return connection;
    }

    public void close(Connection connection) {

        if (connection != null) {

            try {
                connection.close();
                System.out.println("Closed successfully");
            } catch (SQLException e) {
                System.out.println("error while closing connection:" + e);
            }

        } else {
            System.out.println("Connection does not exist");
        }

    }


}
