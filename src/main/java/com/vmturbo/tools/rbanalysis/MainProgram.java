package com.vmturbo.tools.rbanalysis;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmturbo.mediation.connector.common.HttpConnectorException;
import com.vmturbo.mediation.connector.common.HttpConnectorFactory;
import com.vmturbo.mediation.connector.common.HttpConnectorSettings;
import com.vmturbo.tools.rbanalysis.checkers.MultipleReviewsChecker;
import com.vmturbo.tools.rbanalysis.checkers.OneReviewChecker;
import com.vmturbo.tools.rbanalysis.credentials.RbCredentials;
import com.vmturbo.tools.rbanalysis.db.AnalyzableDao;
import com.vmturbo.tools.rbanalysis.db.DbDao;
import com.vmturbo.tools.rbanalysis.deserializer.FileDiffLineTypeDeserializer;
import com.vmturbo.tools.rbanalysis.responce.FileDiffLine;

/**
 * Runner Review Board Analysis.
 */
public class MainProgram {
    private static final Logger LOGGER = LogManager.getLogger(MainProgram.class);

    private MainProgram() {}

    /**
     * Runs method.
     *
     * @param args CLI arguments.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            final Properties properties;
            try {
                properties = new Properties(args[0]);
                try (HttpConnectorFactory<HttpConnectorSettings, RbCredentials> connectorFactory = getConnectorFactory()) {
                    final RbCredentials rbCredentials = new RbCredentials(properties);

                    if (args.length == 2) {
                        final int id = Integer.parseInt(args[1]);
                        final OneReviewChecker checker = new OneReviewChecker(id, -1, properties,
                                connectorFactory.getConnector(rbCredentials));
                        checker.call();
                    } else {
                        final AnalyzableDao db = new DbDao(properties);
                        final MultipleReviewsChecker checker =
                                new MultipleReviewsChecker(properties,
                                        connectorFactory.getConnector(rbCredentials), db);
                        checker.run();
                    }
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            } catch (IOException | HttpConnectorException | InterruptedException e) {
                LOGGER.warn(e);
            }
        } else {
            LOGGER.warn(
                    "Mandatory 1st argument is missing. Usage: program_name configuration_file [review request's id]");
        }
    }

    private static HttpConnectorFactory<HttpConnectorSettings, RbCredentials> getConnectorFactory() {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'")
                .registerTypeAdapter(FileDiffLine.class, new FileDiffLineTypeDeserializer())
                .create();
        return RbHttpConnectorFactory.createConnectorFactoryBuilder().setGson(gson).build();
    }
}
