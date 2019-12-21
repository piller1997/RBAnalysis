package com.vmturbo.tools.rbanalysis.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmturbo.tools.rbanalysis.Properties;

/**
 * Worker with database.
 */
public class DbDao implements AnalyzableDao {
    private final String url;
    private final Logger logger = LogManager.getLogger(getClass());

    /**
     * Creates new instance.
     *
     * @param properties program's properties
     * @throws IOException properties file failed exception.
     */
    public DbDao(@Nonnull Properties properties) {
        Objects.requireNonNull(properties);
        url = "jdbc:sqlite:" + properties.getUrlDatabase();
    }

    /**
     * Connects to database.
     *
     * @return Connection to database.
     * @throws SQLException Sql exception
     */
    @Nonnull
    private Connection connect() throws SQLException {
        final Connection connection = DriverManager.getConnection(url);
        logger.debug("Database connected");
        return connection;
    }

    /**
     * Insert into database information about analyzed review request.
     *
     * @param reviewRequestId review request's id.
     * @param revision review request's revision.
     * @throws SQLException SQL Exception.
     */
    @Override
    public void updateInfoAboutReviewRequest(int reviewRequestId, int revision)
            throws SQLException {
        connectAndExecute((connection) -> {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement(Queries.UPDATE_INFO_ABOUT_RR.getValue());
            preparedStatement.setInt(1, revision);
            preparedStatement.setInt(2, reviewRequestId);
            preparedStatement.execute();
            return null;
        });
    }

    /**
     * Inserts into database information about analyzed review request.
     *
     * @param reviewRequestId review request's id.
     * @param revision review request's revision.
     * @throws SQLException SQL Exception
     * @throws ParseException Parse data exception
     */
    @Override
    public void insertInfoAboutReviewRequest(@Nonnull int reviewRequestId, @Nonnull int revision)
            throws SQLException {
        connectAndExecute(connection -> {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement(Queries.INSERT_INFO_ABOUT_RR.getValue());
            preparedStatement.setInt(1, revision);
            preparedStatement.setInt(2, reviewRequestId);
            preparedStatement.execute();
            return null;
        });
    }

    /**
     * Gets last revision from database.
     *
     * @param reviewRequestId review request's id.
     * @return review request's last revision.
     * @throws SQLException SQL Exception.
     * @throws ParseException Parse data exception.
     */
    @Nonnull
    @Override
    public int getLastRevision(@Nonnull int reviewRequestId) throws SQLException {
        return connectAndExecute(connection -> {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement(Queries.SELECT_REVISION.getValue());
            preparedStatement.setInt(1, reviewRequestId);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        });
    }

    /**
     * Connects and execute method, implemented in {@param lambda}.
     *
     * @param executable implementing {@link Executable} instance.
     * @param <T> Type of returning.
     * @return result of executing {@param executable}.
     * @throws SQLException SQL Exception.
     * @throws ParseException Parse data exception.
     */
    private <T> T connectAndExecute(@Nonnull Executable<T> executable)
            throws SQLException {
        Objects.requireNonNull(executable);
        final T result;
        final Connection connection = connect();
        if (!connection.isClosed()) {
            result = executable.execute(connection);
        } else {
            result = null;
        }
        disconnect(connection);
        return result;
    }

    /**
     * Disconnects from database.
     *
     * @param connection connection for disconnecting.
     */
    private void disconnect(@Nonnull Connection connection) {
        Objects.requireNonNull(connection);
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn(e);
        }
    }
}
