package com.vmturbo.tools.rbanalysis.queries;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.vmturbo.mediation.connector.common.HttpBody;
import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.mediation.connector.common.Response;
import com.vmturbo.mediation.connector.common.http.AbstractHttpBodyAwareQuery;
import com.vmturbo.mediation.connector.common.http.HttpParameter;
import com.vmturbo.tools.rbanalysis.Properties;

/**
 * Executes GET HTTP requests to the RB instance.
 *
 * @param <R> Type of response.
 */
@Immutable
public class GetQuery<R extends Response> extends
        AbstractHttpBodyAwareQuery<R, HttpBody> {
    private String commandPath;
    private HttpMethodType methodType;
    private Map<HttpParameter, String> parameters;

    /**
     * Creates {@link GetQuery} instance.
     *
     * @param methodType HTTP method type.
     * @param commandPath command path.
     * @param body HTTP body.
     * @param responseType Response type.
     * @param parameters Map of parameters.
     * @param properties program's properties
     */
    public GetQuery(@Nonnull HttpMethodType methodType, @Nonnull String commandPath,
            HttpBody body, @Nonnull Class<R> responseType,
            @Nonnull Map<HttpParameter, String> parameters,
            @Nonnull Properties properties) {
        super(methodType, properties.getPrefix() + commandPath, body, responseType);
        Objects.requireNonNull(commandPath);
        this.commandPath = super.getCommandPath();
        this.methodType = Objects.requireNonNull(methodType);
        this.parameters = Objects.requireNonNull(parameters);
    }

    /**
     * Get query's method type.
     *
     * @return method type.
     */
    @Nonnull
    public HttpMethodType getMethodType() {
        return methodType;
    }

    /**
     * Get query's command path.
     *
     * @return command path.
     */
    @Nonnull
    public String getCommandPath() {
        return commandPath;
    }

    /**
     * Get query's parameters.
     *
     * @return parameters.
     */
    @Nonnull
    public Map<HttpParameter, String> getParameters() {
        return parameters;
    }
}
