package org.nmcpye.activitiesmanagement.extended.domain.person;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for {@link Person}.
 *
 */
public class PersonTest {

    private PersonAuthorityGroup userAuthorityGroup1;

    private PersonAuthorityGroup userAuthorityGroup2;

    private PersonAuthorityGroup userAuthorityGroup1Super;

    @Before
    public void setUp() {
        userAuthorityGroup1 = new PersonAuthorityGroup();
        userAuthorityGroup1.setUid("uid1");
        userAuthorityGroup1.setAuthoritiesString(new HashSet<>(Arrays.asList("x1", "x2")));

        userAuthorityGroup2 = new PersonAuthorityGroup();
        userAuthorityGroup2.setUid("uid2");
        userAuthorityGroup2.setAuthoritiesString(new HashSet<>(Arrays.asList("y1", "y2")));

        userAuthorityGroup1Super = new PersonAuthorityGroup();
        userAuthorityGroup1Super.setUid("uid4");
        userAuthorityGroup1Super.setAuthoritiesString(new HashSet<>(Arrays.asList("z1", PersonAuthorityGroup.AUTHORITY_ALL)));
    }

    @Test
    public void isSuper() {
        final Person userCredentials = new Person();
        userCredentials.setPersonAuthorityGroups(
            new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup1Super, userAuthorityGroup2))
        );

        Assert.assertTrue(userCredentials.isSuper());
        Assert.assertTrue(userCredentials.isSuper());
    }

    @Test
    public void isNotSuper() {
        final Person userCredentials = new Person();
        userCredentials.setPersonAuthorityGroups(new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup2)));

        Assert.assertFalse(userCredentials.isSuper());
        Assert.assertFalse(userCredentials.isSuper());
    }

    @Test
    public void isSuperChanged() {
        final Person userCredentials = new Person();
        userCredentials.setPersonAuthorityGroups(
            new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup1Super, userAuthorityGroup2))
        );

        Assert.assertTrue(userCredentials.isSuper());

        userCredentials.setPersonAuthorityGroups(new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup2)));
        Assert.assertFalse(userCredentials.isSuper());
    }

    @Test
    public void getAllAuthorities() {
        final Person userCredentials = new Person();
        userCredentials.setPersonAuthorityGroups(new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup1Super)));

        Set<String> authorities1 = userCredentials.getAllAuthorities();
        Assert.assertThat(authorities1, Matchers.containsInAnyOrder("x1", "x2", "z1", PersonAuthorityGroup.AUTHORITY_ALL));

        Set<String> authorities2 = userCredentials.getAllAuthorities();
        Assert.assertSame(authorities1, authorities2);
    }

    @Test
    public void getAllAuthoritiesChanged() {
        final Person userCredentials = new Person();
        userCredentials.setPersonAuthorityGroups(new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup1Super)));

        Set<String> authorities1 = userCredentials.getAllAuthorities();
        Assert.assertThat(authorities1, Matchers.containsInAnyOrder("x1", "x2", "z1", PersonAuthorityGroup.AUTHORITY_ALL));

        userCredentials.setPersonAuthorityGroups(new HashSet<>(Arrays.asList(userAuthorityGroup1, userAuthorityGroup2)));
        Set<String> authorities2 = userCredentials.getAllAuthorities();
        Assert.assertThat(authorities2, Matchers.containsInAnyOrder("x1", "x2", "y1", "y2"));
    }
}
