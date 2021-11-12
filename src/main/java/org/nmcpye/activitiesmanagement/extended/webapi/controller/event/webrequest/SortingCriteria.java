package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.webrequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Sorting parameters
 *
 */
public interface SortingCriteria
{

    /**
     * order params
     */
    List<OrderCriteria> getOrder();

    /**
     * Implementors should return a list of fields on which it is allowed to
     * perform ordering Defaults to empty list which means all fields are
     * allowed for ordering
     */
    default List<String> getAllowedOrderingFields()
    {
        return Collections.emptyList();
    }

    /**
     * By default it does not translate any field
     *
     * @return
     */
    default Optional<String> translateField(String dtoFieldName, boolean isLegacy)
    {
        return Optional.ofNullable( dtoFieldName );
    }

}
