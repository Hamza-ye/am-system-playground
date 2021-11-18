package org.nmcpye.activitiesmanagement.extended.person;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class PersonStoreTest extends AMTest {

    @Autowired
    private PersonStore personStore;

    @Autowired
    private OrganisationUnitServiceExt organisationUnitServiceExt;

    private OrganisationUnit unit1;
    private OrganisationUnit unit2;

    @Override
    public void setUpTest() throws Exception {
        unit1 = createOrganisationUnit('A');
        unit2 = createOrganisationUnit('B');

        organisationUnitServiceExt.addOrganisationUnit(unit1);
        organisationUnitServiceExt.addOrganisationUnit(unit2);
    }

    @Test
    public void testAddGetUser() {
        Set<OrganisationUnit> units = new HashSet<>();

        units.add(unit1);
        units.add(unit2);

        Person userA = createUserCredentials('A', null);
        Person userB = createUserCredentials('B', null);

        userA.setOrganisationUnits(units);
        userB.setOrganisationUnits(units);

        personStore.saveObject(userA);
        Long idA = userA.getId();
        personStore.saveObject(userB);
        Long idB = userB.getId();

        assertEquals(userA, personStore.get(idA));
        assertEquals(userB, personStore.get(idB));

        assertEquals(units, personStore.get(idA).getOrganisationUnits());
        assertEquals(units, personStore.get(idB).getOrganisationUnits());
    }

    @Test
    public void testUpdateUser() {
        Person userA = createUserCredentials('A', null);
        Person userB = createUserCredentials('B', null);

        personStore.saveObject(userA);
        Long idA = userA.getId();
        personStore.saveObject(userB);
        Long idB = userB.getId();

        assertEquals(userA, personStore.get(idA));
        assertEquals(userB, personStore.get(idB));

        userA.setLogin("UpdatedSurnameA");

        personStore.update(userA);

        assertEquals(personStore.get(idA).getLogin(), "UpdatedSurnameA");
    }

    @Test
    public void testDeleteUser() {
        Person userA = createUserCredentials('A', null);
        Person userB = createUserCredentials('B', null);

        personStore.saveObject(userA);
        Long idA = userA.getId();
        personStore.saveObject(userB);
        Long idB = userB.getId();

        assertEquals(userA, personStore.get(idA));
        assertEquals(userB, personStore.get(idB));

        personStore.delete(userA);

        assertNull(personStore.get(idA));
        assertNotNull(personStore.get(idB));
    }
}
