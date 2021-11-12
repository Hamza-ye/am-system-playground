package org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.operators;

import org.hibernate.criterion.MatchMode;

import java.util.List;

import static java.util.Arrays.asList;

public class TokenUtils {
    private TokenUtils() {
        throw new UnsupportedOperationException("util");
    }

    public static List<String> getTokens(String value) {
        return asList(value.replaceAll("[^\\p{L}0-9]", " ").split("[\\s@&.?$+-]+"));
    }

    public static StringBuilder createRegex(String value) {
        StringBuilder regex = new StringBuilder();

        List<String> tokens = getTokens(value);

        if (tokens.isEmpty()) {
            return regex;
        }

        for (String token : getTokens(value)) {
            regex.append("(?=.*").append(token).append(")");
        }
        return regex;
    }

    public static <T> boolean test(T value, String searchTerm, boolean caseSensitive, MatchMode mode) {
        if (value == null) {
            return false;
        }
        String searchString = caseSensitive ? searchTerm : searchTerm.toLowerCase();
        String valueString = caseSensitive ? value.toString() : value.toString().toLowerCase();
        List<String> searchTokens = getTokens(searchString);
        List<String> valueTokens = getTokens(valueString);
        return searchTokens.stream().allMatch(searchToken -> testToken(searchToken, valueTokens, mode));
    }

    private static boolean testToken(String searchToken, List<String> valueTokens, MatchMode mode) {
        switch (mode) {
            case EXACT:
                return valueTokens.stream().anyMatch(token -> token.equals(searchToken));
            case START:
                return valueTokens.stream().anyMatch(token -> token.startsWith(searchToken));
            case END:
                return valueTokens.stream().anyMatch(token -> token.endsWith(searchToken));
            default:
            case ANYWHERE:
                return valueTokens.stream().anyMatch(token -> token.contains(searchToken));
        }
    }
}
