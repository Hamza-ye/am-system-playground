package org.nmcpye.activitiesmanagement.extended.common.filter;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;

public class UserAuthorityGroupCanIssueFilter implements Filter<PersonAuthorityGroup> {

    private Person userCredentials;

    private boolean canGrantOwnUserAuthorityGroups = false;

    protected UserAuthorityGroupCanIssueFilter() {}

    public UserAuthorityGroupCanIssueFilter(User user, boolean canGrantOwnUserAuthorityGroups) {
        if (user != null && user.getPerson() != null) {
            this.userCredentials = user.getPerson();
            this.canGrantOwnUserAuthorityGroups = canGrantOwnUserAuthorityGroups;
        }
    }

    @Override
    public boolean retain(PersonAuthorityGroup group) {
        return userCredentials != null && userCredentials.canIssueUserRole(group, canGrantOwnUserAuthorityGroups);
    }
}
