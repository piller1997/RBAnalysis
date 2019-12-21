package com.vmturbo.tools.rbanalysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Getter property from file.
 */
public final class Properties {
    private final String propertyFile;
    private final Map<String, String> propertiesMap;
    private final List<String> listOfRepositories;
    private static final String messageAboutError =
            "Property %s is absent in configuration file %s!";

    /**
     * Creates {@link Properties} instance.
     *
     * @param propertyFile path to property file
     * @throws IOException properties file failed exception.
     */
    public Properties(final String propertyFile) throws IOException {
        this.propertyFile = propertyFile;
        final Map<String, String> propertiesMap = new HashMap<>();
        final List<String> listOfRepositories = new ArrayList<>();
        try (FileInputStream propertyFIS = new FileInputStream(this.propertyFile)) {
            final java.util.Properties properties = new java.util.Properties();
            properties.load(propertyFIS);
            final Set<String> set = properties.stringPropertyNames();
            for (String property : set) {
                if (!property.startsWith("repositoryTitle.")) {
                    propertiesMap.put(property, properties.getProperty(property));
                } else {
                    listOfRepositories.add(properties.getProperty(property));
                }
            }
        }
        this.propertiesMap = Collections.unmodifiableMap(propertiesMap);
        this.listOfRepositories = Collections.unmodifiableList(listOfRepositories);
    }

    /**
     * Gets port for connection to site.
     *
     * @return port.
     * @throws IllegalArgumentException Throws, when port connection is illegal.
     */
    public int getPortConnection() throws IllegalArgumentException {
        final String fieldName = "port";
        final String port = propertiesMap.get(fieldName);
        final int result;
        try {
            result = Integer.parseInt(Objects.requireNonNull(port,
                    () -> String.format(messageAboutError, fieldName, propertyFile)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Specified port is not correct!");
        }
        return result;
    }

    /**
     * Gets URL site for connection.
     *
     * @return URL site.
     */
    @Nonnull
    public String getSite() {
        final String fieldName = "site";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets API prefix.
     *
     * @return API prefix.
     */
    @Nonnull
    public String getPrefix() {
        final String fieldName = "prefix";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets token to connecting to site.
     *
     * @return token.
     */
    @Nonnull
    public String getToken() {
        final String fieldName = "token";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets names of repository that contain generated file.
     *
     * @return names of repository.
     */
    @Nonnull
    public List<String> getRepositoryWithGeneratedFile() {
        return listOfRepositories;
    }

    /**
     * Gets url to connecting to database.
     *
     * @return url.
     */
    @Nonnull
    public String getUrlDatabase() {
        final String fieldName = "urlDatabase";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets count reviews for analysis.
     *
     * @return count.
     */
    @Nonnull
    public String getCountReviewsForAnalysis() {
        final String fieldName = "countReviewsForAnalysis";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets url to checkstyle configuration.
     *
     * @return url to checkstyle configuration.
     */
    @Nonnull
    public String getCheckstyleConfiguration() {
        final String fieldName = "checkstyleConfiguration";
        return Objects.requireNonNull(propertiesMap.get(fieldName),
                () -> String.format(messageAboutError, fieldName, propertyFile));
    }

    /**
     * Gets size of thread pool.
     *
     * @return size of thread pool.
     */
    public int getThreadPoolSize() {
        final String fieldName = "threadPoolSize";
        final String port = propertiesMap.get(fieldName);
        final int result;
        try {
            result = Integer.parseInt(Objects.requireNonNull(port,
                    () -> String.format(messageAboutError, fieldName, propertyFile)));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Specified thread pool size is not correct!");
        } catch (NullPointerException e) {
            throw new NullPointerException("Thread pool size isn't specified.");
        }
        return result;
    }
}
