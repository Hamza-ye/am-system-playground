package org.nmcpye.activitiesmanagement.extended.common;

import org.nmcpye.activitiesmanagement.domain.User;

import java.util.Set;

public interface SubscribableObject
    extends IdentifiableObject
{
    Set<String> getSubscribers();

    boolean isSubscribed();

    boolean subscribe(User user);

    boolean unsubscribe(User user);
}
