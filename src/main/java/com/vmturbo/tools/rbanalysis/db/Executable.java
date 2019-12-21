package com.vmturbo.tools.rbanalysis.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;

/**
 * {@link Executable} action which should be executed using connection.
 *
 * @param <R> Type of responce.
 */
public interface Executable<R> {
    /**
     * Action to execute using connection.
     *
     * @param connection SQL connection to database
     * @return Any result of action.
     * @throws SQLException SQL Exception.
     */
    R execute(@Nonnull Connection connection) throws SQLException;
}
