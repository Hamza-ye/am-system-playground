package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.PagerUtils;
import org.nmcpye.activitiesmanagement.extended.hibernate.HibernateProxyUtils;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.ReflectionUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Component("org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.InMemoryQueryEngine")
public class InMemoryQueryEngine<T extends IdentifiableObject>
    implements QueryEngine<T> {
    private final SchemaService schemaService;

//    private final AclService aclService;

    private final UserService userService;

    public InMemoryQueryEngine(SchemaService schemaService, UserService userService) {
        this.schemaService = schemaService;
        this.userService = userService;
    }

    @Override
    public List<T> query(Query query) {
        validateQuery(query);
        List<T> list = runQuery(query);
        list = runSorter(query, list);

        return query.isSkipPaging() ? list
            : PagerUtils.pageCollection(list, query.getFirstResult(), query.getMaxResults());
    }

    @Override
    public long count(Query query) {
        validateQuery(query);
        List<T> list = runQuery(query);

        return list.size();
    }

    private void validateQuery(Query query) {
        if (query.getUser() == null) {
            query.setUser(userService.getUserWithAuthorities().orElse(null));
        }

        if (query.getSchema() == null) {
            throw new QueryException("Invalid Query object, does not contain Schema");
        }

        if (query.getObjects() == null) {
            throw new QueryException("InMemoryQueryEngine requires an existing object list to work on.");
        }
    }

    @SuppressWarnings("unchecked")
    private List<T> runQuery(Query query) {
        return query.getObjects().stream()
            .filter(object -> test(query, (T) object))
            .map(object -> (T) object)
            .collect(Collectors.toList());
    }

    private List<T> runSorter(Query query, List<T> objects) {
        List<T> sorted = new ArrayList<>(objects);

        sorted.sort((o1, o2) -> {
            for (Order order : query.getOrders()) {
                int result = order.compare(o1, o2);
                if (result != 0)
                    return result;
            }

            return 0;
        });

        return sorted;
    }

    private boolean test(Query query, T object) {
        List<Boolean> testResults = new ArrayList<>();

        for (Criterion criterion : query.getCriterions()) {
            boolean testResult = false;

            // normal Restriction, just assume Conjunction
            if (criterion instanceof Restriction) {
                Restriction restriction = (Restriction) criterion;
                testResult = testAnd(query, object, singletonList(restriction));
            } else if (criterion instanceof Conjunction) {
                Conjunction conjunction = (Conjunction) criterion;
                testResult = testAnd(query, object, conjunction.getCriterions());
            } else if (criterion instanceof Disjunction) {
                Disjunction disjunction = (Disjunction) criterion;
                testResult = testOr(query, object, disjunction.getCriterions());
            }

            testResults.add(testResult);
        }

        if (query.getRootJunctionType() == Junction.Type.OR) {
            return testResults.contains(Boolean.TRUE);
        }

        return !testResults.contains(Boolean.FALSE);
    }

    private boolean testAnd(Query query, T object, List<? extends Criterion> criterions) {
        for (Criterion criterion : criterions) {
            if (criterion instanceof Restriction) {
                Restriction restriction = (Restriction) criterion;
                Object value = getValue(query, object, restriction.getPath());

                if (!(value instanceof Collection)) {
                    if (!restriction.getOperator().test(value)) {
                        return false;
                    }
                } else {
                    Collection<?> collection = (Collection<?>) value;

                    for (Object item : collection) {
                        if (restriction.getOperator().test(item)) {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private boolean testOr(Query query, T object, List<? extends Criterion> criterions) {
        for (Criterion criterion : criterions) {
            if (criterion instanceof Restriction) {
                Restriction restriction = (Restriction) criterion;
                Object value = getValue(query, object, restriction.getPath());

                if (!(value instanceof Collection)) {
                    if (restriction.getOperator().test(value)) {
                        return true;
                    }
                } else {
                    Collection<?> collection = (Collection<?>) value;

                    for (Object item : collection) {
                        if (restriction.getOperator().test(item)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private Object getValue(Query query, Object object, String path) {
        String[] paths = path.split("\\.");
        Schema currentSchema = query.getSchema();

//        if ( path.contains( "access" ) && query.getSchema().isIdentifiableObject() )
//        {
//            ((BaseIdentifiableObject) object).setAccess( aclService.getAccess( (T) object, query.getUser() ) );
//        }

        for (int i = 0; i < paths.length; i++) {
            Property property = currentSchema.getProperty(paths[i]);

            if (property == null) {
                throw new QueryException("No property found for path " + path);
            }

            if (property.isCollection()) {
                currentSchema = schemaService.getDynamicSchema(property.getItemKlass());
            } else {
                currentSchema = schemaService.getDynamicSchema(property.getKlass());
            }

            object = collect(object, property);

//            if ( path.contains( "access" ) && property.isIdentifiableObject() )
//            {
//                if ( property.isCollection() )
//                {
//                    for ( Object item : ((Collection<?>) object) )
//                    {
//                        ((BaseIdentifiableObject) item).setAccess( aclService.getAccess( (T) item, query.getUser() ) );
//                    }
//                }
//                else
//                {
//                    ((BaseIdentifiableObject) object).setAccess( aclService.getAccess( (T) object, query.getUser() ) );
//                }
//            }

            if (i == (paths.length - 1)) {
                if (property.isCollection()) {
                    return Lists.newArrayList(object);
                }

                return object;
            }

        }

        throw new QueryException("No values found for path " + path);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object collect(Object object, Property property) {
        object = HibernateProxyUtils.unproxy(object);

        if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            List<Object> items = new ArrayList<>();

            for (Object item : collection) {
                Object collect = collect(item, property);

                if (collect instanceof Collection) {
                    items.addAll(((Collection) collect));
                } else {
                    items.add(collect);
                }
            }

            return items;
        }

        return ReflectionUtils.invokeMethod(object, property.getGetterMethod());
    }
}
