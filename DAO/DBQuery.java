package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * This class sets and gets SQL statements to be used on the database.
 */
public class DBQuery {
    private static PreparedStatement statement;

    public static void setStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getStatement() {
        return statement;
    }
}
