package com.vmturbo.tools.rbanalysis.db;

/**
 * Queries for database.
 */
public enum Queries {
    INSERT_INFO_ABOUT_RR("INSERT INTO checked_diffs(revision, review_request_id) VALUES(?, ?)"),
    UPDATE_INFO_ABOUT_RR("UPDATE checked_diffs SET revision=? WHERE review_request_id=?"),
    SELECT_REVISION("SELECT revision FROM checked_diffs WHERE review_request_id=?;");

    private final String value;

    /**
     * Creates instance.
     *
     * @param value query.
     */
    Queries(String value) {
        this.value = value;
    }

    /**
     * Gets value of instance.
     *
     * @return query for database.
     */
    public String getValue() {
        return value;
    }
}
