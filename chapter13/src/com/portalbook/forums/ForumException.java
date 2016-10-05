package com.portalbook.forums;

/**
 * Copyright &copy; David Minter 2004
 * 
 * @author Dave
 */
public class ForumException extends RuntimeException {

    /**
     * This exception is desgined to be thrown to announce a problem to higher
     * tiers in the forum system. If the exception remains uncaught, the presentation
     * layer will catch it, format it, and present it to the user.
     * 
     * @param userMessage A non technical description of the problem
     * @param technicalMessage A technical description of the problem for debugging
     * @param cause The exception which was thrown
     */
    public ForumException(String userMessage, String technicalMessage, Throwable cause) {
        super(technicalMessage,cause);
        this.userMessage = userMessage;
    }

    /**
     * Retrieve non-technical description of the problem
     * 
     * @return The problem description 
     */
    public String getUserMessage() {
        return userMessage;
    }

    // The user oriented message
    private String userMessage;
}
