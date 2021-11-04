package org.nmcpye.activitiesmanagement.extended.system;

import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.repository.PeriodTypeRepository;
import org.nmcpye.activitiesmanagement.extended.systemmodule.startup.AbstractStartupRoutine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class PeriodTypePopulator extends AbstractStartupRoutine {

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(PeriodTypePopulator.class);

    private final PeriodTypeRepository periodTypeRepository;

    public PeriodTypePopulator(PeriodTypeRepository periodTypeRepository) {
        checkNotNull(periodTypeRepository);

        this.periodTypeRepository = periodTypeRepository;
        this.setRunlevel(3);
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        List<PeriodType> types = PeriodType.getAvailablePeriodTypes();

        Collection<PeriodType> storedTypes = periodTypeRepository.findAll();

        types.removeAll(storedTypes);

        // ---------------------------------------------------------------------
        // Populate missing
        // ---------------------------------------------------------------------

        try {
            types.forEach(
                type -> {
                    periodTypeRepository.save(type);
                    log.debug("Added PeriodType: " + type.getName());
                }
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        types.forEach(type -> periodTypeRepository.findById(type.getId()));
    }
}
