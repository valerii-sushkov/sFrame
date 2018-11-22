package Run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LogCollector {
    private String loggerName;
    private static List<String> resLines = new ArrayList<>();
    private static String screenShot;

    private static int result;

    public LogCollector(final String name) {
        loggerName = name;
    }

    public void debug(final String message) {
        log(Level.FINE, message);
    }

    public void info(final String message) {
        log(Level.INFO, message);
    }

    public void warn(final String message) {
        log(Level.WARNING, message);
    }

    public void severe(final String message) {
        log(Level.SEVERE, message);
    }

    public void severe(final String message, final Throwable exception) {
        log(Level.SEVERE, message, exception);
    }


    private void log(final Level level, final String message) {
        String text = formatLine(level.getName(), message);
        resLines.add(text);
    }

    private void log(final Level level, final String message, final Throwable exception) {
        String text = formatLine(level.getName(), message + "\n"
                + exception.toString() + "\n" +
                Arrays.stream(exception.getStackTrace())
                        .limit(10).map(elem -> elem.toString())
                        .collect(Collectors.joining("\n")));
        resLines.add(text);
    }

    private String formatLine(final String prefix, final String text) {
        return "[" + prefix + "] [" + loggerName + "] " + text;
    }

    public static List<String> extractLog() {
        return resLines;
    }
    public static void consumeLog(final List<String> newLines) {
        resLines.addAll(newLines);
    }

    public static int getResult() {
        return result;
    }

    public static void setResult(int newResult) {
        result = newResult;
    }

    public static String getScreenShot() {
        return screenShot;
    }

    public static void setScreenShot(String newScreenShot) {
        screenShot = newScreenShot;
    }
}
