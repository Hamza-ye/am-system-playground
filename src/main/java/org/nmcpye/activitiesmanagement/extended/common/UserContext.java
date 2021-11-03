package org.nmcpye.activitiesmanagement.extended.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.nmcpye.activitiesmanagement.domain.User;

public final class UserContext {

    private static final ThreadLocal<User> threadUser = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, Serializable>> threadUserSettings = new ThreadLocal<>();

    public static void reset() {
        threadUser.remove();
        threadUserSettings.remove();
    }

    public static void setUser(User user) {
        threadUser.set(user);
    }

    public static User getUser() {
        return threadUser.get();
    }

    public static String getUsername() {
        User user = getUser();

        return user != null ? user.getLogin() : "system-process";
    }

    public static boolean haveUser() {
        return getUser() != null;
    }

    // TODO Needs synchronized?

    public static void setUserSetting(String key, Serializable value) {
        if (threadUserSettings.get() == null) {
            threadUserSettings.set(new HashMap<>());
        }

        if (value != null) {
            threadUserSettings.get().put(key, value);
        } else {
            threadUserSettings.get().remove(key);
        }
    }
}
