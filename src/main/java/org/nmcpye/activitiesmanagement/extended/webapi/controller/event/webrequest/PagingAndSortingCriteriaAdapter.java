package org.nmcpye.activitiesmanagement.extended.webapi.controller.event.webrequest;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

/**
 * simplest implementation of PagingCriteria and SortingCriteria
 */
public abstract class PagingAndSortingCriteriaAdapter implements PagingCriteria, SortingCriteria {

    private final Logger log = LoggerFactory.getLogger(PagingAndSortingCriteriaAdapter.class);
    /**
     * Page number to return.
     */
    private Integer page;

    /**
     * Page size.
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * Indicates whether to include the total number of pages in the paging
     * response.
     */
    private boolean totalPages;

    /**
     * Indicates whether paging should be skipped.
     */
    private Boolean skipPaging;

    /**
     * order params
     */
    private List<OrderCriteria> order;

    /**
     * TODO: legacy flag can be removed when new tracker will have it's own
     * services. All new tracker export Criteria class extending this, will
     * override isLegacy returning false, so that it's true only for older ones.
     */
    private boolean isLegacy = true;

    private final Function<List<OrderCriteria>, List<OrderCriteria>> dtoNameToDatabaseNameTranslator = orderCriteria -> CollectionUtils
        .emptyIfNull(orderCriteria)
        .stream()
        .filter(Objects::nonNull)
        .map(oc -> OrderCriteria.of(
            translateField(oc.getField(), isLegacy())
                .orElse(oc.getField()),
            oc.getDirection()))
        .collect(Collectors.toList());

    protected PagingAndSortingCriteriaAdapter() {
    }

    public boolean isPagingRequest() {
        return !isSkipPaging();
    }

    public boolean isSkipPaging() {
        return Optional.ofNullable(skipPaging)
            .orElse(false);
    }

    @Override
    public List<OrderCriteria> getOrder() {
        if (getAllowedOrderingFields().isEmpty()) {
            return dtoNameToDatabaseNameTranslator.apply(order);
        }

        Map<Boolean, List<OrderCriteria>> orderCriteriaPartitionedByAllowance = CollectionUtils.emptyIfNull(order)
            .stream()
            .collect(
                partitioningBy(this::isAllowed));

        CollectionUtils.emptyIfNull(orderCriteriaPartitionedByAllowance.get(false))
            .forEach(disallowedOrderFieldConsumer());

        return dtoNameToDatabaseNameTranslator.apply(orderCriteriaPartitionedByAllowance.get(true));
    }

    private boolean isAllowed(OrderCriteria orderCriteria) {
        return getAllowedOrderingFields().contains(orderCriteria.getField());
    }

    protected Consumer<OrderCriteria> disallowedOrderFieldConsumer() {
        return orderCriteria -> log.warn("Ordering by " + orderCriteria.getField() + " is not supported");
    }

    public boolean isSortingRequest() {
        return !CollectionUtils.emptyIfNull(getOrder()).isEmpty();
    }

    public interface EntityNameSupplier {
        String getEntityName();
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isTotalPages() {
        return totalPages;
    }

    public Boolean getSkipPaging() {
        return skipPaging;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public Function<List<OrderCriteria>, List<OrderCriteria>> getDtoNameToDatabaseNameTranslator() {
        return dtoNameToDatabaseNameTranslator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PagingAndSortingCriteriaAdapter that = (PagingAndSortingCriteriaAdapter) o;

        if (totalPages != that.totalPages) return false;
        if (isLegacy != that.isLegacy) return false;
        if (page != null ? !page.equals(that.page) : that.page != null) return false;
        if (pageSize != null ? !pageSize.equals(that.pageSize) : that.pageSize != null) return false;
        if (skipPaging != null ? !skipPaging.equals(that.skipPaging) : that.skipPaging != null) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return dtoNameToDatabaseNameTranslator != null ? dtoNameToDatabaseNameTranslator.equals(that.dtoNameToDatabaseNameTranslator) : that.dtoNameToDatabaseNameTranslator == null;
    }

    @Override
    public int hashCode() {
        int result = page != null ? page.hashCode() : 0;
        result = 31 * result + (pageSize != null ? pageSize.hashCode() : 0);
        result = 31 * result + (totalPages ? 1 : 0);
        result = 31 * result + (skipPaging != null ? skipPaging.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (isLegacy ? 1 : 0);
        result = 31 * result + (dtoNameToDatabaseNameTranslator != null ? dtoNameToDatabaseNameTranslator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PagingAndSortingCriteriaAdapter{" +
            "page=" + page +
            ", pageSize=" + pageSize +
            ", totalPages=" + totalPages +
            ", skipPaging=" + skipPaging +
            ", order=" + order +
            ", isLegacy=" + isLegacy +
            ", dtoNameToDatabaseNameTranslator=" + dtoNameToDatabaseNameTranslator +
            '}';
    }
}
