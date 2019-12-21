package com.vmturbo.tools.rbanalysis.queries;

import java.util.Map;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.mediation.connector.common.http.HttpParameter;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequestsResponse;

/**
 * Executes GET HTTP requests to the RB instance to get review requests.
 */
public class ReviewRequestsQuery extends GetQuery<ReviewRequestsResponse> {
    /**
     * Creates {@link ReviewRequestsQuery} instance.
     *
     * @param parameters HTTP parameters for query
     * @param properties program's properties
     */
    public ReviewRequestsQuery(Map<HttpParameter, String> parameters,
            @Nonnull Properties properties) {
        super(HttpMethodType.GET, "review-requests/", null, ReviewRequestsResponse.class,
                parameters, properties);
    }
}
