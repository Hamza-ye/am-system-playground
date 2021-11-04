package org.nmcpye.activitiesmanagement.extended.scheduling;

import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;

import java.io.Serializable;
import java.util.Optional;

/**
 * Interface for job specific parameters. Serializable so that we can store the object in the database.
 *
 */
public interface JobParameters extends Serializable {
    Optional<ErrorReport> validate();
}
