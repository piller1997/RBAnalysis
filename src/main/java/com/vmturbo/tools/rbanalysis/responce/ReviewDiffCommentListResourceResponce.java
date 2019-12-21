package com.vmturbo.tools.rbanalysis.responce;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class ReviewDiffCommentListResourceResponce implements Response {
    @SerializedName("stat")
    private final String stat;

    /**
     * Creates {@link ReviewDiffCommentListResourceResponce} instance.
     *
     * @param stat status of response.
     */
    public ReviewDiffCommentListResourceResponce(String stat) {
        this.stat = stat;
    }

    /**
     * Gets stat of response.
     *
     * @return stat of response.
     */
    public String getStat() {
        return stat;
    }
}
