package com.vmturbo.tools.rbanalysis.checkstyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Checkstyle listener for saving checkstyle errors.
 */
public class CheckstyleListener extends AutomaticBean implements AuditListener {
    private final Logger logger = LogManager.getLogger(getClass());
    private final Map<Integer, List<AuditEvent>> errors = new HashMap<>();

    /**
     * Gets list of errors after checking on checkstyle.
     *
     * @return list of errors
     */
    public Map<Integer, List<AuditEvent>> getErrors() {
        return errors;
    }

    @Override
    public void auditStarted(AuditEvent auditEvent) {
    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
    }

    @Override
    public void addError(AuditEvent auditEvent) {
        errors.computeIfAbsent(auditEvent.getLine(), k -> new ArrayList<>()).add(auditEvent);
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable throwable) {
        logger.warn(throwable.getMessage());
    }

    @Override
    protected void finishLocalSetup() throws CheckstyleException {
    }
}
