package common;
import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

    private static final String ERROR = "red";
    private static final String WARNING = "orange";
    private static final String INFO = "green";
    private static final String FINE = "grey";
    private static final Map<Level, String> LEVEL_COLOR;
    static {
        LEVEL_COLOR = new HashMap<>();
        LEVEL_COLOR.put(Level.INFO, INFO);
        LEVEL_COLOR.put(Level.WARNING, WARNING);
        LEVEL_COLOR.put(Level.SEVERE, ERROR);
    }

    @Override
    public void publish(final LogRecord record) {
        Reporter.log(format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    private String format(final LogRecord record) {
        String color = Optional.ofNullable(LEVEL_COLOR.get(record.getLevel())).orElse(FINE);
        String colorize = "<font color=\"" + color + "\">%s</font><br>";
        Date date = new Date(record.getMillis());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return String.format(colorize, String.format("[%s] [%s] [%s] %s", record.getLevel().getName(),
                dateFormatted, record.getLoggerName(), record.getMessage()));
    }

    private void consolePublish(final LogRecord record) {
        if (record.getLevel().equals(Level.INFO)) {
            System.out.println(String.format("[%s] [%s] %s", record.getLevel().getName(),
                    record.getLoggerName(), record.getMessage()));
        } else {
            System.err.println(String.format("[%s] [%s] %s", record.getLevel().getName(),
                    record.getLoggerName(), record.getMessage()));
        }
    }
}
