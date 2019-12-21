package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class ReviewRequestResponse implements Response {

    @Nonnull
    @SerializedName("review_request")
    private final ReviewRequests reviewRequest;

    /**
     * Creates of {@link ReviewRequestResponse} instance.
     *
     * @param reviewRequest Review requests.
     */
    public ReviewRequestResponse(@Nonnull ReviewRequests reviewRequest) {
        this.reviewRequest = reviewRequest;
    }

    /**
     * Gets list of review requests.
     *
     * @return review requests.
     */
    @Nonnull
    public ReviewRequests getReviewRequest() {
        return reviewRequest;
    }
}
