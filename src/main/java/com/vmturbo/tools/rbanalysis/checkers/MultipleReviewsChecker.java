package com.vmturbo.tools.rbanalysis.checkers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmturbo.mediation.connector.common.HttpConnector;
import com.vmturbo.mediation.connector.common.HttpConnectorException;
import com.vmturbo.mediation.connector.common.http.HttpParameter;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.db.AnalyzableDao;
import com.vmturbo.tools.rbanalysis.queries.HttpParameters;
import com.vmturbo.tools.rbanalysis.queries.ReviewRequestsQuery;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequests;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequestsResponse;

/**
 * Review requests are checker.
 */
public class MultipleReviewsChecker {
    private final Properties properties;
    private final HttpConnector connector;
    private final AnalyzableDao dao;
    private final Logger logger;

    /**
     * Creates {@link MultipleReviewsChecker} instance.
     *
     * @param properties Program's configuration.
     * @param connector Http connector.
     * @param dao Dao for working with data.
     */
    public MultipleReviewsChecker(@Nonnull Properties properties, @Nonnull HttpConnector connector,
            @Nonnull AnalyzableDao dao) {
        this.properties = Objects.requireNonNull(properties);
        this.connector = Objects.requireNonNull(connector);
        this.dao = Objects.requireNonNull(dao);
        this.logger = LogManager.getLogger(getClass());
    }

    /**
     * Runs checking.
     *
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    public void run() throws HttpConnectorException, InterruptedException {
        final ReviewRequestsResponse reviewRequestsResponse = getReviewRequests();
        final ExecutorService service = Executors.newFixedThreadPool(properties.getThreadPoolSize(),
                new ThreadFactoryBuilder().setNameFormat("Checker-%d").build());
        final Map<Integer, Future<Integer>> futures = new HashMap<>();
        final Map<Integer, Integer> lastRevisions = new HashMap<>();

        try {
            for (ReviewRequests reviewRequest : reviewRequestsResponse.getReviewRequests()) {
                final int lastRevision = dao.getLastRevision(reviewRequest.getId());
                final OneReviewChecker checker =
                        new OneReviewChecker(reviewRequest.getId(), lastRevision, properties,
                                connector);
                futures.put(reviewRequest.getId(), service.submit(checker));
                lastRevisions.put(reviewRequest.getId(), lastRevision);
            }

            for (ReviewRequests reviewRequest : reviewRequestsResponse.getReviewRequests()) {
                try {
                    final int revision = futures.get(reviewRequest.getId()).get();
                    final int lastRevision = lastRevisions.get(reviewRequest.getId());
                    if (lastRevision == -1) {
                        dao.insertInfoAboutReviewRequest(reviewRequest.getId(), revision);
                    } else {
                        if (revision > lastRevision) {
                            dao.updateInfoAboutReviewRequest(reviewRequest.getId(), revision);
                        }
                    }
                } catch (ExecutionException e) {
                    logger.warn("Error occurred while processing review request #" +
                            reviewRequest.getId(), e);
                }
            }
        } catch (SQLException e) {
            logger.warn("Database error. ", e);
        } finally {
            service.shutdown();
        }
    }

    /**
     * Gets last review requests.
     *
     * @return Last review requests.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private ReviewRequestsResponse getReviewRequests()
            throws HttpConnectorException, InterruptedException {
        final Map<HttpParameter, String> httpParameters =
                Collections.singletonMap(HttpParameters.MAX_RESULTS,
                        properties.getCountReviewsForAnalysis());
        final ReviewRequestsQuery query = new ReviewRequestsQuery(httpParameters, properties);
        return connector.execute(query);
    }
}
