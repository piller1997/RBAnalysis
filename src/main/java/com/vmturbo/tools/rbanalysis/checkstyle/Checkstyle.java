package com.vmturbo.tools.rbanalysis.checkstyle;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import com.vmturbo.tools.rbanalysis.Properties;

/**
 * Checkstyle analyzer.
 */
public final class Checkstyle {

    private final String configurationFile;

    /**
     * Creates {@link Checkstyle} instance.
     *
     * @param properties program's properties
     */
    public Checkstyle(@Nonnull Properties properties) {
        Objects.requireNonNull(properties);
        configurationFile = properties.getCheckstyleConfiguration();
    }

    /**
     * Runs checking on checkstyle.
     *
     * @param file File, that's checks on checkstyle.
     * @return List of checkstyle's errors.
     * @throws CheckstyleException Exception
     */
    public Map<Integer, List<AuditEvent>> runCheckstyle(@Nonnull File file)
            throws CheckstyleException {
        final List<File> filesToProcess = Collections.singletonList(Objects.requireNonNull(file));
        final CheckstyleListener listener = new CheckstyleListener();
        final Checker checker = new Checker();
        final Configuration config = ConfigurationLoader.loadConfiguration(configurationFile,
                new PropertiesExpander(System.getProperties()));

        final Map<Integer, List<AuditEvent>> auditEvents;

        try {
            final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
            checker.setModuleClassLoader(moduleClassLoader);
            checker.configure(config);
            checker.addListener(listener);
            checker.process(filesToProcess);
            auditEvents = listener.getErrors();
        } finally {
            checker.destroy();
        }
        return Collections.unmodifiableMap(auditEvents);
    }
}
