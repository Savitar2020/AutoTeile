package ch.bzz.autoTeile.model;

import java.util.Random;

/**
 * an application user
 * <p>
 *
 * @author Jason A. Caviezel
 */
public class User {
    private String userUUID;
    private String username;
    private String password;
    private String role;

    public User() {
        setRole("guest");
    }

    /**
     * Get the userUUID
     * @return value of userUUID
     */
    public String getUserUUID() {
        return userUUID;
    }

    /**
     * Sets the userUUID
     * @param userUUID
     */
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    /**
     * Gets the username
     *
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username the value to set
     */

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role
     *
     * @return value of role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role
     *
     * @param role the value to set
     */

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * generates code for 2 factor authentication
     *
     * @return generatedString
     */
    public String codeGenerator() {
        int limitL = 97;
        int limitR = 122;
        int targetLength = 9;
        Random random = new Random();

        String generatedString = random.ints(limitL, limitR + 1)
                .limit(targetLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

}