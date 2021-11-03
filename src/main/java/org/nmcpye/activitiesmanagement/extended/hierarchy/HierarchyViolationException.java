package org.nmcpye.activitiesmanagement.extended.hierarchy;

/**
 * An Exception class that can be used for illegal operations on a hierarchy.
 *
 */
public class HierarchyViolationException extends RuntimeException {

    public HierarchyViolationException(String message) {
        super(message);
    }
}
