package org.nmcpye.activitiesmanagement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * Created by Hamza on 02/10/2021.
 */
@RunWith( SpringRunner.class )
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public abstract class AMTest extends AMConvenienceTest {

    protected static final Logger log = LoggerFactory.getLogger(AMTest.class);

    @Autowired
    protected ApplicationContext context;

    @BeforeEach
    public final void before() throws Exception {
        executeStartupRoutines();
        setUpTest();
    }

    @AfterEach
    public final void after() throws Exception {
        clearSecurityContext();
        tearDownTest();
    }

    /**
     * Methods to override.
     */
    protected void setUpTest() throws Exception {}

    protected void tearDownTest() throws Exception {}

    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------

    /**
     * Retrieves a bean from the application context.
     *
     * @param beanId the identifier of the bean.
     */
    protected Object getBean(String beanId) {
        return context.getBean(beanId);
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void executeStartupRoutines() throws Exception {
        String id = "org.nmcpye.activitiesmanagement.extended.systemmodule.system.startup.StartupRoutineExecutor";
        log.info("################ executeStartupRoutines() #############");
        if (context != null && context.containsBean(id)) {
            Object object = context.getBean(id);

            Method method = object.getClass().getMethod("executeForTesting", new Class[0]);

            method.invoke(object, new Object[0]);
        }
    }
}
