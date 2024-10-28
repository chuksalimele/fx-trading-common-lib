/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util.log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author user
 */
public class LogReader {

    public static List<LogEntry> readLogFile(String filePath) throws FileNotFoundException, IOException {
        List<LogEntry> logEntries = new LinkedList<>();

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        List list = new LinkedList();
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        return readLogRecords((String[])list.toArray(String[]::new), logEntries);
    }

    public static List<LogEntry> readLogRecords(String[] records) {
        List<LogEntry> logEntries = new LinkedList<>();
        return readLogRecords(records, logEntries);
    }

    public static List<LogEntry> readLogRecords(String[] records, List<LogEntry> logEntries) {

        String line;
        String log_record = "";
        String[] parts;
        for (String record : records) {
            line = record;
            // Parse the log line based on your log format
            parts = line.split(" - ", 2);
            String l = line + "\n";
            if (parts.length > 1) {
                if (!log_record.isEmpty()) {
                    addLogEntries(logEntries, log_record);
                }
                log_record = l;

            } else {
                log_record += l;
            }
        }

        addLogEntries(logEntries, log_record);

        return logEntries;
    }

    private static List addLogEntries(List<LogEntry> logEntries, String log_record) {
        String[] parts = parseLogLine(log_record);
        if (parts.length >= 5) {
            logEntries.add(new LogEntry(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
        }
        return logEntries;
    }

    /**
     * Assuming the log format is "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level
     * %logger{36} - %msg%n%throwable OR Assuming the log format is
     * "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %marker %logger{36} -
     * %msg%n%throwable
     *
     * @param record
     * @return
     */
    private static String[] parseLogLine(String record) {

        String[] parts = record.split(" - ", 2);
        if (parts.length < 2) {
            return new String[]{};
        }
        String[] metaData = parts[0].split(" ");
        String[] p = parts[1].split("\n");

        String date = metaData[0];
        String time = metaData[1];
        String thread = metaData[2].replace("[", "").replace("]", "");
        String level = metaData[3];
        String marker = getMarker(metaData);
        String logger = metaData[metaData.length - 1];

        String message = p[0];
        String throwable = parts[1].replaceFirst(p[0], "").trim();

        // Combine timestamp, thread, level, logger
        return new String[]{
            date + " " + time, // Timestamp
            thread, // Thread
            level, // Level
            marker, // marker
            logger, // Logger
            message, // Message
            throwable // Throwable
        };
    }

    private static String getMarker(String[] metaData) {
        String marker = "";
        for (int i = 4; i < metaData.length - 1; i++) {
            marker += metaData[i] + " ";
        }

        return marker.trim();
    }

    public static void main(String[] args) throws IOException {
        List<LogEntry> logEntries = LogReader.readLogFile("logs/error.log");
        for (LogEntry entry : logEntries) {
            System.out.println(entry);
        }
    }

}
