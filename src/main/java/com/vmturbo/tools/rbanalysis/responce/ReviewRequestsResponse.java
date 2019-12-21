package com.vmturbo.tools.rbanalysis.responce;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class ReviewRequestsResponse implements Response {

    @Nonnull
    @SerializedName("review_requests")
    private final Collection<ReviewRequests> reviewRequests;

    /**
     * Creates of {@link ReviewRequestsResponse} instance.
     *
     * @param reviewRequests Collection of review requests.
     */
    public ReviewRequestsResponse(
            @Nonnull Collection<ReviewRequests> reviewRequests) {
        this.reviewRequests = reviewRequests;
    }

    /**
     * Gets list of review requests.
     *
     * @return review requests.
     */
    @Nonnull
    public Collection<ReviewRequests> getReviewRequests() {
        return reviewRequests;
    }
}
