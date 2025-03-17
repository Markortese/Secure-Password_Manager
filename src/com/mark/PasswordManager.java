
package com.mark;

/**
 *
 * @author marko
 */
public class PasswordManager {
    // Save a password securely
    public static void savePassword(String site, String username, String password) {
        DatabaseHelper.savePassword(site, username, password);
    }

    // Retrieve a stored password
    public static void getPassword(String site) {
        DatabaseHelper.getPassword(site);
    }
}
