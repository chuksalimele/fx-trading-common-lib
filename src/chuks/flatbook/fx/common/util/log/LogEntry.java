/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util.log;

/**
 *
 * @author user
 */
public class LogEntry {
    private String timestamp;
    private String thread;
    private String level;
    private String marker;
    private String logger;
    private String message;
    private String throwable;

    // Constructor, getters, and setters
    public LogEntry(String timestamp, String thread, String level, String marker, String logger, String message, String throwable) {
        this.timestamp = timestamp;
        this.thread = thread;
        this.level = level;
        this.marker = marker;
        this.logger = logger;
        this.message = message;
        this.throwable = throwable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getThrowable() {
        return throwable;
    }

    public String getThread() {
        return thread;
    }

    public String getMessage() {
        return message;
    }

    public String getMarker() {
        return marker;
    }

    public String getLogger() {
        return logger;
    }

    public String getLevel() {
        return level;
    }

}

