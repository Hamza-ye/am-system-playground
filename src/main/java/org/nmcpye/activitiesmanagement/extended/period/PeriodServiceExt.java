package org.nmcpye.activitiesmanagement.extended.period;

import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.nmcpye.activitiesmanagement.extended.i18n.I18nFormat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PeriodServiceExt {
    String ID = PeriodServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    /**
     * Adds a Period.
     *
     * @param period the Period to add.
     * @return a generated unique id of the added Period.
     */
    Long addPeriod(Period period);

    /**
     * Deletes a Period.
     *
     * @param period the Period to delete.
     */
    void deletePeriod(Period period);

    /**
     * Returns a Period.
     *
     * @param id the id of the Period to return.
     * @return the Period with the given id, or null if no match.
     */
    Period getPeriod(Long id);

    /**
     * Gets the Period with the given ISO period identifier.
     *
     * @param isoPeriod the ISO period identifier.
     * @return the Period with the given ISO period identifier.
     */
    Period getPeriod(String isoPeriod);

    /**
     * Returns a Period.
     *
     * @param startDate the start date of the Period.
     * @param endDate the end date of the Period.
     * @param periodType the PeriodType of the Period
     * @return the Period matching the dates and periodtype, or null if no match.
     */
    Period getPeriod(Date startDate, Date endDate, PeriodType periodType);

    /**
     * Returns a Period.
     *
     * @param startDate the start date of the Period.
     * @param endDate the end date of the Period.
     * @param periodType the PeriodType of the Period
     * @return the Period matching the dates and periodtype, or null if no match.
     */
    Period getPeriodFromDates(Date startDate, Date endDate, PeriodType periodType);

    /**
     * Returns all persisted Periods.
     *
     * @return all persisted Periods.
     */
    List<Period> getAllPeriods();

    /**
     * Returns all Periods with start date after or equal the specified start
     * date and end date before or equal the specified end date.
     *
     * @param startDate the ultimate start date.
     * @param endDate the ultimate end date.
     * @return a list of all Periods with start date after or equal the
     *         specified start date and end date before or equal the specified
     *         end date, or an empty list if no Periods match.
     */
    List<Period> getPeriodsBetweenDates(Date startDate, Date endDate);

    /**
     * Returns all Periods of the specified PeriodType with start date after or
     * equal the specified start date and end date before or equal the specified
     * end date.
     *
     * @param periodType the PeriodType.
     * @param startDate the ultimate start date.
     * @param endDate the ultimate end date.
     * @return a list of all Periods with start date after or equal the
     *         specified start date and end date before or equal the specified
     *         end date, or an empty list if no Periods match.
     */
    List<Period> getPeriodsBetweenDates(PeriodType periodType, Date startDate, Date endDate);

    /**
     * Returns all Periods with either i) start and end date between the given
     * start and end date or ii) start date before the given start date and end
     * date after the given end date.
     *
     * @param startDate the start date.
     * @param endDate the end date.
     * @return a list of Periods.
     */
    List<Period> getPeriodsBetweenOrSpanningDates(Date startDate, Date endDate);

    /**
     * Returns all Intersecting Periods between the startDate and endDate based on PeriodType
     * For example if the startDate is 2007-05-01 and endDate is 2007-08-01 and periodType is Quarterly
     * then it returns the periods for Q2,Q3
     *
     * @param periodType is the ultimate period type
     * @param startDate is intercepting startDate
     * @param endDate is intercepting endDate
     * @return a list of Periods.
     */
    List<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate);

    /**
     * Returns Periods where at least one its days are between the given start date and end date.
     *
     * @param startDate the start date.
     * @param endDate the end date.
     * @return Periods where at least one its days are between the given start date and end date.
     */
    List<Period> getIntersectingPeriods(Date startDate, Date endDate);

    /**
     * Returns Periods where at least one its days are between each of the Periods
     * start date and end date in the given collection.
     *
     * @param periods the collection of Periods.
     * @return a list of Periods.
     */
    List<Period> getIntersectionPeriods(Collection<Period> periods);

    /**
     * Returns all Periods from the given collection of Periods which span the border of either the
     * start date OR end date of the given Period.
     *
     * @param period the base Period.
     * @param periods the collection of Periods.
     * @return all Periods from the given list of Periods which span the border of either the
     *         start date or end date of the given Period.
     */
    List<Period> getBoundaryPeriods(Period period, Collection<Period> periods);

    /**
     * Returns all Periods from the given collection of Periods which are completely within the
     * span of the of the given Period.
     *
     * @param period the base Period.
     * @param periods the collection of Periods.
     * @return all Periods from the given collection of Periods which are completely within the
     *         span of the of the given Period.
     */
    List<Period> getInclusivePeriods(Period period, Collection<Period> periods);

    /**
     * Returns all Periods with a given PeriodType.
     *
     * @param periodType the PeriodType of the Periods to return.
     * @return all Periods with the given PeriodType, or an empty list if
     *         no Periods match.
     */
    List<Period> getPeriodsByPeriodType(PeriodType periodType);

    /**
     * Enforces that each Period in the given collection is loaded in the current
     * session. Persists the Period if it does not exist.
     *
     * @param periods the list of Periods.
     * @return the list of Periods.
     */
    List<Period> reloadPeriods(List<Period> periods);

    /**
     * Returns historyLength number of Periods chronologically ending with lastPeriod.
     *
     * @param lastPeriod the last Period in the provided collection.
     * @param historyLength the number of Periods in the provided collection.
     * @return a collection of Periods.
     */
    List<Period> getPeriods(Period lastPeriod, int historyLength);

    /**
     * Populates the name property of Period with the formatted name for the
     * Periods in the given collection.
     *
     * @param periods the collection of Periods.
     * @param format the I18nFormat.
     * @return a collection of Periods.
     */
    Collection<Period> namePeriods(Collection<Period> periods, I18nFormat format);

    /**
     * Checks if the given Period is associated with the current session. If not,
     * replaces the Period with a Period associated with the current session.
     * Persists the Period if not already persisted.
     *
     * @param period the Period to reload.
     * @return a Period.
     */
    Period reloadPeriod(Period period);

    Period reloadIsoPeriodInStatelessSession(String isoPeriod);

    /**
     * Retrieves the period with the given ISO period identifier. Reloads the
     * period in the session if found.
     *
     * @param isoPeriod the ISO period identifier.
     * @return a Period.
     */
    Period reloadIsoPeriod(String isoPeriod);

    /**
     * Retrieves the period with the given ISO period identifiers. Reloads the
     * periods in the session if found.
     *
     * @param isoPeriods the list of ISO period identifiers.
     * @return a list of Periods.
     */
    List<Period> reloadIsoPeriods(List<String> isoPeriods);

    /**
     * Returns a PeriodHierarchy instance.
     *
     * @param periods the Periods to include in the PeriodHierarchy.
     * @return a PeriodHierarchy instance.
     */
    PeriodHierarchy getPeriodHierarchy(Collection<Period> periods);

    /**
     * Returns how many days into period date is. If date is before period.startDate,
     * returns 0. If date is after period.endDate, return last day of period.
     *
     * @param period the period.
     * @param date the date.
     * @return the day in period.
     */
    int getDayInPeriod(Period period, Date date);

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    /**
     * Returns a PeriodType.
     *
     * @param id the id of the PeriodType to return.
     * @return the PeriodType with the given id, or null if no match.
     */
    PeriodType getPeriodType(int id);

    /**
     * Returns all PeriodTypes.
     *
     * @return a list of all PeriodTypes, or an empty list if there are no
     *         PeriodTypes. The PeriodTypes have a natural order.
     */
    List<PeriodType> getAllPeriodTypes();

    /**
     * Returns a PeriodType with a given name.
     *
     * @param name the name of the PeriodType to return.
     * @return the PeriodType with the given name, or null if no match.
     */
    PeriodType getPeriodTypeByName(String name);

    /**
     * Returns a PeriodType represented by the given Class.
     *
     * @param periodType the Class type of the PeriodType.
     * @return a PeriodType instance.
     */
    PeriodType getPeriodTypeByClass(Class<? extends PeriodType> periodType);

    /**
     * Checks if the given periodType is associated with the current session and loads
     * it if not. Null is returned if the period does not exist.
     *
     * @param periodType the Period to reload.
     * @return a Period.
     */
    PeriodType reloadPeriodType(PeriodType periodType);

    // -------------------------------------------------------------------------
    // RelativePeriods
    // -------------------------------------------------------------------------

    /**
     * Deletes a RelativePeriods instance.
     *
     * @param relativePeriods the RelativePeriods instance.
     */
    void deleteRelativePeriods(RelativePeriods relativePeriods);

    //////////////////////////////////////////////////////
    //
    // JHipster Methods
    //
    //////////////////////////////////////////////////////
    /**
     * Save a period.
     *
     * @param period the entity to save.
     * @return the persisted entity.
     */
    Period save(Period period);

    /**
     * Partially updates a period.
     *
     * @param period the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Period> partialUpdate(Period period);

    /**
     * Get all the periods.
     *
     * @return the list of entities.
     */
    List<Period> findAll();

    /**
     * Get the "id" period.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Period> findOne(Long id);

    /**
     * Delete the "id" period.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
