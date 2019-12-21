package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class ReviewListResourceResponse implements Response {
    @Nonnull
    @SerializedName("review")
    private final Review review;

    /**
     * Gets {@link ReviewListResourceResponse} instance.
     *
     * @param review review.
     */
    public ReviewListResourceResponse(@Nonnull Review review) {
        this.review = review;
    }

    /**
     * Gets review.
     *
     * @return review.
     */
    @Nonnull
    public Review getReview() {
        return review;
    }
}
