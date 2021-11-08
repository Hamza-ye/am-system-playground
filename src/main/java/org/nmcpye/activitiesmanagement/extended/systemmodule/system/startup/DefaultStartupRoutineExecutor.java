package org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Default implementation of StartupRoutineExecutor. The execute method will
 * execute the added StartupRoutines ordered by their run levels.
 *
 */
@Component("org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup.StartupRoutineExecutor")
public class DefaultStartupRoutineExecutor implements StartupRoutineExecutor {

    private final Logger log = LoggerFactory.getLogger(DefaultStartupRoutineExecutor.class);

    private static final String TRUE = "true";
    private static final String SKIP_PROP = "am.skip.startup";

    private final List<StartupRoutine> startupRoutines;

    @Autowired(required = false)
    public DefaultStartupRoutineExecutor(List<StartupRoutine> startupRoutines) {
        this.startupRoutines = startupRoutines;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void execute() throws Exception {
        execute(false);
    }

    @Override
    public void executeForTesting() throws Exception {
        execute(true);
    }

    private void execute(boolean testing) throws Exception {
        if (startupRoutines == null || startupRoutines.isEmpty()) {
            log.debug("No startup routines found");
            return;
        }

        if (TRUE.equalsIgnoreCase(System.getProperty(SKIP_PROP))) {
            log.info("Skipping startup routines, system property " + SKIP_PROP + " is true");
            return;
        }

        //        if ( config.isReadOnlyMode() )
        //        {
        //            log.info( "Skipping startup routines, read-only mode is enabled" );
        //            return;
        //        }

        startupRoutines.sort(new StartupRoutineComparator());

        int total = startupRoutines.size();
        int index = 1;

        for (StartupRoutine routine : startupRoutines) {
            if (!(testing && routine.skipInTests())) {
                log.info(
                    "Executing startup routine [" +
                    index +
                    " of " +
                    total +
                    ", runlevel " +
                    routine.getRunlevel() +
                    "]: " +
                    routine.getName()
                );

                routine.execute();

                ++index;
            }
        }

        log.info("All startup routines done");
    }
}
