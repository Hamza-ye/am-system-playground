package org.nmcpye.activitiesmanagement.extended.common;

import org.apache.commons.lang3.StringUtils;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.user.UserSetting;
import org.nmcpye.activitiesmanagement.extended.user.UserSettingKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void setUserSetting(UserSettingKey key, Serializable value) {
        setUserSettingInternal(key.getName(), value);
    }

    private static void setUserSettingInternal(String key, Serializable value) {
        if (threadUserSettings.get() == null) {
            threadUserSettings.set(new HashMap<>());
        }

        if (value != null) {
            threadUserSettings.get().put(key, value);
        } else {
            threadUserSettings.get().remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getUserSetting(UserSettingKey key) {
        return threadUserSettings.get() != null ? (T) threadUserSettings.get().get(key.getName()) : null;
    }

    public static void setUserSettings(List<UserSetting> userSettings) {
        userSettings.stream()
            .filter(userSetting -> !StringUtils.isEmpty(userSetting.getName()))
            .forEach(
                userSetting -> UserContext.setUserSettingInternal(userSetting.getName(), userSetting.getValue()));
    }
}
