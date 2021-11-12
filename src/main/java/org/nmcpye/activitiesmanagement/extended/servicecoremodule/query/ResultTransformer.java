package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import java.util.List;

@FunctionalInterface
public interface ResultTransformer<T> {
    List<T> transform(List<T> result);
}
