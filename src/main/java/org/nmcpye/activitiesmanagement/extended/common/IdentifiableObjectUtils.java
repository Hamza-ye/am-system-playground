package org.nmcpye.activitiesmanagement.extended.common;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.WeeklyAbstractPeriodType;
import org.nmcpye.activitiesmanagement.extended.common.calendar.Calendar;
import org.nmcpye.activitiesmanagement.extended.common.calendar.DateTimeUnit;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class IdentifiableObjectUtils {

    public static final String SEPARATOR = "-";
    public static final String SEPARATOR_JOIN = ", ";

    public static final DateTimeFormatter LONG_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateTimeFormatter MEDIUM_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Joins the names of the IdentifiableObjects in the given list and separates
     * them with {@link IdentifiableObjectUtils#SEPARATOR_JOIN} (a comma and a space).
     * Returns null if the given list is null or has no elements.
     *
     * @param objects the list of IdentifiableObjects.
     * @return the joined string.
     */
    public static String join(Collection<? extends IdentifiableObject> objects) {
        if (objects == null || objects.isEmpty()) {
            return null;
        }

        List<String> names = objects.stream().map(IdentifiableObject::getDisplayName).collect(Collectors.toList());
        return StringUtils.join(names, SEPARATOR_JOIN);
    }

    /**
     * Returns a list of uids for the given collection of IdentifiableObjects.
     *
     * @param objects the list of IdentifiableObjects.
     * @return a list of uids.
     */
    public static <T extends IdentifiableObject> List<String> getUids(Collection<T> objects) {
        return objects != null ? objects.stream().filter(o -> o != null).map(o -> o.getUid()).collect(Collectors.toList()) : null;
    }

    /**
     * Returns a list of codes for the given collection of IdentifiableObjects.
     *
     * @param objects the list of IdentifiableObjects.
     * @return a list of codes.
     */
    public static <T extends IdentifiableObject> List<String> getCodes(Collection<T> objects) {
        return objects != null ? objects.stream().map(o -> o.getCode()).collect(Collectors.toList()) : null;
    }

    /**
     * Returns a list of internal identifiers for the given collection of IdentifiableObjects.
     *
     * @param objects the list of IdentifiableObjects.
     * @return a list of identifiers.
     */
    public static <T extends IdentifiableObject> List<Long> getIdentifiers(Collection<T> objects) {
        return objects != null ? objects.stream().map(o -> o.getId()).collect(Collectors.toList()) : null;
    }

    /**
     * Returns a map from internal identifiers to IdentifiableObjects,
     * for the given collection of IdentifiableObjects.
     *
     * @param objects the collection of IdentifiableObjects
     * @return a map from the object internal identifiers to the objects
     */
    public static <T extends IdentifiableObject> Map<Long, T> getIdentifierMap(Collection<T> objects) {
        Map<Long, T> map = new HashMap<>();

        for (T object : objects) {
            map.put(object.getId(), object);
        }

        return map;
    }

    /**
     * Returns a list of calendar specific period identifiers for the given collection of
     * periods and calendar.
     *
     * @param periods  the list of periods.
     * @param calendar the calendar to use for generation of iso periods.
     * @return a list of iso period identifiers.
     */
    public static <T extends IdentifiableObject> List<String> getLocalPeriodIdentifiers(Collection<T> periods, Calendar calendar) {
        List<String> localIdentifiers = new ArrayList<>();

        for (IdentifiableObject object : periods) {
            Period period = (Period) object;
            DateTimeUnit dateTimeUnit = calendar.fromIso(period.getStartDate());
            localIdentifiers.add(period.getPeriodType().getIsoDate(dateTimeUnit));
        }

        return localIdentifiers;
    }

    /**
     * Returns a local period identifier for a specific period / calendar.
     *
     * @param period   the list of periods.
     * @param calendar the calendar to use for generation of iso periods.
     * @return Period identifier based on given calendar
     */
    public static String getLocalPeriodIdentifier(Period period, Calendar calendar) {
        if (calendar.isIso8601()) {
            return period.getIsoDate();
        }

        return period.getPeriodType().getIsoDate(calendar.fromIso(period.getStartDate()));
    }

    /**
     * Returns the {@link Period} of the argument period type which corresponds to the argument period.
     * The frequency order of the given period type must greater than or equal to the period type
     * of the given period (represent "longer" periods). Weeks are converted to "longer" periods by
     * determining which period contains at least 4 days of the week.
     * <p>
     * As an example, providing
     * {@code Quarter 1, 2017} and {@code Yearly} as arguments will return the yearly
     * period {@code 2017}.
     *
     * @param period the period.
     * @param periodType the period type of the period to return.
     * @param calendar the calendar to use when calculating the period.
     * @return a period.
     */
    public static Period getPeriodByPeriodType(Period period, PeriodType periodType, Calendar calendar) {
        Assert.isTrue(
            periodType.getFrequencyOrder() >= period.getPeriodType().getFrequencyOrder(),
            "Frequency order of period type must be greater than or equal to period"
        );

        Date date = period.getStartDate();

        if (WeeklyAbstractPeriodType.class.isAssignableFrom(period.getPeriodType().getClass())) {
            date = new DateTime(date.getTime()).plusDays(3).toDate();
        }

        return periodType.createPeriod(date, calendar);
    }

    /**
     * Filters the given list of IdentifiableObjects based on the given key.
     *
     * @param identifiableObjects the list of IdentifiableObjects.
     * @param key                 the key.
     * @param ignoreCase          indicates whether to ignore case when filtering.
     * @return a filtered list of IdentifiableObjects.
     */
    public static <T extends IdentifiableObject> List<T> filterNameByKey(List<T> identifiableObjects, String key, boolean ignoreCase) {
        List<T> objects = new ArrayList<>();
        ListIterator<T> iterator = identifiableObjects.listIterator();

        if (ignoreCase) {
            key = key.toLowerCase();
        }

        while (iterator.hasNext()) {
            T object = iterator.next();
            String name = ignoreCase ? object.getDisplayName().toLowerCase() : object.getDisplayName();

            if (name.indexOf(key) != -1) {
                objects.add(object);
            }
        }

        return objects;
    }

    /**
     * Removes duplicates from the given list while maintaining the order.
     *
     * @param list the list.
     */
    public static <T extends IdentifiableObject> List<T> removeDuplicates(List<T> list) {
        final List<T> temp = new ArrayList<>(list);
        list.clear();

        for (T object : temp) {
            if (!list.contains(object)) {
                list.add(object);
            }
        }

        return list;
    }

    /**
     * Generates a tag reflecting the date of when the most recently updated
     * IdentifiableObject in the given collection was modified.
     *
     * @param objects the collection of IdentifiableObjects.
     * @return a string tag.
     */
    public static <T extends IdentifiableObject> String getLastUpdatedTag(Collection<T> objects) {
        Date latest = null;

        if (objects != null) {
            for (IdentifiableObject object : objects) {
                if (object != null && object.getLastUpdated() != null && (latest == null || object.getLastUpdated().after(latest))) {
                    latest = object.getLastUpdated();
                }
            }
        }

        return latest != null && objects != null ? objects.size() + SEPARATOR + LONG_DATE_FORMAT.print(new DateTime(latest)) : null;
    }

    /**
     * Generates a tag reflecting the date of when the object was last updated.
     *
     * @param object the identifiable object.
     * @return a string tag.
     */
    public static String getLastUpdatedTag(IdentifiableObject object) {
        return object != null ? LONG_DATE_FORMAT.print(new DateTime(object.getLastUpdated())) : null;
    }

    /**
     * Returns a mapping between the uid and the display name of the given
     * identifiable objects.
     *
     * @param objects the identifiable objects.
     * @return mapping between the uid and the display name of the given objects.
     */
    public static Map<String, String> getUidNameMap(Collection<? extends IdentifiableObject> objects) {
        return objects.stream().collect(Collectors.toMap(IdentifiableObject::getUid, IdentifiableObject::getDisplayName));
    }

    /**
     * Returns a mapping between the uid and the property defined by the given
     * identifiable property for the given identifiable objects.
     *
     * @param objects the identifiable objects.
     * @param property the identifiable property.
     * @return a mapping between uid and property.
     */
    public static Map<String, String> getUidPropertyMap(Collection<? extends IdentifiableObject> objects, IdentifiableProperty property) {
        Map<String, String> map = Maps.newHashMap();

        objects.forEach(obj -> map.put(obj.getUid(), obj.getPropertyValue(IdScheme.from(property))));

        return map;
    }

    /**
     * Returns a mapping between the uid and the name of the given identifiable
     * objects.
     *
     * @param objects the identifiable objects.
     * @return mapping between the uid and the name of the given objects.
     */
    public static <T extends IdentifiableObject> Map<String, T> getUidObjectMap(Collection<T> objects) {
        return objects != null ? Maps.uniqueIndex(objects, T::getUid) : Maps.newHashMap();
    }

    /**
     * Returns a map of the identifiable property specified by the given id scheme
     * and the corresponding object.
     *
     * @param objects the objects.
     * @param idScheme the id scheme.
     * @return a map.
     */
    public static <T extends IdentifiableObject> Map<String, T> getIdMap(List<T> objects, IdScheme idScheme) {
        Map<String, T> map = new HashMap<>();

        for (T object : objects) {
            String value = object.getPropertyValue(idScheme);

            if (value != null) {
                map.put(value, object);
            }
        }

        return map;
    }

    /**
     * @param object Object to get display name for
     * @return A usable display name
     */
    public static String getDisplayName(Object object) {
        if (object == null) {
            return "[ object is null ]";
        } else if (IdentifiableObject.class.isInstance(object)) {
            IdentifiableObject identifiableObject = (IdentifiableObject) object;

            if (identifiableObject.getDisplayName() != null && !identifiableObject.getDisplayName().isEmpty()) {
                return identifiableObject.getDisplayName();
            } else if (identifiableObject.getUid() != null && !identifiableObject.getUid().isEmpty()) {
                return identifiableObject.getUid();
            } else if (identifiableObject.getCode() != null && !identifiableObject.getCode().isEmpty()) {
                return identifiableObject.getCode();
            }
        }

        return object.getClass().getName();
    }

    /**
     * Returns an ID for given object based on given idScheme. However, does not work for Attribute idScheme.
     * Attribute idScheme has to have special treatment in the client code.
     * @param object An identifiable object
     * @param idScheme An idScheme defining what property should be used as an ID
     * @param <T>
     * @return Returns an ID for given object based on given idScheme
     */
    public static <T extends BaseIdentifiableObject> String getIdentifierBasedOnIdScheme(T object, IdScheme idScheme) {
        //idScheme with IdentifiableProperty.ATTRIBUTE has to be specially treated in the code dealing with Attribute IDs
        if (idScheme.isNull() || idScheme.is(IdentifiableProperty.UID)) {
            return object.getUid();
        } else if (idScheme.is(IdentifiableProperty.CODE)) {
            return object.getCode();
        } else if (idScheme.is(IdentifiableProperty.NAME)) {
            return object.getName();
        } else if (idScheme.is(IdentifiableProperty.ID) && object.getId() > 0) {
            return String.valueOf(object.getId());
        }

        return null;
    }
}
