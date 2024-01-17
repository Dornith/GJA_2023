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
    
    /**
     * Parses a given name to a format usable for naming in linux.
     * The new name is also trimmed to a specified length.
     * 
     * @param nonOS The name to be parsed.
     * @param maxLength The maximum length of the new name.
     * @return Parsed name usable in linux with trimmed length.
     */
    public static String toOS(String nonOS, int maxLength) {
        String newName = nonOS.replaceAll(REGEX_FORBIDDEN_SYMBOLS, "");
        String uid = java.util.UUID.randomUUID().toString();

        if (newName.length() > maxLength) {
            newName = newName.substring(0, maxLength);
        }
        
        return newName + uid;
    }
}
