package vut.fit.gja2023.app.util;

/**
 * Used to parse names to a format usable for naming in linux.
 */
public class OSNameParser {
    private static final String REGEX_FORBIDDEN_SYMBOLS = "[^a-zA-Z\\d]";

    /**
     * Parses a given name to a format usable for naming in linux.
     * 
     * @param nonOS The name to be parsed.
     * @return Parsed name usable in linux.
     */
    public static String toOS(String nonOS) {
        String newName = nonOS.replaceAll(REGEX_FORBIDDEN_SYMBOLS, "");
        String uid = java.util.UUID.randomUUID().toString();

        return newName + uid;
    }
}
