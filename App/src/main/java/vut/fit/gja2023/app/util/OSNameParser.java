package vut.fit.gja2023.app.util;

public class OSNameParser {
    private static final String REGEX_FORBIDDEN_SYMBOLS = "[^a-zA-Z\\d]";

    public static String toOS(String nonOS) {
        String newName = nonOS.replaceAll(REGEX_FORBIDDEN_SYMBOLS, "");
        String uid = java.util.UUID.randomUUID().toString();

        return newName + uid;
    }
}
