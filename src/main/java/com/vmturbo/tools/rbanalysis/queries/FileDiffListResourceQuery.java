package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.FileDiffListResourcesResponse;

/**
 * Executes GET HTTP requests to the RB instance to get per-file diffs.
 */
public class FileDiffListResourceQuery extends GetQuery<FileDiffListResourcesResponse> {

    /**
     * Creates {@link FileDiffListResourceQuery} instance.
     *
     * @param reviewRequestId review request's id
     * @param diffRevision diff's revision
     * @param properties program's properties
     */
    public FileDiffListResourceQuery(int reviewRequestId, int diffRevision,
            @Nonnull Properties properties) {
        super(HttpMethodType.GET,
                String.format("review-requests/%d/diffs/%d/files/", reviewRequestId, diffRevision),
                null, FileDiffListResourcesResponse.class, Collections.emptyMap(),
                properties);
    }
}
