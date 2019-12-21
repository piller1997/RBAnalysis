package com.vmturbo.tools.rbanalysis.db;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Interface worker with storing data.
 */
public interface AnalyzableDao {
    /**
     * Updates into database information about analyzed review request.
     *
     * @param reviewRequestId review request's id.
     * @param revision review request's revision.
     * @throws SQLException SQL Exception
     * @throws ParseException Parse data exception
     */
    void updateInfoAboutReviewRequest(int reviewRequestId, int revision)
            throws SQLException;

    /**
     * Inserts into database information about analyzed review request.
     *
     * @param reviewRequestId review request's id.
     * @param revision review request's revision.
     * @throws SQLException SQL Exception
     * @throws ParseException Parse data exception
     */
    void insertInfoAboutReviewRequest(int reviewRequestId, int revision)
            throws SQLException;

    /**
     * Gets last revision from database.
     *
     * @param reviewRequestId review request's id.
     * @return is there data about review request.
     * @throws SQLException SQL Exception.
     * @throws ParseException Parse data exception.
     */

    int getLastRevision(int reviewRequestId) throws SQLException;
}
