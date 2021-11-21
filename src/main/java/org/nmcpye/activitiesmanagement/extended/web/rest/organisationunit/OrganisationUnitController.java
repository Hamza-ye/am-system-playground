package org.nmcpye.activitiesmanagement.extended.web.rest.organisationunit;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitQueryParams;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.nmcpye.activitiesmanagement.extended.organisationunit.comparator.OrganisationUnitByLevelComparator;
import org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors.OrganisationUnitSchemaDescriptor;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Order;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.Query;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryParserException;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.GeoUtils;
import org.nmcpye.activitiesmanagement.extended.util.ObjectUtils;
import org.nmcpye.activitiesmanagement.extended.web.constants.ApiEndPoint;
import org.nmcpye.activitiesmanagement.extended.web.rest.AbstractCrudController;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebMetadata;
import org.nmcpye.activitiesmanagement.extended.web.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = ApiEndPoint.API_END_POINT + OrganisationUnitSchemaDescriptor.API_ENDPOINT)
public class OrganisationUnitController
    extends AbstractCrudController<OrganisationUnit> {
    @Autowired
    private OrganisationUnitServiceExt organisationUnitServiceExt;

    @Override
    @SuppressWarnings("unchecked")
    protected List<OrganisationUnit> getEntityList(WebMetadata metadata, WebOptions options, List<String> filters,
                                                   List<Order> orders)
        throws QueryParserException {
        List<OrganisationUnit> objects = Lists.newArrayList();

        User currentUser = userService.getUserWithAuthorities().orElse(null);

        boolean anySpecialPropertySet = ObjectUtils.anyIsTrue(options.isTrue("userOnly"),
            options.isTrue("userDataViewOnly"), options.isTrue("userDataViewFallback"),
            options.isTrue("levelSorted"));
        boolean anyQueryPropertySet = ObjectUtils.firstNonNull(options.get("query"), options.getInt("level"),
            options.getInt("maxLevel")) != null || options.isTrue("withinUserHierarchy")
            || options.isTrue("withinUserSearchHierarchy");
        String memberObject = options.get("memberObject");
        String memberCollection = options.get("memberCollection");

        // ---------------------------------------------------------------------
        // Special parameter handling
        // ---------------------------------------------------------------------

        if (options.isTrue("userOnly") ) {
            objects = new ArrayList<>(getPersonOrganisationUnits(currentUser.getPerson()));
        } else if (options.isTrue("userDataViewOnly")) {
            objects = new ArrayList<>(getPersonDataViewOrganisationUnits(currentUser.getPerson()));
        } else if (options.isTrue("userDataViewFallback")) {
            if (currentUser.hasDataViewOrganisationUnit()) {
                objects = new ArrayList<>(getPersonDataViewOrganisationUnits(currentUser.getPerson()));
            } else {
                objects = organisationUnitServiceExt.getOrganisationUnitsAtLevel(1);
            }
        } else if (options.isTrue("levelSorted")) {
            objects = new ArrayList<>(manager.getAll(getEntityClass()));
            objects.sort(OrganisationUnitByLevelComparator.INSTANCE);
        }

        // ---------------------------------------------------------------------
        // OrganisationUnitQueryParams query parameter handling
        // ---------------------------------------------------------------------

//        else if (anyQueryPropertySet) {
        else if (anyQueryPropertySet) {
            OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
            params.setQuery(options.get("query"));
            params.setLevel(options.getInt("level"));
            params.setMaxLevels(options.getInt("maxLevel"));
            if (currentUser != null && currentUser.getPerson() != null) {
                params.setParents(options.isTrue("withinUserHierarchy") ? currentUser.getPerson().getOrganisationUnits()
//                : options.isTrue("withinUserSearchHierarchy")
//                ? currentUser.getPerson().getTeiSearchOrganisationUnitsWithFallback()
                    : Sets.newHashSet());
            }
            objects = organisationUnitServiceExt.getOrganisationUnitsByQuery(params);
        }

        // ---------------------------------------------------------------------
        // Standard Query handling
        // ---------------------------------------------------------------------

        Query query = queryService.getQueryFromUrl(getEntityClass(), filters, orders, getPaginationData(options),
            options.getRootJunction());
        query.setUser(currentUser);
        query.setDefaultOrder();

        if (anySpecialPropertySet || anyQueryPropertySet) {
            query.setObjects(objects);
        }

        List<OrganisationUnit> list = (List<OrganisationUnit>) queryService.query(query);

        // ---------------------------------------------------------------------
        // Collection member count in hierarchy handling
        // ---------------------------------------------------------------------

        IdentifiableObject member;

        if (memberObject != null && memberCollection != null && (member = manager.get(memberObject)) != null) {
            for (OrganisationUnit unit : list) {
                Long count = organisationUnitServiceExt.getOrganisationUnitHierarchyMemberCount(unit, member,
                    memberCollection);

                unit.setMemberCount((count != null ? count.intValue() : 0));
            }
        }

        return list;
    }

    private Set<OrganisationUnit> getPersonOrganisationUnits(Person person) {
        if (person != null) {
            return person.getOrganisationUnits();
        }
        return Sets.newHashSet();
    }

    private Set<OrganisationUnit> getPersonDataViewOrganisationUnits(Person person) {
        if (person != null) {
            return person.getDataViewOrganisationUnits();
        }
        return Sets.newHashSet();
    }


    @Override
    protected List<OrganisationUnit> getEntity(String uid, WebOptions options) {
        OrganisationUnit organisationUnit = manager.get(getEntityClass(), uid);

        List<OrganisationUnit> organisationUnits = Lists.newArrayList();

        if (organisationUnit == null) {
            return organisationUnits;
        }

        if (options.contains("includeChildren")) {
            options.getOptions().put("useWrapper", "true");
            organisationUnits.add(organisationUnit);

            organisationUnits.addAll(organisationUnit.getChildren());
        } else if (options.contains("includeDescendants")) {
            options.getOptions().put("useWrapper", "true");
            organisationUnits.addAll(organisationUnitServiceExt.getOrganisationUnitWithChildren(uid));
        } else if (options.contains("includeAncestors")) {
            options.getOptions().put("useWrapper", "true");
            organisationUnits.add(organisationUnit);
            List<OrganisationUnit> ancestors = organisationUnit.getAncestors();
            Collections.reverse(ancestors);
            organisationUnits.addAll(ancestors);
        } else if (options.contains("level")) {
            options.getOptions().put("useWrapper", "true");
            int level = options.getInt("level");
            int ouLevel = organisationUnit.getLevel();
            int targetLevel = ouLevel + level;
            organisationUnits
                .addAll(organisationUnitServiceExt.getOrganisationUnitsAtLevel(targetLevel, organisationUnit));
        } else {
            organisationUnits.add(organisationUnit);
        }

        return organisationUnits;
    }

    @GetMapping("/{uid}/parents")
    public @ResponseBody
    List<OrganisationUnit> getEntityList(@PathVariable("uid") String uid,
                                         @RequestParam Map<String, String> parameters, Model model,
                                         HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        setUserContext();
        OrganisationUnit organisationUnit = manager.get(getEntityClass(), uid);
        List<OrganisationUnit> organisationUnits = Lists.newArrayList();

        if (organisationUnit != null) {
            OrganisationUnit organisationUnitParent = organisationUnit.getParent();

            while (organisationUnitParent != null) {
                organisationUnits.add(organisationUnitParent);
                organisationUnitParent = organisationUnitParent.getParent();
            }
        }

        WebMetadata metadata = new WebMetadata();
        metadata.setOrganisationUnits(organisationUnits);

        return organisationUnits;
    }

    @GetMapping(value = "", produces = {"application/json+geo",
        "application/json+geojson"})
    public void getGeoJson(
        @RequestParam(value = "level", required = false) List<Integer> rpLevels,
        @RequestParam(value = "parent", required = false) List<String> rpParents,
        @RequestParam(value = "properties", required = false, defaultValue = "true") boolean rpProperties,
        User currentUser, HttpServletResponse response)
        throws IOException {
        rpLevels = rpLevels != null ? rpLevels : new ArrayList<>();
        rpParents = rpParents != null ? rpParents : new ArrayList<>();

        List<OrganisationUnit> parents = manager.getByUid(OrganisationUnit.class, rpParents);

        if (rpLevels.isEmpty()) {
            rpLevels.add(1);
        }

        if (parents.isEmpty()) {
            parents.addAll(organisationUnitServiceExt.getRootOrganisationUnits());
        }

        List<OrganisationUnit> organisationUnits = organisationUnitServiceExt.getOrganisationUnitsAtLevels(rpLevels,
            parents);

        response.setContentType(APPLICATION_JSON_VALUE);

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(response.getOutputStream());

        generator.writeStartObject();
        generator.writeStringField("type", "FeatureCollection");
        generator.writeArrayFieldStart("features");

        for (OrganisationUnit organisationUnit : organisationUnits) {
            writeFeature(generator, organisationUnit, rpProperties, currentUser);
        }

        generator.writeEndArray();
        generator.writeEndObject();

        generator.close();
    }

    private void writeFeature(JsonGenerator generator, OrganisationUnit organisationUnit,
                              boolean includeProperties, User user)
        throws IOException {
        if (organisationUnit.getGeometry() == null) {
            return;
        }

        generator.writeStartObject();

        generator.writeStringField("type", "Feature");
        generator.writeStringField("id", organisationUnit.getUid());

        generator.writeObjectFieldStart("geometry");
        generator.writeObjectField("type", organisationUnit.getGeometry().getGeometryType());

        generator.writeFieldName("coordinates");
        generator.writeRawValue(GeoUtils.getCoordinatesFromGeometry(organisationUnit.getGeometry()));

        generator.writeEndObject();

        generator.writeObjectFieldStart("properties");

        if (includeProperties) {
//            Set<OrganisationUnit> roots = user.getPerson().getDataViewOrganisationUnitsWithFallback();
            Set<OrganisationUnit> roots = user.getPerson() != null ? user.getPerson().getDataViewOrganisationUnitsWithFallback()
                : Sets.newHashSet();

            generator.writeStringField("code", organisationUnit.getCode());
            generator.writeStringField("name", organisationUnit.getName());
            generator.writeStringField("level", String.valueOf(organisationUnit.getLevel()));

            if (organisationUnit.getParent() != null) {
                generator.writeStringField("parent", organisationUnit.getParent().getUid());
            }

            generator.writeStringField("parentGraph", organisationUnit.getParentGraph(roots));

            generator.writeArrayFieldStart("groups");

            for (OrganisationUnitGroup group : organisationUnit.getGroups()) {
                generator.writeString(group.getUid());
            }

            generator.writeEndArray();
        }

        generator.writeEndObject();

        generator.writeEndObject();
    }

    @Override
    protected void postCreateEntity(OrganisationUnit entity) {
//        versionService.updateVersion(VersionService.ORGANISATIONUNIT_VERSION);
    }

    @Override
    protected void postUpdateEntity(OrganisationUnit entity) {
//        versionService.updateVersion(VersionService.ORGANISATIONUNIT_VERSION);
    }

    @Override
    protected void postDeleteEntity(String entityUID) {
//        versionService.updateVersion(VersionService.ORGANISATIONUNIT_VERSION);
    }
}
