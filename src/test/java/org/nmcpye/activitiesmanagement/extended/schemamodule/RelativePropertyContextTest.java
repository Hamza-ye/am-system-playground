package org.nmcpye.activitiesmanagement.extended.schemamodule;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the {@link RelativePropertyContext}. We use a integration test so
 */

@IntegrationTest
public class RelativePropertyContextTest extends AMTest {

    @Autowired
    private SchemaService schemaService;

    @Test
    public void testResolve_DirectProperty() {
        assertPropertyDoesExist(PeopleGroup.class, "members", Person.class);
        assertPropertyDoesExist(PeopleGroup.class, "managedGroups", PeopleGroup.class);
        assertPropertyDoesExist(OrganisationUnit.class, "ancestors", OrganisationUnit.class);
        assertPropertyDoesExist(OrganisationUnit.class, "level", Integer.class);

        assertPropertyDoesNotExist(PeopleGroup.class, "people");
        assertPropertyDoesNotExist(User.class, "password");
    }

    @Test
    public void testResolve_ChildProperty() {
        assertPropertyDoesExist(PeopleGroup.class, "members.groups", PeopleGroup.class);
        assertPropertyDoesExist(PeopleGroup.class, "members.organisationUnits", OrganisationUnit.class);
        assertPropertyDoesExist(PeopleGroup.class, "members.mobile", String.class);

        assertPropertyDoesNotExist(OrganisationUnit.class, "ancestors.pony");
        assertPropertyDoesNotExist(OrganisationUnit.class, "pony.ancestors");
    }

    @Test
    public void testResolve_GrantChildProperty() {
        assertPropertyDoesExist(PeopleGroup.class, "members.groups.members", Person.class);
        assertPropertyDoesExist(PeopleGroup.class, "members.organisationUnits.level", Integer.class);
        assertPropertyDoesExist(PeopleGroup.class, "members.dataViewOrganisationUnits.ancestors", OrganisationUnit.class);

        assertPropertyDoesNotExist(OrganisationUnit.class, "ancestors.ancestors.pony");
        assertPropertyDoesNotExist(OrganisationUnit.class, "pony.ancestors.ancestors");
    }

    private void assertPropertyDoesExist(Class<?> type, String path, Class<?> expectedType) {
        RelativePropertyContext context = new RelativePropertyContext(type, schemaService::getDynamicSchema);

        // test "resolve"
        Property property = context.resolve(path);
        assertNotNull(property);
        assertSame(expectedType, property.isCollection() ? property.getItemKlass() : property.getKlass());

        // test "resolveMandatory"
        Property mandatory = context.resolveMandatory(path);
        assertNotNull(mandatory);
        assertSame(property, mandatory);

        // test "resolvePath"
        List<Property> elements = context.resolvePath(path);
        assertEquals(path.split("\\.").length, elements.size());
        assertSame(property, elements.get(elements.size() - 1));
        assertEquals(path, elements.stream().map(Property::key).collect(joining(".")));
    }

    private void assertPropertyDoesNotExist(Class<?> type, String path) {
        RelativePropertyContext context = new RelativePropertyContext(type, schemaService::getDynamicSchema);
        assertNull(context.resolve(path));
        assertThrows(NoSuchElementException.class, () -> context.resolveMandatory(path));
    }
}
