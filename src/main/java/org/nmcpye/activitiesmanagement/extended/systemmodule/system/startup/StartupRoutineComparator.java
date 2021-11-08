package org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup;

import java.util.Comparator;

/**
 * Compares StartupRoutines based on their runlevel values.
 *
 */
public class StartupRoutineComparator implements Comparator<StartupRoutine> {

    @Override
    public int compare(StartupRoutine routineA, StartupRoutine routineB) {
        return routineA.getRunlevel() - routineB.getRunlevel();
    }
}
