package com.vmturbo.tools.rbanalysis.responce;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class DiffDataResponse implements Response {
    @SerializedName("diff_data")
    private final DiffData diffData;

    /**
     * Creates {@link DiffDataResponse} instance.
     *
     * @param diffData diff data.
     */
    public DiffDataResponse(DiffData diffData) {
        this.diffData = diffData;
    }

    /**
     * Gets diff data.
     *
     * @return diff data.
     */
    public DiffData getDiffData() {
        return diffData;
    }
}
