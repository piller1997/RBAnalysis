package com.vmturbo.tools.rbanalysis.responce;

import java.util.Date;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class ReviewRequests implements Response {
    @Nonnull
    @SerializedName("id")
    private final int id;
    @Nonnull
    @SerializedName("status")
    private final String status;
    @Nonnull
    @SerializedName("description")
    private final String description;
    @Nonnull
    @SerializedName("summary")
    private final String summary;
    @Nonnull
    @SerializedName("last_updated")
    private final Date lastUpdated;
    @Nonnull
    @SerializedName("links")
    private final ReviewRequestLinks links;

    /**
     * Creates of {@link ReviewRequests} instance.
     *
     * @param id review request's id.
     * @param status review request's status.
     * @param description review request's status.
     * @param summary review request's summary.
     * @param lastUpdated time of last updated.
     * @param links list of links.
     */
    public ReviewRequests(@Nonnull int id, @Nonnull String status,
            @Nonnull String description, @Nonnull String summary, @Nonnull Date lastUpdated,
            @Nonnull ReviewRequestLinks links) {
        this.id = id;
        this.status = status;
        this.description = description;
        this.summary = summary;
        this.lastUpdated = lastUpdated;
        this.links = links;
    }

    /**
     * Gets review request's id.
     *
     * @return id.
     */
    @Nonnull
    public int getId() {
        return id;
    }

    /**
     * Gets review request's status.
     *
     * @return status.
     */
    @Nonnull
    public String getStatus() {
        return status;
    }

    /**
     * Gets review request's description.
     *
     * @return description.
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Gets review request's summary.
     *
     * @return summary.
     */
    @Nonnull
    public String getSummary() {
        return summary;
    }

    /**
     * Gets some links of request.
     *
     * @return links.
     */
    @Nonnull
    public ReviewRequestLinks getLinks() {
        return links;
    }

    /**
     * Returns date of last review update.
     *
     * @return last review update date
     */
    @Nonnull
    public Date getLastUpdated() {
        return lastUpdated;
    }
}
