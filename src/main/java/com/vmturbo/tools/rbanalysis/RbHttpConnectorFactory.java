package com.vmturbo.tools.rbanalysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import com.vmturbo.mediation.connector.common.HttpConnectorFactoryBuilder;
import com.vmturbo.mediation.connector.common.HttpConnectorSettings;
import com.vmturbo.mediation.connector.common.HttpQuery;
import com.vmturbo.mediation.connector.common.http.response.processor.HttpResponseUnmarshaller;
import com.vmturbo.tools.rbanalysis.credentials.RbCredentials;
import com.vmturbo.tools.rbanalysis.queries.DiffDataQuery;
import com.vmturbo.tools.rbanalysis.responce.FileResponse;

/**
 * HTTP connector.
 */
public final class RbHttpConnectorFactory {
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCEPT = "Accept";

    private RbHttpConnectorFactory() {
    }

    /**
     * Creates {@link com.vmturbo.mediation.connector.common.HttpConnectorFactory} instance that used by the ServiceNow
     * probe.
     *
     * @return HTTP cached connector factory.
     */
    @Nonnull
    public static HttpConnectorFactoryBuilder<HttpConnectorSettings, RbCredentials> createConnectorFactoryBuilder() {

        final BiFunction<? extends HttpQuery<?>, RbCredentials, List<Header>>
                headerCreatorFunction = (query, credentials) -> {
            final List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader(AUTHORIZATION, credentials.getToken()));
            if (query.getClass() == DiffDataQuery.class) {
                headers.add(
                        new BasicHeader(ACCEPT, "application/vnd.reviewboard.org.diff.data+json"));
            }
            return headers;
        };

        return com.vmturbo.mediation.connector.common.HttpConnectorFactory.<HttpConnectorSettings, RbCredentials>jsonConnectorFactoryBuilder()
                .registerDefaultHeaderCreator(headerCreatorFunction)
                .registerResponseUnmarshaller(FileResponse.class,
                        new HttpResponseUnmarshaller<HttpConnectorSettings, FileResponse>() {
                            @Nonnull
                            @Override
                            public Optional<FileResponse> unmarshall(
                                    @Nullable InputStream responseContent,
                                    @Nonnull CloseableHttpResponse httpResponse,
                                    @Nonnull Class<? extends FileResponse> responseType,
                                    @Nonnull HttpConnectorSettings settings) throws IOException {
                                final String textResponse =
                                        IOUtils.toString(responseContent, StandardCharsets.UTF_8);
                                final FileResponse response =
                                        new FileResponse(textResponse);
                                return Optional.of(response);
                            }
                        })
                .registerStatusCodeToResponseProcessor(201, (query, response, settings) -> {
                    final FileResponse fileResponse =
                            new FileResponse(response.toString());
                    return fileResponse;
                });
    }
}
