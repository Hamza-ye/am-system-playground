package org.nmcpye.activitiesmanagement.extended.common;

import org.springframework.context.ApplicationEvent;

public class ObjectDeletionRequestedEvent
    extends ApplicationEvent {
    /**
     * Should rollback the transaction if DeleteNotAllowedException is thrown
     */
    private boolean shouldRollBack = true;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ObjectDeletionRequestedEvent(Object source) {
        super(source);
    }

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------

    public boolean isShouldRollBack() {
        return shouldRollBack;
    }

    public void setShouldRollBack(boolean shouldRollBack) {
        this.shouldRollBack = shouldRollBack;
    }

    /**
     * Check whether the given class should be skipped for DeletionHandler.
     *
     * @param type the class type.
     * @return true if the given class should be skipped.
     */
    public static boolean shouldSkip(Class<?> type) {
        return false;
//        return UserAccess.class.isAssignableFrom( type ) || UserGroupAccess.class.isAssignableFrom( type ) ? true
//            : false;
    }
}
