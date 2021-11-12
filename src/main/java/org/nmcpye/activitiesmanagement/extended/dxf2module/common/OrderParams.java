package org.nmcpye.activitiesmanagement.extended.dxf2module.common;

import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Order;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderParams {
    private Set<String> order = new HashSet<>();

    public OrderParams() {
    }

    public OrderParams(Set<String> order) {
        this.order = order;
    }

    public void setOrder(Set<String> order) {
        this.order = order;
    }

    public List<Order> getOrders(Schema schema) {
        return QueryUtils.convertOrderStrings(order, schema);
    }

    public Set<String> getOrders() {
        return order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final OrderParams other = (OrderParams) obj;

        return Objects.equals(this.order, other.order);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("order", order)
            .toString();
    }
}
