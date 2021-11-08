package org.nmcpye.activitiesmanagement.extended.config.systemmoduleconfig;

import org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification.InMemoryNotifier;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification.Notifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class deals with the configuring an appropriate notifier depending on
 * whether redis is enabled or not.
 *
 */
@Configuration
public class NotifierConfiguration
{
    @Bean
    @Qualifier( "notifier" )
    public Notifier inMemoryNotifier()
    {
        return new InMemoryNotifier();
    }
}
