package org.nmcpye.activitiesmanagement.extended.scheduling;

import java.io.Serializable;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;

/**
 * Interface for job specific parameters. Serializable so that we can store the object in the database.
 *
 */
public interface JobParameters extends Serializable {
    Optional<ErrorReport> validate();
}
