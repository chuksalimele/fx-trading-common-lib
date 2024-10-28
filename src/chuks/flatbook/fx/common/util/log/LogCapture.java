/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util.log;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author user
 */
public class LogCapture {

    private static final DateTimeFormatter FILE_NAME_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOG_ENTRY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String INFO = "info";
    private static final String DEBUG = "debug";
    private static final String WARN = "warn";
    private static final String ERROR = "error";
    private static final String TRACE = "trace";
    private static final String REJECTED_IPS = "rejected_ips";
    private static final String SUSPICIOUS_IPS = "suspicious_ips";

    private static final String SEPARATOR = ".";
    private static final String LOG_EXTENSION = ".log";

    public static List<String> captureLogs(LogLevel LogLevel, Path logDir, LocalDateTime startTime, LocalDateTime endTime) throws IOException {

        String level;
        switch (LogLevel) {
            case INFO -> level = INFO;
            case DEBUG -> level = DEBUG;
            case WARN -> level = WARN;
            case ERROR -> level = ERROR;
            case TRACE -> level = TRACE;
            case REJECTED_IPS -> level = REJECTED_IPS;
            case SUSPICIOUS_IPS -> level = SUSPICIOUS_IPS;
            default -> {
                return  new ArrayList<>();
            }
        }

        List<String> logsInTimePeriod = new ArrayList<>();
        
        String unrotatedFileName = level + LOG_EXTENSION;
        String rotatedFilePrefix = level+SEPARATOR;
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDir)) {
            for (Path logFile : stream) {
                String fileName = logFile.getFileName().toString();

                if (fileName.equals(unrotatedFileName)) {
                    // Handle the initial "info" log file without a timestamp in the name
                    captureLogsInFile(logFile, startTime, endTime, logsInTimePeriod);
                } else if (fileName.startsWith(rotatedFilePrefix)) {
                    // Handle the rotated log files with date in the file name
                    Optional<LocalDate> fileDate = extractDateFromFileName(level, fileName);

                    if (fileDate.isPresent() && isDateWithinPeriod(fileDate.get(), startTime, endTime)) {
                        captureLogsInFile(logFile, startTime, endTime, logsInTimePeriod);
                    }
                }
            }
        }
        return logsInTimePeriod;
    }

    private static Optional<LocalDate> extractDateFromFileName(String log_file_prefix, String fileName) {
        try {
            String datePart = fileName.substring(log_file_prefix.length(), log_file_prefix.length() + 10);
            return Optional.of(LocalDate.parse(datePart, FILE_NAME_DATE_FORMAT));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean isDateWithinPeriod(LocalDate fileDate, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        return !fileDate.isBefore(startDate) && !fileDate.isAfter(endDate);
    }

    private static void captureLogsInFile(Path logFile, LocalDateTime startTime, LocalDateTime endTime, List<String> logs) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Optional<LocalDateTime> logTime = extractLogTimestamp(line);

                if (logTime.isPresent() && isWithinPeriod(logTime.get(), startTime, endTime)) {
                    logs.add(line);
                }
            }
        }
    }

    private static Optional<LocalDateTime> extractLogTimestamp(String logLine) {
        try {
            String timestampPart = logLine.substring(0, 19); // Extract the timestamp part of the log line
            return Optional.of(LocalDateTime.parse(timestampPart, LOG_ENTRY_DATE_FORMAT));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean isWithinPeriod(LocalDateTime logTime, LocalDateTime startTime, LocalDateTime endTime) {
        return !logTime.isBefore(startTime) && !logTime.isAfter(endTime);
    }

    public static void main(String[] args) throws IOException {
        Path logDir = Paths.get("C:\\Users\\user\\Documents\\NetBeansProjects\\fx-client-broker\\logs");
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 8, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 10, 26, 18, 16, 59);

        List<String> logs = captureLogs(LogLevel.DEBUG, logDir, startTime, endTime);
        logs.forEach(System.out::println);
    }
}
