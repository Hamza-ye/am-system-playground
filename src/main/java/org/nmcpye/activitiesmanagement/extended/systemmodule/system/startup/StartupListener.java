package org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup;

import org.nmcpye.activitiesmanagement.extended.common.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Implementation of {@link javax.servlet.ServletContextListener} which hooks
 * into the context initialization and executes the configured
 * {@link StartupRoutineExecutor}.
 *
 */
public class StartupListener implements ServletContextListener {

    private final Logger log = LoggerFactory.getLogger(StartupListener.class);

    // -------------------------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------------------------

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        StartupRoutineExecutor startupRoutineExecutor = (StartupRoutineExecutor) applicationContext.getBean(StartupRoutineExecutor.ID);

        try {
            startupRoutineExecutor.execute();
        } catch (Exception ex) {
            log.error(DebugUtils.getStackTrace(ex));

            throw new RuntimeException("Failed to run startup routines: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.info("De-registering jdbc driver: " + driver);
            } catch (SQLException e) {
                log.info("Error de-registering driver " + driver + " :" + e.getMessage());
            }
        }
    }
}
