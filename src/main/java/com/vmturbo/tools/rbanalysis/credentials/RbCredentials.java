package com.vmturbo.tools.rbanalysis.credentials;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.credentials.TargetSecurePortAwareCredentials;
import com.vmturbo.mediation.connector.common.credentials.TokenAwareCredentials;
import com.vmturbo.tools.rbanalysis.Properties;

/**
 * Credentials for connecting to Review Board.
 */
public class RbCredentials implements TargetSecurePortAwareCredentials, TokenAwareCredentials {

    @Nonnull
    private final Properties properties;

    /**
     * Creates {@link RbCredentials} instance.
     *
     * @param properties properties of program
     */
    public RbCredentials(@Nonnull Properties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    /**
     * Gets address of site.
     *
     * @return address.
     */
    @Nonnull
    @Override
    public String getNameOrAddress() {
        return properties.getSite();
    }

    /**
     * Gets port of connection.
     *
     * @return port.
     */
    @Nonnull
    @Override
    public int getPort() {
        return properties.getPortConnection();
    }

    /**
     * Gets is HTTPS connection?
     *
     * @return
     */
    @Nonnull
    @Override
    public boolean isSecure() {
        return true;
    }

    /**
     * Gets token for connecting.
     *
     * @return token + [token]
     */
    @Nonnull
    @Override
    public String getToken() {
        return "token " + properties.getToken();
    }
}
