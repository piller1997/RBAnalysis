package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.DiffListResourceResponse;

/**
 * Executes GET HTTP requests to the RB instance to get of complete diffs.
 */
public class DiffListResourceQuery extends GetQuery<DiffListResourceResponse> {

    /**
     * Creates {@link DiffListResourceQuery} instance.
     *
     * @param reviewRequestId review request's id.
     * @param properties program's properties
     */
    public DiffListResourceQuery(int reviewRequestId,
            @Nonnull Properties properties) {
        super(HttpMethodType.GET, String.format("review-requests/%d/diffs/", reviewRequestId), null,
                DiffListResourceResponse.class, Collections.emptyMap(), properties);
    }
}
